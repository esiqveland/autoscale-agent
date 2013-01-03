package no.uio.master.autoscale.agent.host;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import no.uio.master.autoscale.agent.config.Config;

import org.apache.cassandra.tools.NodeProbe;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraNodeCmd implements NodeCmd {
	private static Logger LOG = LoggerFactory.getLogger(CassandraNodeCmd.class);

	private String address;
	private Integer port;
	
	private NodeProbe nodeProbe;
	
	public CassandraNodeCmd(String node_address, Integer node_port) {
		LOG.debug("Initialize NodeCommand for {}:{}",node_address,node_port);
		this.address = node_address;
		this.port = node_port;
	}
	
	public boolean connect() {
		boolean connected = true;
		
		if(null == nodeProbe) {
			try {
				LOG.debug("Connecting to NodeProbe {}:{}",address,port);
				nodeProbe = new NodeProbe(address, port); 
				LOG.debug("Connected to NodeProbe");
				connected = true;
			} catch (Exception e) {
				LOG.error("Failed to initialize Cassandra NodeCmd: " + address +":"+port);
				connected = false;
			}
		}
		
		return connected;
	}

	@Override
	public void startupNode() throws IOException, InterruptedException {
		LOG.info("Startup node {}",address);
		Runtime.getRuntime().exec(Config.root + "/" + Config.startup_command);
		// Sleep to stay synchronized with the Cassandra-startup
		Thread.sleep(30000 * 3);
		LOG.debug("Startup completed");
	}

	@Override
	public void shutdownNode(Integer pid) throws InterruptedException, IOException {
		LOG.debug("Shutdown process {}",pid);
		if(!connect()) {
			LOG.debug("NodeProbe not running");
			return;
		}
		
		if(null == pid) {
			LOG.debug("Cannot shutdown node. Process-id is missing");
			return;
		}
		
		LOG.info("Shutdown node");
		nodeProbe.decommission();

		try {
			cleanDirectories();
		} catch (IOException e) {
			LOG.error("Failed to remove data ",e);
		}
		disconnect();
		Runtime.getRuntime().exec(String.format(Config.shutdown_command, pid.intValue()));
		
		LOG.info("Shutdown complete");
	}
	
	@Override
	public Integer getProcessId() {
		Integer pid = null;
		String[] args = new String[3];
		args[0] = "pgrep";
		args[1] = "-f";
		args[2] = "cassandra";//Config.root;
		
		try {
			Process p = Runtime.getRuntime().exec(args);
			
			InputStream iStream = p.getInputStream();

			int n = 0;
			String tempString = "0";
			while((n = iStream.read()) > 0) {
				char ch = (char)n;
				tempString += ch;
			}
			// If returned more than one, return last PID
			String[] splitString = tempString.split("\\r?\\n");
			pid = Integer.valueOf(splitString[splitString.length-1].trim());
			LOG.debug("Process ID: {}",pid);
		} catch (Exception e) {
			LOG.error("Failed to retrieve process id, process not found");
		}
		
		return pid;
	}

	@Override
	public void disconnect() {
		if(null != nodeProbe) {
			try {
				nodeProbe.close();
				nodeProbe = null;
			} catch (IOException e) {
				LOG.error("Failed while closing connection to Node: " + address);
			}
		}
	}

	@Override
	public void cleanDirectories() throws IOException {
		for(String dir : Config.clean_directories) {
			LOG.debug("Wipe data from: {}",dir);
			FileUtils.deleteDirectory(new File(dir));
			recreateDeletedFolder(dir);
		}
	}
	
	private static void recreateDeletedFolder(String dirPath) throws IOException {
		boolean dirCreated = new File(dirPath).mkdir();
		if(dirCreated) {
			LOG.debug("Created directory {}",dirPath);
			Runtime.getRuntime().exec( "chmod 755 "+dirPath);
		} else {
			LOG.error("Failed while creating directory {}",dirPath);
		}
	}

	@Override
	public List<String> getActiveNodes() {
		LOG.debug("Get active nodes");
		List<String> activeNodes = new ArrayList<String>();
		if(!connect()) {
			LOG.debug("NodeProbe not running");
			return activeNodes;
		}

		List<String> newActiveNodes = nodeProbe.getLiveNodes();
		LOG.info("Current active nodes: {}",newActiveNodes.size());
		
		activeNodes.addAll(newActiveNodes);
		disconnect();
		return activeNodes;
	}

	@Override
	public Long getUptime() {
		Long uptime = 0L;
		if(!connect()) {
			LOG.debug("NodeProbe not running");
			return uptime;
		}
		
		uptime = nodeProbe.getUptime();
		LOG.info("Current uptime: {}ms",uptime);

		return uptime;
	}

}
