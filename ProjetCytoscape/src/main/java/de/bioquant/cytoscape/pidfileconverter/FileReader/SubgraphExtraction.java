/**
 * This class mainly deals with the subgraph extraction in Step 3 of the plugin UI
 * 
 * @author Hadi Kang
 * @contributor Yamei Sun & Thomas Brunel
 */

package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.io.BufferedReader;
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import de.bioquant.cytoscape.pidfileconverter.View.Controller;
import de.bioquant.cytoscape.pidfileconverter.View.SplashFrame;
import de.bioquant.cytoscape.pidfileconverter.View.Step3;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class SubgraphExtraction {
	private String inputcytosourcetext;
	private String inputcytotargettext;
	private String inputgenetargetfilepath;
	private String inputsigmolsourcefilepath;
	private String inputsigmoltargetfilepath;
	private Step3 step3;
	private ArrayList<CyNode> cytosourcesubgraph = new ArrayList<CyNode>();
	private ArrayList<CyNode> cytotargetsubgraph = new ArrayList<CyNode>();
	private ArrayList<CyNode> cytogenes = new ArrayList<CyNode>(); //list of cytoIDs of genes to keep in the subgraph.
	private ArrayList<String> nodeIDtobekept = new ArrayList<String>();

	// regular expression of e.g. [P98170::Q13490::Q13489:]
	private final static String regex1 = "(\\[*([POQ]\\d|A-Z\\d|A-Z\\d|A-Z\\d|A-Z\\d|A-Z).*)+";

	/**
	 * The constructor for this class. Step3 is instantiated and defined.
	 */
	public SubgraphExtraction(Step3 step3) {

		this.step3 = step3;

		inputcytosourcetext = step3.getCytoidSourceTextArea().getText();
		inputcytotargettext = step3.getCytoidTargetTextArea().getText();
		inputgenetargetfilepath = step3.getGeneTargetTextField().getText();
		inputsigmolsourcefilepath = step3.getSourceSourceTextField().getText();
		inputsigmoltargetfilepath = step3.getSourceTargetTextField().getText();
	}

	/**
	 * Default constructor
	 */
	public SubgraphExtraction() {
	}

	/**
	 * Read the cytoidsourcearea of class ste3 and reads it according to CytoIDs, checks if present in CyNetwork,
	 * before putting the IDs into an ArrayList of type CyNode, which is to undergo subgraph extraction.
	 */
	public void readCytoSourceText() {
		if (!inputcytosourcetext.trim().equals("")) // if the text area is not empty
		{
			BufferedReader reader = null;
			ArrayList<String> temporarylist = new ArrayList<String>(); // temporary list for nodes NOT in the graph
			try {
				reader = new BufferedReader(new StringReader(
						inputcytosourcetext));
				String l;
				// get the network view object and its nodelists
				CyNetwork cynetwork = Cytoscape.getCurrentNetwork();
				@SuppressWarnings("unchecked")
				List<CyNode> cynodelist = Cytoscape.getCyNodesList();
				while ((l = reader.readLine()) != null) {
					temporarylist.add(l.trim()); // as per default, the lines
													// are added to the list
					if (cynetwork == null || cynodelist.isEmpty()) {
						JOptionPane
								.showMessageDialog(
										new JFrame(),
										"There are no network/nodes to build a subgraph from. Load a network with Step 1 above!",
										"Caution!", JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						for (int i = 0; i < cynodelist.size(); i++) {
							// if the read line l is same as one of the list ofnodes, add that node to the list to be subgraphed!
							if (cynodelist.get(i).getIdentifier()
						.equals(l.trim())) {
								cytosourcesubgraph.add(cynodelist.get(i));
								temporarylist.remove(l.trim()); // if the line is not present in the list of nodes, delete it from temporary list!
							}
						}
					}
				}
			} catch (IOException e) {

			} finally {
				System.out
						.println("Nodes in the cyto ID source text not recognized:");
				for (int i = 0; i < temporarylist.size(); i++) {
					System.out.println(temporarylist.get(i));
				}
			}
		}
	}

	/**
	 * Read the cytoidtargetarea of class MainFrame and reads it according to CytoIDs, checks if present in CyNetwork,
	 * before putting the IDs into an ArrayList of type CyNode, which is to undergo subgraph extraction.
	 */
	public void readCytoTargetText() {
		if (!inputcytotargettext.trim().equals("")) {
			BufferedReader reader = null;
			ArrayList<String> temporarylist = new ArrayList<String>(); // temporary list for nodes NOT inthe graph
			try {
				reader = new BufferedReader(new StringReader(
						inputcytotargettext));
				String l;
				// get the network view object and its nodelists
				CyNetwork cynetwork = Cytoscape.getCurrentNetwork();
				@SuppressWarnings("unchecked")
				List<CyNode> cynodelist = Cytoscape.getCyNodesList();
				while ((l = reader.readLine()) != null) {
					temporarylist.add(l.trim()); // as per default, the lines
													// are added to the list
					if (cynetwork == null || cynodelist.isEmpty()) {
						JOptionPane
								.showMessageDialog(
										new JFrame(),
										"There are no network/nodes to build a subgraph from. Load a network with Step 1 above!",
										"Caution!", JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						for (int i = 0; i < cynodelist.size(); i++) {
							// if the read line l is same as one of the list of nodes, add that node to the list to be subgraphed!
							if (cynodelist.get(i).getIdentifier()
									.equals(l.trim())) {
								// TODO: Check Transcription!!
								cytotargetsubgraph.add(cynodelist.get(i));
								temporarylist.remove(l.trim()); // if the line is not present in the list of nodes, delete it from temporary  list!
							}
						}
					}
				}
			} catch (IOException e) {

			} finally {
				System.out
						.println("Nodes from the cyto ID target text not recognized:");
				for (int i = 0; i < temporarylist.size(); i++) {
					System.out.println(temporarylist.get(i));
				}
			}
		}
	}

	/**
	 * This method reads the gene target file and checks if the input nodes are present in the cynodelist. if they are
	 * present, check if the node comes from a translation event. If yes, check if it is a protein family or complex, if
	 * no, then add it into the arraylist cytotargetsubgraph
	 */
	public void readGeneTargetFile(String sifpath, String nodetypefilepath) {
		if (!inputgenetargetfilepath.trim().equals("")) {
			BufferedReader reader = null;
			ArrayList<String> temporarylist = new ArrayList<String>(); // temporary list for nodes NOT in the graph
			try {
				reader = new BufferedReader(new FileReader(
						inputgenetargetfilepath));
				String l;
				// get the network view object and its nodelists
				CyNetwork cynetwork = Cytoscape.getCurrentNetwork();
				@SuppressWarnings("unchecked")
				List<CyNode> cynodelist = Cytoscape.getCyNodesList();
				while ((l = reader.readLine()) != null) {
					temporarylist.add(l.trim()); // as per default, the lines are added to the list
					if (cynetwork == null || cynodelist.isEmpty()) {
						JOptionPane
								.showMessageDialog(
										new JFrame(),
										"There are no network/nodes to build a subgraph from. Load a network with Step 1 above!",
										"Caution!", JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
	
						//If the default ID is UniprotID, look for the corresponding EntrezGeneID
						//We can only look for a perfect match in case of an EntrezGeneID
						String otherID=l.trim(); 
						if (Pattern.matches(regex1, l.trim())) {
							String entrezGeneID = AffymetrixRegexReader
									.getUniprottogeneidFullhashmap().get(l.trim());
							if ((entrezGeneID != null) && !entrezGeneID.isEmpty()) {
								otherID = entrezGeneID; //corresponding EntrezGene ID
							}
							
							if (isComingFromTranscription(l.trim(), sifpath,
									nodetypefilepath)) // if l comes from a translation event
							{
								for (int i = 0; i < cynodelist.size(); i++) {
									if (!cynodelist.get(i).getIdentifier()
											.contains(":")) // if cytoID doesntcontain ":" i.e. notfamily or complex
									{
										if (cynodelist.get(i).getIdentifier()
												.contains(l.trim()) || cynodelist.get(i).getIdentifier()
												.equals(otherID) )// if cytoIDcontains l, i.e. P10291@nucleus is possible
										{
											if (!cytotargetsubgraph
													.contains(cynodelist.get(i))) // then add the cytoid to the target subgraph list
											{
												cytotargetsubgraph.add(cynodelist
														.get(i));
												cytogenes.add(cynodelist.get(i));
												temporarylist.remove(l.trim()); // if the line is not present in the list of nodes, delete it  from temporary list!
											}
										}
									}
								}
							}
							
						}
						//If the default ID is an EntrezGeneID, look the corresponding UniprotID
						else if (Pattern.matches("([0-9])+",l.trim()) && AffymetrixRegexReader
								.getUniprottogeneidFullhashmap().containsValue(l.trim())){
							for(String uniprotID : AffymetrixRegexReader
									.getUniprottogeneidFullhashmap().keySet()){
								if (AffymetrixRegexReader
										.getUniprottogeneidFullhashmap().get(uniprotID).equals(l.trim())){
									otherID = uniprotID;
									break;
								}
							}

							if (!otherID.equals(l.trim()) && isComingFromTranscription(otherID, sifpath,
									nodetypefilepath)) // if otherID comes from a translation event
							{
								for (int i = 0; i < cynodelist.size(); i++) {
									if (!cynodelist.get(i).getIdentifier()
											.contains(":")) // if cytoID doesntcontain ":" i.e. notfamily or complex
									{
										if (cynodelist.get(i).getIdentifier()
												.equals(l.trim()) || (!otherID.equals(l.trim()) && cynodelist.get(i).getIdentifier()
												.contains(otherID)) )// if cytoIDcontains l, i.e. P10291@nucleus is possible
										{
											if (!cytotargetsubgraph
													.contains(cynodelist.get(i))) // then add the cytoid to the target subgraph list
											{
												cytotargetsubgraph.add(cynodelist
														.get(i));
												temporarylist.remove(l.trim()); // if the line is not present in the list of nodes, delete it  from temporary list!
											}
										}
									}
								}
							}
						}


					}
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"Please check the gene target file! Click ? for help.",
						"Caution!", JOptionPane.ERROR_MESSAGE);
				return;
			} finally {
				System.out
						.println("Nodes from the gene target not recognized:");
				for (int i = 0; i < temporarylist.size(); i++) {
					System.out.println(temporarylist.get(i));
				}
				// have to close the reader, else wasting of resources
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * This method reads the file and checks if the checkbox is ticked. If yes, the method will search also in protein
	 * families and complexes. If no, the method will only search for nodes of proteins. In both ways, the method adds
	 * positive searches into the cytosourcesubgraph arraylist.
	 */
	public void readSigmolSourceFile() {
		if (!inputsigmolsourcefilepath.trim().equals("")) {
			BufferedReader reader = null;
			ArrayList<String> temporarylist = new ArrayList<String>(); // temporary list for nodes NOT in the graph
			try {
				reader = new BufferedReader(new FileReader(
						inputsigmolsourcefilepath));
				String l;
				// get the network view object and its nodelists
				CyNetwork cynetwork = Cytoscape.getCurrentNetwork();
				@SuppressWarnings("unchecked")
				List<CyNode> cynodelist = Cytoscape.getCyNodesList();
				while ((l = reader.readLine()) != null) {
				
					temporarylist.add(l.trim()); // as per default, the lines are added to the list
					if (cynetwork == null || cynodelist.isEmpty()) {
						JOptionPane
								.showMessageDialog(
										new JFrame(),
										"There are no network/nodes to build a subgraph from. Load a network with Step 1 above!",
										"Caution!", JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						for (int i = 0; i < cynodelist.size(); i++) {
							if (step3.isIncludeComplexesChecked()) // if the checkbox is ticked
							{
								addIfPossible(l, cytosourcesubgraph, cynodelist, i, temporarylist);
							} else // else if the checkbox is not ticked
							{
								if (!cynodelist.get(i).getIdentifier()
										.contains(":")) // if cytoID doesnt contain ":" i.e. not family or complex
								{
									addIfPossible(l.trim(), cytosourcesubgraph, cynodelist, i, temporarylist);
								}
							}
						}
					}
				}
			} catch (IOException e) {
				JOptionPane
						.showMessageDialog(
								new JFrame(),
								"Please check the signalling molecules source file! Click ? for help.",
								"Caution!", JOptionPane.ERROR_MESSAGE);
				return;
			} finally {
				System.out
						.println("Nodes from the signalling molecules source not recognized:");
				for (int i = 0; i < temporarylist.size(); i++) {
					System.out.println(temporarylist.get(i));
				}
				// have to close the reader, else wasting of resources
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * This method reads the file and checks if the checkbox is ticked. If yes, the method will search also in protein
	 * families and complexes. If no, the method will only search for nodes of proteins. In both ways, the method adds
	 * positive searches into the cytotargetsubgraph arraylist.
	 */
	public void readSigmolTargetFile() {
		if (!inputsigmoltargetfilepath.trim().equals("")) {
			BufferedReader reader = null;
			ArrayList<String> temporarylist = new ArrayList<String>(); // temporary list for nodes NOT in the graph
			try {
				reader = new BufferedReader(new FileReader(
						inputsigmoltargetfilepath));
				String l;
				// get the network view object and its nodelists
				CyNetwork cynetwork = Cytoscape.getCurrentNetwork();
				@SuppressWarnings("unchecked")
				List<CyNode> cynodelist = Cytoscape.getCyNodesList();
				while ((l = reader.readLine()) != null) {
					temporarylist.add(l.trim()); // as per default, the lines
													// are added to the list
					if (cynetwork == null || cynodelist.isEmpty()) {
						JOptionPane
								.showMessageDialog(
										new JFrame(),
										"There are no network/nodes to build a subgraph from. Load a network with Step 1 above!",
										"Caution!", JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						for (int i = 0; i < cynodelist.size(); i++) {

							if (step3.isIncludeComplexesChecked()) // if the checkbox is ticked
							{
								addIfPossible(l, cytotargetsubgraph, cynodelist, i, temporarylist);
									
							} else // else if the checkbox is not ticked
							{
								if (!cynodelist.get(i).getIdentifier()
										.contains(":")) // if cytoID doesnt contain ":" i.e. not family or complex
								{
									addIfPossible(l, cytotargetsubgraph, cynodelist, i, temporarylist);
									
								}
							}
						}
					}
				}
			} catch (IOException e) {
				JOptionPane
						.showMessageDialog(
								new JFrame(),
								"Please check the signalling molecules file! Click ? for help.",
								"Caution!", JOptionPane.ERROR_MESSAGE);
				return;
			} finally {
				System.out
						.println("Nodes from the signalling molecules target not recognized:");
				for (int i = 0; i < temporarylist.size(); i++) {
					System.out.println(temporarylist.get(i));
				}
				// have to close the reader, else wasting of resources
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 * Method reads the arraylist of IDs and draws the subgraph using the shortest path method. Checks both
	 * cytosourcesubgraph and cytotargetsubgraph for a shortest path between the source and target nodes.
	 * 
	 * @author Hadi Kang
	 * @author Aristotelis Kittas
	 */
	public void drawJungGraph(SplashFrame sp, ProcessSubgraph process, String nodetypefilepath) {

		// Progress
		int progress = 0;

		DirectedGraph<CyNode, CyEdge> myGraph = cytotojungGraph(nodetypefilepath); // creates the JUNG graph

		DijkstraShortestPath<CyNode, CyEdge> dpath = new DijkstraShortestPath<CyNode, CyEdge>(
				myGraph, false); // create shortest path object

		//progress bar
		progress += 10;
		sp.getBar().setValue(progress);
		if (!process.isContinueThread()) {
			return;
		}

		Set<CyNode> s = new HashSet<CyNode>(); // set of nodes in the shortest path that will be included in the final network

		for (int i = 0; i < cytosourcesubgraph.size(); i++) {
			CyNode startnode = cytosourcesubgraph.get(i);
			for (int j = 0; j < cytotargetsubgraph.size(); j++) {
				progress++;
				sp.getBar().setValue(progress);
				if (!process.isContinueThread()) {
					return;
				}

				CyNode endnode = cytotargetsubgraph.get(j);

				if (!endnode.equals(startnode)) // if we are dealing with unique nodes
				{
					List<CyEdge> gp = dpath.getPath(startnode, endnode);
					if (gp != null) {
						for (CyEdge e : gp) {
							s.add(myGraph.getEndpoints(e).getFirst());
							s.add(myGraph.getEndpoints(e).getSecond());
						}
					}
				}
			}
		}

		//progress bar
		progress += 10;
		sp.getBar().setValue(progress);
		if (!process.isContinueThread()) {
			return;
		}

		System.out.println("Nodes in the source graph:");
		for (int i = 0; i < cytosourcesubgraph.size(); i++) {
			// colour the corresponding source node
			colourThisNode(cytosourcesubgraph.get(i), "255,0,0"); // source node colour red
			System.out.println(cytosourcesubgraph.get(i).toString());
		}
		System.out.println("Nodes in the target graph:");
		for (int i = 0; i < cytotargetsubgraph.size(); i++) {
			// colour the corresponding target node
			colourThisNode(cytotargetsubgraph.get(i), "0,0,255"); // target node colour blue
			System.out.println(cytotargetsubgraph.get(i).toString());
		}
		System.out.println("Nodes in the set:");
		for (CyNode n : s) {
			// add the node in the set to the nodeIDtobekept arraylist
			nodeIDtobekept.add(n.getIdentifier());
			System.out.println(n.getIdentifier());
		}
	}

	/**
	 * Helper method for drawJungGraph above
	 * 
	 * @return
	 */
	public DirectedGraph<CyNode, CyEdge> cytotojungGraph(String nodetypefilepath){
		DirectedGraph<CyNode, CyEdge> myGraph = new DirectedSparseGraph<CyNode, CyEdge>();
		@SuppressWarnings("unchecked")
		List<CyEdge> l = Cytoscape.getCyEdgesList();
		for (CyEdge e : l) {
			
			//In the subgraph, we want every gene to come for a translation, and only a transcription.
			//If the target of the edge is one of the genes, keep the edge only if it is the source is of type transcription.
			//i.e if the target is not a gene, or the source is a transcription, keep the edge.
			try {
				if (!cytogenes.contains(((CyNode) e.getTarget())) 
						|| isPrecursorNodeTranscription(e.getSource().getIdentifier(),nodetypefilepath)){
					myGraph.addEdge(e, (CyNode) e.getSource(), (CyNode) e.getTarget());
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return myGraph;
	}

	/**
	 * This method reads the input SIF file and compares its contents with the list of nodes to be deleted. Then a new
	 * SIF would be created, depending whether the input SIF is present in the list of nodes to be deleted
	 * 
	 * @param pathname
	 *            the original SIF file to be read
	 * @throws IOException
	 */
	public void SIFreaderAndNewCreator(String pathname) throws IOException {
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(pathname));
			String[] temporarypath = pathname.split(".sif");
			String filetobewritten = temporarypath[0].concat(Controller
					.getSubgraphed() + ".sif");
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
					// if the list of nodes to be kept contains the IDcyto read
					// from the SIF, then pass it to the new SIF by printing it
					if (nodeIDtobekept.contains(node1)
							&& nodeIDtobekept.contains(node2)) {
						writer.println(line);
					} else // if list of nodes does not contain IDcyto to be kept,
					{
						// do nothing here, new SIF would not contain the line
					}
				}
			}
		} finally {
			// have to close the writer, else wasting of resources
			if (writer != null) {
				writer.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * This method checks the sif and the nodetype.na files to see if the node is present, and if the node origins from
	 * a transcription event
	 * 
	 * @param nodename
	 *            the node of interest
	 * @param sifpath
	 *            the sif file
	 * @param nodetypefilepath
	 *            the nodetype.na file
	 * @return
	 * @throws IOException
	 */
	public boolean isComingFromTranscription(String nodename, String sifpath,
			String nodetypefilepath) throws IOException {
		boolean value = false;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(sifpath));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains(nodename)) // if the line in the SIF contains the node
				{
					// recognises whole line where there is a tab space.
					if (line.contains("\t")) {
						String node1;
						String node2;

						String[] splittedString = line.split("[\\t]"); // splits the line at the tab space
						node1 = splittedString[0].trim();
						node2 = splittedString[2].trim();
						if (!node2.contains(":")) // if there is no ":", i.e. not family nor complex
						{
							if (node2.contains(nodename)) {
								if (isPrecursorNodeTranscription(node1,
										nodetypefilepath)) {
									value = true;
									return value;
								}
							} else {
								value = false;
							}
						}
					}
				} else {
					value = false;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// have to close the reader, else wasting of resources
			if (reader != null) {
				reader.close();
			}
		}
		return value;
	}

	/**
	 * This method checks if the node is of type transcription by checking the nodetype.na file and returns true if so.
	 * 
	 * @param precursornode
	 *            the node of interest
	 * @param nodetypefilepath
	 *            the path of the nodetype.na file
	 * @return true if of type transcription, false if not
	 * @throws IOException
	 */
	public boolean isPrecursorNodeTranscription(String precursornode,
			String nodetypefilepath) throws IOException {
		boolean value = false;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(nodetypefilepath));
			String line;
			while ((line = reader.readLine()) != null) {
				// if the line in the NodeType file contains the node referring
				// to transcription
				if (line.contains(precursornode)
						&& line.contains("transcription")) {
					value = true;
					return value;
				} else {
					value = false;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// have to close the reader, else wasting of resources
			if (reader != null) {
				reader.close();
			}
		}
		return value;
	}

	/**
	 * This method colours the node
	 * 
	 * @param node
	 *            the CyNode object to be coloured
	 * @param nodecolourRGB
	 *            the RGB code of the colour, e.g. "255,0,0" for red
	 */
	public void colourThisNode(CyNode node, String nodecolourRGB) {
		CyAttributes nodeAtts = Cytoscape.getNodeAttributes();
		nodeAtts.setAttribute(node.getIdentifier(), "node.fillColor",
				nodecolourRGB);
		// node.getIdentifier() is the name of the individual node,
		// "node.fillColor" is a constant to change the fill color,
		// and the last string is the RGB color code
	}
	
	/**
	 * Function used in readSigmolSourceFile() and readSigmolTargetFile()
	 * Finds if possible the UniprotID or the EntrezGene ID of the ID given in the file,
	 * and adds it to the list "cytosubgraph", which is cytotargetsubgraph or cytosourcesubgraph
	 * @param l
	 * 		default ID
	 * @param cytosubgraph
	 * 		cytotargetsubgraph or cytosourcesubgraph
	 * @param cynodelist
	 * 		list of nodes in the initial graph
	 * @param i
	 * 		position of the node of cynodelist considered
	 * @param temporarylist
	 * 		list of IDs still not recognized
	 */
	private void addIfPossible(String l, ArrayList<CyNode> cytosubgraph, List<CyNode> cynodelist, int i, ArrayList<String> temporarylist){
		//If the default ID is UniprotID, look for the corresponding EntrezGeneID
		//We can only look for a perfect match in case of an EntrezGeneID
		String otherID=l.trim(); 
		if (Pattern.matches(regex1, l)) {
			String entrezGeneID = AffymetrixRegexReader
					.getUniprottogeneidFullhashmap().get(l.trim());
			if ((entrezGeneID != null) && !entrezGeneID.isEmpty()) {
				otherID = entrezGeneID; //corresponding EntrezGene ID
			}
			
			if (cynodelist.get(i).getIdentifier()
					.contains(l.trim()) || (cynodelist.get(i).getIdentifier()
					.equals(otherID)))  // if so, add the text to the subgraph list
			{
				if (!cytosubgraph
						.contains(cynodelist.get(i))) {
					cytosubgraph.add(cynodelist
							.get(i));
					temporarylist.remove(l.trim()); // if the line is not present in the list of nodes, delete it from temporary list!
				}
			}
		}
		//If the default ID is an EntrezGeneID, look the corresponding UniprotID
		else if (Pattern.matches("([0-9])+",l.trim()) && AffymetrixRegexReader
				.getUniprottogeneidFullhashmap().containsValue(l.trim())){
			for(String uniprotID : AffymetrixRegexReader
					.getUniprottogeneidFullhashmap().keySet()){
				if (AffymetrixRegexReader
						.getUniprottogeneidFullhashmap().get(uniprotID).equals(l.trim())){
					otherID = uniprotID;
					break;
				}
			}
			
			if (cynodelist.get(i).getIdentifier()
					.equals(l.trim()) || (!otherID.equals(l.trim()) && cynodelist.get(i).getIdentifier()
							.contains(otherID)))  // if so, add the text to the subgraph list
			{
				if (!cytosubgraph
						.contains(cynodelist.get(i))) {
					cytosubgraph.add(cynodelist
							.get(i));
					temporarylist.remove(l.trim()); // if the line is not present in the list of nodes, delete it from temporary list!
				}
			}
		}
	}
}
