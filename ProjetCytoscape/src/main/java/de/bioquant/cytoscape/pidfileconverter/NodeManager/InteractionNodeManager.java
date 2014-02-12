package de.bioquant.cytoscape.pidfileconverter.NodeManager;

import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;

public interface InteractionNodeManager {

	/**
	 * Adds the specified interaction to manager's control. if it is not already present. More formally, adds the specified element newInteraction to
	 * this manager if it contains no element e2 such that (newInteraction==null ? e2==null : newInteraction.equals(e2)). If this manager already
	 * contains the element, the call leaves it unchanged and returns false.
	 * 
	 * @param newInteraction
	 * @return true if manager did not already contain the specified element
	 */
	public boolean addInteraction(InteractionNode newInteraction);

	/**
	 * Returns true if this manager contains the specified interaction. More formally, returns true if and only if this manager contains an element e
	 * such that (interaction==null ? interaction==null : o.equals(e)).
	 * 
	 * @param interaction
	 * @return true if this manager contains the specified element
	 */
	public boolean containsInteraction(InteractionNode interaction);

	/**
	 * Takes specified interaction out of manager control
	 * 
	 * @param interactionToDelete
	 * @return true if manager contained the specified element
	 */
	public boolean deleteInteraction(InteractionNode interactionToDelete);

	public InteractionNode getEqualInteractionNodeInManager(InteractionNode interaction);

	public Collection<InteractionNode> getAllInteractions();

	public int getInteractionCount();

	void deleteAllInteractions();
}
