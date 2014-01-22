package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.PrintWriter;
import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorUniprotWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;

public final class NodeTypeAttributeForUniprotWithModWriter extends
		AbstractNodeAttributeWriter {

	private static NodeTypeAttributeForUniprotWithModWriter instance = null;
	private NameCreator naming = CreatorUniprotWithModification.getInstance();

	private NodeTypeAttributeForUniprotWithModWriter() {
	}

	public static NodeTypeAttributeForUniprotWithModWriter getInstance() {
		if (instance == null)
			instance = new NodeTypeAttributeForUniprotWithModWriter();
		return instance;
	}

	protected void writeInteractions(PrintWriter out, NodeManagerImpl manager) {
		Collection<InteractionNode> interactions = manager.getAllInteractions();
		for (InteractionNode inter : interactions) {
			String left = naming.getNameForInteraction(inter);

			String right;
			OntologyElement type = inter.getType();
			if (!type.isRootChild() && type.getParent()!=null)
				
				right = type.getParent().getName();
			else
				right=type.getName();			
				
			TupelWriter.printTupel(out, left, right);
			if (inter.hasPosCondition())
			{
				String posCond=inter.getPosCondition();
				TupelWriter.printTupel(out, posCond, "positive_condition");
			}
		}
	}
	
	public void writeMolecules(PrintWriter out, NodeManagerImpl manager) {
		Collection<InteractionComponent> components = manager
				.getAllInteractionComponents();
		for (InteractionComponent node : components) {

			String id = naming.getNameForCompMolMember(node);

			MoleculeNode mol = node.getMolecule();
			String type = mol.getType().getName();

			TupelWriter.printTupel(out, id, type);
		}
	}
	
	protected void writePathways(PrintWriter out, NodeManagerImpl manager) {
		Collection<PathwayNode> pathways = manager.getAllPathways();
		for (PathwayNode pathway : pathways) {
			String left=pathway.getName();
			if (left.isEmpty()) left=pathway.getFullPid();
			String right = "pathway";
			TupelWriter.printTupel(out, left, right);
		}
	}

	@Override
	public String getAttributeName() {
		return "NODE_TYPE";
	}

	@Override
	public void writeAttributes(PrintWriter writer, NodeManagerImpl manager) {
		writeInteractions(writer, manager);
		writeMolecules(writer, manager);
		writePathways(writer, manager);
		
	}

}
