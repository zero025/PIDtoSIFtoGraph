package de.bioquant.cytoscape.pidfileconverter.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.MoleculeTypeOntology;

/**
 * An object of this class represents a molecule WITHOUT any modifications. To
 * It is characterized by his pid. (To create a molecule with modifications
 * {@link de.bioquant.cytoscape.pidfileconverter.Model.CompMolMemberImpl
 * CompMolMemberImpl}). It can have a Uniprot and a preferred symbol. If it is a
 * complex/family, it contains the complex components / family members. If it is
 * a part of another molecule, then it contains this part relation.
 * 
 * @author Florian Dittmann
 * 
 */
public class MoleculeNode extends AbstractGraphNode {

	public final static String PREFIX = "pid_m_";
	private OntologyElement type;
	private UniprotID uniProdID;
	private String preferredSymbol = "";
	private List<PartRelation> partRelations;
	private List<CompMolMember> complexComponents = new ArrayList<CompMolMember>();
	private List<CompMolMember> familyMembers = new ArrayList<CompMolMember>();

	public MoleculeNode(String pid) throws InvalidIdException {
		super(pid);
	}

	@Override
	public String getClearID(final String pid) throws InvalidIdException {
		return IdClearer.clearUpPidM(pid);
	}

	@Override
	public String getPrefix() {
		return PREFIX;
	}

	/**
	 * @return the type
	 */
	public final OntologyElement getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public final void setType(OntologyElement type) {
		this.type = type;
	}

	/**
	 * @return the uniProdID
	 */
	public String getUniProdID() {
		if (null == uniProdID)
			return "";
		else
			return uniProdID.getId();
	}

	/**
	 * 
	 * @param id
	 *            Uniprot ID to set, must not be null
	 * @throws InvalidArgumentException
	 *             thrown if id is null
	 */
	public void setUniProdID(String id) throws InvalidArgumentException {
		this.uniProdID = new UniprotID(id, false);
	}

	/**
	 * 
	 * @param id
	 *            Uniprot ID to set, must not be null
	 * @param partmolecule
	 *            true - part tag will be added to Uniprot; false - normal id
	 *            will be set as uniprot
	 * @throws InvalidArgumentException thrown if id is null
	 */
	public void setUniProdID(String id, boolean partmolecule)
			throws InvalidArgumentException {
		this.uniProdID = new UniprotID(id, partmolecule);
	}

	/**
	 * 
	 * @return true - if molecule type is protein
	 * @throws UnknownOntologyException
	 *             if MoleculeTypeOntology is unknown
	 * @throws InconsistentOntologyException
	 *             if MoleculeTypeOntology is inconsistent
	 */
	public boolean isProtein() throws UnknownOntologyException,
			InconsistentOntologyException {
		Ontology onto = AbstractGraphNode.ONTOMANAGER
				.getOntology(MoleculeTypeOntology.NAME);
		if (onto == null)
			throw new UnknownOntologyException(
					"Molecule type ontology is not known!");
		else if (onto.getClass() == MoleculeTypeOntology.class) {
			MoleculeTypeOntology newOnto = (MoleculeTypeOntology) onto;
			return newOnto.isProtein(type);
		} else
			throw new UnknownOntologyException(
					"Molecule type ontology is not known!");
	}

	public boolean hasPreferredSymbol() {
		if (preferredSymbol != null && !preferredSymbol.isEmpty())
			return true;
		else
			return false;
	}

	/**
	 * @return the preferredSymbol
	 */
	public String getPreferredSymbol() {

		return preferredSymbol;
	}

	/**
	 * 
	 * @param preferredSymbol
	 *            must not be null
	 * @throws InvalidArgumentException
	 *             thrown if parameter is null
	 */
	public void setPreferredSymbol(String preferredSymbol)
			throws InvalidArgumentException {
		if (null == preferredSymbol)
			throw new InvalidArgumentException();
		this.preferredSymbol = preferredSymbol;
	}

	/**
	 * @return returns an unmodifiable list of all part relations of this
	 *         molecule
	 */
	public final List<PartRelation> getPartRelations() {
		return Collections.unmodifiableList(partRelations);
	}

	/**
	 * Adds a new part relation.
	 * 
	 * @param partRelation
	 */
	public final void addPartRelation(PartRelation partRelation) {
		if (null == partRelations)
			partRelations = new ArrayList<PartRelation>();
		this.partRelations.add(partRelation);
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasUniprot() {
		if (uniProdID != null)
			if (!uniProdID.getId().isEmpty())
				return true;
		return false;
	}

	/**
	 * @return an unmodifiable list of all complex components of this molecule
	 */
	public final List<CompMolMember> getComplexComponents() {
		return Collections.unmodifiableList(complexComponents);
	}

	/**
	 * @return an unmodifiable list of all family members of this molecule
	 */
	public final List<CompMolMember> getFamilyMembers() {
		return Collections.unmodifiableList(familyMembers);
	}

	/**
	 * Adds a new complex component and also the connection from the component
	 * to this molecule.
	 * 
	 * @param component
	 *            complex component to add
	 * @return true - if member is successfully added; false - else
	 */
	public boolean addComplexComponent(CompMolMember component) {
		if (null == complexComponents)
			complexComponents = new ArrayList<CompMolMember>();
		if (complexComponents.add(component))
			return component.addComplexConnection(this.getFullPid());
		else
			return false;
	}

	/**
	 * Adds a new family member and also the connection from the member to this
	 * molecule.
	 * 
	 * @param member
	 *            family member to add
	 * @return true - if member is successfully added; false - else
	 */
	public boolean addFamilyMember(CompMolMember member) {
		if (null == familyMembers)
			familyMembers = new ArrayList<CompMolMember>();
		if (familyMembers.add(member))
			return member.addFamilyConnection(this.getFullPid());
		else
			return false;

	}

	public boolean hasComplexComponents() {
		if (null == complexComponents)
			return false;
		if (complexComponents.isEmpty())
			return false;
		return true;
	}

	public boolean hasFamilyMembers() {
		if (null == familyMembers)
			return false;
		if (familyMembers.isEmpty())
			return false;
		return true;
	}

}
