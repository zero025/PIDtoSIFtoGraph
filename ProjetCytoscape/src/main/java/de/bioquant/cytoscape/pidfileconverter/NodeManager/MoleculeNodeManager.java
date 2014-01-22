package de.bioquant.cytoscape.pidfileconverter.NodeManager;

import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;

public interface MoleculeNodeManager {
	
	/**
	 * Adds the specified molecule to manager's control. if it is not already
	 * present. More formally, adds the specified element newMolecule to this
	 * manager if it contains no element e2 such that (newInteraction==null ?
	 * e2==null : newMolecule(e2)). If this manager already contains
	 * the element, the call leaves it unchanged and returns false.
	 * 
	 * @param newMolecule
	 * @return true if manager did not already contain the specified element
	 */
	public boolean addMolecule(MoleculeNode newMolecule);

	/**
	 * Returns true if this manager contains the specified molecule. More
	 * formally, returns true if and only if this manager contains an element e
	 * such that (pathway==null ? interaction==null : o.equals(e)).
	 * 
	 * @param molecule
	 * @return true if this manager contains the specified element
	 */
	public boolean containsMolecule(MoleculeNode molecule);

	/**
	 * Takes specified molecule out of manager control
	 * 
	 * @param moleculeToDelete
	 * @return true if manager contained the specified element
	 */
	public boolean deleteMolecule(MoleculeNode moleculeToDelete);
	
	
	public MoleculeNode getEqualMoleculeNodeInManager(MoleculeNode molecule);

	public void deleteAllMolecules();
	
	public int getMoleculeCount();
	
	public Collection<MoleculeNode> getAllMolecules();

}
