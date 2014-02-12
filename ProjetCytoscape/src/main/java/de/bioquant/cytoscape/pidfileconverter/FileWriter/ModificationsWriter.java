package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.ComponentModification;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorIDWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.ModificationExtractor;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public final class ModificationsWriter extends AbstractNodeAttributeWriter implements FileWriter {

	private static ModificationsWriter instance = null;
	private NameCreator naming = CreatorIDWithModification.getInstance();

	private ModificationsWriter() {
	}

	public static ModificationsWriter getInstance() {
		if (null == instance) {
			instance = new ModificationsWriter();
		}
		return instance;
	}

	@Override
	public String getAttributeName() {
		return "MODIFICATIONS";
	}

	@Override
	public void writeAttributes(PrintWriter writer, NodeManagerImpl manager) {
		Collection<InteractionComponent> components = manager.getAllInteractionComponents();
		for (InteractionComponent node : components) {
			String id = naming.getNameForCompMolMember(node);
			if (node.hasModification()) {
				ComponentModification mod = node.getModification();
				if (mod.hasAnyModifications()) {
					String modis = ModificationExtractor.getDetailedModifactionString(mod);
					TupelWriter.printTupel(writer, id, modis);

				}
			}
			MoleculeNode molecule = node.getMolecule();
			if (molecule.hasFamilyMembers()) {
				List<CompMolMember> members = molecule.getFamilyMembers();
				String memberString = ModificationExtractor.getDetailledModificatonStringForComplexMolecules(members,
						CreatorIDWithModification.FAMSEPERATOR);
				TupelWriter.printTupel(writer, id, memberString);
			}

			if (molecule.hasComplexComponents()) {
				List<CompMolMember> members = molecule.getComplexComponents();
				String memberString = ModificationExtractor.getDetailledModificatonStringForComplexMolecules(members,
						CreatorIDWithModification.COMPLEXSEPERATOR);
				TupelWriter.printTupel(writer, id, memberString);
			}
		}
	}
}
