package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.util.Collection;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidEntrezGeneId;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidUniProtId;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.NoValidManagerSetException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.UnknownMoleculeException;
import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.ComponentModification;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponentImpl;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.Modification;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PTMterm;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.InteractionComponentManager;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.InteractionNodeManager;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.ModificationManager;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.MoleculeNodeManager;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.PathwayNodeManager;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Ontology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyManager;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyElementException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.ActivityStateOntology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.EdgeTypeOntology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.LocationOntology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.PtmOntology;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Specialized.SpecialOntologies;

public class PidFileHandler extends DefaultHandler {

	private InteractionNodeManager iManager;

	private PathwayNodeManager pManager;
	private ModificationManager modiManager;
	private MoleculeNodeManager mManager;
	private InteractionComponentManager intCompMan;
	// private OntologyManager oManager=OntologyManager.getInstance();

	private InteractionNode currentInter = null;
	private Ontology currentOnto = null;

	private InteractionComponent currComponent = null;
	private ComponentModification currModification = null;

	MoleculeHandler molHandler;

	public PidFileHandler(final InteractionNodeManager iManager,
			final MoleculeNodeManager mManager,
			final PathwayNodeManager pManager,
			final ModificationManager modiManager,
			final InteractionComponentManager intCompMan)
			throws NoValidManagerSetException {
		if (null == iManager || mManager == null || pManager == null
				|| modiManager == null)
			throw new NoValidManagerSetException();
		this.iManager = iManager;
		this.molHandler = new MoleculeHandler(mManager, modiManager, intCompMan);
		this.mManager = mManager;
		this.pManager = pManager;
		this.modiManager = modiManager;
		this.intCompMan = intCompMan;
	}

	private String getInteractionTypeFromAttr(final Attributes attr) {
		return attr.getValue("interaction_type");
	}

	private String getInteractionIdFromAttr(final Attributes attr) {
		return attr.getValue("id");
	}

	private String getInteractionComponentRoleTypeFromAttr(final Attributes attr) {
		return attr.getValue("role_type");
	}

	private String getInteractionComponentMoleculeIdFromAttr(
			final Attributes attr) {
		return attr.getValue("molecule_idref");
	}

	private void handleInteraction(Attributes atts) throws InvalidIdException,
			InvalidArgumentException, UnknownOntologyException,
			UnknownOntologyElementException {
		if (PidTags.INTERACTIONLIST.isIn()) {
			final String id = this.getInteractionIdFromAttr(atts);
			final String type = this.getInteractionTypeFromAttr(atts);
			final InteractionNode newInteraction = new InteractionNode(
					InteractionNode.PREFIX + id);
			if (iManager.addInteraction(newInteraction)) {
				currentInter = newInteraction;
			} else {
				currentInter = iManager
						.getEqualInteractionNodeInManager(newInteraction);
			}
			currentInter.setType(type);
		}
	}

	private void handleInteractionComponent(Attributes atts)
			throws InvalidIdException, InvalidArgumentException,
			UnknownMoleculeException {
		if (PidTags.INTERACTIONLIST.isIn() && PidTags.INTERACTION.isIn()) {
			final String role_type = getInteractionComponentRoleTypeFromAttr(atts);
			final String mol_id = getInteractionComponentMoleculeIdFromAttr(atts);
			Ontology onto = OntologyManager.getInstance().getOntology(
					EdgeTypeOntology.NAME);
			if (mol_id != null) {
				MoleculeNode molNode = new MoleculeNode(MoleculeNode.PREFIX
						+ mol_id);
				molNode = mManager.getEqualMoleculeNodeInManager(molNode);

				if (null != molNode) {

					InteractionComponent newIntComp = new InteractionComponentImpl(
							molNode);
					OntologyElement roleType=onto.getElement(role_type);
					newIntComp.setRoleTypeForInteraction(currentInter.getFullPid(),roleType);
					this.currComponent = newIntComp;
				} else
					throw new UnknownMoleculeException(mol_id);

			}
		}
	}

