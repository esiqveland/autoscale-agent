package no.uio.master.autoscale.agent.stat;

import no.uio.master.autoscale.agent.config.Config;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All internal contact with JVM to retrieve statuses.
 * @author andreas
 *
 */
public class NodeStatus {
	private static Logger LOG = LoggerFactory.getLogger(NodeStatus.class);
	private static Sigar sigar;

	private static final Long BYTES_IN_MB = 1048576L;
	
	public NodeStatus() {
		sigar = new Sigar();
	}
	/**
	 * Retrieve current memory usage (percentage)
	 * @return
	 */
	public Double getMemoryUsage() {
		Double memUsed = 0.0;
		Mem mem;
		try {
			mem = sigar.getMem();
			memUsed = mem.getUsedPercent();
		} catch (Exception e) {
			LOG.error("Failed to get memory usage ",e);
		}
		LOG.debug("Memory usage: {}%",memUsed);
		return memUsed;
	}
	
	/**
	 * Retrieve Cpu-usage
	 * @return
	 */
	public Double getCPUUsage() {
		Double cpuUsed = 0.0;
		
		try {
			cpuUsed = sigar.getCpuPerc().getCombined();
		} catch (SigarException e) {
			LOG.error("Failed to retrieve CPU-usage ",e);
		}
		
		LOG.debug("CPU used: {}%",cpuUsed);
		return cpuUsed;
	}

	/**
	 * Retrieve disk-usage in percentage
	 * @return
	 */
	public Double getDiskUsage() {
		Long space = 0L;
		
		try {
			FileSystemUsage fsUsage;
			for(String dir : Config.clean_directories) {
				fsUsage = sigar.getFileSystemUsage(dir);
				space += (fsUsage.getUsed() / BYTES_IN_MB);
			}
		} catch (SigarException e) {
			LOG.error("Failed to retrieve disk space used in megabytes ",e);
		} 
		
		Double diskUsed = ((double)space / (double)Config.max_disk_space_used) * 100;
		LOG.debug("Diskspace used: {}%",diskUsed);
		return diskUsed;
	}
	
	/**
	 * Retrieve diskusage in size (Megabytes)
	 * @return
	 */
	public Long getDiskSpaceUsed() {
		Long space = 0L;
		
		try {
			FileSystemUsage fsUsage;
			for(String dir : Config.clean_directories) {
				fsUsage = sigar.getFileSystemUsage(dir);
				space += (fsUsage.getUsed() / BYTES_IN_MB);
			}
		} catch (SigarException e) {
			LOG.error("Failed to retrieve disk space used in megabytes ",e);
		} 
		
		LOG.debug("Diskspace used: {}MB",space);
		return space;
	}
}
