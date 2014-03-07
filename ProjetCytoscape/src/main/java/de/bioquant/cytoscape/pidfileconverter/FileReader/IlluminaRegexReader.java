/**
 * This class deals with the step 2, Illumina, of the plugin.
 * @contributor Yamei Sun & Thomas Brunel
 */
package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import de.bioquant.cytoscape.pidfileconverter.View.Controller;
import de.bioquant.cytoscape.pidfileconverter.View.SplashFrame;

public class IlluminaRegexReader {
	
	//counter of suppressed nodes
	private static int counter = 0;

	// regular expression of e.g. [P98170::Q13490::Q13489:]
	private final static String regex1 = "(\\[*([POQ]\\d|A-Z\\d|A-Z\\d|A-Z\\d|A-Z\\d|A-Z).*)+";

	//nodes to be deleted
	private static ArrayList<String> nodeIDtobedeleted = new ArrayList<String>();

	// the hashmap for the barcode values. Key=GeneID, Value=true(expressed) or false(not expressed)
	private static HashMap<String, Boolean> barcodehashmap = new HashMap<String, Boolean>();

	public IlluminaRegexReader() {
	}

	/**
	 * This method reads one Illumina file and assigns a value (barcode) to a
	 * key (GeneID) and puts them into a hashmap, 1 or 0 from
	 * barcodeFilter(String line)
	 * 
	 * @param barcodefilepath
	 * 				Illumina file 
	 * @param inputCondition1Text
	 * 		 		first condition given by the user
	 * @param inputCondition2Text
	 * 				second condition given by the user
	 * @throws IOException
	 */
	public static void fileReader(String barcodefilepath,
			String inputCondition1Text, String inputCondition2Text)
			throws IOException {
		BufferedReader reader = null;
		try {
			String condition1 = "\"" + inputCondition1Text + ".mean\"";
			String condition2 = "\"" + inputCondition2Text + ".mean\"";
			
			reader = new BufferedReader(new FileReader(barcodefilepath));

			// First line
			String line = reader.readLine();
			String[] conditionsNames = line.split("\t");
			
			//Find the columns with the conditions and the one with entrez gene ID
			int indexEntrezGene=-1;
			int endOfConditions=-1;
			for (int i=2;i<conditionsNames.length;i++){
				if (conditionsNames[i].equals("\"TargetID\"")){
					endOfConditions = i;
				}
				else if (conditionsNames[i].equals("\"Entrez_Gene_ID\"")){
					indexEntrezGene = i;
					break;
				}
			}
			if (indexEntrezGene == -1 || endOfConditions == -1)
			{
				JOptionPane.showMessageDialog(
						new JFrame(),
						"The Illumina file does not match the expected structure. Aborting the process.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			while ((line = reader.readLine()) != null) {
				String geneIDvalue;
				ArrayList<Float> conditionsMeans = new ArrayList<Float>();
				ArrayList<Float> conditionsP = new ArrayList<Float>();
				String[] splittedString = line.split("\t"); // splits the line at the tab sign
				if (splittedString.length > indexEntrezGene){
					geneIDvalue = splittedString[indexEntrezGene].trim();

					// We keep the ".mean" and ".p" values : first and third values of each set of four columns,
					// corresponding to an experiment
					for (int i = 2; i < endOfConditions; i += 4) {
						if ((inputCondition1Text.equals("") && inputCondition2Text
								.equals(""))
								|| condition1.equals(conditionsNames[i].trim())
								|| condition2.equals(conditionsNames[i].trim())) {
							conditionsMeans.add(Float.valueOf(splittedString[i]
									.trim()));
							conditionsP.add(Float.valueOf(splittedString[i + 2]
									.trim()));
						}
					}
					if (geneIDvalue != "NA") {
						// If the molecule cannot be found on a previous line or
						// it can be found on a previous line with a false:
						if (barcodehashmap.get(geneIDvalue) == null || barcodehashmap.get(geneIDvalue) == false){
							barcodehashmap.put(geneIDvalue, false);
							for (int i = 0; i < conditionsMeans.size(); i++) {
								// If one of the two conditions for the presence
								// to be true is not respected (see Report ElKoursi&Lavergne)
								if (conditionsMeans.get(i) > 150
										&& conditionsP.get(i) <= 0.01) {
									barcodehashmap.put(geneIDvalue, true);
									break;
								}
							}
						}
					}
				}
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					new JFrame(),
					"File read exception in field 'Control'. Make sure you have selected a valid Illumina formatted file",
					"Warning", JOptionPane.WARNING_MESSAGE);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * This method checks if the defaultID is present in the hashmap
	 * uniprottogeneid_fullhashmap and then crosschecking in the barcodehashmap, 
	 * before concluding which idcyto to delete, and
	 * returning a boolean of true if something is deleted, else false!
	 * 
	 * @param defaultid
	 */
	public static boolean isPresentInHashMaps(String defaultid) {
		boolean ispresent = false;
		if (Pattern.matches(regex1, defaultid)) {
			// if Uniprot ID key gives a geneID value in
			// uniprottogeneid_fullhashmap
			String uniprottogeneid = AffymetrixRegexReader
					.getUniprottogeneidFullhashmap().get(defaultid);
			if ((uniprottogeneid != null) && !uniprottogeneid.isEmpty()) {
				String geneID = uniprottogeneid;

				// if there exists a set of values in the barcode1hashmap for the geneID
				if (barcodehashmap.containsKey(geneID)) {
					// if both barcode files give 0 for the geneID,
					if (barcodehashmap.get(geneID) == false) {
						ispresent = true;
					}
				}
			}
		} else {// EntrezGene
			// if there exists a set of values in the barcode1hashmap for the geneID
			if (barcodehashmap.containsKey(defaultid)) {
				// if both barcode files give 0 for the affyID,
				if (barcodehashmap.get(defaultid) == false) {
					ispresent = true;
				}
			}
		}
		return ispresent;
	}

	/**
	 * This method reads the barcode hashmap and decides what to do with the
	 * barcode lines. If not expressed (0), delete the node corresponding to the
	 * illuminaID. If expressed (1), leave it
	 * 
	 * @param sp
	 * 			the splashframe window
	 * @param 
	 * 			the illumina process
	 */
	public static void compareConditions(SplashFrame sp, ProcessIllumina process) {

		// Progress
		int total = AffymetrixRegexReader.getProteincomplexhashmap().size()
				+ AffymetrixRegexReader.getProteinfamilyhashmap().size()
				+ AffymetrixRegexReader.getProteincomplexhashmap().size();
		int progress = 0;

		// for every entry in proteinhashmap
		for (Map.Entry<String, String> entryFromProteinHashmap : AffymetrixRegexReader
				.getProteinhashmap().entrySet()) {
			
			//progress bar
			progress++;
			sp.getBar().setValue((progress * 100) / total + 1);
			if(!process.isContinueThread()){
				return;
			}

			String key = entryFromProteinHashmap.getKey(); // the IDcyto
			String value = entryFromProteinHashmap.getValue(); // the single defaultID
			try {
				if (isPresentInHashMaps(value) == true) {
					destroyNode(key);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// for every entry in proteinfamilyhashmap
		// protein family to be deleted if ALL members are present as 0 in the barcodes
		for (Map.Entry<String, String> entryFromProteinFamilyHashmap : AffymetrixRegexReader
				.getProteinfamilyhashmap().entrySet()) {
			
			//progress bar
			progress++;
			sp.getBar().setValue((progress * 100) / total + 1);
			if(!process.isContinueThread()){
				return;
			}
			
			String key = entryFromProteinFamilyHashmap.getKey(); // the IDcyto
			String value = entryFromProteinFamilyHashmap.getValue(); // the defaultID(s)
			if (value.contains("::")) // if there are many IDs
			{
				String[] splittedvalue = value.split("::");
				int number_of_members = splittedvalue.length;
				int localproteinmembercounter = 0;
				for (int i = 0; i < number_of_members; i++) {
					try {
						if (isPresentInHashMaps(splittedvalue[i]) == true) {
							localproteinmembercounter++;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				// if all members of the family are present as 0 in the barcodes
				if (localproteinmembercounter == number_of_members) {
					destroyNode(key);
				}
			} else // just in case there is a protein family with only a single
					// member, i.e. no "value" contains "::"
			{
				try {
					if (isPresentInHashMaps(value) == true) {
						destroyNode(key);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// for every entry in proteincomplexhashmap
		// entry to be deleted if ANY of the members show 0
		for (Map.Entry<String, String> entryFromProteinComplexHashmap : AffymetrixRegexReader
				.getProteincomplexhashmap().entrySet()) {
			
			//progress bar
			progress++;
			sp.getBar().setValue((progress * 100) / total + 1);
			if(!process.isContinueThread()){
				return;
			}
			
			String key = entryFromProteinComplexHashmap.getKey(); // the IDcyto
			String value = entryFromProteinComplexHashmap.getValue(); // the single defaultID
			if (value.contains(":")) {
				String[] splittedvalue = value.split(":");
				int number_of_members = splittedvalue.length;
				int localproteinmembercounter = 0;
				for (int i = 0; i < number_of_members; i++) {
					try {
						if (isPresentInHashMaps(splittedvalue[i]) == true) {
							localproteinmembercounter++;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				// if ANY member of the complex are present as 0 in the barcodes
				if (localproteinmembercounter > 0) {
					destroyNode(key);
				}
			} else // just in case there is a protein complex with only a single
					// member, i.e. "value" doesn't contain ":"
			{
				try {
					if (isPresentInHashMaps(value) == true) {
						destroyNode(key);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		JOptionPane.showMessageDialog(new JFrame(),
				"The graph view has been filtered and updated. " + counter
						+ " nodes have been removed.", "Filtered",
				JOptionPane.PLAIN_MESSAGE);
		counter = 0;
	}

	/**
	 * This method adds to the arraylist the node IDs of nodes to be deleted and
	 * counts how many are (to be) deleted
	 * 
	 * @param nodename
	 *            The node identifier of the node to be removed
	 */
	public static void destroyNode(String nodename) {
		// get the network view object
		CyNetwork cynetwork = Cytoscape.getCurrentNetwork();
		@SuppressWarnings("unchecked")
		List<CyNode> cynodelist = Cytoscape.getCyNodesList();
		if (cynetwork == null || cynodelist.isEmpty()) {
			return;
		} else {
			for (int i = 0; i < cynodelist.size(); i++) {
				if (cynodelist.get(i).getIdentifier().equals(nodename)) {
					nodeIDtobedeleted.add(cynodelist.get(i).toString());
					counter++;
				}
			}
			Cytoscape.getCurrentNetworkView().redrawGraph(false, true);
		}
	}

	/**
	 * This method reads the input SIF file and compares its contents with the
	 * list of nodes to be deleted. Then a new SIF would be created, depending
	 * whether the input SIF is present in the list of nodes to be deleted
	 * 
	 * @param pathname
	 *            the original SIF file to be read
	 * @throws IOException
	 */
	public static void SIFreaderAndNewCreator(String pathname)
			throws IOException {
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(pathname));
			String[] temporarypath = pathname.split(".sif");
			String filetobewritten = temporarypath[0].concat(Controller
					.getAbsentProteinsConcatenation() + ".sif");
			writer = new PrintWriter(filetobewritten);
			String line;
			while ((line = reader.readLine()) != null) {
				// recognises whole line where there is a tab space.
				if (line.contains("\t")) {
					String node1;
					String node2;

					String[] splittedString = line.split("[\\t]"); // splits the line at the tab space
					node1 = splittedString[0].trim();
					node2 = splittedString[2].trim();
					// if the list of nodes to be deleted contains the IDcyto
					// read from the SIF,
					if (!(nodeIDtobedeleted.contains(node1) || nodeIDtobedeleted
							.contains(node2))) {
						// if list of nodes does not contain IDcyto to bedeleted, then pass it to the
						// new SIF by printing it
						writer.println(line);
					}
				}
			}
		}

		finally {
			// have to close the writer, else wasting of resources
			if (writer != null) {
				writer.close();
			}
			if (reader != null) {
				reader.close();
			}
			// clear the barcode hashmaps
			clearBarcodeHashMaps();
		}
	}

	/**
	 * This method clears the barcode hashmap and the ArrayList
	 * nodeIDtobedeleted. If the Hashmap is not cleared, then the next
	 * filtering might be erroneous!
	 */
	public static void clearBarcodeHashMaps() {
		barcodehashmap.clear();
		nodeIDtobedeleted.clear();
	}

}