	private void handleAbstraction(Attributes atts) throws InvalidIdException,
			InvalidArgumentException, UnknownOntologyElementException {
		if (PidTags.INTERACTIONLIST.isIn() && PidTags.INTERACTION.isIn()) {
			final String pathwayID = atts.getValue("pathway_idref");
			final String pathwayName = atts.getValue("pathway_name");
			if (pathwayID != null) {
				PathwayNode newPathway = new PathwayNode(PathwayNode.PREFIX
						+ pathwayID);
				if (!pManager.addPathway(newPathway)) {
					newPathway = pManager
							.getEqualPathwayNodeInManager(newPathway);
				}
				newPathway.setName(pathwayName);
				this.currentInter.addPathway(newPathway);
			}
		}
	}

	private void handlePositiveCondition(Attributes atts) {
		if (PidTags.INTERACTIONLIST.isIn() && PidTags.INTERACTION.isIn()) {
			final String posCond = atts.getValue("condition_type");
			this.currentInter.setPosConditions(posCond);
		}
	}

	private void handleLabelType(Attributes atts)
			throws UnknownOntologyException {
		if (PidTags.LABELTYPE.isIn()) {
			final String name = atts.getValue("name");
			final String id = atts.getValue("id");
			final OntologyManager man = OntologyManager.getInstance();
			final Ontology onto = OntologyManager.getInstance().getOntology(
					name);
			if (null == onto) {
				Ontology newOnto = SpecialOntologies.getValue(name)
						.newSpecialOntology(id, name);
				if (man.addOntology(newOnto))
					currentOnto = newOnto;
			} else
				currentOnto = onto;
		}

	}
	
	private void handleLabelTypeEnd() throws InconsistentOntologyException{
		if (currentOnto!=null)
		{
			currentOnto.connectOntologyElements();
		}
	}

	private void handleLabelValue(Attributes atts)
			throws InvalidArgumentException, UnknownOntologyElementException {
		if (PidTags.LABELTYPE.isIn() && PidTags.LABELVALUELIST.isIn()) {
			String name = atts.getValue("name");
			String id = atts.getValue("id");
			String parentID = atts.getValue("parent_idref");
			OntologyElement newElement = new OntologyElement(id, parentID, name);
			if (null == currentOnto)
				throw new UnknownOntologyElementException("Ontology element "
						+ name + " could not be added!");
			currentOnto.addElement(newElement);
		}
	}

	private void handleLabel(Attributes atts) {
		OntologyManager ontoManager = OntologyManager.getInstance();
		Ontology onto = ontoManager
				.getOntology(atts.getValue("label_type"));
		if (onto != null) {
			if (null == currModification)
				currModification = new ComponentModification();
			OntologyElement element = onto.getElement(atts
					.getValue("value"));
			if (onto.getClass() == LocationOntology.class) {
				if (PidTags.INTERACTIONCOMPONENT.isIn())
					currModification.setLocation(element);
				else if (PidTags.MOLECULE.isIn())
					molHandler.setLocation(element);				
					
			} else if (onto.getClass() == ActivityStateOntology.class) {
				if (PidTags.INTERACTIONCOMPONENT.isIn())
					currModification.setActivityState(element);
				else if (PidTags.MOLECULE.isIn())
					molHandler.setActivityState(element);
			}
		}
		 

	}

	private PTMterm getPTMterm(Attributes atts)
			throws UnknownOntologyElementException, InvalidArgumentException, InconsistentOntologyException {
		if (PidTags.PTMEXPRESSION.isIn() && PidTags.PTMTERM.isIn()) {
			OntologyManager ontoManager = OntologyManager.getInstance();
			Ontology onto = ontoManager.getOntology(PtmOntology.NAME);
			if (onto != null) {
				String mod = atts.getValue("modification");
				if (null == mod || mod.isEmpty())
					throw new UnknownOntologyElementException(
							"PTM-Term is declared, but there is no modification specified - will be ignored");
				OntologyElement modification = onto.getElement(mod);
				if (null == modification)
				{
					System.out.println("Modification '"+mod+"' is not known in '"+onto.getName()+"' Ontology! Will be automatically added to root...");
					//TODO Logger output for unknown ontology element
					onto.addInvalidElementToRoot(mod);
					modification = onto.getElement(mod);
				}
				String position = atts.getValue("position");
				String aa = atts.getValue("aa");
				return new PTMterm(position, aa, modification);
			}
		}
		return null;
	}

