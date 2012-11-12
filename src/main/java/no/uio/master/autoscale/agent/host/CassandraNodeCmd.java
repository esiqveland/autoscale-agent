package no.uio.master.autoscale.agent.host;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import no.uio.master.autoscale.agent.config.Config;

import org.apache.cassandra.tools.NodeProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class CassandraNodeCmd implements NodeCmd {
	private static Logger LOG = LoggerFactory.getLogger(CassandraNodeCmd.class);

	private NodeProbe nodeProbe;
	
	public CassandraNodeCmd() {
	}
	
	public boolean connect() {
		try {
			nodeProbe = new NodeProbe(Config.node_address, Config.node_port);
			LOG.debug("Initialized nodeCmd for {}:{}",Config.node_address,Config.node_port);
			return true;
		} catch (Exception e) {
			LOG.error("Failed to initialize Cassandra NodeCmd: " + Config.node_address +":"+Config.node_port);
			return false;
		}
	}

	@Override
	public void startupNode() throws IOException, InterruptedException {
		LOG.debug("Startup node {}",Config.node_address);
		Config.runtime_process = Runtime.getRuntime().exec(Config.root + "/" + Config.startup_command);
		//Sleep to stay synchronized with the Cassandra-startup
		Thread.sleep(30000 * 3);
		LOG.debug("Startup complete");
	}

	@Override
	public void shutdownNode(Integer pid) throws InterruptedException, IOException {
		if(!connect()) {
			LOG.info("NodeProbe not running");
			return;
		}
		
		if(null == pid) {
			LOG.info("Cannot shutdown node. Process-id is missing");
			return;
		}
		
		LOG.debug("Shutdown node {}", Config.node_address);
		if(null != Config.runtime_process) {
			nodeProbe.decommission();

			try {
				cleanDirectories();
			} catch (IOException e) {
				LOG.error("Failed to remove data ",e);
			}
			disconnect();
			//TODO: Klarer ikke drepe prosessen.. den kj¿rer fortsatt... skaper problemer nŒr
			// Instansen skal startes opp igjen (da er port i bruk)
			Runtime.getRuntime().exec(String.format(Config.shutdown_command, pid.intValue()));
			//Config.runtime_process.destroy();
			
			LOG.debug("Shutdown complete");
		} else {
			LOG.error("Lost process reference");
		}
	}
	
	@Override
	public Integer getProcessId() {
		Integer pid = null;
		String[] args = new String[3];
		args[0] = "pgrep";
		args[1] = "-f";
		args[2] = Config.root;
		
		try {
			Process p = Runtime.getRuntime().exec(args);
			
			InputStream iStream = p.getInputStream();

			int n = 0;
			String tempString = "0";
			while((n = iStream.read()) > 0) {
				char ch = (char)n;
				tempString += ch;
			}
			
			pid = Integer.valueOf(tempString.trim());
		} catch (Exception e) {
			LOG.error("Failed to retrieve process id, process not found");
		}
		
		return pid;
	}

	@Override
	public void disconnect() {
		try {
			nodeProbe.close();
		} catch (IOException e) {
			LOG.error("Failed while closing connection to Node: " + Config.node_address);
		}
	}

	@Override
	public void cleanDirectories() throws IOException {
		//TODO: Bruke Java.io.files istede!!!
		for(String dir : Config.clean_directories) {
			LOG.debug("Wipe data from: {}",dir);
			String cmd = "rm -R " + dir + "/";
			Process proc = Runtime.getRuntime().exec(cmd);
			logErrorMessageIfAny(proc.getErrorStream());
			recreateDeletedFolder(dir);
		}
	}
	
	private static void recreateDeletedFolder(String dirPath) throws IOException {
		boolean dirCreated = new File(dirPath).mkdir();
		if(dirCreated) {
			LOG.debug("Created directory {} and chmod 755",dirPath);
			Runtime.getRuntime().exec( "chmod 755 "+dirPath);
		} else {
			LOG.error("Failed while creating directory {}",dirPath);
		}
	}
	/**
	 * Logs error message if any
	 * @param errorStream
	 * @throws IOException
	 */
	private void logErrorMessageIfAny(InputStream errorStream) throws IOException {
		StringBuilder str = new StringBuilder();
		while(true) {
			int c = errorStream.read();
			if(c == -1) {
				break;
			}
			str.append((char)c);
			
		}
		
		String string = str.toString().trim();
		if(!Strings.isNullOrEmpty(string)) {
			LOG.error(string);
		}
	}

}
