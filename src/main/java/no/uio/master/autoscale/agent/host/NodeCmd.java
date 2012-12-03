package no.uio.master.autoscale.agent.host;

import java.io.IOException;
import java.util.List;

public interface NodeCmd {
	
	public boolean connect();
	
	public void startupNode() throws IOException, InterruptedException;
	
	public void shutdownNode(Integer pid) throws InterruptedException, IOException;
	
	public void disconnect();
	
	public void cleanDirectories() throws IOException;
	
	public Integer getProcessId();
	
	public List<String> getActiveNodes();
	
	public Long getUptime();
}
