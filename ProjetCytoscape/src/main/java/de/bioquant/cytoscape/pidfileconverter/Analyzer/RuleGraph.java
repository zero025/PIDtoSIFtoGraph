/**
 * Represents one rule graph which contains all the output information for
 * writing sif-files. It contains only the nodes and and their incoming/outgoing
 * edges, represented by their name. This graph doesn't contain any additional
 * information about the nodes or from which type they are. To manage the nodes
 * more easily the graph contains a mapping of the node names to the graph
 * objects.
 * 
 * @author Florian Dittmann
 * 
 */

package de.bioquant.cytoscape.pidfileconverter.Analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyManager;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.EdgeTypeOntology;

public class RuleGraph {

	private NameCreator naming = CreatorIDWithModification.getInstance();
	
	private Map<String, RuleGraphNode> nodes = new HashMap<String, RuleGraphNode>();
	
	private Collection<String> nodesToDelete = new ArrayList<String>();

	/**
	 * Generates an empty rule graph without any nodes.
	 */
	public RuleGraph() {
		super();
	}

	/**
	 * 
	 * @param manager
	 * @throws InconsistentOntologyException
	 * @throws UnknownOntologyException
	 */
	public RuleGraph(final NodeManagerImpl manager)
			throws InconsistentOntologyException, UnknownOntologyException {
		this.initializeNodesForRuleGraph(manager);
		this.initializeEdgesForRuleGraph(manager);
	}

	/**
	 * Adds a new node to graph.
	 * 
	 * @param name
	 *            name of the new node
	 * @return false - if graph already contains an equal node; true - node was
	 *         successfully added
	 */
	public final boolean addRuleGraphNode(final String name) {
		final RuleGraphNode node = new RuleGraphNode(name);
		if (nodes.containsKey(name)) {
			System.out.println("Rule graph already contains key: " + name);
			return false;
		} 
		else {
			nodes.put(name, node);
			return true;
		}
	}

	/**
	 * Adds a new connections to graph
	 * 
	 * @param startNode
	 *            name of start node 'endNode' will be in outgoing of the start
	 *            node)
	 * @param connection
	 *            name of the connection
	 * @param endNode
	 *            name of the end node 'startNode' will be in outgoing of the
	 *            end node)
	 * @return 'false' - if doesn't contain both nodes, no connections is added;
	 *         'true' - connection was successfully added
	 */
	public boolean addConnection(String startNode, String connection,
			String endNode) {
		if (!nodes.containsKey(startNode) || !nodes.containsKey(endNode)) {
			return false;
		}
		RuleGraphNode start = nodes.get(startNode);
		RuleGraphNode end = nodes.get(endNode);
		start.addOutgoingConnection(end, connection);
		return true;
	}

	/**
	 * Marks node for deleting from the graph. This method doesn't delete the
	 * node completely. It only removes all connections to this node this and
	 * adds this node to the 'nodes-to-delete-list'. To remove a node
	 * completely: run this method and {@link RuleGraph}#persistDeletingNodes()
	 * persistDeletingNodes} afterwards
	 * 
	 * @param name
	 *            name of the node to delete
	 * @return false - graph doesn't contain a node with this name; true - else
	 */
	public boolean deleteNodeFromGraph(String name) {
		if (nodes.containsKey(name)) {
			nodes.get(name).deleteAllConnections();
			this.nodesToDelete.add(name);
			return true;
		} 
		else {
			return false;
		}
	}

	/**
	 * Deletes all nodes completely from the graph, which was marked by
	 * {@link #deleteNodeFromGraph(String) deleteNodeFromGraph} beforehand.
	 */
	public void persistDeletingNodes() {
		for (String delNode : nodesToDelete) {
			RuleGraphNode node = nodes.get(delNode);
			if (null != node) {
				if (node.hasConnections()) {
					node.deleteAllConnections();
				}
				this.nodes.remove(delNode);
			}
		}
		nodesToDelete.clear();
	}

