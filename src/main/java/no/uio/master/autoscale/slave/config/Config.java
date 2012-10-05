package no.uio.master.autoscale.slave.config;

public class Config {

	public static Integer intervall_timer = 1;
	public static Integer threshold_breach_limit = 10;
	public static Double min_memory_usage = 10.0;
	public static Double max_memory_usage = 90.0;
	public static Long min_free_disk_space = 60L;
	public static Long max_free_disk_space = 20000L;

}
