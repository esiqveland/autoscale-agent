import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import no.uio.master.autoscale.message.SlaveMessage;
import no.uio.master.autoscale.message.enumerator.SlaveMessageType;
import no.uio.master.autoscale.net.Communicator;
import no.uio.master.autoscale.slave.AutoscaleSlave;
import no.uio.master.autoscale.slave.config.Config;
import no.uio.master.autoscale.slave.service.AutoscaleSlaveServer;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class AutoscaleSlaveINTTest {

	private static ScheduledExecutorService executor;
	private static AutoscaleSlaveServer server;
	private static Communicator communicator;
	private static Integer master_input_port = 7798;
	private static Integer master_output_port = 7799;
	
	/* Total runtime of test - Seconds */
	private static final int TEST_RUNTIME_TIMER = 30;
	
	/* Interval timer of slave server - Seconds*/
	private static final int SLAVE_SERVER_INTERVALL_TIMER = 1;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			server = new AutoscaleSlaveServer();
		} catch (IOException e) {
			Assert.fail("Failed to initialize slave server");
		}
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(server, 0, SLAVE_SERVER_INTERVALL_TIMER, TimeUnit.SECONDS);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Thread.sleep(TEST_RUNTIME_TIMER * 1000);
		executor.shutdownNow();
		communicator = null;
	}

	@Test
	public void testSendSlaveMessage() throws InterruptedException {
		// Construct slave-message
				SlaveMessage slaveMsg = new SlaveMessage(SlaveMessageType.INITIALIZATION);
				slaveMsg.put("intervall_timer", 1);
				slaveMsg.put("threshold_breach_limit", Config.threshold_breach_limit);
				slaveMsg.put("min_memory_usage", Config.min_memory_usage);
				slaveMsg.put("max_memory_usage", Config.max_memory_usage);
				slaveMsg.put("min_free_disk_space",Config.min_free_disk_space);
				slaveMsg.put("max_free_disk_space",Config.max_free_disk_space);
				slaveMsg.put("storage_location", Config.storage_location);
		
				communicator = new Communicator(master_input_port, master_output_port);
				communicator.sendMessage("127.0.0.1", slaveMsg);
	}

}
