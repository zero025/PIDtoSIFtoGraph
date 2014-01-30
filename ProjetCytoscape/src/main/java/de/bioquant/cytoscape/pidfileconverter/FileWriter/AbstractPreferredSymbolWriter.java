package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.PrintWriter;
import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public abstract class AbstractPreferredSymbolWriter extends
		AbstractNodeAttributeWriter {

	private NameCreator naming = CreatorIDWithModification.getInstance();

	abstract String getPreferredSymbol(CompMolMember molMember);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.FileWriter.
	 * AbstractNodeAttributeWriter#writeAttributes(java.io.PrintWriter,
	 * de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.NodeManager.NodeManagerImpl)
	 */
	@Override
	public void writeAttributes(PrintWriter writer, NodeManagerImpl manager) {
		Collection<InteractionComponent> components = manager
				.getAllInteractionComponents();
		for (CompMolMember node : components) {
			String id = naming.getNameForCompMolMember(node);

			String symbol = getPreferredSymbol(node);

			TupelWriter.printTupel(writer, id, symbol);

		}

		Collection<InteractionNode> interactions = manager.getAllInteractions();
		for (InteractionNode inter : interactions) {
			String symbol = naming.getNameForInteraction(inter);
			if (symbol.equals(inter.getFullPid()))
				TupelWriter.printTupel(writer, symbol, "");
			else
				TupelWriter.printTupel(writer, symbol, symbol);
			
			if (inter.hasPosCondition()) {
				String posCond = inter.getPosCondition();
				TupelWriter.printTupel(writer, posCond, posCond);
			}
		}

		Collection<PathwayNode> pathways = manager.getAllPathways();
		for (PathwayNode pathway : pathways) {
			String left = naming.getNameForPathway(pathway);
			TupelWriter.printTupel(writer, left, left);
		}

	}

}
