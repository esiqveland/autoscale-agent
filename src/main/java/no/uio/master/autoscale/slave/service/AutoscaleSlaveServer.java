package no.uio.master.autoscale.slave.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import no.uio.master.autoscale.model.SlaveMessage;
import no.uio.master.autoscale.slave.config.Config;
import no.uio.master.autoscale.slave.stat.NodeStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoscaleSlaveServer implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(AutoscaleSlaveServer.class);
	private static final Integer DEFAULT_PORT = 7799;
	
	private static ServerSocket serverSocket = null;
	
	private static AutoscaleSlaveDaemon daemon = null;
	private static ScheduledExecutorService executor;
	
	private static NodeStatus status = new NodeStatus();
	
	private static Integer port;
	
	public AutoscaleSlaveServer() throws IOException {
		port = DEFAULT_PORT;
		serverSocket = new ServerSocket(port);
	}
	
	public AutoscaleSlaveServer(Integer port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
	}

	

	@Override
	public void run() {
			SlaveMessage msg = CommunicationManager.readMessage(serverSocket);
			performAction(msg);
			
	}
	
	
	/**
	 * Perform action, based upon SlaveMessage.type
	 * @param msg
	 */
	public void performAction(SlaveMessage msg) {
		
		switch(msg.getType()) {
		case INITIALIZATION:
		case UPDATE:
			updateConfig(msg);
			initDaemon();
			break;
			
		case STOP_DAEMON:
			stopDaemon();
			break;
			
		default:
			LOG.error("Slave message-type didn't match any predefined types!");
			break;
		}
		
	}
	
	
	/**
	 * Update local Configurations with configurations received from master
	 * @param msg
	 */
	private void updateConfig(SlaveMessage msg) {
		LOG.debug("Update configurations...");
		Config.intervall_timer = (Integer)msg.getMap().get("intervall_timer");
		Config.threshold_breach_limit = (Integer)msg.getMap().get("threshold_breach_limit");
		Config.min_memory_usage = (Double)msg.getMap().get("min_memory_usage");
		Config.max_memory_usage = (Double)msg.getMap().get("max_memory_usage");
		Config.min_free_disk_space = (Long)msg.getMap().get("min_free_disk_space");
		Config.max_free_disk_space = (Long)msg.getMap().get("max_free_disk_space");
		Config.storage_location = (String)msg.getMap().get("storage_location");
	}
	
	/**
	 * Initialize / Re-initialize daemon
	 */
	private void initDaemon() {
		if(null != daemon) {
			executor.shutdownNow();
		}
		daemon = new AutoscaleSlaveDaemon();
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(daemon, 0, Config.intervall_timer, TimeUnit.SECONDS);
	}
	
	/**
	 * Shutdown currently running daemon (NOT the server)
	 */
	private void stopDaemon() {
		LOG.debug("Stopping daemon");
		executor.shutdown();
	}
}
