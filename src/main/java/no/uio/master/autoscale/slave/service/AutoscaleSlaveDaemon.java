package no.uio.master.autoscale.slave.service;

import no.uio.master.autoscale.slave.stat.NodeStatus;
import no.uio.master.autoscale.slave.stat.SlaveStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The runnable instance of the autoscale-slave daemon.
 * @author andreas
 *
 */
public class AutoscaleSlaveDaemon implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(AutoscaleSlaveDaemon.class);
	private static NodeStatus nodeStatus;
	private static SlaveStatus slaveStatus;
	
	
	public AutoscaleSlaveDaemon() {
		LOG.debug("Autoscale slave-daemon started");
		slaveStatus = SlaveStatus.IDLE;
		nodeStatus = new NodeStatus();
	}
	
	public AutoscaleSlaveDaemon(SlaveStatus status) {
		LOG.debug("Autoscale slave-daemon started with status = " + status.toString().toLowerCase());
		slaveStatus = status;
		nodeStatus = new NodeStatus();
	}


	@Override
	public void run() {
		switch(slaveStatus) {
		case RUNNING:
			systemStatus();
			break;
			
		case IDLE:
		default:
			break;
		}
	}
	
	private void systemStatus() {
		Double memUsed = nodeStatus.getMemoryUsage();
		Double cpuUsed = nodeStatus.getCPUUsage();
		Double diskUsed = nodeStatus.getDiskUsage();
		
		LOG.debug("Slave running - " +
				"\nMem: " + memUsed + "%" +
				"\nCPU: " + cpuUsed + "% " +
				"\nDisk: " + diskUsed + "%");
	}

	public static SlaveStatus getSlaveStatus() {
		return slaveStatus;
	}

	public static void setSlaveStatus(SlaveStatus slaveStatus) {
		AutoscaleSlaveDaemon.slaveStatus = slaveStatus;
	}	
}
