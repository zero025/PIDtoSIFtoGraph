package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.InteractionComponent;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.Naming.CreatorUniprotWithModification;
import de.bioquant.cytoscape.pidfileconverter.Naming.NameCreator;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public final class StatisticsWriter implements FileWriter {

	private static StatisticsWriter instance = null;
	private NameCreator naming = CreatorUniprotWithModification.getInstance();
	
	private int count;

	private StatisticsWriter() {
	}

	public static StatisticsWriter getInstance() {
		if (instance == null)
			instance = new StatisticsWriter();
		return instance;
	}

	@Override
	public void write(String path, NodeManagerImpl manager)
			throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(path);

		List<String> out = new ArrayList<String>();
		out.add("Molecule");
		out.add("Families");
		out.add("FamilyCount");
		out.add("Complexes");
		out.add("ComplexCount");
		writeCsvLine(writer, out);

		Collection<InteractionComponent> intComps = manager
				.getAllInteractionComponents();
		for (CompMolMember comp : intComps) {
			try {
				List<String> output = new ArrayList<String>();
				output.add(naming.getNameForCompMolMember(comp));
				Collection<String> families = comp.getFamilies();

				output.add(getParentString(families, "::", manager));

				output.add("" + count);
				Collection<String> complexes = comp.getComplexes();
				output.add(getParentString(complexes, "::", manager));
				output.add("" + count);
				writeCsvLine(writer, output);
			} catch (InvalidIdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		writer.close();

	}

	private void writeCsvLine(PrintWriter writer, List<String> values) {
		StringBuffer output = new StringBuffer();
		for (String value : values)
			output.append(value + ";");
		String out = output.toString();
		if (out.length() > 0)
			out = out.substring(0, out.length() - 1);
		writer.println(out);

	}

	private String getParentString(Collection<String> parents,
			String seperator, NodeManagerImpl manager)
			throws InvalidIdException {
		StringBuffer result = new StringBuffer();
		count=0;
		for (String mol : parents) {
			Collection<InteractionComponent> intComps = manager
			.getAllInteractionComponents();
			for (InteractionComponent intComp:intComps)
			{
				if (mol.equals(intComp.getFullPid()) && !intComp.getInteractionsIds().isEmpty())
				{
					MoleculeNode molecule=new MoleculeNode(mol);
					molecule=manager.getEqualMoleculeNodeInManager(molecule);
					result.append(naming.getNameForMolecule(molecule) + seperator);
					count++;
					break;
				}
			}

		}
		if (result.length() > 0)
			return result.substring(0, result.length() - seperator.length());
		return "";

	}

}
