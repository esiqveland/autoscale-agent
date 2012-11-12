package no.uio.master.autoscale.agent.host;

import java.io.IOException;

public interface NodeCmd {
	
	public boolean connect();
	
	public void startupNode() throws IOException, InterruptedException;
	
	public void shutdownNode(Integer pid) throws InterruptedException, IOException;
	
	public void disconnect();
	
	public void cleanDirectories() throws IOException;
	
	public Integer getProcessId();
}
