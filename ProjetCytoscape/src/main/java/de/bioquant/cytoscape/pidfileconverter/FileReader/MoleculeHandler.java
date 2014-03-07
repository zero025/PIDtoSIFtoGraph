/**
 * Creates the molecule nodes from the loaded PID
 * @author Florian Dittmann
 * @contributor Yamei & Thomas
 */
package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidEntrezGeneId;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidUniProtId;
import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.ComponentModification;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponentImpl;
import de.bioquant.cytoscape.pidfileconverter.Model.Modification;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PTMterm;
import de.bioquant.cytoscape.pidfileconverter.Model.PartRelation;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.InteractionComponentManager;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.ModificationManager;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.MoleculeNodeManager;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyManager;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.MoleculeTypeOntology;

public class MoleculeHandler {

	private MoleculeNodeManager mManager;
	private InteractionComponentManager intCompMan;

	private MoleculeNode currentMolecule = null;
	private Map<String, Collection<CompMolMember>> familyMembers = new HashMap<String, Collection<CompMolMember>>();
	private Map<String, Collection<CompMolMember>> complexComponents = new HashMap<String, Collection<CompMolMember>>();

	private boolean partMolecule;

	private boolean hasUniprot;
	private boolean hasEntrezGene;

	private String uniprot;
	private String entrezGene;

	private InteractionComponent member;
	private boolean newMember;
	private boolean memHasModification;
	private ComponentModification memModification;

	private ModificationManager modificationManager;

	public MoleculeHandler(MoleculeNodeManager mManager,
			ModificationManager modificationManager,
			InteractionComponentManager intCompMan) {
		super();
		this.mManager = mManager;
		this.modificationManager = modificationManager;
		this.intCompMan = intCompMan;
		this.resetMoleculeStates();
	}

	private void resetMoleculeStates() {
		partMolecule = false;
		hasUniprot = false;
		hasEntrezGene = false;
		newMember = false;
		memHasModification = false;
	}

	/**
	 * Handle the name of the molecule
	 * @param atts
	 * @throws InvalidUniProtId
	 * @throws InvalidEntrezGeneId
	 * @throws UnknownOntologyException
	 * @throws InconsistentOntologyException
	 * @throws InvalidArgumentException
	 */
	public void handleName(Attributes atts) throws InvalidUniProtId,
			InvalidEntrezGeneId, UnknownOntologyException,
			InconsistentOntologyException, InvalidArgumentException {
		if (PidTags.MOLECULELIST.isIn() && PidTags.MOLECULE.isIn()
				&& PidTags.NAME.isIn()) {
			String name_type = atts.getValue("name_type");
			String long_name = atts.getValue("long_name_type");
			if (name_type != null && long_name != null) {
				String value = atts.getValue("value");
				if (name_type.equals("UP") && long_name.equals("UniProt")) {
					handleUniProtId(value);
				} else if (name_type.equals("LL")
						&& long_name.equals("EntrezGene")) {
					handleEntrezGeneId(value);
				} else if (name_type.equals("PF")
						&& long_name.equals("preferred symbol")) {
					currentMolecule.setPreferredSymbol(value);
				} else if (name_type.equals("AS")
						&& long_name.equals("alias")
						&& (currentMolecule.getPreferredSymbol() == null || currentMolecule
								.getPreferredSymbol().equals(""))) {
					currentMolecule.setPreferredSymbol(value);
				} else if (name_type.equals("OF")
						&& long_name.equals("official symbol")
						&& (currentMolecule.getPreferredSymbol() == null || currentMolecule
								.getPreferredSymbol().equals(""))) {
					currentMolecule.setPreferredSymbol(value);
				}
			}
		}
	}

	/**
	 * Handle the uniprot id
	 * @param value
	 * @throws UnknownOntologyException
	 * @throws InconsistentOntologyException
	 * @throws InvalidUniProtId
	 */
	public void handleUniProtId(final String value)
			throws UnknownOntologyException, InconsistentOntologyException,
			InvalidUniProtId {
		if (currentMolecule.isProtein() || currentMolecule.isRna()) {

			if (value.matches("[P,Q,O]......*")) {
				this.hasUniprot = true;
				this.uniprot = value.substring(0, 6);
			}
		}
	}

	/**
	 * Handle the entrez gene id
	 * @param value
	 * @throws UnknownOntologyException
	 * @throws InconsistentOntologyException
	 * @throws InvalidUniProtId
	 * @throws InvalidEntrezGeneId
	 */
	public void handleEntrezGeneId(final String value)
			throws UnknownOntologyException, InconsistentOntologyException,
			InvalidUniProtId, InvalidEntrezGeneId {
		if (currentMolecule.isProtein() || currentMolecule.isRna()) {
			if (value.matches("([0-9])+")) {
				this.hasEntrezGene = true;
				this.entrezGene = value;
			}
		}
	}
	
	/**
	 * Handle the members of a family
	 * @param atts
	 * @throws InvalidIdException
	 */
	public void handleMember(Attributes atts) throws InvalidIdException {
		if (PidTags.MOLECULE.isIn() && PidTags.FAMILYMEMBERLIST.isIn()
				&& PidTags.MEMBER.isIn()) {

			String id = atts.getValue("member_molecule_idref");
			member = new InteractionComponentImpl(MoleculeNode.PREFIX + id);
			newMember = true;

		}
	}

