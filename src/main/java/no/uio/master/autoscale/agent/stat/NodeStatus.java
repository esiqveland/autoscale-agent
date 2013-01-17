package no.uio.master.autoscale.agent.stat;

import java.text.DecimalFormat;

import no.uio.master.autoscale.agent.config.Config;

import org.hyperic.sigar.DirUsage;
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
/**
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
			memUsed = NodeStatus.doubleFormatted(mem.getUsedPercent());
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
			cpuUsed = NodeStatus.doubleFormatted(sigar.getCpuPerc().getCombined());
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
		
		Double diskUsed = ((spaceInBytes / BYTES_TO_MB.doubleValue()) / Config.max_disk_space_used) * 100;
		diskUsed = NodeStatus.doubleFormatted(diskUsed);
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
		Double diskSpace = 0.0;
		try {
			DirUsage dirUsage;
			String dir = Config.clean_directories.containsKey("data") ? Config.clean_directories.get("data") : "/";
			dirUsage = sigar.getDirUsage(dir);
			space = (dirUsage.getDiskUsage() / BYTES_TO_MB);
			
			// For debug/logging purpose
			diskSpace = dirUsage.getDiskUsage() / BYTES_TO_MB.doubleValue();
			diskSpace = NodeStatus.doubleFormatted(diskSpace);
			
		} catch (SigarException e) {
			LOG.error("Failed to retrieve disk space used in megabytes ", e);
		}

		LOG.debug("Disk usage {}MB",diskSpace);
		return space;
	}
	
	private static Double doubleFormatted(Double val) {
		DecimalFormat twoDForm = new DecimalFormat("#.###");
		return Double.valueOf(twoDForm.format(val));
	}
}
