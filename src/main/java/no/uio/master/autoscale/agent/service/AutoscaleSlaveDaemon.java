package no.uio.master.autoscale.agent.service;

import java.net.ServerSocket;

import no.uio.master.autoscale.agent.stat.NodeMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The runnable instance of the autoscale-slave daemon.
 * @author andreas
 *
 */
public class AutoscaleSlaveDaemon implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(AutoscaleSlaveDaemon.class);
	private static NodeMonitor monitor = new NodeMonitor();
	
	public AutoscaleSlaveDaemon() {
		LOG.debug("Autoscale slave-daemon started");
	}
	
	@Override
	public void run() {
			LOG.debug("Running...");
			monitor.monitor();
	}
}
