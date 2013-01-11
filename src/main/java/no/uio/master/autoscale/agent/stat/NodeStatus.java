package no.uio.master.autoscale.agent.stat;

import no.uio.master.autoscale.agent.config.Config;

import org.hyperic.sigar.DirUsage;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All internal contact with JVM to retrieve statuses.
 * 
 * @author andreas
 * 
 */
public class NodeStatus {
	private static Logger LOG = LoggerFactory.getLogger(NodeStatus.class);
	private static Sigar sigar;

	private static Long BYTES_TO_MB = 1024*1024L;
	public NodeStatus() {
		sigar = new Sigar();
	}

	/**
	 * Retrieve current memory usage (percentage)
	 * 
	 * @return
	 */
	public Double getMemoryUsage() {
		Double memUsed = 0.0;
		Mem mem;
		try {
			mem = sigar.getMem();
			memUsed = mem.getUsedPercent();
		} catch (Exception e) {
			LOG.error("Failed to get memory usage ", e);
		}
		LOG.debug("Memory usage: {}%", memUsed);
		return memUsed;
	}

	/**
	 * Retrieve Cpu-usage
	 * 
	 * @return
	 */
	public Double getCPUUsage() {
		Double cpuUsed = 0.0;

		try {
			cpuUsed = sigar.getCpuPerc().getCombined();
		} catch (SigarException e) {
			LOG.error("Failed to retrieve CPU-usage ", e);
		}

		LOG.debug("CPU used: {}%", cpuUsed);
		return cpuUsed;
	}

	/**
	 * Retrieve disk-usage in percentage
	 * 
	 * @return
	 */
	public Double getDiskUsage() {
		Long spaceInBytes = 0L;

		try {
			DirUsage dirUsage;
			String dir = Config.clean_directories.containsKey("data") ? Config.clean_directories.get("data") : "/";
			dirUsage = sigar.getDirUsage(dir);
			spaceInBytes = dirUsage.getDiskUsage();

		} catch (SigarException e) {
			LOG.error("Failed to retrieve disk space used in megabytes ", e);
		}
		
		Double diskUsed = ((double) (spaceInBytes / BYTES_TO_MB) / (double) Config.max_disk_space_used) * 100;
		LOG.debug("Diskspace used: {}%", diskUsed);
		return diskUsed;
	}

	/**
	 * Retrieve diskusage in size (Megabytes)
	 * 
	 * @return
	 */
	public Long getDiskSpaceUsed() {
		Long space = 0L;

		try {
			DirUsage dirUsage;
			String dir = Config.clean_directories.containsKey("data") ? Config.clean_directories.get("data") : "/";
			dirUsage = sigar.getDirUsage(dir);
			space = (dirUsage.getDiskUsage() / BYTES_TO_MB);
			
		} catch (SigarException e) {
			LOG.error("Failed to retrieve disk space used in megabytes ", e);
		}

		LOG.debug("Diskspace used: {}MB", space);
		return space;
	}
}
