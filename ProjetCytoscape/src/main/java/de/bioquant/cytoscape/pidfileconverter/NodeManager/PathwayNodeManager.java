package de.bioquant.cytoscape.pidfileconverter.NodeManager;

import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;

public interface PathwayNodeManager{

	/**
	 * Adds the specified pathway to manager's control. if it is not already
	 * present. More formally, adds the specified element newPathway to this
	 * manager if it contains no element e2 such that (newInteraction==null ?
	 * e2==null : newInteraction.equals(e2)). If this manager already contains
	 * the element, the call leaves it unchanged and returns false.
	 * 
	 * @param newPathway
	 * @return true if manager did not already contain the specified element
	 */
	public boolean addPathway(PathwayNode newPathway);

	/**
	 * Returns true if this manager contains the specified pathway. More
	 * formally, returns true if and only if this manager contains an element e
	 * such that (pathway==null ? interaction==null : o.equals(e)).
	 * 
	 * @param pathway
	 * @return true if this manager contains the specified element
	 */
	public boolean containsPathway(PathwayNode pathway);

	/**
	 * Takes specified pathway out of manager control
	 * 
	 * @param pathwayToDelete
	 * @return true if manager contained the specified element
	 */
	public boolean deletePathway(PathwayNode pathwayToDelete);
	
	
	public PathwayNode getEqualPathwayNodeInManager(PathwayNode pathway);
	
	public Collection<PathwayNode> getAllPathways();

	public void deleteAllPathways();
	
	public int getPathwayCount();
}
