package no.uio.master.autoscale.agent.config;

import java.util.ArrayList;
import java.util.List;

public class Config {

	public static Integer intervall_timer = 1;
	public static Integer threshold_breach_limit = 10;
	public static Double min_memory_usage = 10.0;
	public static Double max_memory_usage = 90.0;
	public static Long min_free_disk_space = 60L;
	public static Long max_free_disk_space = 20000L;
	public static String storage_location = "/";
	public static String master_host = "127.0.0.1";
	
	/* Communication-ports */
	public static Integer slave_input_port = 7799;
	public static Integer slave_output_port = 7798;
	
	/* Local configurations - Not modified by master */
	public static String configuration_file = "./conf/autoscale-agent.yaml";

	public static String root = "path-to-root/apache-cassandra-1.1.5";
	public static String startup_command = "bin/cassandra";
	public static String shutdown_command = "kill %d";
	
	public static List<String> clean_directories = new ArrayList<String>();
	
	public static Process runtime_process = null;
	
	/* Current node- address and port */
	public static String node_address = "127.0.0.1";
	public static Integer node_port = 8001;
}
