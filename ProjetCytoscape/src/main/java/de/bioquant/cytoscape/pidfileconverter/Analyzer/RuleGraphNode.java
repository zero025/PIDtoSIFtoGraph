package de.bioquant.cytoscape.pidfileconverter.Analyzer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RuleGraphNode {

	private String name;
	
	private boolean renamed = false;
	
	protected Map<RuleGraphNode, String> incomingConnections = new HashMap<RuleGraphNode, String>();
	
	protected Map<RuleGraphNode, String> outgoingConnections = new HashMap<RuleGraphNode, String>();

	public RuleGraphNode() {
		super();
	}

	public RuleGraphNode(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(String name) {
		this.name = name;
		this.renamed = true;
	}

	/**
	 * @return the incoming connections
	 */
	public final Map<RuleGraphNode, String> getIncomingConnections() {
		return Collections.unmodifiableMap(incomingConnections);
	}

	/**
	 * @return the outgoing connections
	 */
	public final Map<RuleGraphNode, String> getOutgoingConnections() {
		return Collections.unmodifiableMap(outgoingConnections);
	}

	public String addIncomingConnection(RuleGraphNode node, String connection) {
		node.outgoingConnections.put(this,connection);
		return this.incomingConnections.put(node, connection);
	}

	/**
	 * 
	 * @param node
	 * @param connection
	 * @return
	 */
	public String addOutgoingConnection(RuleGraphNode node, String connection) {
		node.incomingConnections.put(this,connection);
		return this.outgoingConnections.put(node, connection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * True if the name of the objects is equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RuleGraphNode)) {
			return false;
		}
		RuleGraphNode other = (RuleGraphNode) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the renamed
	 */
	public final boolean isRenamed() {
		return renamed;
	}

	/**
	 * Deletes connection all connection from this to the given node.
	 * @param node node which is connected to the actual node
	 * @return true- if connections were successfully deleted; false - if there isn't any connection to the given node
	 * @throws InvalidRuleGraphConnectionException thrown if the connection to node is not a two-way connection
	 */
	public boolean deleteConnectionToNode(RuleGraphNode node) throws InvalidRuleGraphConnectionException{
		boolean result=false;
		if (incomingConnections.containsKey(node)){
			if (!node.outgoingConnections.containsKey(this)) {
				throw new InvalidRuleGraphConnectionException("Connection from '"+this.name+"' to '"+node.name+"' is invalid!");
			}
			node.outgoingConnections.remove(this);
			incomingConnections.remove(node);
			result= true;
		}
		if (outgoingConnections.containsKey(node)){
			if (!node.incomingConnections.containsKey(node)) {
				throw new InvalidRuleGraphConnectionException("Connection from '"+this.name+"' to '"+node.name+"' is invalid!");
			}
			node.incomingConnections.remove(this);
			outgoingConnections.remove(node);
			result=true;
		}
		return result;
	}

	/**
	 * Deletes all incoming and outgoing connections of this node.
	 */
	public void deleteAllConnections(){
		Collection<RuleGraphNode> nodes=incomingConnections.keySet();	
		for(RuleGraphNode node:nodes)
			try {
				if (!node.outgoingConnections.containsKey(this)) {
					throw new InvalidRuleGraphConnectionException();
				}
				node.outgoingConnections.remove(this);
			} catch (InvalidRuleGraphConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		incomingConnections.clear();			

		nodes=outgoingConnections.keySet();
		for(RuleGraphNode node:nodes)
			try {
				if (!node.incomingConnections.containsKey(this)) {
					throw new InvalidRuleGraphConnectionException();
				}
				node.incomingConnections.remove(this);
			} catch (InvalidRuleGraphConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		outgoingConnections.clear();
	}

	/**
	 * @return true - if outgoing or incoming connections contain any members; false - else
	 */
	public boolean hasConnections(){
		return (!this.incomingConnections.isEmpty() || !this.outgoingConnections.isEmpty());
	}
}
