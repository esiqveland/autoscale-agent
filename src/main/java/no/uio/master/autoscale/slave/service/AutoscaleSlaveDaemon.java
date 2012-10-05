package no.uio.master.autoscale.slave.service;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import no.uio.master.autoscale.slave.config.Config;
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
	private static final Integer DEFAULT_SERVER_PORT = 7799;
	
	private static NodeStatus nodeStatus;
	private static SlaveStatus slaveStatus;
	
	
	public AutoscaleSlaveDaemon() {
		LOG.debug("Autoscale slave-daemon started");
		slaveStatus = SlaveStatus.RUNNING; //TODO: Set to IDLE
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
			//systemStatus();
			LOG.debug("Running..." + Config.intervall_timer);
			break;
			
		case IDLE:
			LOG.debug("Idle...");
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
