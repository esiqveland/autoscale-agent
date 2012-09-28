package no.uio.master.autoscale.slave.message;

public enum BreachType {
	MAX_MEMORY_USAGE,
	MIN_MEMORY_USAGE,
	
	MAX_CPU_USAGE,
	MIN_CPU_USAGE,
	
	MAX_DISK_USAGE,
	MIN_DISK_USAGE;
}
