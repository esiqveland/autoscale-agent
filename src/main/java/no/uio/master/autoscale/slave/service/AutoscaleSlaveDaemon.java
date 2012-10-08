package no.uio.master.autoscale.slave.service;

import no.uio.master.autoscale.slave.stat.NodeMonitor;

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
