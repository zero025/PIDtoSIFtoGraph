/**
 * This interface is for a complex component or family member.
 * 
 * @author Florian Dittmann
 * 
 */

package de.bioquant.cytoscape.pidfileconverter.Model;

import java.util.Collection;

public interface CompMolMember extends PidNode {

	/**
	 * @return the molecule
	 */
	public MoleculeNode getMolecule();

	/**
	 * @param molecule
	 *           the molecule to set
	 */
	public void setMolecule(MoleculeNode molecule);

	/**
	 * @return the modification
	 */
	public ComponentModification getModification();

	/**
	 * @param modification
	 *            the modification to set
	 */
	public void setModification(ComponentModification modification);

	public boolean hasModification();

	/**
	 * 
	 * @return
	 */
	public boolean isConnectedToMolecule();

	/**
	 * Adds a connection to a family parent.
	 * 
	 * @param parent
	 *            id of parent
	 * @return
	 */
	public boolean addFamilyConnection(final String parent);

	/**
	 * Returns a collection of ids of the family parents.
	 * 
	 * @return
	 */
	public Collection<String> getFamilies();

	public boolean addComplexConnection(final String parent);

	/**
	 * Returns a collection of ids of the complex parents.
	 * 
	 * @return
	 */
	public Collection<String> getComplexes();
}
