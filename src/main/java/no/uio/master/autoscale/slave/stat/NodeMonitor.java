package no.uio.master.autoscale.slave.stat;

import no.uio.master.autoscale.slave.config.Config;
import no.uio.master.autoscale.slave.net.Communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMonitor {
	private static Logger LOG = LoggerFactory.getLogger(NodeMonitor.class);
	private static NodeStatus nodeStatus = new NodeStatus();
	private static Communicator communicator = new Communicator(Config.slave_input_port, Config.slave_output_port);
	
	/* Timers to keep track of duration of breach */
	private static int memMaxBreachTimer = 0;
	private static int memMinBreachTimer = 0;
	private static int diskMaxBreachTimer = 0;
	private static int diskMinBreachTimer = 0;
	
	/**
	 * Perfom 1 monitor-cycle
	 */
	public void monitor() {
		monitorMemory();
		monitorDisk();
	}
	
	/**
	 * Monitor current disk-usage in bytes
	 * @return
	 */
	private void monitorDisk() {
		Long diskUsed = nodeStatus.getDiskSpaceUsed();
		
		// Minimums breach
		if(diskUsed < Config.min_free_disk_space) {
			diskMaxBreachTimer = 0;
			diskMinBreachTimer += Config.intervall_timer;
			
			// Scale down
			if(diskMinBreachTimer > Config.threshold_breach_limit) {
				LOG.debug("Disk breach - Sending scale down message...");
				diskMinBreachTimer = 0;
			}
		}
		// Maximum breach
		else if(diskUsed > Config.max_free_disk_space) {
			diskMinBreachTimer = 0;
			diskMaxBreachTimer += Config.intervall_timer;
			
			//Scale up
			if(diskMaxBreachTimer > Config.threshold_breach_limit) {
				LOG.debug("Disk breach - Sending scale up message...");
//				BreachMessage<Long> breachMessage = new BreachMessage<Long>(BreachType.MAX_DISK_USAGE, diskUsed);
				communicator.sendMessage(Config.master_host, "Works!");
				diskMaxBreachTimer = 0;
			}
		}
		else {
			diskMinBreachTimer = 0;
			diskMaxBreachTimer = 0;
		}
	}
	/**
	 * Monitor current memory status
	 * @return
	 */
	private void monitorMemory() {
		Double memUsed = nodeStatus.getMemoryUsage();
		
		// Min breach
		if(memUsed < Config.min_memory_usage) {
			memMaxBreachTimer = 0;
			memMinBreachTimer += Config.intervall_timer;
			
			if(memMinBreachTimer > Config.threshold_breach_limit) {
				// Scale down!
				LOG.debug("Memory breach - Sending scale down message...");
				memMinBreachTimer = 0;
			}
		} 
		
		// Max breach
		else if(memUsed > Config.max_memory_usage) {
			memMinBreachTimer = 0;
			memMaxBreachTimer += Config.intervall_timer;
			
			if(memMaxBreachTimer > Config.threshold_breach_limit) {
				// Scale up!
				LOG.debug("Memory breach - Sending scale up message...");
				memMinBreachTimer = 0;
			}
		}
		else {
			memMaxBreachTimer = 0;
			memMinBreachTimer = 0;
		}
	}
}
