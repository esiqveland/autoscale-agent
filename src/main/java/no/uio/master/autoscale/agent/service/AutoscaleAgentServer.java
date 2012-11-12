package no.uio.master.autoscale.agent.service;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import no.uio.master.autoscale.agent.config.Config;
import no.uio.master.autoscale.agent.host.CassandraNodeCmd;
import no.uio.master.autoscale.agent.host.NodeCmd;
import no.uio.master.autoscale.agent.stat.NodeStatus;
import no.uio.master.autoscale.message.AgentMessage;
import no.uio.master.autoscale.net.Communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class AutoscaleAgentServer implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(AutoscaleAgentServer.class);
	private static Communicator communicator;
	
	private static AutoscaleSlaveDaemon daemon = null;
	private static ScheduledExecutorService executor;
	
	private static NodeStatus status = new NodeStatus();
	private static NodeCmd nodeCmd = new CassandraNodeCmd();
	
	public AutoscaleAgentServer() throws IOException {
		communicator = new Communicator(Config.slave_input_port, Config.slave_output_port);
	}

	@Override
	public void run() {
			AgentMessage msg = (AgentMessage)communicator.readMessage();
			try {
				performAction(msg);
			} catch (Exception e) {
				LOG.error("Failed while performing action ",e);
			}
			
	}
	
	/**
	 * Perform action, based upon SlaveMessage.type
	 * @param msg
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void performAction(AgentMessage msg) throws IOException, InterruptedException {
		
		switch(msg.getType()) {
		case STARTUP_NODE:
			nodeCmd.startupNode();
		case UPDATE:
			updateConfig(msg);
			initAgent();
			break;
			
		case STOP_AGENT:
			stopAgent();
			break;

		case START_AGENT:
			initAgent();
			break;
			
		case SHUTDOWN_NODE:
			nodeCmd.shutdownNode();
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
	private void updateConfig(AgentMessage msg) {
		LOG.debug("Update configurations");
		
		if(msg.getMap().containsKey("intervall_timer")) {
			Config.intervall_timer = (Integer)msg.getMap().get("intervall_timer");
		}

		if(msg.getMap().containsKey("threshold_breach_limit")) {
			Config.threshold_breach_limit = (Integer)msg.getMap().get("threshold_breach_limit");
		}
		
		if(msg.getMap().containsKey("min_memory_usage")) {
			Config.min_memory_usage = (Double)msg.getMap().get("min_memory_usage");
		}

		if(msg.getMap().containsKey("max_memory_usage")) {
			Config.max_memory_usage = (Double)msg.getMap().get("max_memory_usage");
		}

		if(msg.getMap().containsKey("min_free_disk_space")) {
			Config.min_free_disk_space = (Long)msg.getMap().get("min_free_disk_space");
		}

		if(msg.getMap().containsKey("max_free_disk_space")) {
			Config.max_free_disk_space = (Long)msg.getMap().get("max_free_disk_space");
		}

		if(msg.getMap().containsKey("storage_location")) {
			Config.storage_location = (String)msg.getMap().get("storage_location");
		}
		
		if(!Strings.isNullOrEmpty(msg.getSenderHost())) {
			Config.master_host = (String)msg.getSenderHost();
		}
	}
	
	/**
	 * Initialize / Re-initialize daemon
	 */
	private void initAgent() {
		LOG.debug("Initialize agent");
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
	private void stopAgent() {
		LOG.debug("Stop agent");
		executor.shutdown();
	}
}
