/**
 * 
 */
package de.bioquant.cytoscape.pidfileconverter.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;

/**
 * This class is the implemenation of a complex component or family member. It
 * contains the molec
 * 
 * @author Florian Dittmann
 * 
 */
public class CompMolMemberImpl extends AbstractGraphNode implements
		CompMolMember {

	private MoleculeNode molecule;
	private ComponentModification modification;
	private Collection<String> familyParents = new ArrayList<String>();
	private Collection<String> complexParents = new ArrayList<String>();

	/**
	 * Initializes a new complex component / family member with the given id.
	 * 
	 * @param pid
	 *            has to start with "pid_m_"
	 * @throws InvalidIdException
	 *             thrown if id is invalid
	 */
	public CompMolMemberImpl(String pid) throws InvalidIdException {
		super(pid);
	}

	/**
	 * Initializes a new complex component / family member with the given
	 * molecule. Id of molecule will be equal to id of this object.
	 * 
	 * @param molecule
	 *            which should be connected to this complex component / family
	 *            member
	 * @throws InvalidIdException
	 *             thrown if id is invalid
	 */
	public CompMolMemberImpl(MoleculeNode molecule) throws InvalidIdException {
		super(molecule.getFullPid());
		this.molecule = molecule;
	}

	/**
	 * 
	 * @param pid
	 *            id has to start with "pid_m_"
	 * @param modification
	 *            modification of the molecule
	 * @throws InvalidIdException
	 */
	public CompMolMemberImpl(String pid, ComponentModification modification)
			throws InvalidIdException {
		super(pid);
		this.setModification(modification);
	}

	/**
	 * 
	 * @param molecule
	 *            which should be connected to this complex component / family
	 *            member
	 * @param modification
	 *            modification of the molecule
	 * @throws InvalidIdException
	 */
	public CompMolMemberImpl(MoleculeNode molecule,
			ComponentModification modification) throws InvalidIdException {
		super(molecule.getFullPid());
		this.setMolecule(molecule);
		this.setModification(modification);
	}

	@Override
	public String getClearID(final String pid) throws InvalidIdException {
		return IdClearer.clearUpPidM(pid);
	}

	/**
	 * @return the molecule
	 */
	public final MoleculeNode getMolecule() {
		return molecule;
	}

	/**
	 * @param molecule
	 *            the molecule to set
	 */
	public final void setMolecule(MoleculeNode molecule) {
		this.molecule = molecule;
	}

	/**
	 * @return the modification
	 */
	public final ComponentModification getModification() {
		return modification;
	}

	/**
	 * @param modification
	 *            the modification to set
	 */
	public final void setModification(ComponentModification modification) {
		this.modification = modification;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((modification == null) ? 0 : modification.hashCode());
		result = prime * result
				+ ((molecule == null) ? 0 : molecule.hashCode());
		return result;
	}

	@Override
	public String getPrefix() {
		return MoleculeNode.PREFIX;
	}

	@Override
	public boolean isConnectedToMolecule() {
		return molecule != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Model.AbstractGraphObject
	 * #getPid()
	 */
	@Override
	public String getPid() {
		if (isConnectedToMolecule())
			return this.molecule.getPid();
		else
			return super.getPid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Model.AbstractGraphObject
	 * #getFullPid()
	 */
	@Override
	public String getFullPid() {
		if (isConnectedToMolecule())
			return molecule.getFullPid();
		else
			return getPrefix() + getPid();
	}

	public boolean hasModification() {
		return this.modification != null;
	}

	@Override
	public boolean addFamilyConnection(final String parent) {
		if (this.familyParents.contains(parent))
			return false;
		return familyParents.add(parent);
	}

	@Override
	public Collection<String> getFamilies() {
		return Collections.unmodifiableCollection(familyParents);
	}

	@Override
	public boolean addComplexConnection(String parent) {
		if (this.complexParents.contains(parent))
			return false;
		return complexParents.add(parent);
	}

	@Override
	public Collection<String> getComplexes() {
		return Collections.unmodifiableCollection(complexParents);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Model.AbstractGraphObject
	 * #compareTo
	 * (de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.Model.AbstractGraphObject)
	 */
	@Override
	public int compareTo(AbstractGraphNode compObject) {
		int result = super.compareTo(compObject);
		if (0 != result)
			return result;
		if (compObject instanceof CompMolMember) {
			NameCreator naming=CreatorIDWithModification.getInstance();
			String name2=naming.getNameForCompMolMemberManagement((CompMolMember)compObject);
			result=naming.getNameForCompMolMemberManagement(this).compareTo(name2);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof CompMolMemberImpl)) {
			return false;
		}
		CompMolMemberImpl other = (CompMolMemberImpl) obj;
		if (modification == null) {
			if (other.modification != null) {
				return false;
			}
		} else if (!modification.equals(other.modification)) {
			return false;
		}/*
		 * if (molecule == null) { if (other.molecule != null) { return false; }
		 * } else if (!molecule.equals(other.molecule)) { return false; }
		 */
		return true;
	}

}
