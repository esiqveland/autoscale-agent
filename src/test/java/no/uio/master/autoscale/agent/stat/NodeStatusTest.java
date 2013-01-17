package no.uio.master.autoscale.agent.stat;

import junit.framework.Assert;
import no.uio.master.autoscale.agent.config.YamlReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class NodeStatusTest {
	private static NodeStatus nodeStatus;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		YamlReader.loadYaml();
		nodeStatus = new NodeStatus();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		nodeStatus = null;
	}

	@Test
	public void testGetMemoryUsage() {
		Double memUsage = nodeStatus.getMemoryUsage();
		Assert.assertNotNull(memUsage);
		Assert.assertNotSame(0.0, memUsage.doubleValue());
	}

	@Test
	public void testGetCPUUsage() {
		Double cpuUsage = nodeStatus.getCPUUsage();
		Assert.assertNotNull(cpuUsage);
		Assert.assertNotSame(0.0, cpuUsage.doubleValue());
	}

	@Test
	public void testGetDiskUsage() {
		Double diskUsage = nodeStatus.getDiskUsage();
		Assert.assertNotNull(diskUsage);
	}

	@Test
	public void testGetDiskSpaceUsed() {
		Long diskUsage = nodeStatus.getDiskSpaceUsed();
		Assert.assertNotNull(diskUsage);
	}

}
