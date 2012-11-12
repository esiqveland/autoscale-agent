package no.uio.master.autoscale.agent.config;

import java.io.FileNotFoundException;

import no.uio.master.autoscale.agent.config.Config;
import no.uio.master.autoscale.agent.config.YamlReader;

import org.junit.Assert;
import org.junit.Test;


public class YamlReaderINTTest {

	@Test
	public void testLoadYaml() {
		try {
			YamlReader.loadYaml();
		} catch (FileNotFoundException e) {
			Assert.fail(e.getMessage());
		}
		
		Assert.assertEquals("/Users/andreas/UiO/cassandra-runtime/2/apache-cassandra-1.1.5/bin/cassandra -f", Config.startup_command);
		Assert.assertEquals(3, Config.clean_directories.size());
	}

}
