package de.bioquant.cytoscape.pidfileconverter.FileWriter;

import java.io.PrintWriter;

public class TupelWriter {

	public static void printTriple(PrintWriter out, String left, String middle, String right) {
		out.println(left + "\t" + middle + "\t" + right);
	}

	public static void printTupel(PrintWriter out, String left, String right) {
		out.println(left + " = " + right);
	}
}
