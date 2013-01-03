Autoscale agent
===============
Agent which runs together with the node-implementation. It is currently made for Cassandra, 
but it should only be necessary to change the configuration files and replace Cassandra-specific 
implementations for your needs, as interfaces are provided for this functionality.

The agent may be initialized on already running clusters, since it does not interfer directly 
into the cluster itselves. The agent runs alongside the implementation, monitoring CPU, disk 
and memory-usage, and depending on thresholds sent from master, sends breach messages back 
whenever breaches occurs over a certain timespan. 

The master will then collect messages over time, and decide if a node should be taken down, or 
if there is necessary to add another node.

The agent uses 'Sigar API' to retrieve Disk, Memory and CPU-usage from the current system.


Install instructions
=====================
1. Make sure ports are open, and accessible from the internet. Default ports are '7799' and '7798'

2. Download release-N.N.N.tar.gz

3. Unpack and configurate 'conf/autoscale-agent.yaml' and 'log4j.properties' for your needs

4. Start agent by: 'bin/autoscale'


Where to find master implementation
===================================
Go to https://github.com/baakind/autoscale to download master implementation, which is responsible 
for communicating with each agent running, and control the scaling of the cluster.


Footnote
========
This is only an early release for an autoscaler-implementation as a part of the master thesis for
Andreas Baakind at University of Oslo.