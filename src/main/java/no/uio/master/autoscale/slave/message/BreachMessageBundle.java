package no.uio.master.autoscale.slave.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BreachMessageBundle implements Serializable {
	
	private static final long serialVersionUID = 3490403077979246333L;
	private String nodeIp;
	List<BreachMessage<?>> breachMessage;

	public BreachMessageBundle() {
		breachMessage = new ArrayList<BreachMessage<?>>();
	}

	public BreachMessageBundle(String ip) {
		nodeIp = ip;
		breachMessage = new ArrayList<BreachMessage<?>>();
	}

	public String getNodeIp() {
		return nodeIp;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public List<BreachMessage<?>> getBreachMessage() {
		return breachMessage;
	}

	public void setBreachMessage(List<BreachMessage<?>> breachMessage) {
		this.breachMessage = breachMessage;
	}
	
	public void addMessage(BreachMessage<?> msg) {
		this.breachMessage.add(msg);
	}

	@Override
	public String toString() {
		return "BreachMessageBundle [nodeIp=" + nodeIp + ", breachMessage="
				+ breachMessage + "]";
	}
	
}
