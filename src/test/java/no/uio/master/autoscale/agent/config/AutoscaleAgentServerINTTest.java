package no.uio.master.autoscale.agent.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import no.uio.master.autoscale.agent.service.AutoscaleAgentServer;
import no.uio.master.autoscale.message.AgentMessage;
import no.uio.master.autoscale.message.enumerator.AgentMessageType;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AutoscaleAgentServerINTTest {
	private static AutoscaleAgentServer agentServer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		agentServer = new AutoscaleAgentServer();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		agentServer = null;
	}
	
	@Test
	public void testPerformActionStartupNode() throws IOException, InterruptedException {
		
		AgentMessage msg = new AgentMessage();
		msg.setType(AgentMessageType.STARTUP_NODE);
		
		agentServer.performAction(msg);
	}

	@Test
	public void testPerformActionUpdate() throws IOException, InterruptedException {
		AgentMessage msg = new AgentMessage();
		
		msg.setType(AgentMessageType.UPDATE);
		Map<String, String> map = new HashMap<String, String>();
		map.put("intervall_timer", "10");

		agentServer.performAction(msg);
	}

	@Test
	public void testPerformActionStopAgent() throws IOException, InterruptedException {
		AgentMessage msg = new AgentMessage();
		msg.setType(AgentMessageType.STOP_AGENT);
		
		agentServer.performAction(msg);
	}
	
	@Test
	public void testPerformActionStartAgent() throws IOException, InterruptedException {
		AgentMessage msg = new AgentMessage();
		msg.setType(AgentMessageType.START_AGENT);
		agentServer.performAction(msg);
	}
	
	@Test
	public void testPerformActionShutdownNode() throws IOException, InterruptedException {
		Thread.sleep(10000);
		AgentMessage msg = new AgentMessage();
		msg.setType(AgentMessageType.SHUTDOWN_NODE);
		
		agentServer.performAction(msg);
	}
	
}