	private void handlePtmTerm(Attributes atts)
			throws UnknownOntologyException, UnknownOntologyElementException,
			InvalidArgumentException, InconsistentOntologyException {
		if (PidTags.INTERACTIONCOMPONENT.isIn()) {
			if (null == currModification)
				currModification = new ComponentModification();
			currModification.addModification(getPTMterm(atts));
		} else if (PidTags.MOLECULE.isIn()) {
			this.molHandler.handlePtmTerm(getPTMterm(atts));
		}
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		try {
			PidTags tag = PidTags.getValue(qName);
			tag.setIn(true);

			switch (tag) {
			case INTERACTION:
				handleInteraction(atts);
				break;
			case INTERACTIONCOMPONENT:
				handleInteractionComponent(atts);
				break;
			case ABSTRACTION:
				handleAbstraction(atts);
				break;
			case POSITIVECONDITION:
				handlePositiveCondition(atts);
				break;
			case LABELTYPE:
				handleLabelType(atts);
				break;
			case LABELVALUE:
				handleLabelValue(atts);
				break;
			case MOLECULE:
				molHandler.handleMolecule(atts);
				break;
			case NAME:
				molHandler.handleName(atts);
				break;
			case LABEL:
				handleLabel(atts);
				break;
			case PTMTERM:
				handlePtmTerm(atts);
				break;
			case MEMBER:
				molHandler.handleMember(atts);
				break;
			case PART:
				molHandler.handlePart(atts);
				break;
			case COMPLEXCOMPONENT:
				molHandler.handleComplexComponent(atts);
			default:
				break;
			}

		} catch (InvalidArgumentException iae) {
			iae.printStackTrace();
			throw new SAXException("Malformed syntax of xml-file!", iae);
		} catch (InvalidIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownOntologyElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownMoleculeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidUniProtId e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidEntrezGeneId e) {
			e.printStackTrace();
		} catch (InconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void handleInteractionComponentEnd()
			throws InvalidArgumentException, InvalidInteractionIdException {
		if (null != currModification) {
			String id = currComponent.getFullPid();
			Modification modification=currModification.getModification();
			if (modiManager.containsModification(id, modification))
			{
			
				currModification.setModification(modiManager.getEqualModification(id, modification));
			}
			else
				modiManager.addModificationforPid(id, modification);
			
			currComponent.setModification(currModification);
		}
		this.intCompMan.addInteractionComponent(currComponent);
		currModification = null;
		currComponent = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		PidTags tag = PidTags.getValue(qName);
		try {
			switch (tag) {
			case INTERACTIONCOMPONENT:
				handleInteractionComponentEnd();
				break;
			case MOLECULE:
				molHandler.handleMoleculeEnd();
				break;
			case MEMBER:
				molHandler.handleMemberEnd();
				break;
			case COMPLEXCOMPONENT:
				molHandler.handleComplexComponentEnd();
				break;
			case LABELTYPE:
				handleLabelTypeEnd();
			default:
				break;
			}
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tag.setIn(false);
	}

	/**
	 * @return the complexComponents
	 */
	public final Map<String, Collection<CompMolMember>> getComplexComponents() {
		return molHandler.getComplexComponents();
	}

	/**
	 * @return the multipleMolecules
	 */
	public final Map<String, Collection<CompMolMember>> getFamilyMembers() {
		return molHandler.getFamilyMembers();
	}

}
