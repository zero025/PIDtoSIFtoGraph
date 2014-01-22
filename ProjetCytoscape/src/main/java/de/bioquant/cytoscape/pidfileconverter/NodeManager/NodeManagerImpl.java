package de.bioquant.cytoscape.pidfileconverter.NodeManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidArgumentException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.Modification;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyManager;

public final class NodeManagerImpl implements InteractionNodeManager,
		PathwayNodeManager, MoleculeNodeManager, ModificationManager,
		InteractionComponentManager, GraphConnector {

	private static NodeManagerImpl instance = null;
	protected final static OntologyManager ONTOMANAGER = OntologyManager
			.getInstance();

	private Map<String, InteractionNode> interactions = new HashMap<String, InteractionNode>();
	private Map<String, PathwayNode> pathways = new HashMap<String, PathwayNode>();
	private Map<String, MoleculeNode> molecules = new HashMap<String, MoleculeNode>();
	private Map<String, ArrayList<Modification>> modifications = new TreeMap<String, ArrayList<Modification>>();
	private SortedSet<InteractionComponent> interComps = new TreeSet<InteractionComponent>();

	private NodeManagerImpl() {
	}

	public static NodeManagerImpl getInstance() {
		if (instance == null)
			instance = new NodeManagerImpl();
		return instance;
	}

	public void reset() {
		this.deleteAllInteractions();
		this.deleteAllPathways();
		this.deleteAllMolecules();
	}

	@Override
	public boolean addPathway(PathwayNode newPathway) {
		String id = newPathway.getPid();
		if (pathways.containsKey(id))
			return false;
		else {
			pathways.put(id, newPathway);
			return true;
		}
	}

	@Override
	public boolean deletePathway(PathwayNode pathwayToDelete) {
		PathwayNode node = pathways.remove(pathwayToDelete.getPid());
		return node != null;
	}

	@Override
	public boolean addInteraction(InteractionNode newInteraction) {
		String id = newInteraction.getPid();
		if (interactions.containsKey(id))
			return false;
		else {
			interactions.put(id, newInteraction);
			return true;
		}
	}

	@Override
	public boolean deleteInteraction(final InteractionNode interactionToDelete) {
		InteractionNode node = interactions
				.remove(interactionToDelete.getPid());
		return node != null;
	}

	@Override
	public void deleteAllInteractions() {
		interactions.clear();
	}

	@Override
	public boolean containsPathway(final PathwayNode pathway) {
		return pathways.containsKey(pathway.getPid());
	}

	@Override
	public boolean containsInteraction(final InteractionNode interaction) {
		return interactions.containsKey(interaction.getPid());
	}

	@Override
	public PathwayNode getEqualPathwayNodeInManager(final PathwayNode pathway) {

		return pathways.get(pathway.getPid());

	}

	@Override
	public InteractionNode getEqualInteractionNodeInManager(
			final InteractionNode interaction) {
		return interactions.get(interaction.getPid());
	}

	@Override
	public void deleteAllPathways() {
		this.pathways.clear();
	}

	@Override
	public int getInteractionCount() {
		return interactions.size();
	}

	@Override
	public int getPathwayCount() {
		return pathways.size();
	}

	@Override
	public boolean addMolecule(MoleculeNode newMolecule) {
		String id = newMolecule.getPid();
		if (molecules.containsKey(id))
			return false;
		else {
			molecules.put(id, newMolecule);
			return true;
		}
	}

	@Override
	public boolean containsMolecule(MoleculeNode molecule) {
		return molecules.containsKey(molecule.getPid());
	}

	@Override
	public boolean deleteMolecule(MoleculeNode moleculeToDelete) {
		MoleculeNode node = molecules.remove(moleculeToDelete.getPid());
		return node != null;
	}

	@Override
	public MoleculeNode getEqualMoleculeNodeInManager(
			final MoleculeNode molecule) {
		return molecules.get(molecule.getPid());
	}

	@Override
	public void deleteAllMolecules() {
		molecules.clear();
	}

	@Override
	public int getMoleculeCount() {
		return molecules.size();
	}

	@Override
	public Collection<PathwayNode> getAllPathways() {
		return pathways.values();
	}

	@Override
	public Collection<InteractionNode> getAllInteractions() {
		return interactions.values();
	}

	@Override
	public Collection<MoleculeNode> getAllMolecules() {
		return molecules.values();
	}

	@Override
	public boolean addModificationforPid(String pid, Modification modification) {
		if (modifications.containsKey(pid)) {
			ArrayList<Modification> list = modifications.get(pid);
			if (list.contains(modification))
				return false;
			else
				return list.add(modification);
		} else {
			ArrayList<Modification> list = new ArrayList<Modification>();
			modifications.put(pid, list);
			return list.add(modification);
		}
	}

	@Override
	public Modification getEqualModification(String pid,
			Modification modification) {
		if (modifications.containsKey(pid)) {
			ArrayList<Modification> list = modifications.get(pid);
			for (Modification mod : list) {
				if (mod.equals(modification))
					return mod;
			}
		}
		return null;
	}

	@Override
	public boolean containsModification(String pid, Modification modification) {
		if (modifications.containsKey(pid)) {
			ArrayList<Modification> list = modifications.get(pid);
			for (Modification mod : list) {
				if (mod.equals(modification))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean addInteractionComponent(InteractionComponent component)
			throws InvalidInteractionIdException, InvalidArgumentException {
		if (!containsInteractionComponent(component))
			return interComps.add(component);
		else {
			InteractionComponent oldComp = getEqualInteractionComponent(component);
			for (String comp : component.getComplexes())
				oldComp.addComplexConnection(comp);
			for (String comp : component.getFamilies())
				oldComp.addFamilyConnection(comp);
			return oldComp.setRoleTypesForInteractions(component
					.getRolesForInteraction());
		}
	}

	@Override
	public boolean containsInteractionComponent(InteractionComponent component) {
		for (InteractionComponent intComp : interComps)
			if (intComp.equals(component))
				return true;
		// return interComps.contains(component);
		return false;
	}

	@Override
	public boolean deleteInteractionComponent(InteractionComponent component) {
		return interComps.remove(component);
	}

	@Override
	public Collection<InteractionComponent> getAllInteractionComponents() {
		return Collections.unmodifiableCollection(interComps);
	}

	@Override
	public int getInteractionComponentCount() {
		return interComps.size();
	}

	@Override
	public void deleteAllInteractionComponents() {
		interComps.clear();

	}

	@Override
	public void connectIntCompsToMolecules(MoleculeNodeManager molManager,
			InteractionComponentManager intCompManager) {
		Collection<InteractionComponent> intComps = intCompManager
				.getAllInteractionComponents();
		for (InteractionComponent intComp : intComps) {
			try {
				MoleculeNode memMolecule = new MoleculeNode(
						intComp.getFullPid());
				memMolecule = molManager
						.getEqualMoleculeNodeInManager(memMolecule);
				if (null == memMolecule)
					System.out
							.println("InteractionComponent could not be connected to molecule "
									+ intComp.getFullPid()
									+ " Molecule is not known!");
				intComp.setMolecule(memMolecule);
			} catch (InvalidIdException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void connectInteractionsToInteractionComponents(
			InteractionComponentManager intCompManager,
			InteractionNodeManager interactionManager) {
		Collection<InteractionComponent> intComps = intCompManager
				.getAllInteractionComponents();
		for (InteractionComponent intComp : intComps) {
			Collection<String> interIds = intComp.getInteractionsIds();
			for (String id : interIds) {
				InteractionNode interaction = this.interactions.get(id);
				try {
					if (null == interaction)
						System.out
								.println("InteractionComponent could not be connected to interaction "
										+ id + "! Interaction is not known!");
					else
						interaction.addInteractionComponent(intComp);
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public InteractionComponent getEqualInteractionComponent(
			InteractionComponent component) {
		/*
		 * SortedSet<InteractionComponent> comps=interComps.tailSet(component);
		 * if (comps.first().equals(component)) return comps.first(); else
		 * return null;
		 */
		for (InteractionComponent intComp : interComps) {
			if (intComp.equals(component))
				return intComp;
		}
		return null;
	}

}