	/**
	 * Creates a new graph which contains all nodes ({@link
	 * #de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Model.InteractionNode
	 * interactions}, {@link
	 * #de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Model.PathwayNode pathways},
	 * {@link
	 * #de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Model.InteractionComponent
	 * interaction components}, positive conditions for interactions), but
	 * without any any connections between the nodes.
	 * 
	 * @param manager
	 *            manager of the nodes which contains alle the nodes, this
	 *            method should create
	 */
	public final void initializeNodesForRuleGraph(NodeManagerImpl manager) {
		nodes.clear();
		Collection<InteractionNode> interactions = manager.getAllInteractions();
		for (InteractionNode interaction : interactions) {
			String name = naming.getNameForInteraction(interaction);
			this.addRuleGraphNode(name);
			if (interaction.hasPosCondition()) {
				String posCond = interaction.getPosCondition();
				if (!nodes.containsKey(posCond)) {
					this.addRuleGraphNode(posCond);
				}
			}
		}

		Collection<InteractionComponent> intComps = manager
				.getAllInteractionComponents();
		for (InteractionComponent intComp : intComps) {
			String name = naming.getNameForCompMolMember(intComp);
			this.addRuleGraphNode(name);
		}

		Collection<PathwayNode> pathways = manager.getAllPathways();
		for (PathwayNode pathway : pathways) {
			String name = naming.getNameForPathway(pathway);
			this.addRuleGraphNode(name);
		}

	}

	/**
	 * Adds the edges between the available nodes. No nodes will be added, only
	 * the edges between available nodes
	 * 
	 * @param manager
	 *            manager of the nodes which contains all the nodes with their
	 *            connections , this method should create
	 * @throws InconsistentOntologyException
	 *             if available EdgeType ontology is inconsistent (for
	 *             incoming/outgoing interaction components)
	 * @throws UnknownOntologyException
	 *             if EdgeType ontology is not known in ontology manager
	 */
	public final void initializeEdgesForRuleGraph(NodeManagerImpl manager)
			throws InconsistentOntologyException, UnknownOntologyException {
		Collection<InteractionNode> interactions = manager.getAllInteractions();
		for (InteractionNode inter : interactions) {

			try {
				String interID = naming.getNameForInteraction(inter);
				String interPid = inter.getFullPid();

				Ontology onto = OntologyManager.getInstance().getOntology(
						EdgeTypeOntology.NAME);
				if (onto != null) {
					if (onto instanceof EdgeTypeOntology) {

						EdgeTypeOntology eOnto = (EdgeTypeOntology) onto;

						Collection<InteractionComponent> intComponents = inter
								.getInteractionComponents();
						for (InteractionComponent node : intComponents) {
							String component = naming
									.getNameForCompMolMember(node);
							Collection<OntologyElement> roles;

							roles = node.getRolesTypeForInteraction(interPid);

							for (OntologyElement role : roles) {
								String roleString = role.getName();
								if (eOnto.isSpecialEdge(role,
										EdgeTypeOntology.INCOMINGNAME)) {
									this.addConnection(component, roleString,
											interID);
								}
								else
								{
									this.addConnection(interID, roleString,
											component);
								}
							}
						}
					}
				}

				Collection<PathwayNode> pathways = inter.getPatways();
				for (PathwayNode pathway : pathways) {
					String right = naming.getNameForPathway(pathway);
					String middle = "hasPathway";
					String left = interID;
					this.addConnection(left, middle, right);
				}

				if (inter.hasPosCondition()) {
					String left = inter.getPosCondition();
					String middle = "positiveCond";
					String right = interID;
					this.addConnection(left, middle, right);
				}

			} catch (InvalidInteractionIdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns all nodes of the graph
	 * 
	 * @return
	 */
	public Collection<RuleGraphNode> getAllNodes() {
		return Collections.unmodifiableCollection(nodes.values());
	}

	/**
	 * Returns the node object with the given name.
	 * 
	 * @param name
	 *            of the node
	 * @return null - if a node with this name is not available; else - the node
	 *         object
	 */
	public RuleGraphNode getNodeForName(String name) {
		return nodes.get(name);
	}

	/**
	 * Returns the name of the connection between the two nodes.
	 * 
	 * @param start
	 *            name of start node
	 * @param end
	 *            name of end note
	 * @return name of connection; null - if there isn't any connection between
	 *         these nodes
	 */
	public final String getConnectionForNodes(String start, String end) {
		if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
			return null;
		}
		RuleGraphNode startNode = nodes.get(start);
		RuleGraphNode endNode = nodes.get(end);
		return startNode.outgoingConnections.get(endNode);
	}
}
