package no.uio.master.autoscale.agent.host;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import no.uio.master.autoscale.agent.config.Config;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraNodeCmdINTTest {
	private static Logger LOG = LoggerFactory.getLogger(CassandraNodeCmdINTTest.class);

	private static NodeCmd nodeCmd;
	private static Integer pid;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Runtime.getRuntime().exec("/Users/andreas/UiO/cassandra-runtime/1/apache-cassandra-1.1.5/bin/cassandra");
		LOG.info("Starting node1...");
		Thread.sleep(30000);
		// Make sure proper loopback is initialized for 127.0.0.2
		LOG.info("Starting node2...");
		Runtime.getRuntime().exec("/Users/andreas/UiO/cassandra-runtime/2/apache-cassandra-1.1.5/bin/cassandra -f");
		Thread.sleep(1000);
		LOG.info("Startup complete");

		Config.root = "/Users/andreas/UiO/cassandra-runtime/2/apache-cassandra-1.1.5";
		Config.startup_command = "bin/cassandra";
		Config.node_address = "127.0.0.2";
		Config.node_port = 8002;
		nodeCmd = new CassandraNodeCmd(Config.node_address, Config.node_port);

		Config.clean_directories = new ArrayList<String>();
		Config.clean_directories.add("/Users/andreas/UiO/cassandra-runtime/2/data");
		Config.clean_directories.add("/Users/andreas/UiO/cassandra-runtime/2/commitlog");
		Config.clean_directories.add("/Users/andreas/UiO/cassandra-runtime/2/saved_caches");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		nodeCmd = null;
	}

	@Test
	public void testConnect() {
		Assert.assertTrue(nodeCmd.connect());
	}

	@Test
	public void testStartupNode() throws IOException, InterruptedException {
		nodeCmd.startupNode();
	}

	@Test
	public void testGetProcessId() {
		pid = nodeCmd.getProcessId();
		LOG.info("ProcessID: {}", pid);
	}

	@Test
	public void testGetActiveNodes() {
		List<String> activeNodes = nodeCmd.getActiveNodes();

		Assert.assertNotNull(activeNodes);
		Assert.assertNotSame(0, activeNodes.size());
	}

	@Test
	public void testGetUptime() {
		Long uptime = nodeCmd.getUptime();

		Assert.assertNotNull(uptime);
		Assert.assertNotSame(0, uptime.longValue());
	}

	@Test
	public void testShutdownNode() throws InterruptedException, IOException {
		nodeCmd.shutdownNode(pid);
	}

	@Test
	public void testDisconnect() {
		nodeCmd.disconnect();
	}
}
