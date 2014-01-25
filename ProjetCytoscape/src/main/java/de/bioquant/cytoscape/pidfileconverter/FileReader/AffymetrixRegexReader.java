package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.view.CyNetworkView;
import de.bioquant.cytoscape.pidfileconverter.View.Controller;

public class AffymetrixRegexReader
{
    private static int counter = 0;
    
	// regular expression of e.g. [P98170::Q13490::Q13489:]
    private final static String regex1 = "(\\[*([POQ]\\d|A-Z\\d|A-Z\\d|A-Z\\d|A-Z\\d|A-Z).*)+";
    
	// the file name of the full human mapping uniprot to geneID
	private static final String UNIPROTTOGENEIDMAP = "UPtoGeneIDFULL.txt";
	// the file name of the full human mapping uniprot to geneID
	private static final String AFFYMETRIXSOURCE = "A-AFFY-44.adf.txt";
	
	// the arraylists for the IDs for the different files to be written: File1 IDcyto to UP, file2 unique UPs, file3 UP to GeneID mapping
	private static ArrayList<String> file1tobewritten = new ArrayList<String>();
	private static ArrayList<String> file2tobewritten = new ArrayList<String>();
	private static ArrayList<String> file3tobewritten = new ArrayList<String>();
	private static ArrayList<String> file4tobewritten = new ArrayList<String>();
	private static ArrayList<String> nodeIDtobedeleted = new ArrayList<String>();
	
	// the arraylist for the unique uniprots
	private static ArrayList<String> uniqueuniprots = new ArrayList<String>();
	// the arraylist for the unique GeneIDs
	private static ArrayList<String> uniqueGeneIDs = new ArrayList<String>();
	
	// the hashmap for the first barcode values. Key=AffymetrixID, Value=true(expressed) or false(not expressed)
	private static HashMap<String, Boolean> barcode1hashmap = new HashMap<String, Boolean>();

	// the hashmap for the second barcode values. Key=AffymetrixID, Value=true(expressed) or false(not expressed)
	private static HashMap<String, Boolean> barcode2hashmap = new HashMap<String, Boolean>();
	
	// the hashmaps for storing of protein / protein family / protein complexes and assigning the substituent UPs (values)
	private static HashMap<String, String> proteinhashmap = new HashMap<String, String>();
	private static HashMap<String, String> proteinfamilyhashmap = new HashMap<String, String>();
	private static HashMap<String, String> proteincomplexhashmap = new HashMap<String, String>();
	
	// the COMPLETE STATIC HASHMAPS for UniprotID to GeneID
	private static HashMap<String, String> uniprottogeneid_fullhashmap = new HashMap<String, String>();
	// the COMPLETE STATIC HASHMAPS for GeneID to AffymetrixID
	private static HashMap<String, String> geneidtoaffymetrixid_fullhashmap = new HashMap<String, String>();
	
	// the hashmap for the GeneID values. Key=AffymetrixID, Value=GeneID
	private static HashMap<String, String> affytogeneIDhashmap = new HashMap<String, String>();

	// the hashmap for the UniprotID values. Key=GeneID, Value=UniprotID
	private static HashMap<String, String> geneIDtouniprothashmap = new HashMap<String, String>();

	// the hashmap for the IDCyto values. Key=UniprotID, Value=IDCyto
	private static HashMap<String, String> uniprottoIDcytohashmap = new HashMap<String, String>();
	
	public AffymetrixRegexReader()
	{

	}
	
