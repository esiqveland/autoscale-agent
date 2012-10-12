package no.uio.master.autoscale.slave;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import no.uio.master.autoscale.slave.service.AutoscaleSlaveServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initial startup of autocale-slave implementation.<br>
 * This class is only used for initial setup of the daemon.
 * @author andreas
 *
 */
public class AutoscaleSlave {
	private static Logger LOG = LoggerFactory.getLogger(AutoscaleSlave.class);

	private static ScheduledExecutorService executor;
	private static AutoscaleSlaveServer server;
	
	private static int INTERVALL_TIMER = 1;
	
	
	public static void main(String[] args) {
		LOG.debug("Autoscale slave invoked...");
		try {
			server = new AutoscaleSlaveServer();
		} catch (IOException e) {
			LOG.error("Failed to initialize slave server");
		}
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(server, 0, INTERVALL_TIMER, TimeUnit.SECONDS);
		LOG.info("Invoked");
	}
	
}
