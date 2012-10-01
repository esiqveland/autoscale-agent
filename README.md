autoscale-slave
===============

Slaves, which runs together with the (Cassandra) implementation.


This slave should be initiated when cassandra-starts, and runs as a 
background-service at a custom port. When 'autoscale' is initiated, it 
tries to contact 'autoscale-slave', on each node, which should be running 
on its own port.

When the 'autoscale-slave' received contact from autoscaler, it is initiated 
with the parameters provided; autoscale-master ip & port, threshold-parameters 
set for master-initialization.

The autoscale-slave will use the 'SigarAPI' to retrieve Disk, Memory and CPU-usage 
of the machine its running on (therefore, it is required to be running on each 
node).

When the autoscale-slave encounters a threshold-breach, it sends an event to the 
autoscale-master:

BreachMessage {
	String nodeIP;
	BreachType type;  
}


The breachType is an enum constisting of different types of recorded breaches:

enum BreachType {
	MIN_MEMORY_USE,
	MAX_MEMORY_USE,
	MIN_CPU_USE,
	MAX_CPU_USE,
	MIN_DISK_USE,
	MAX_DISK_USE
}

The master recieved the breachMessage, and sort the breachTypes for scaling down or up:
BreachType type;

switch(type) {
	case MIN_MEMORY_USE:
	case MIN_CPU_USE:
	case MIN_DISK_USE:
		scaleDown();
		break;
	case MAX_MEMORY_USE:
	case MAX_CPU_USE:
	case MAX_DISK_USE:
		scaleUp();
		break;
	default:
		break;
} 