	/**
	 * This method reads a file (NODE_TYPE.NA) and then parses it accordingly.
	 * @param filename the node_type.NA file
	 * @throws IOException
	 */
	public static void readAndWriteFiles(String filename, String firstfilepath, String secondfilepath, String thirdfilepath, String fourthfilepath) throws IOException
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(filename));
			String l;
			String tobewritten = "";
			String matchedUPtoGeneIDmapping = "";
			String matchedGeneIDtoAffymetrixmapping = "";
			ArrayList<String> listofFile1 = new ArrayList<String>();
			ArrayList<String> listofFile2 = new ArrayList<String>();
			
			while ((l = reader.readLine()) != null)
			{
				if (l.contains("=")) // recognises whole line where there is an '=' sign
				{
					String trimmedName;
					String trimmedDetail;
					String[] splittedString = l.split("="); // splits line at the '=' sign
					trimmedName= splittedString[0].trim();
					trimmedDetail = splittedString[1].trim();
					String tobewrittenIDcytoname = "";
					String tobewrittenuniprotname = "";
					// if protein or protein family
					if(trimmedDetail.equals("protein")||trimmedDetail.equals("rna"))
					{
						tobewrittenIDcytoname = trimmedName;
						// try to destroy the [ in trimmedName
						if(trimmedName.contains("["))
						{
							trimmedName = trimmedName.replace("[", "");
						}
						
						// try to destroy the :] in trimmedName
						if(trimmedName.contains(":]"))
						{
							trimmedName = trimmedName.replace(":]", "");
						}
						
						// if the name has :: in them -> protein family
						if(trimmedName.contains("::"))
						{
							// split the name at the ::
							String[] splittedName = trimmedName.split("::");
							// for every part of the split, make the names
							for(int i = 0 ; i < splittedName.length ; i++)
							{
						
								// for the second half of the equation [ID cyto] = UP1::UP2
								// if at first half of the split 
								if(i == 0)
								{
									// write detail as UP1
									tobewrittenuniprotname = splittedName[i];
								}
								// if at second half or more of the split
								if(i > 0)
								{
									// if the first UP of the line doesn't match the pattern,
									// DO NOT add the ::
									if(tobewrittenuniprotname.equals(""))
									{
										tobewrittenuniprotname = splittedName[i];
									}
									else
									{
										// write detail as UP1 :: UP2 ...
										tobewrittenuniprotname = tobewrittenuniprotname + "::" + splittedName[i];											
									}								
								}
							}
							// add IDCyto (key) and the associated uniprots tobewrittenuniprotname (value) into the appropriate hashmap
							proteinfamilyhashmap.put(tobewrittenIDcytoname, tobewrittenuniprotname);
							// after the first loop has completed, loop the same again for every part of the split
							// this time add the stuff into the list
							for (int i = 0 ; i < splittedName.length ; i++)
							{							
								String linetobeaddedtoFile1 = tobewrittenIDcytoname + " = " + tobewrittenuniprotname;
								String tobeadded = splittedName[i] + " protein family";
								// if the first list doesnt yet contain the thing to be added
								if(!listofFile1.contains(linetobeaddedtoFile1))
								{
									// add this to the list of uniprots
									listofFile1.add(linetobeaddedtoFile1);
								}
								// if the second list doesnt yet contain the thing to be added
								if(!listofFile2.contains(tobeadded))
								{
									// add this to the list of uniprots
									listofFile2.add(tobeadded);
								}
								// if the list of unique proteins doesnt yet contain the uniprot to be added
								if(!uniqueuniprots.contains(splittedName[i]))
								{
									uniqueuniprots.add(splittedName[i]);
								}
							}
						}
						
						// if the expression is as plain as "P56915 = protein"
						else
						{
							tobewrittenuniprotname = trimmedName;
							// put IDCyto (key) together with associated UP (value) into the corresponding hashmap
							proteinhashmap.put(tobewrittenIDcytoname, tobewrittenuniprotname);
							String linetobeaddedtoFile1 = tobewrittenIDcytoname + " = " + tobewrittenuniprotname;
							String tobeadded = trimmedName;
							if(trimmedDetail.equals("protein"))
							{
								tobeadded += " protein";
							}
							else if (trimmedDetail.equals("rna"))
							{
								tobeadded += " rna";
							}
							// if the list doesnt yet contain the thing to be added
							if(!listofFile1.contains(linetobeaddedtoFile1))
							{
								// put the key of tobewrittenuniprotname to the hashmap with value tobewrittenIDcytoname
								uniprottoIDcytohashmap.put(tobewrittenuniprotname, tobewrittenIDcytoname);
								// add this to the list of uniprots
								listofFile1.add(linetobeaddedtoFile1);
							}
							// if the second list doesnt yet contain the thing to be added
							if(!listofFile2.contains(tobeadded))
							{
								// add this to the list of uniprots
								listofFile2.add(tobeadded);
							}
							// if the list of unique proteins doesnt yet contain the uniprot to be added
							if(!uniqueuniprots.contains(trimmedName))
							{
								uniqueuniprots.add(trimmedName);
							}
						}						
					}
					
					// if compound
					else if(trimmedDetail.equals("compound"))
					{
						
					}
					
					// if protein complex
					else if(trimmedDetail.equals("complex"))
					{
						tobewrittenIDcytoname = trimmedName;
						// try to destroy the [ in trimmedName
						if(trimmedName.contains("["))
						{
							trimmedName = trimmedName.replace("[", "");
						}
						
						// try to destroy the :] in trimmedName
						if(trimmedName.contains(":]"))
						{
							trimmedName = trimmedName.replace(":]", "");
						}
						
						// if the name has : in them -> protein complex
						if(trimmedName.contains(":"))
						{
							// split the name at the ::
							String[] splittedName = trimmedName.split(":");
							// for every part of the split, make the detail
							for(int i = 0 ; i < splittedName.length ; i++)
							{
								// for the second half of the equation [ID cyto] = UP1::UP2
								// if at first half of the split 
								if(i == 0)
								{
									// write detail as UP1
									tobewrittenuniprotname = splittedName[i];
								}
								// if at second half or more of the split
								if(i > 0)
								{
									// if the first UP of the line doesn't match the pattern,
									// DO NOT add the :
									if(tobewrittenuniprotname.equals(""))
									{
										tobewrittenuniprotname = splittedName[i];
									}
									else
									{
										// write detail as UP1 : UP2 ...
										tobewrittenuniprotname = tobewrittenuniprotname + ":" + splittedName[i];											
									}								
								}
							}
							// add IDCyto (key) and the associated uniprots tobewrittenuniprotname (value) into the appropriate hashmap
							proteincomplexhashmap.put(tobewrittenIDcytoname, tobewrittenuniprotname);
							// after the first loop has completed, loop the same again for every part of the split
							// this time add the stuff into the list
							for (int i = 0 ; i < splittedName.length ; i++)
							{							
								String linetobeaddedtoFile1 = tobewrittenIDcytoname + " = " + tobewrittenuniprotname;
								String tobeadded = splittedName[i] + " protein complex";
								// if the list doesnt yet contain the thing to be added
								if(!listofFile1.contains(linetobeaddedtoFile1))
								{
									// add this to the list of uniprots
									listofFile1.add(linetobeaddedtoFile1);
								}
								// if the second list doesnt yet contain the thing to be added
								if(!listofFile2.contains(tobeadded))
								{
									// add this to the list of uniprots
									listofFile2.add(tobeadded);
								}
								// if the list of unique proteins doesnt yet contain the uniprot to be added
								if(!uniqueuniprots.contains(splittedName[i]))
								{
									uniqueuniprots.add(splittedName[i]);
								}
							}
						}
						
						// if the expression is as plain as "P56915 = complex"
						else
						{
							tobewrittenuniprotname = trimmedName;
							// put IDCyto (key) together with associated UP (value) into the corresponding hashmap
							proteincomplexhashmap.put(tobewrittenIDcytoname, tobewrittenuniprotname);
							String linetobeaddedtoFile1 = tobewrittenIDcytoname + " = " + tobewrittenuniprotname;
							String tobeadded = trimmedName + " complex";
							// if the list doesnt yet contain the thing to be added
							if(!listofFile1.contains(linetobeaddedtoFile1))
							{
								// put the key of tobewrittenuniprotname to the hashmap with value tobewrittenIDcytoname
								uniprottoIDcytohashmap.put(tobewrittenuniprotname, tobewrittenIDcytoname);
								// add this to the list of uniprots
								listofFile1.add(linetobeaddedtoFile1);
							}
							// if the second list doesnt yet contain the thing to be added
							if(!listofFile2.contains(tobeadded))
							{
								// add this to the list of uniprots
								listofFile2.add(tobeadded);
							}
							// if the list of unique proteins doesnt yet contain the uniprot to be added
							if(!uniqueuniprots.contains(trimmedName))
							{
								uniqueuniprots.add(trimmedName);
							}
						}
					}
					
				}
			}					
			// for every one of the entry in the first list, add it to the arraylist file1tobewritten
			for (int i = 0; i < listofFile1.size() ; i++)
			{
				// add the whole listofuniprot to the ArrayList to be printed out
				file1tobewritten.add(listofFile1.get(i));
				tobewritten = tobewritten.concat(listofFile1.get(i).toString() + "\n");
//				System.out.println(listofuniprot.get(i).toString());
			}				
			// for every one of the entry in the second list, add it to the arraylist file2tobewritten
			for (int i = 0; i < listofFile2.size() ; i++)
			{
				// add the whole listofuniprot to the ArrayList to be printed out
				file2tobewritten.add(listofFile2.get(i));
				tobewritten = tobewritten.concat(listofFile2.get(i).toString() + "\n");
//				System.out.println(listofuniprot.get(i).toString());
			}
			
			// for every unique uniprot, look up the right geneID
			// the below will be unnecessary depending on the necessity of file2tobewritten -> addition of
			// e.g. "protein family" or "protein" or "protein complex"
			for (int i = 0; i < uniqueuniprots.size(); i++)
			{
				if (Pattern.matches(regex1, uniqueuniprots.get(i)))
				{
					matchedUPtoGeneIDmapping = lookUpGeneIDofUniprot(uniqueuniprots.get(i), UNIPROTTOGENEIDMAP);
					// if there is a match, add that to the arraylist file3tobewritten,
					// also adding to the list of uniquegeneIDs
					// and also to the hashmap which goes in the direction GeneID -> Uniprot
					if(matchedUPtoGeneIDmapping != "")
					{
						uniqueGeneIDs.add(matchedUPtoGeneIDmapping);
						geneIDtouniprothashmap.put(matchedUPtoGeneIDmapping, uniqueuniprots.get(i));
						file3tobewritten.add(uniqueuniprots.get(i) + " = " + matchedUPtoGeneIDmapping + "\n");
					}
				}
				else//EntrezGene
				{
					uniqueGeneIDs.add(uniqueuniprots.get(i));
				}
				
			}
			
			// For every unique GeneID, find the corresponding affymetrixID and add the mapping into the arraylist file4tobewritten
			for (int i = 0; i < uniqueGeneIDs.size(); i++)
			{
				matchedGeneIDtoAffymetrixmapping = lookUpAffymetrixIDofGeneID(uniqueGeneIDs.get(i), AFFYMETRIXSOURCE);
				// if there is a match, add that to the arraylist file4tobewritten,
				// and to the hashmap which points in the opposite direction (affyID -> GeneID)
				if(matchedGeneIDtoAffymetrixmapping != "")
				{
					affytogeneIDhashmap.put(matchedGeneIDtoAffymetrixmapping, uniqueGeneIDs.get(i));
					file4tobewritten.add(uniqueGeneIDs.get(i) + " = " + matchedGeneIDtoAffymetrixmapping + "\n");
				}
			}
			
			// get the first file printed out
			multipleFileWriter("IDCyto_to_UniProt_Map", firstfilepath, file1tobewritten);
			// get the second file printed out. The quoted file2tobewritten -> addition of
			// e.g. "protein family" or "protein" or "protein complex" -> look at necessity
