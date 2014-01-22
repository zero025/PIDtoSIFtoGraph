package de.bioquant.cytoscape.pidfileconverter.FileWriter.MemberExpansion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import de.bioquant.cytoscape.pidfileconverter.Analyzer.IntCompAnalyzer;
import de.bioquant.cytoscape.pidfileconverter.Analyzer.IntCompAnalyzerImpl;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.FileWriter;
import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorUniprotWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.InconsistentOntologyException;
import de.bioquant.cytoscape.pidfileconverter.Ontology.Exceptions.UnknownOntologyException;

public abstract class AbstractExpandMolWriter implements FileWriter {

	public final static String FAMMEMBERCONNECTOR = "isFamMemberOf";
	public final static String COMPCOMPONENTCONNECTOR = "isCompMemberOf";
	public final static String PARTMOLCONNECTOR = "isPartMolOf";

	private Set<String> writtenLines = new TreeSet<String>();

	private NameCreator naming = CreatorUniprotWithModification.getInstance();

	protected boolean duplicateExclude = true;

	@Override
	public void write(String path, NodeManagerImpl manager)
			throws FileNotFoundException {
		java.io.FileWriter wr;
		PrintWriter writer = null;
		try {
			wr = new java.io.FileWriter(path, true);
			writer = new PrintWriter(wr);
			IntCompAnalyzer analyzer = IntCompAnalyzerImpl.getInstance();

			Collection<InteractionComponent> intComps = manager
					.getAllInteractionComponents();
			for (InteractionComponent intComp : intComps) {
				if (!analyzer.hasPredecessors(intComp)
						&& intComp.isConnectedToMolecule()) {
					MoleculeNode molecule = intComp.getMolecule();
					if (molecule.hasFamilyMembers()) {
						Collection<CompMolMember> members = molecule
								.getFamilyMembers();
						writeMembers(writer, intComp, members,
								FAMMEMBERCONNECTOR);

					}
					if (molecule.hasComplexComponents()) {
						Collection<CompMolMember> members = molecule
								.getComplexComponents();
						writeMembers(writer, intComp, members,
								COMPCOMPONENTCONNECTOR);
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InconsistentOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != writer)
				writer.close();
		}

	}

	private void writeMembers(PrintWriter writer, CompMolMember intComp,
			Collection<CompMolMember> members, String connector) {
		String interCompString = naming.getNameForCompMolMember(intComp);
		for (CompMolMember member : members) {
			String memberString = naming.getNameForCompMolMember(member);

			String fullString = memberString + connector + interCompString;
			if (duplicateExclude && writtenLines.contains(fullString))
				continue;
			writtenLines.add(fullString);
			writeLine(writer, memberString, connector, interCompString);
			
			MoleculeNode molecule = member.getMolecule();
			if (molecule.hasComplexComponents() || molecule.hasFamilyMembers()) {
				if (molecule.hasComplexComponents())
					writeMembers(writer, member,
							molecule.getComplexComponents(),
							COMPCOMPONENTCONNECTOR);
				if (molecule.hasFamilyMembers())
					writeMembers(writer, member, molecule.getFamilyMembers(),
							FAMMEMBERCONNECTOR);

			}

		}

	}

	protected abstract void writeLine(PrintWriter writer, String string1,
			String string2, String string3);

}
