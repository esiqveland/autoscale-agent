package no.uio.master.autoscale.slave.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoscaleSlaveServer implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(AutoscaleSlaveServer.class);
	private static final Integer DEFAULT_PORT = 7799;
	
	private static ServerSocket serverSocket = null;
	
	
	private static Integer port;
	
	public AutoscaleSlaveServer() throws IOException {
		port = DEFAULT_PORT;
		serverSocket = new ServerSocket(port);
	}
	
	public AutoscaleSlaveServer(Integer port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
	}

	

	@Override
	public void run() {
			CommunicationManager.readMessage(serverSocket);
	}
}
