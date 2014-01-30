package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.PrintWriter;
import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public final class PidForIDWithModWriter extends AbstractNodeAttributeWriter implements FileWriter {

	private static PidForIDWithModWriter instance = null;
	private NameCreator naming = CreatorIDWithModification.getInstance();

	private PidForIDWithModWriter() {
	}

	public static PidForIDWithModWriter getInstance() {
		if (null == instance)
			instance = new PidForIDWithModWriter();
		return instance;
	}

	@Override
	public String getAttributeName() {
		return "PID";
	}

	@Override
	public void writeAttributes(PrintWriter writer, NodeManagerImpl manager) {
		Collection<InteractionNode> interactions = manager.getAllInteractions();
		for (InteractionNode inter : interactions) {
			String name = naming.getNameForInteraction(inter);
			TupelWriter.printTupel(writer, name, inter.getFullPid());
		}
		Collection<InteractionComponent> components = manager
				.getAllInteractionComponents();
		for (InteractionComponent node : components) {
			String id = naming.getNameForCompMolMember(node);
			TupelWriter.printTupel(writer, id, node.getFullPid());

		}
		
		Collection<PathwayNode> pathways=manager.getAllPathways();
		for (PathwayNode pathway:pathways)
		{
			String name=naming.getNameForPathway(pathway);
			TupelWriter.printTupel(writer, name, pathway.getFullPid());
		}
		
	}

}
