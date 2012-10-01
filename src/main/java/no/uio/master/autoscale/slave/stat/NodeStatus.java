package no.uio.master.autoscale.slave.stat;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.DirStat;
import org.hyperic.sigar.DirUsage;
import org.hyperic.sigar.DiskUsage;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.Df;
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
			LOG.error("Failed to get memory usage",e);
		}
		
		return memUsed;
	}
	
	/**
	 * Retrieve Cpu-usage
	 * @return
	 */
	public Double getCPUUsage() {
		Double cpuUsed = 0.0;
		
		try {
			CpuPerc[] cpuPercList = sigar.getCpuPercList();
			for (CpuPerc cpuPerc : cpuPercList) {
				cpuUsed += cpuPerc.getCombined(); //TODO: Gir fra 0.3 > 1.6 ved bygging av cassandra.. feil?
			}
		} catch (SigarException e) {
			e.printStackTrace();
		}
		return cpuUsed;
	}

	public Double getDiskUsage() {
		Double diskUsed = 0.0;
		
		try {
			FileSystemUsage fsUsage = sigar.getFileSystemUsage("/"); //TODO: Cassandra-storage-location
			diskUsed = fsUsage.getUsePercent();
		} catch (SigarException e) {
			e.printStackTrace();
		} 
		
		return diskUsed;
	}
}
