/**
 * @author Yamei Sun & Thomas Brunel
 */

package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.PrintWriter;
import java.util.Collection;

import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public final class EntrezGeneIdforEntrezGeneWithModWriter extends AbstractNodeAttributeWriter implements FileWriter {

	private static EntrezGeneIdforEntrezGeneWithModWriter instance = null;
	private NameCreator naming = CreatorIDWithModification.getInstance();

	private EntrezGeneIdforEntrezGeneWithModWriter() {
	}

	public static EntrezGeneIdforEntrezGeneWithModWriter getInstance() {
		if (instance == null)
			instance = new EntrezGeneIdforEntrezGeneWithModWriter();
		return instance;
	}

	@Override
	public String getAttributeName() {
		return "EntrezGene_ID class=String";
	}

	@Override
	public void writeAttributes(PrintWriter writer, NodeManagerImpl manager) {
		Collection<InteractionComponent> components = manager.getAllInteractionComponents();
		for (InteractionComponent node : components) {

			String id = naming.getNameForCompMolMember(node);
			MoleculeNode mol = node.getMolecule();
			if (mol.hasEntrezGene()) {
				String entrezGene = mol.getEntrezGeneID();
				TupelWriter.printTupel(writer, id, entrezGene);
			}
		}
	}
}