//			idCytoUniProtFileWriter("Unique UniProts", secondfilepath, file2tobewritten);
			multipleFileWriter("Unique_UniProts", secondfilepath, uniqueuniprots);
			// get the third file printed out
			multipleFileWriter("UniProt_to_GeneID_Map", thirdfilepath, file3tobewritten);
			// get the fourth file printed out
			multipleFileWriter("GeneID_to_Affymetrix_Map", fourthfilepath, file4tobewritten);			
			
			// printing the top rows of the list
//			System.out.println("ID Cyto = UniProt Name");
//			System.out.println(tobewritten);
			
			// clear the arraylists for next time
		}
		
		finally
		{
			// have to close the reader, else wasting of resources
			if (reader != null) {
				reader.close();
			}
			
			// clear the arraylists for next read
			clearArrayLists();
		}
	}
	
	/**
	 * This method writes a list of tobewritten stuff to a file under the filename parameter
	 * @param documentname the first line of the document to be printed
	 * @param filename the absolute path of the document to be printed
	 * @param listoftobewritten the arraylist of the data to be printed in the document
	 * @throws IOException
	 */
	public static void multipleFileWriter(String documentname, String filename, ArrayList<String> listoftobewritten) throws IOException
	{
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(filename);
			writer.println(documentname);
			for(int i = 0; i < listoftobewritten.size(); i++)
			{
				writer.println(listoftobewritten.get(i));
			}
		}
		
		finally
		{
			// have to close the writer, else wasting of resources
			writer.close();
		}
	}
	
	/**
	 * This method looks up the string Uniprot ID in the source file and returns the corresponding GeneID
	 * @param id the uniprotID
	 * @param sourcefile the filename of the source file
	 * @return String of the corresponding GeneID
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static String lookUpGeneIDofUniprot(String id, String sourcefile) throws IOException
	{
		BufferedReader reader = null;
		String geneID = "";
		try
		{
			reader = new BufferedReader(new FileReader(sourcefile));
			String line;
			while((line = reader.readLine()) != null)
			{
				// recognises whole line where there is an equal sign. prerequisite for the sourcefile to have them!
				if(line.contains("="))
				{
					String notmapped;
					String mappedfrom;
					String mappedto;
					String remarks;
					
					String[] splittedString = line.split("="); // splits the line at the equal signs
					notmapped = splittedString[0].trim();
					mappedfrom = splittedString[1].trim();
					mappedto = splittedString[2].trim();
					remarks = splittedString[3].trim();
					// if found the ID, go to the corresponding mappedto and return UP = geneID
					if(mappedfrom.equals(id))
					{
						geneID = mappedto;
						return geneID;
					}
				}
			}
		}
		finally
		{
			if(reader != null)
			{
				reader.close();
			}
		}
		return geneID;
	}
	
	/**
	 * This method looks up the Affymetrix ID of an input GeneID, searching in a sourcefile (from Affymetrix)
	 * @param id GeneID
	 * @param sourcefile Affymetrix source file
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static String lookUpAffymetrixIDofGeneID(String id, String sourcefile) throws IOException
	{
		BufferedReader reader = null;
		String affyID = "";
		try
		{
			reader = new BufferedReader(new FileReader(sourcefile));
			String line;
			while((line = reader.readLine()) != null)
			{
				// recognizes whole line where there is a tab space
				if(line.contains("\t"))
				{
					String composite_element_name = "";
					String comment_AECompositeName = "";
					String affymetrix_netaffx = "";
					String embl = "";
					String ensembl = "";
					String swall = "";
					String locus = "";
					String interpro = "";
					String unigene = "";
					String omim = "";
					String refseq = "";
					String scop = "";
					String genbank = "";
					String pkr_hanks = "";
					String ec = "";
					String cp450 = "";
					
					String[] splittedString = line.split("[\\t]", 16); // splits the line at the tab space and force cuts it 16 times						
					if (splittedString[0] != "")
					{
						composite_element_name = splittedString[0];						
					}
					if (splittedString[1] != "")
					{
						comment_AECompositeName = splittedString[1];
					}
					if (splittedString[2] != "")
					{
						affymetrix_netaffx = splittedString[2];
					}
					if (splittedString[3] != "")
					{
						embl = splittedString[3];
					}
					if (splittedString[4] != "")
					{
						ensembl = splittedString[4];
					}
					if (splittedString[5] != "")
					{
						swall = splittedString[5];
					}
					if (splittedString[6] != "")
					{
						locus = splittedString[6];
					}
					if (splittedString[7] != "")
					{
						interpro = splittedString[7];
					}
					if (splittedString[8] != "")
					{
						unigene = splittedString[8];
					}
					if (splittedString[9] != "")
					{
						omim = splittedString[9];
					}
					if (splittedString[10] != "")
					{
						refseq = splittedString[10];
					}
					if (splittedString[11] != "")
					{
						scop = splittedString[11];
					}
					if (splittedString[12] != "")
					{
						genbank = splittedString[12];
					}
					if (splittedString[13] != "")
					{
						pkr_hanks = splittedString[13];
					}
					if (splittedString[14] != "")
					{
						ec = splittedString[14];
					}
					if (splittedString[15] != "")
					{
						cp450 = splittedString[15];
					}
					// if found the ID, go to the corresponding locus and return geneID = Affymetrix
					// also add to the list of affymetrix and also hashmap affyid -> geneID
					if(locus != "" && comment_AECompositeName != "")
					{
						if (locus.equals(id))
						{
							affytogeneIDhashmap.put(comment_AECompositeName, id);
							affyID = comment_AECompositeName;
							return affyID;
						}
					}
				}
			}
		}
		finally
		{
			if(reader != null)
			{
				reader.close();
			}
		}
		return affyID;
	}
	
	/**
	 * This method reads one barcode file and assigns a value (barcode) to a 
	 * key (affyID) and puts them into a hashmap,  1 or 0 from <code>barcodeFilter(String line)</code>
	 * @param barcodefilepath
	 * @throws IOException 
	 */
	public static void barcode1Reader(String barcodefilepath) throws IOException
	{
		BufferedReader reader = null;
		ArrayList<String> trya = new ArrayList<String>();
		try
		{
			reader = new BufferedReader(new FileReader(barcodefilepath));
			String line;
			while((line = reader.readLine()) != null)
			{
				// recognises whole line where there is a comma sign. prerequisite for the barcodefile to have them!
				if(line.contains(","))
				{
					String affyvalue;
					String oneORzero;
					
					String[] splittedString = line.split(","); // splits the line at the comma sign; also force split into 2 parts only
					affyvalue = splittedString[0].trim().replace("\"", ""); // and replaces the " sign
					oneORzero = splittedString[1].trim();
					if(oneORzero.length()==1) // if this line is a 'value' line
					{
						if(oneORzero.equals("0")) // if this value is 0 -> not expressed
						{
							barcode1hashmap.put(affyvalue, false);
						}
						if(oneORzero.equals("1")) // if this value is 1 -> expressed
						{
							barcode1hashmap.put(affyvalue, true);
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			JOptionPane
			.showMessageDialog(new JFrame(),
					"File read exception in field 'Control'. Make sure you have selected a valid barcode formatted file",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
		finally
		{
			if(reader != null)
			{
				reader.close();
			}
		}
	}
	
	public static void barcode2Reader(String barcodefilepath) throws IOException
	{
		BufferedReader reader = null;
		ArrayList<String> trya = new ArrayList<String>();
		try
		{
			reader = new BufferedReader(new FileReader(barcodefilepath));
			String line;
			while((line = reader.readLine()) != null)
			{
				// recognises whole line where there is a comma sign. prerequisite for the barcodefile to have them!
				if(line.contains(","))
				{
					String affyvalue;
					String oneORzero;
					
					String[] splittedString = line.split(","); // splits the line at the comma sign; also force split into 2 parts only
					affyvalue = splittedString[0].trim().replace("\"", ""); // and replaces the " sign
					oneORzero = splittedString[1].trim();
					// if this line is a 'value' line
					if(oneORzero.length()==1)
					{
						// if this value is 0 -> not expressed
						if(oneORzero.equals("0"))
						{
							barcode2hashmap.put(affyvalue, false);
//							trya.add(affyvalue + " " + " not expressed, mapped GeneID=" + affytogeneIDhashmap.get(affyvalue));
						}

						// if this value is 1 -> expressed
						if(oneORzero.equals("1"))
						{
							barcode2hashmap.put(affyvalue, true);
//							trya.add(affyvalue + " " + " expressed, mapped GeneID=" + affytogeneIDhashmap.get(affyvalue));
						}
					}
				}
			}
		}

		finally
		{
			if(reader != null)
			{
				reader.close();
			}
		}
	}
	
	/**
	 * This method checks if the uniprot is present in the hashmaps:
	 * uniprottogeneid_fullhashmap & geneidtoaffymetrixid_fullhashmap
	 * and then crosschecking in the barcode1hashmap and barcode2hashmap,
	 * before concluding which idcyto to delete,
	 * and returning a boolean of true if something is deleted, else false!
	 * @param uniprotid
	 */
	public static boolean isPresentInHashMaps(String uniprotid)
	{
		boolean ispresent = false;
		if(Pattern.matches(regex1,uniprotid))
		{
			//if Uniprot ID key gives a geneID value in uniprottogeneid_fullhashmap
			if(!uniprottogeneid_fullhashmap.get(uniprotid).isEmpty() && (uniprottogeneid_fullhashmap.get(uniprotid) != null))
			{
				String geneID = uniprottogeneid_fullhashmap.get(uniprotid);
				//if geneID key gives a affyID value in geneidtoaffymetrixid_fullhashmap
				if(!geneidtoaffymetrixid_fullhashmap.get(geneID).isEmpty())	
				{
					String affyID = geneidtoaffymetrixid_fullhashmap.get(geneID);
					//if there exists a set of values in the barcode1hashmap for the affyID
					if(barcode1hashmap.containsKey(affyID))
					{
						// check for corresponding existence in the barcode2 hashmap
						if(barcode2hashmap.containsKey(affyID))
						{
							// if both barcode files give 0 for the affyID,
							if(barcode1hashmap.get(affyID) == false)
							{
								if (barcode2hashmap.get(affyID) == false)
								{
//									destroyNode(idcyto); // mark the node to be destroyed
									ispresent = true;
								}
							}
						}
					}
				}
			}
		}
		else //EntrezGene 
		{
			if(!geneidtoaffymetrixid_fullhashmap.get(uniprotid).isEmpty())	
			{
				String affyID = geneidtoaffymetrixid_fullhashmap.get(uniprotid);
				//if there exists a set of values in the barcode1hashmap for the affyID
				if(barcode1hashmap.containsKey(affyID))
				{
					// check for corresponding existence in the barcode2 hashmap
					if(barcode2hashmap.containsKey(affyID))
					{
						// if both barcode files give 0 for the affyID,
						if(barcode1hashmap.get(affyID) == false)
						{
							if (barcode2hashmap.get(affyID) == false)
							{
//								destroyNode(idcyto); // mark the node to be destroyed
								ispresent = true;
							}
						}
					}
				}
			}
		}
		return ispresent;
	}
	
	/**
	 * This method reads the barcode hashmaps and decides what to do with the barcode lines.
	 * If not expressed (0), delete the node corresponding to the affymetrixID.
	 * If expressed (1), leave it
	 * @param affymetrixID the affymetrixID
	 * @param value either 0 or 1
	 */
	public static void compareBarcodes()
	{
		// for every entry in proteinhashmap
		for (Map.Entry<String, String> entryFromProteinHashmap : proteinhashmap.entrySet())
		{
			String key = entryFromProteinHashmap.getKey(); // the IDcyto
			String value = entryFromProteinHashmap.getValue(); // the single UniProtID
			try
			{
//				checkIfPresentInHashMaps(key, value);
				if(isPresentInHashMaps(value)==true)
				{
					destroyNode(key);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
		// for every entry in proteinfamilyhashmap
		// protein family to be deleted if ALL members are present as 0 in the barcodes
		for (Map.Entry<String, String> entryFromProteinFamilyHashmap : proteinfamilyhashmap.entrySet())
		{
			String key = entryFromProteinFamilyHashmap.getKey(); // the IDcyto
			String value = entryFromProteinFamilyHashmap.getValue(); // the UniProtID(s)
			if(value.contains("::")) // if there are many UniProts
			{
				String[] splittedvalue = value.split("::");
				int number_of_members = splittedvalue.length;
				int localproteinmembercounter = 0;
				for(int i = 0; i < number_of_members; i++)
				{
					try
					{
						if(isPresentInHashMaps(splittedvalue[i]) == true)
						{
							localproteinmembercounter++;
						}
//						String geneID = lookUpGeneIDofUniprot(splittedvalue[i], UNIPROTTOGENEIDMAP);
//						String affyID = lookUpAffymetrixIDofGeneID(geneID, AFFYMETRIXSOURCE);
//						// if there exists a set of values in the barcode1 hashmap
//						if(barcode1hashmap.containsKey(affyID))
//						{
//							// check for corresponding existence in the barcode2 hashmap
//							if(barcode2hashmap.containsKey(affyID))
//							{
//								// if both barcode files give 0 for the affyID,
//								if(barcode1hashmap.get(affyID) == false)
//								{
//									if (barcode2hashmap.get(affyID) == false)
//									{
//										// cumulate counter
//										localproteinmembercounter++;
//									}
//								}
//							}
//						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
				}
				// if all members of the family are present as 0 in the barcodes
				if(localproteinmembercounter==number_of_members)
				{
					destroyNode(key);
				}
			}
			else // just in case there is a protein family with only a single member, i.e. no "value" contains "::"
			{
				try
				{
					if(isPresentInHashMaps(value)==true)
					{
						destroyNode(key);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		// for every entry in proteincomplexhashmap
		// entry to be deleted if ANY of the members show 0
		for (Map.Entry<String, String> entryFromProteinComplexHashmap : proteincomplexhashmap.entrySet())
		{
			String key = entryFromProteinComplexHashmap.getKey(); // the IDcyto
			String value = entryFromProteinComplexHashmap.getValue(); // the single UniProtID
			if(value.contains(":"))
			{
				String[] splittedvalue = value.split(":");
				int number_of_members = splittedvalue.length;
				int localproteinmembercounter = 0;
				for(int i = 0; i < number_of_members; i++)
				{
					try
					{
						if(isPresentInHashMaps(splittedvalue[i]) == true)
						{
							localproteinmembercounter++;
						}
//						String geneID = lookUpGeneIDofUniprot(splittedvalue[i], UNIPROTTOGENEIDMAP);
//						String affyID = lookUpAffymetrixIDofGeneID(geneID, AFFYMETRIXSOURCE);
//						// if there exists a set of values in the barcode1 hashmap
//						if(barcode1hashmap.containsKey(affyID))
//						{
//							// check for corresponding existence in the barcode2 hashmap
//							if(barcode2hashmap.containsKey(affyID))
//							{
//								// if both barcode files give 0 for the affyID,
//								if(barcode1hashmap.get(affyID) == false)
//								{
//									if (barcode2hashmap.get(affyID) == false)
//									{
//										// cumulate counter
//										localproteinmembercounter++;
//									}
//								}
//							}
//						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
				}
				// if ANY member of the complex are present as 0 in the barcodes
				if(localproteinmembercounter > 0)
				{
					destroyNode(key);
				}
			}
			else // just in case there is a protein complex with only a single member, i.e. "value" doesn't contain ":"
			{
				try
				{
					if(isPresentInHashMaps(value)==true)
					{
						destroyNode(key);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
//		// for every entry in barcode1hashmap
//		for(Map.Entry<String, Boolean> entryFromBarcode1 : barcode1hashmap.entrySet())
//		{
//			if(barcode2hashmap.containsKey(entryFromBarcode1.getKey()))
//			{
//				// if the value in barcode2 is equals value in barcode1
//				if(barcode2hashmap.get(entryFromBarcode1.getKey()).equals(entryFromBarcode1.getValue()))
//				{
//					// if {false,false}
//					if(barcode2hashmap.get(entryFromBarcode1.getKey()) == false)
//					{
//						//TODO: destroy the node
//						String geneID = affytogeneIDhashmap.get(entryFromBarcode1.getKey());
//						String uniprotID = geneIDtouniprothashmap.get(geneID);
//						String idcyto = uniprottoIDcytohashmap.get(uniprotID);
//						destroyNode(idcyto);
//						System.out.println("IDCyto=" + idcyto + ", Uniprot=" + uniprotID + ", GeneID=" + geneID + ", AffyID=" + entryFromBarcode1.getKey()+" false,false");
//				        
//					}
//					
//					// if {true,true}
//					if(barcode2hashmap.get(entryFromBarcode1.getKey()) == true)
//					{
//						//TODO: dont destroy anything
//						String geneID = affytogeneIDhashmap.get(entryFromBarcode1.getKey());
//						String uniprotID = geneIDtouniprothashmap.get(geneID);
//						String idcyto = uniprottoIDcytohashmap.get(uniprotID);
//						System.out.println("IDCyto=" + idcyto + ", Uniprot=" + uniprotID + ", GeneID=" + geneID + ", AffyID=" + entryFromBarcode1.getKey()+" true,true");
//					}
//				}
//				
//				// if the values in barcode1 is not the same as barcode2, e.g. {true,false} or {false,true}
//				else
//				{
//					// if {true,false}
//					if(barcode2hashmap.get(entryFromBarcode1.getKey()) == false)
//					{
//						//TODO: dont destroy anything
//						String geneID = affytogeneIDhashmap.get(entryFromBarcode1.getKey());
//						String uniprotID = geneIDtouniprothashmap.get(geneID);
//						String idcyto = uniprottoIDcytohashmap.get(uniprotID);
//						destroyNode(idcyto);
//						System.out.println("IDCyto=" + idcyto + ", Uniprot=" + uniprotID + ", GeneID=" + geneID + ", AffyID=" + entryFromBarcode1.getKey()+" true,false");
//					}
//
//					// if {false,true}
//					if(barcode2hashmap.get(entryFromBarcode1.getKey()) == true)
//					{
//						//TODO: dont destroy anything
//						String geneID = affytogeneIDhashmap.get(entryFromBarcode1.getKey());
//						String uniprotID = geneIDtouniprothashmap.get(geneID);
//						String idcyto = uniprottoIDcytohashmap.get(uniprotID);
//						System.out.println("IDCyto=" + idcyto + ", Uniprot=" + uniprotID + ", GeneID=" + geneID + ", AffyID=" + entryFromBarcode1.getKey()+" false,true");
//					}
//					
//				}
//			}
//		}
		
		JOptionPane
		.showMessageDialog(new JFrame(),
				"The graph view has been filtered and updated. " + counter + " nodes have been removed.",
				"Filtered", JOptionPane.PLAIN_MESSAGE);
		counter = 0;
	}
	
	/**
	 * This method adds to the arraylist the node IDs of nodes to be deleted and counts how many are (to be) deleted
	 * @param nodename The node identifier of the node to be removed
	 */
	public static void destroyNode(String nodename)
	{
		//get the network view object
        CyNetwork cynetwork = Cytoscape.getCurrentNetwork();
        @SuppressWarnings("unchecked")
		List<CyNode> cynodelist = Cytoscape.getCyNodesList();
        if(cynetwork==null || cynodelist.isEmpty())
        {
        	return;
        }
        else
        {
        	for (int i = 0; i < cynodelist.size(); i++)
            {
            	if(cynodelist.get(i).getIdentifier().equals(nodename))
            	{
//            		cynetwork.removeNode(cynodelist.get(i).getRootGraphIndex(), true);
            		nodeIDtobedeleted.add(cynodelist.get(i).toString());
            		counter++;
            	}
            }
    		Cytoscape.getCurrentNetworkView().redrawGraph(false, true);
        }
	}
	
	/**
	 * This method reads the input SIF file and compares its contents with the list of nodes to be deleted.
	 * Then a new SIF would be created, depending whether the input SIF is present in the list of nodes to be deleted
	 * @param pathname the original SIF file to be read
	 * @throws IOException
	 */
	public static void SIFreaderAndNewCreator(String pathname) throws IOException
	{
		BufferedReader reader = null;
		PrintWriter writer = null;
		try
		{
			reader = new BufferedReader(new FileReader(pathname));
			String[] temporarypath = pathname.split(".sif");
			String filetobewritten = temporarypath[0].concat(Controller.getAbsentProteinsConcatenation()+".sif");
			writer = new PrintWriter(filetobewritten);
			String line;
			while((line = reader.readLine()) != null)
			{
				// recognises whole line where there is a tab space.
				if(line.contains("\t"))
				{
					String node1;
					String node2;
					String edge;
					
					String[] splittedString = line.split("[\\t]"); // splits the line at the tab space
					node1 = splittedString[0].trim();
					edge = splittedString[1].trim();
					node2 = splittedString[2].trim();
					// if the list of nodes to be deleted contains the IDcyto read from the SIF,
					if (nodeIDtobedeleted.contains(node1) || nodeIDtobedeleted.contains(node2))
					{
						// do nothing here, new SIF would not contain the line
					}
					else // if list of nodes does not contain IDcyto to be deleted, then pass it to the new SIF by printing it
					{
						writer.println(line);
					}
				}
			}
		}

		finally
		{
			// have to close the writer, else wasting of resources
			if (writer != null)
			{
				writer.close();				
			}
			if(reader != null)
			{
				reader.close();
			}
			// clear the barcode hashmaps
			clearBarcodeHashMaps();
		}
	}
	
	/**
	 * This method clears all the array lists. If they are not cleared then 'residue' data from a previous read might still be present.
	 */
	public static void clearArrayLists()
	{
		file1tobewritten.clear();
		file2tobewritten.clear();
		file3tobewritten.clear();
		file4tobewritten.clear();
	}
	
	/**
	 * This method clears the barcode hashmaps and the ArrayList nodeIDtobedeleted.
	 * If the Hashmaps are not cleared, then the next filtering might be erroneous!
	 */
	public static void clearBarcodeHashMaps()
	{
		barcode1hashmap.clear();
		barcode2hashmap.clear();
		nodeIDtobedeleted.clear();
	}
	
	/**
	 * This method runs to fill up the UniprotID to GeneID Full hashmap,
	 * @param sourcefile
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static void makeHashMapUniProtToGeneID() throws IOException
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(UNIPROTTOGENEIDMAP));
			String line;
			while((line = reader.readLine()) != null)
			{
				// recognises whole line where there is an equal sign. prerequisite for the sourcefile to have them!
				if(line.contains("="))
				{
					String notmapped;
					String mappedfrom;
					String mappedto;
					String remarks;
					
					String[] splittedString = line.split("="); // splits the line at the equal signs
					notmapped = splittedString[0].trim();
					mappedfrom = splittedString[1].trim(); // UniprotID
					mappedto = splittedString[2].trim(); // GeneID
					remarks = splittedString[3].trim();
					// mappedfrom and mappedto is not empty, then add the pair into the appropriate hashmap
					if(!mappedfrom.isEmpty())
					{
						if(!mappedto.isEmpty())
						{
							uniprottogeneid_fullhashmap.put(mappedfrom, mappedto);
						}
					}
				}
			}
		}
		finally
		{
			if(reader != null)
			{
				reader.close();
			}
		}
	}
	
	/**
	 * This method runs once at the start to fill up the geneID to Affymetrix ID hashmap
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static void makeHashMapGeneIDtoAffyID() throws IOException
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(AFFYMETRIXSOURCE));
			String line;
			while((line = reader.readLine()) != null)
			{
				// recognises whole line where there is a tab space
				if(line.contains("\t"))
				{
					String composite_element_name = "";
					String comment_AECompositeName = "";
					String affymetrix_netaffx = "";
					String embl = "";
					String ensembl = "";
					String swall = "";
					String locus = "";
					String interpro = "";
					String unigene = "";
					String omim = "";
					String refseq = "";
					String scop = "";
					String genbank = "";
					String pkr_hanks = "";
					String ec = "";
					String cp450 = "";
					
					String[] splittedString = line.split("[\\t]", 16); // splits the line at the tab space and force cuts it 16 times						
					if (splittedString[0] != "")
					{
						composite_element_name = splittedString[0];						
					}
					if (splittedString[1] != "")
					{
						comment_AECompositeName = splittedString[1];
					}
					if (splittedString[2] != "")
					{
						affymetrix_netaffx = splittedString[2];
					}
					if (splittedString[3] != "")
					{
						embl = splittedString[3];
					}
					if (splittedString[4] != "")
					{
						ensembl = splittedString[4];
					}
					if (splittedString[5] != "")
					{
						swall = splittedString[5];
					}
					if (splittedString[6] != "")
					{
						locus = splittedString[6];
					}
					if (splittedString[7] != "")
					{
						interpro = splittedString[7];
					}
					if (splittedString[8] != "")
					{
						unigene = splittedString[8];
					}
					if (splittedString[9] != "")
					{
						omim = splittedString[9];
					}
					if (splittedString[10] != "")
					{
						refseq = splittedString[10];
					}
					if (splittedString[11] != "")
					{
						scop = splittedString[11];
					}
					if (splittedString[12] != "")
					{
						genbank = splittedString[12];
					}
					if (splittedString[13] != "")
					{
						pkr_hanks = splittedString[13];
					}
					if (splittedString[14] != "")
					{
						ec = splittedString[14];
					}
					if (splittedString[15] != "")
					{
						cp450 = splittedString[15];
					}
					//if locus (geneID) and comment_AECompositeName (AffyID) not empty,
					//then add the pair into the full geneidtoaffymetrixid_fullhashmap
					if(locus != "" && comment_AECompositeName != "")
					{
						geneidtoaffymetrixid_fullhashmap.put(locus, comment_AECompositeName);
					}
				}
			}
		}
		finally
		{
			if(reader != null)
			{
				reader.close();
			}
		}
	}
}
