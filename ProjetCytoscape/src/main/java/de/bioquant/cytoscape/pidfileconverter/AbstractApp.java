/**
 * 
 */
package de.bioquant.cytoscape.pidfileconverter;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.FileParsingException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.NoValidManagerSetException;
import de.bioquant.cytoscape.pidfileconverter.FileReader.FileReader;
import de.bioquant.cytoscape.pidfileconverter.FileReader.PidFileReader;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.ExtPreferredSymbolWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.FileWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.IdWithPreferredSymbolWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.ModificationsWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.NodeTypeAttributeForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.PidForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.PreferredSymbolForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.SifFileWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.UniprotIdForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.MemberExpansion.SifFileExpandMolWriter;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import de.bioquant.cytoscape.pidfileconverter.View.Controller;
import de.bioquant.cytoscape.pidfileconverter.View.MainFrame;

/**
 * @author Florian Dittmann
 * @author Hadi Adisurya Kang (extensions)
 * 
 */
public abstract class AbstractApp {

	private Controller controller;
	private MainFrame mainframe;
	
	public void run() throws Exception
	{
		//macode
		mainframe = new MainFrame(controller);
		mainframe.setTitle("Plugin");
		mainframe.setLocation(10, 10);
		mainframe.setSize(800, 400);
		mainframe.setResizable(false);
		mainframe.setVisible(true);
		mainframe.pack();
		
	/*******************************************************************/	
//		BufferedReader console = new BufferedReader(new InputStreamReader(
//		System.in));
//		System.out
//		.print("How many pid-database xml-files would you like to read: ");
//		ArrayList<String> inputFiles = new ArrayList<String>();
//		String path = "";
//		String outputfile = "";
//		boolean expand = false;
//		try {
//			String row = console.readLine();
//			if (row == null) {
//				System.out.println("Incorrect number of input files! EXIT");
//				return;
//			}
//			int count = Integer.parseInt(row.trim());
//			System.out.println();
//			for (int i = 1; i <= count; i++) {
//				System.out.print("input file [" + i + "]: ");
//				row = console.readLine();
//				inputFiles.add(row);
//				System.out.println();
//			}
//
//			System.out.print("Expand members? (y/n): ");
//			row = console.readLine();
//			if (row != null && row.equals("y"))
//				expand = true;
//			System.out.println();
//
//			System.out.print("output path (optional): ");
//			row = console.readLine();
//			if (row != null && !row.isEmpty())
//				path = row + "\\";
//			System.out.println();
//
//			System.out.print("output sif-file-name (optional): ");
//			row = console.readLine();
//
//			if (null != row && !row.isEmpty())
//				outputfile = path + row;
//			else
//				outputfile = path + "GRAPH.sif";
//			System.out.println();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//
//		NodeManagerImpl manager = NodeManagerImpl.getInstance();
//
//		FileReader reader = PidFileReader.getInstance();
//		try {
//			for (String file : inputFiles) {
//				System.out.println("Reading file: " + file);
//				reader.read(file);
//			}
//		} catch (NoValidManagerSetException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (FileParsingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		System.out.println("Writing files...");
//
//		FileWriter writer = SifFileWriter.getInstance();
//		try {
//			writer.write(outputfile, manager);
//			if (expand) {
//				writer = SifFileExpandMolWriter.getInstance();
//				writer.write(outputfile, manager);
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		FileWriter nWriter = NodeTypeAttributeForUniprotWithModWriter
//				.getInstance();
//		try {
//			nWriter.write(path + "NODE_TYPE.NA", manager);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		FileWriter uWriter = UniprotIdForUniprotWithModWriter.getInstance();
//		try {
//			uWriter.write(path + "UNIPROT.NA", manager);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		FileWriter modiWriter = ModificationsWriter.getInstance();
//		try {
//			modiWriter.write(path + "MODIFICATIONS.NA", manager);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		FileWriter pWriter = PreferredSymbolForUniprotWithModWriter
//				.getInstance();
//		try {
//			pWriter.write(path + "PREFERRED_SYMBOL.NA", manager);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		pWriter = ExtPreferredSymbolWriter.getInstance();
//		try {
//			pWriter.write(path + "PREFERRED_SYMBOL_EXT.NA", manager);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		FileWriter pidWriter = PidForUniprotWithModWriter.getInstance();
//		try {
//			pidWriter.write(path + "PID.NA", manager);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		FileWriter prefIdWriter = IdWithPreferredSymbolWriter.getInstance();
//		try {
//			prefIdWriter.write(path + "ID_PREF.NA", manager);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			this.extraOutput(console, manager, path);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			console.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		
//		System.out.println("Writing files: DONE");

		/*******************************************************************/	
		
	}

	public abstract void extraOutput(BufferedReader reader,
			NodeManagerImpl manager, String path) throws FileNotFoundException;

}
