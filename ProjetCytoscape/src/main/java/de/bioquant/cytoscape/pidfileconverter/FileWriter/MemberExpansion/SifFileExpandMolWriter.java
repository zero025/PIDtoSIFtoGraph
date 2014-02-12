package de.bioquant.cytoscape.pidfileconverter.FileWriter.MemberExpansion;

import java.io.PrintWriter;

import de.bioquant.cytoscape.pidfileconverter.FileWriter.TupelWriter;

public final class SifFileExpandMolWriter extends AbstractExpandMolWriter {

	private static SifFileExpandMolWriter instance = null;

	private SifFileExpandMolWriter() {
	}

	public static SifFileExpandMolWriter getInstance() {
		if (instance == null) {
			instance = new SifFileExpandMolWriter();
			instance.duplicateExclude = false;
		}
		return instance;
	}

	@Override
	protected void writeLine(PrintWriter writer, String string1, String string2, String string3) {
		TupelWriter.printTriple(writer, string1, string2, string3);
	}
}
