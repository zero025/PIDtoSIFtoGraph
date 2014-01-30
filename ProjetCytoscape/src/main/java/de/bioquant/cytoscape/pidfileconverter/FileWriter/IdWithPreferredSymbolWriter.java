package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.PrintWriter;
import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorPreferredSymbolWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public final class IdWithPreferredSymbolWriter extends AbstractNodeAttributeWriter {

	private static IdWithPreferredSymbolWriter instance;
	private NameCreator naming = CreatorIDWithModification.getInstance();
	private NameCreator naming2 = CreatorPreferredSymbolWithModification.getInstance();
	
	
	private IdWithPreferredSymbolWriter(){}
	
	public static IdWithPreferredSymbolWriter getInstance() {
		if (null == instance)
			instance = new IdWithPreferredSymbolWriter();
		return instance;
	}
	
	@Override
	public String getAttributeName() {
		return "ID_PREFERRED_SYMBOL";
	}

	@Override
	public void writeAttributes(PrintWriter writer,NodeManagerImpl manager) {
		Collection<InteractionComponent> intComps=manager.getAllInteractionComponents();
		for (InteractionComponent node : intComps) {
			String id = naming.getNameForCompMolMember(node);
			
			String idPref = naming2.getNameForCompMolMember(node);
			TupelWriter.printTupel(writer, id, idPref);
		}
		Collection<InteractionNode> interactions=manager.getAllInteractions();
		for (InteractionNode inter:interactions){
			String id=naming.getNameForInteraction(inter);
			TupelWriter.printTupel(writer, id, id);
		}
		
	}

}
