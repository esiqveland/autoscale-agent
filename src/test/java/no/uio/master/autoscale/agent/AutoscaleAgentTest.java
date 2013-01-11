package no.uio.master.autoscale.agent;

import org.junit.Test;

/**
 * Test startup of agent server
 * @author andreas
 *
 */
public class AutoscaleAgentTest {
	@Test
	public void testStartup() throws InterruptedException {
		AutoscaleAgent.main(new String[0]);
		Thread.sleep(5000L);
	}

}
