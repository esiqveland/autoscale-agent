package no.uio.master.autoscale.slave.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import no.uio.master.model.SlaveMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommunicationManager {
	private static Logger LOG = LoggerFactory.getLogger(CommunicationManager.class);
	private static ObjectOutputStream outputStream;
	private static ObjectInputStream inputStream;
	private static Socket connection;
	
	
	public static SlaveMessage readMessage(ServerSocket serverSocket) {
		SlaveMessage msg = null;
		try {
			LOG.debug("Waiting for connection...");
			connection = serverSocket.accept();
			
			LOG.debug("Reading message");
			inputStream = new ObjectInputStream(connection.getInputStream());
			
			msg = (SlaveMessage)inputStream.readObject();
			LOG.debug("Read message");
		} catch (Exception e) {
			LOG.error("Failed to read message - ",e);
		}
		return msg;
	}
	
	public static void sendMessage(Object obj, ServerSocket serverSocket) {
		try {
			LOG.debug("Sending message");
			connection = serverSocket.accept();
			outputStream = new ObjectOutputStream(connection.getOutputStream());
			outputStream.writeObject(obj);
			outputStream.flush();
			LOG.debug("Message sent");
		} catch (IOException e) {
			LOG.error("Failed to send message - ",e);
		}
	}
}