	private void connectModificationToMember() {
		if (memHasModification) {
			String pid = member.getFullPid();
			Modification modification = memModification.getModification();
			if (modificationManager.containsModification(pid, modification)) {
				memModification.setModification(modificationManager
						.getEqualModification(pid, modification));
			} else {
				modificationManager.addModificationforPid(pid, modification);
			}
			this.member.setModification(memModification);
			memHasModification = false;
		}
	}

	public void handleMoleculeEnd() throws InvalidArgumentException {
		mManager.addMolecule(currentMolecule);
		if (hasUniprot) {
			currentMolecule.setUniProdID(uniprot, partMolecule);
		}
		if (hasEntrezGene) {
			currentMolecule.setEntrezGeneID(entrezGene, partMolecule);
		}
	}

	public void handleMolecule(Attributes atts) throws InvalidIdException {
		if (PidTags.MOLECULELIST.isIn() && PidTags.MOLECULE.isIn()) {
			this.resetMoleculeStates();
			String type = atts.getValue("molecule_type");
			String id = atts.getValue("id");
			final Ontology onto = OntologyManager.getInstance().getOntology(
					MoleculeTypeOntology.NAME);
			MoleculeNode molNode = new MoleculeNode(MoleculeNode.PREFIX + id);
			OntologyElement molType = onto.getElement(type);
			if (null != molType) {
				molNode.setType(molType);
			}
			currentMolecule = molNode;
		}
	}

	public void handlePart(Attributes atts) throws InvalidArgumentException {
		if (PidTags.MOLECULELIST.isIn() && PidTags.MOLECULE.isIn()
				&& PidTags.PART.isIn()) {
			partMolecule = true;
			String parent = atts.getValue("whole_molecule_idref");
			String start = atts.getValue("start");
			String end = atts.getValue("end");
			PartRelation part = new PartRelation(parent, start, end);
			this.currentMolecule.addPartRelation(part);
		}
	}

	private void newModification() {
		if (!memHasModification) {
			memModification = new ComponentModification();
			this.memHasModification = true;
		}
	}

	public void handlePtmTerm(PTMterm term) {
		newModification();
		memModification.addModification(term);
	}

	/**
	 * @return the multipleMolecules
	 */
	public final Map<String, Collection<CompMolMember>> getFamilyMembers() {
		return familyMembers;
	}

	private boolean addFamilyMemberForMolecule(String parentID,
			CompMolMember member) {
		Collection<CompMolMember> members;
		if (!this.familyMembers.containsKey(parentID)) {
			members = new ArrayList<CompMolMember>();
			familyMembers.put(parentID, members);
		} else {
			members = familyMembers.get(parentID);
		}
		return currentMolecule.addFamilyMember(member) && members.add(member);
	}

	private boolean addComplexComponentForMolecule(String parentID,
			InteractionComponent member2) {
		Collection<CompMolMember> members;
		if (!this.complexComponents.containsKey(parentID)) {
			members = new ArrayList<CompMolMember>();
			complexComponents.put(parentID, members);
		} else {
			members = complexComponents.get(parentID);
		}
		return currentMolecule.addComplexComponent(member2)
				&& members.add(member2);
	}

	/**
	 * @return the complexComponents
	 */
	public final Map<String, Collection<CompMolMember>> getComplexComponents() {
		return complexComponents;
	}

	public void handleComplexComponent(Attributes atts)
			throws InvalidIdException {
		if (PidTags.MOLECULE.isIn() && PidTags.COMPLEXCOMPONENTLIST.isIn()
				&& PidTags.COMPLEXCOMPONENT.isIn()) {

			String id = atts.getValue("molecule_idref");
			this.member = new InteractionComponentImpl(MoleculeNode.PREFIX + id);
			this.newMember = true;
		}
	}

	public void handleComplexComponentEnd()
			throws InvalidInteractionIdException, InvalidArgumentException {
		if (PidTags.MOLECULE.isIn() && PidTags.COMPLEXCOMPONENTLIST.isIn()
				&& PidTags.COMPLEXCOMPONENT.isIn()) {
			if (newMember) {
				connectModificationToMember();
				if (intCompMan.containsInteractionComponent(member)) {
					member = intCompMan.getEqualInteractionComponent(member);
				}
				this.addComplexComponentForMolecule(currentMolecule.getPid(),
						member);
				this.intCompMan.addInteractionComponent(this.member);
			}
			this.newMember = false;
		}
	}

	public void handleMemberEnd() throws InvalidIdException,
			InvalidArgumentException {
		if (PidTags.MOLECULE.isIn() && PidTags.FAMILYMEMBERLIST.isIn()
				&& PidTags.MEMBER.isIn()) {
			if (newMember) {
				connectModificationToMember();
				if (intCompMan.containsInteractionComponent(member)) {
					member = intCompMan.getEqualInteractionComponent(member);
				}
				this.addFamilyMemberForMolecule(currentMolecule.getPid(),
						this.member);
				this.intCompMan.addInteractionComponent(this.member);
			}
			newMember = false;
		}
	}

	public void setLocation(OntologyElement element) {
		this.newModification();
		this.memModification.setLocation(element);
	}

	public void setActivityState(OntologyElement element) {
		this.newModification();
		this.memModification.setActivityState(element);
	}
}
