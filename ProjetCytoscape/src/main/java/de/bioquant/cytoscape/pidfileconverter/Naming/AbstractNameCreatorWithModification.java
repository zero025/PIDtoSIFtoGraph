package de.bioquant.cytoscape.pidfileconverter.Naming;

import java.util.ArrayList;
import java.util.List;

import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.ComponentModification;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionNode;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Model.PathwayNode;
import de.bioquant.cytoscape.pidfileconverter.Ontology.OntologyElement;

public abstract class AbstractNameCreatorWithModification implements
		NameCreator {

	public final static String FAMSEPERATOR = "::";
	public final static String COMPLEXSEPERATOR = ":";

	@Override
	public String getNameForInteraction(final InteractionNode interaction) {
		if (null == interaction)
			return null;
		OntologyElement type = interaction.getType();
		if (type.isRootChild())
			return interaction.getFullPid();
		else
			return type.getName().replaceAll(" ", "_");
	}

	@Override
	public String getNameForCompMolMember(final CompMolMember component) {
		if (null == component)
			return null;
		if (component.isConnectedToMolecule()) {
			MoleculeNode molecule = component.getMolecule();

			StringBuffer string = new StringBuffer(
					this.getNameForMolecule(molecule));
			if (component.hasModification()) {
				string.append(ModificationExtractor
						.getModificationString(component.getModification()));
			}
			return string.toString().replaceAll(" ", "_");
		}
		return "";

	}

	@Override
	public String getNameForCompMolMemberManagement(
			final CompMolMember component) {
		if (null == component)
			return null;

		StringBuffer string = new StringBuffer(component.getFullPid());
		if (component.hasModification()) {
			string.append(ModificationExtractor.getModificationString(component
					.getModification()));
		}
		return string.toString().replaceAll(" ", "_");
	}

	@Override
	public String getNameForPathway(final PathwayNode pathway) {
		String name = pathway.getName();
		if (name.isEmpty())
			return pathway.getFullPid();
		else
			return name.replaceAll(" ", "_");
	}

	public String getId(final MoleculeNode molecule) {
		if (molecule.hasUniprot())
			return molecule.getUniProdID();
		else if (molecule.hasEntrezGene())
			return molecule.getEntrezGeneID();
		else
			return molecule.getFullPid();
	}

	private String getMemberString(List<CompMolMember> list, String sepString) {
		StringBuffer result = new StringBuffer();

		ArrayList<String> memberStrings = new ArrayList<String>();
		for (CompMolMember member : list) {
			StringBuffer memberString = new StringBuffer();
			MoleculeNode molecule = member.getMolecule();
			memberString.append(getId(molecule));
			if (member.hasModification()) {
				ComponentModification mod = member.getModification();
				memberString.append(ModificationExtractor
						.getModificationString(mod));
			}
			String finalMmber = memberString.toString();
			if (!memberStrings.contains(finalMmber)) {
				memberStrings.add(finalMmber);
				result.append(memberString);
				result.append(sepString);
			}
		}
		if (result.length() == 0)
			return "";
		return result.substring(0, result.length() - 1);
	}

	@Override
	public String getNameForMolecule(MoleculeNode molecule) {
		StringBuffer string = new StringBuffer();
		if (molecule.hasComplexComponents()) {
			if (string.length() == 0)
				string.append("[");
			string.append(getMemberString(molecule.getComplexComponents(),
					COMPLEXSEPERATOR));
		}
		if (molecule.hasFamilyMembers()) {
			if (string.length() == 0)
				string.append("[");
			string.append(getMemberString(molecule.getFamilyMembers(),
					FAMSEPERATOR));
		}

		if (string.length() == 0)
			string.append(getId(molecule));
		else
			string.append("]");
		return string.toString().replaceAll(" ", "_");

	}

}
