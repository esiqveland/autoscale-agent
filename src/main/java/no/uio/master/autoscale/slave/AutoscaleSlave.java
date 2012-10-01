package no.uio.master.autoscale.slave;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import no.uio.master.autoscale.slave.service.AutoscaleSlaveDaemon;

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
	private static AutoscaleSlaveDaemon daemon;
	
	private static final String DEFAULT_HOST = "127.0.0.1";
	private static final int DEFAULT_PORT = 7299;
	
	private static int INTERVALL_TIMER = 1;
	
	
	public static void main(String[] args) {
		LOG.debug("Autoscale slave invoked...");
		daemon = new AutoscaleSlaveDaemon();
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(daemon, 0, INTERVALL_TIMER, TimeUnit.SECONDS);
		LOG.info("Invoked");
	}
	
}
