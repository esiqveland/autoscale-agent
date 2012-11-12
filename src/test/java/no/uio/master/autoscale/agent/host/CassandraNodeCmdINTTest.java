package no.uio.master.autoscale.agent.host;

import java.io.IOException;
import java.util.ArrayList;

import no.uio.master.autoscale.agent.config.Config;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CassandraNodeCmdINTTest {
	private static NodeCmd nodeCmd;
	private static Integer pid;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Config.root = "/Users/andreas/UiO/cassandra-runtime/2/apache-cassandra-1.1.5";
		Config.startup_command = "bin/cassandra";
		Config.node_address = "127.0.0.2";
		Config.node_port = 8002;
		nodeCmd = new CassandraNodeCmd();
		
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
	public void testStartupNode() throws IOException, InterruptedException {
		nodeCmd.startupNode();
	}
	
	@Test
	public void testGetProcessId() {
		pid = nodeCmd.getProcessId();
		System.out.println("Process ID: " + pid);
	}

	@Test
	public void testShutdownNode() throws InterruptedException, IOException {
		nodeCmd.shutdownNode(pid);
	}


	@Test
	public void testCleanDirectories() throws IOException {
		nodeCmd.cleanDirectories();
	}

	@Test
	public void testDisconnect() {
		nodeCmd.disconnect();
	}
}
