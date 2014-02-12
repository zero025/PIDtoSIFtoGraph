package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.PrintWriter;
import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public final class UniprotIdForUniprotWithModWriter extends AbstractNodeAttributeWriter implements FileWriter {

	private static UniprotIdForUniprotWithModWriter instance = null;
	private NameCreator naming = CreatorIDWithModification.getInstance();

	private UniprotIdForUniprotWithModWriter() {
	}

	public static UniprotIdForUniprotWithModWriter getInstance() {
		if (instance == null) {
			instance = new UniprotIdForUniprotWithModWriter();
		}
		return instance;
	}

	@Override
	public String getAttributeName() {
		return "Uniprot_ID";
	}

	@Override
	public void writeAttributes(PrintWriter writer, NodeManagerImpl manager) {
		Collection<InteractionComponent> components = manager.getAllInteractionComponents();
		for (InteractionComponent node : components) {

			String id = naming.getNameForCompMolMember(node);
			MoleculeNode mol = node.getMolecule();
			if (mol.hasUniprot()) {
				String uniprot = mol.getUniProdID();
				TupelWriter.printTupel(writer, id, uniprot);
			}
		}
	}
}