package no.uio.master.autoscale.agent.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

public class YamlReader {

	/**
	 * Load yaml-file into Config
	 * 
	 * @throws FileNotFoundException
	 */
	public static void loadYaml() throws FileNotFoundException {
		InputStream input = new FileInputStream(new File(Config.configuration_file));
		Yaml yaml = new Yaml();

		LinkedHashMap<String, ?> data = (LinkedHashMap<String, ?>) yaml.load(input);
		reloadConfig(data);

	}

	/**
	 * Reload Config-variables with yaml-configurations
	 * 
	 * @param data
	 */
	private static void reloadConfig(LinkedHashMap<String, ?> data) {
		Config.root = (String) data.get("root");
		Config.startup_command = (String) data.get("startup_command");
		Config.shutdown_command = (String) data.get("shutdown_command");

		Config.clean_directories = (LinkedHashMap<String, String>) data.get("clear_directories");

		/* Configure JMX communication */
		Config.node_address = (String) data.get("node_address");
		Config.node_port = (Integer) data.get("node_port");

		/* Input- and output port for communication with master */
		Config.slave_input_port = (Integer) data.get("input_port");
		Config.slave_output_port = (Integer) data.get("output_port");

		/* Set thresholds and timers - will be overwritten by update-message */

		Config.intervall_timer = (Integer) data.get("intervall_timer");
		Config.threshold_breach_limit = (Integer) data.get("threshold_breach_limit");
		Config.min_memory_usage = (Double) data.get("min_memory_usage");
		Config.max_memory_usage = (Double) data.get("max_memory_usage");
		Config.min_disk_space_used = Long.parseLong(((Integer) data.get("min_disk_space_used")).toString());
		Config.max_disk_space_used = Long.parseLong(((Integer) data.get("max_disk_space_used")).toString());
	}
}
