package no.uio.master.autoscale.slave.stat;

import no.uio.master.autoscale.slave.config.Config;
import no.uio.master.autoscale.slave.stat.type.DiskStatus;
import no.uio.master.autoscale.slave.stat.type.MemoryStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMonitor {
	private static Logger LOG = LoggerFactory.getLogger(NodeMonitor.class);
	private static NodeStatus nodeStatus = new NodeStatus();
	
	/* Timers to keep track of duration of breach */
	private static int memMaxBreachTimer = 0;
	private static int memMinBreachTimer = 0;
	private static int diskMaxBreachTimer = 0;
	private static int diskMinBreachTimer = 0;
	
	/**
	 * Perfom 1 monitor-cycle
	 */
	public void monitor() {
		// Perform memory-validation
		MemoryStatus memoryStatus = monitorMemory();
		validateMemoryStatus(memoryStatus);
		
		// Perform disk-validation
		DiskStatus diskStatus = monitorDisk();
		validateDiskStatus(diskStatus);
	}
	
	
	/**
	 * Validate current disk status, and send scale-message to master depending in disk-breach.<br>
	 * Important: It is monitoring disk usage in bytes of provided location from <tt>Config.storage_location</tt>
	 * @param status
	 */
	private void validateDiskStatus(DiskStatus status) {
		if(DiskStatus.BREACH_MAX == status) {
			diskMinBreachTimer = 0;
			diskMaxBreachTimer += Config.intervall_timer;
			
			if(diskMaxBreachTimer > Config.threshold_breach_limit) {
				// Scale up!
				LOG.debug("Disk breach - Sending scale up message...");
				diskMaxBreachTimer = 0;
			}
		}
		else if(DiskStatus.BREACH_MIN == status) {
			diskMaxBreachTimer = 0;
			diskMinBreachTimer += Config.intervall_timer;
			
			if(diskMinBreachTimer > Config.threshold_breach_limit) {
				// Scale down!
				LOG.debug("Disk breach - Sending scale down message...");
				diskMinBreachTimer = 0;
			}
		}
		else {
			diskMinBreachTimer = 0;
			diskMaxBreachTimer = 0;
		}
	}
	
	/**
	 * Validate memory status, and eventually send scale-message depending on memory-breach
	 * @param status
	 */
	private void validateMemoryStatus(MemoryStatus status) {
		if(MemoryStatus.BREACH_MAX == status) {
			memMinBreachTimer = 0;
			memMaxBreachTimer += Config.intervall_timer;
			
			if(memMaxBreachTimer > Config.threshold_breach_limit) {
				// Scale up!
				LOG.debug("Memory breach - Sending scale up message...");
				memMinBreachTimer = 0;
			}
		}
		else if(MemoryStatus.BREACH_MIN == status) {
			memMaxBreachTimer = 0;
			memMinBreachTimer += Config.intervall_timer;
			
			if(memMinBreachTimer > Config.threshold_breach_limit) {
				// Scale down!
				LOG.debug("Memory breach - Sending scale down message...");
				memMinBreachTimer = 0;
			}
		} else {
			memMaxBreachTimer = 0;
			memMinBreachTimer = 0;
		}
		
	}
	
	/**
	 * Monitor current disk-usage in bytes
	 * @return
	 */
	private DiskStatus monitorDisk() {
		Long diskUsed = nodeStatus.getDiskSpaceUsed();
		DiskStatus status;
		
		if(diskUsed < Config.min_free_disk_space) {
			status = DiskStatus.BREACH_MIN;
		}
		else if(diskUsed > Config.max_free_disk_space) {
			status = DiskStatus.BREACH_MAX;
		}
		else {
			status = DiskStatus.OK;
		}
		
		return status;
	}
	/**
	 * Monitor current memory status
	 * @return
	 */
	private MemoryStatus monitorMemory() {
		Double memUsed = nodeStatus.getMemoryUsage();
		MemoryStatus status;
		
		if(memUsed < Config.min_memory_usage) {
			status = MemoryStatus.BREACH_MIN;
		} 
		else if(memUsed > Config.max_memory_usage) {
			status = MemoryStatus.BREACH_MAX;
		}
		else {
			status = MemoryStatus.OK;
		}
		
		return status;
	}


}
