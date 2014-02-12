/**
 * @contributor Yamei & Thomas
 */

package de.bioquant.cytoscape.pidfileconverter.View;

import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel mainpanel = new JPanel();
	private JPanel toptitlepanel = new JPanel();
	private JPanel toppanel = new JPanel();
	private JPanel middlepanel = new JPanel();
	private JPanel middletitlepanel = new JPanel();
	private JPanel bottompanel = new JPanel();
	private JPanel bottomtitlepanel = new JPanel();
	private JLabel numberonelabel = new JLabel();
	private JLabel numbertwolabel = new JLabel();
	private JLabel numberthreelabel = new JLabel();
	private JLabel inputfilenamelabel = new JLabel();
	private JLabel outputfilenamelabel = new JLabel();
	private JLabel expandcheckboxlabel = new JLabel();
	private JLabel affymetrixlabel = new JLabel("Affymetrix", SwingConstants.LEFT);
	private JLabel illuminalabel = new JLabel("Illumina", SwingConstants.LEFT);
	private JLabel sourcelabel = new JLabel();
	private JLabel targetlabel = new JLabel();
	private JLabel genelabel = new JLabel();
	private JLabel sigmollabel = new JLabel();
	private JLabel cytolabel = new JLabel();
	private JLabel emptylabel = new JLabel("          ");
	private JLabel includecomplexeslabel = new JLabel();
	private JTextField inputtextfield = new JTextField(20);
	private JTextField outputtextfield = new JTextField(20);
	private JTextField genesourcetextfield = new JTextField(20);
	private JTextField genetargettextfield = new JTextField(20);
	private JTextField sigmolsourcetextfield = new JTextField(20);
	private JTextField sigmoltargettextfield = new JTextField(20);
	private JTextArea cytoidsourcetextarea = new JTextArea();
	private JTextArea cytoidtargettextarea = new JTextArea();
	private JScrollPane cytoidsourcescrollpane;
	private JScrollPane cytoidtargetscrollpane;
	private JButton browsebutton = new JButton("Browse");
	private JButton outputbutton = new JButton("Output");
	private JButton convertbutton = new JButton("Convert/Show");
	private JButton choosebutton = new JButton("Choose");
	private JButton genesourcebrowsebutton = new JButton("Browse");
	private JButton genetargetbrowsebutton = new JButton("Browse");
	private JButton sigmolsourcebrowsebutton = new JButton("Browse");
	private JButton sigmoltargetbrowsebutton = new JButton("Browse");
	private JButton subgraphbutton = new JButton("Subgraph it!");
	private JButton checktoexpandhelpbutton = new JButton("?");
	private JButton step3helpbutton = new JButton("?");
	private JCheckBox expandcheckbox = new JCheckBox();
	private JCheckBox affymetrixcheckbox = new JCheckBox();
	private JCheckBox illuminacheckbox = new JCheckBox();
	private JCheckBox includecomplexescheckbox = new JCheckBox();

	public MainFrame(Controller controller) throws Exception {
		controller = new Controller(this);
		expandcheckboxlabel.setFont(new Font("Ariel", Font.ITALIC, 8));
		expandcheckboxlabel.setText("Check to expand");
		mainpanel.setLayout(new GridBagLayout());
		toptitlepanel.setLayout(new GridBagLayout());
		toppanel.setLayout(new GridBagLayout());
		middletitlepanel.setLayout(new GridBagLayout());
		middlepanel.setLayout(new GridBagLayout());
		bottomtitlepanel.setLayout(new GridBagLayout());
		bottompanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		/* ------------------------------------------------------ */
		// setting the number label and adding it to toptitlepanel
		numberonelabel.setText("Step 1: Generate Network from PID");
		numberonelabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 30; // make this component tall
		c.ipadx = 20;
		c.weightx = 0.0;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		toptitlepanel.add(numberonelabel, c);
		/* ------------------------------------------------------ */
		// adding the inputfilename label to toppanel
		inputfilenamelabel.setText("Select input file (XML/SIF):");
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.ipady = 5;
		c.ipadx = 5;
		c.gridx = 0;
		c.gridy = 0;
		toppanel.add(inputfilenamelabel, c);
		/* ------------------------------------------------------ */
		// adding the inputtextfield to toppanel
		c.gridx = 1;
		c.gridy = 0;
		toppanel.add(inputtextfield, c);
		/* ------------------------------------------------------ */
		// adding the convertbutton to toppanel and adding action commands/listener
		c.gridx = 2;
		c.gridy = 2;
		toppanel.add(convertbutton, c);
		convertbutton.setActionCommand("Convert");
		convertbutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// adding the expandcheckbox label to toppanel
		c.gridx = 3;
		c.gridy = 0;
		toppanel.add(expandcheckboxlabel, c);
		/* ------------------------------------------------------ */
		// adding the check to expand help button to toppanel
		c.gridx = 4;
		c.gridy = 0;
		c.ipady = 0;
		c.ipadx = 0;
		toppanel.add(checktoexpandhelpbutton, c);
		checktoexpandhelpbutton.setActionCommand("Check to expand help");
		checktoexpandhelpbutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// adding the emptylabel to toppanel
		c.gridx = 5;
		c.gridy = 0;
		toppanel.add(emptylabel, c);
		/* ------------------------------------------------------ */
		// adding the outputfilename label to toppanel
		outputfilenamelabel.setText("Output path/file (optional):");
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 5;
		c.ipadx = 5;
		toppanel.add(outputfilenamelabel, c);
		/* ------------------------------------------------------ */
		// adding the outputtextfield label to toppanel
		c.gridx = 1;
		c.gridy = 1;
		toppanel.add(outputtextfield, c);
		/* ------------------------------------------------------ */
		// adding the browsebutton to toppanel and adding action commands/listener
		c.gridx = 2;
		c.gridy = 0;
		toppanel.add(browsebutton, c);
		browsebutton.setActionCommand("Browse");
		browsebutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// adding the expandcheckbox to toppanel
		c.gridx = 3;
		c.gridy = 1;
		toppanel.add(expandcheckbox, c);
		/* ------------------------------------------------------ */
		// adding the outputbutton to toppanel and adding action commands/listener
		c.gridx = 2;
		c.gridy = 1;
		toppanel.add(outputbutton, c);
		outputbutton.setActionCommand("Output");
		outputbutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// Set the middle
		// setting the numbertwo label and adding it to middletitlepanel
		numbertwolabel.setText("Step 2: Overlap Microarray Data");
		numbertwolabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 30; // make this component tall
		c.ipadx = 20;
		c.weightx = 0.0;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		middletitlepanel.add(numbertwolabel, c);
		/* ------------------------------------------------------ */
		// adding the affymetrixcheckbox to middlepanel
		c.ipady = 0;
		c.ipadx = 0;
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		middlepanel.add(affymetrixcheckbox, c);
		/* ------------------------------------------------------ */
		// adding the illuminacheckbox to middlepanel
		c.gridx = 0;
		c.gridy = 1;
		middlepanel.add(illuminacheckbox, c);
		/* ------------------------------------------------------ */
		// adding the affymetrixlabel to middlepanel
		c.gridx = 1;
		c.gridy = 0;
		middlepanel.add(affymetrixlabel, c);
		/* ------------------------------------------------------ */
		// adding the illuminalabel to middlepanel
		c.gridx = 1;
		c.gridy = 1;
		middlepanel.add(illuminalabel, c);
		/* ------------------------------------------------------ */
		// adding the choosebutton to middlepanel and adding action commands/listener
		c.gridx = 1;
		c.gridy = 2;
		middlepanel.add(choosebutton, c);
		choosebutton.setActionCommand("Choose");
		choosebutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// Set the bottom panel
		// setting the numberthreelabel and adding it to bottomtitlepanel
		numberthreelabel.setText("Step 3: Subgraph Extraction");
		numberthreelabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 30; // make this component tall
		c.ipadx = 20;
		c.weightx = 0.0;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		bottomtitlepanel.add(numberthreelabel, c);
		/* ------------------------------------------------------ */
		// adding the step3helpbutton to bottompanel and adding action commands/listener
		c.ipadx = 0;
		c.ipady = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		bottompanel.add(step3helpbutton, c);
		step3helpbutton.setActionCommand("Step3Help");
		step3helpbutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// setting the sourcelabel and adding it to bottompanel
		sourcelabel.setText("SOURCE");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 0;
		bottompanel.add(sourcelabel, c);
		/* ------------------------------------------------------ */
		// setting the targetlabel and adding it to bottompanel
		targetlabel.setText("TARGET");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 2;
		c.gridx = 3;
		c.gridy = 0;
		bottompanel.add(targetlabel, c);
		/* ------------------------------------------------------ */
		// setting the genelabel and adding it to bottompanel
		genelabel.setText("Genes (UniProtID//EntrezGeneID):");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		bottompanel.add(genelabel, c);
		/* ------------------------------------------------------ */
		// adding the genesourcetextfield to bottompanel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 1;
		// bottompanel.add(genesourcetextfield, c);
		/* ------------------------------------------------------ */
		// adding the genesourcebrowsebutton to bottompanel
		c.ipadx = 10;
		c.ipady = 10;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 1;
		// bottompanel.add(genesourcebrowsebutton, c);
		genesourcebrowsebutton.setActionCommand("Gene Source Browse");
		genesourcebrowsebutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// adding the genetargettextfield to bottompanel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 1;
		bottompanel.add(genetargettextfield, c);
		/* ------------------------------------------------------ */
		// adding the genetargetbrowsebutton to bottompanel
		c.ipadx = 10;
		c.ipady = 10;
		c.gridwidth = 1;
		c.gridx = 4;
		c.gridy = 1;
		bottompanel.add(genetargetbrowsebutton, c);
		genetargetbrowsebutton.setActionCommand("Gene Target Browse");
		genetargetbrowsebutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// setting the sigmollabel and adding it to bottompanel
		sigmollabel.setText("Signalling Molecules (UniProtID//EntrezGeneID):");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		bottompanel.add(sigmollabel, c);
		/* ------------------------------------------------------ */
		// adding the sigmolsourcetextfield to bottompanel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		bottompanel.add(sigmolsourcetextfield, c);
		/* ------------------------------------------------------ */
		// adding the sigmolsourcebrowsebutton to bottompanel
		c.ipadx = 10;
		c.ipady = 10;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 2;
		bottompanel.add(sigmolsourcebrowsebutton, c);
		sigmolsourcebrowsebutton.setActionCommand("Sigmol Source Browse");
		sigmolsourcebrowsebutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// adding the sigmoltargettextfield to bottompanel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 2;
		bottompanel.add(sigmoltargettextfield, c);
		/* ------------------------------------------------------ */
		// adding the sigmoltargetbrowsebutton to bottompanel
		c.ipadx = 10;
		c.ipady = 10;
		c.gridwidth = 1;
		c.gridx = 4;
		c.gridy = 2;
		bottompanel.add(sigmoltargetbrowsebutton, c);
		sigmoltargetbrowsebutton.setActionCommand("Sigmol Target Browse");
		sigmoltargetbrowsebutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// setting the includecomplexeslabel and adding it to bottompanel
		includecomplexeslabel
				.setText("Please check here to include signalling molecules within nodes of protein complexes and protein families:");
		includecomplexeslabel.setFont(new Font("Ariel", Font.ITALIC, 9));
		c.ipady = 5;
		c.ipadx = 5;
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 3;
		bottompanel.add(includecomplexeslabel, c);
		/* ------------------------------------------------------ */
		// setting the includecomplexescheckbox and adding it to bottompanel
		c.ipady = 5;
		c.ipadx = 5;
		c.gridwidth = 2;
		c.gridx = 4;
		c.gridy = 3;
		bottompanel.add(includecomplexescheckbox, c);
		/* ------------------------------------------------------ */
		// setting the cytolabel and adding it to bottompanel
		cytolabel.setText("Enter CytoIDs, one in each line:");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		bottompanel.add(cytolabel, c);
		/* ------------------------------------------------------ */
		// adding the cytoidsourcetextarea scrollpane to bottompanel
		cytoidsourcetextarea.setSize(7, 10);
		cytoidsourcescrollpane = new JScrollPane(cytoidsourcetextarea);
		c.ipady = 100;
		c.ipadx = 100;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		bottompanel.add(cytoidsourcescrollpane, c);
		/* ------------------------------------------------------ */
		// adding the cytoidtargettextarea scrollpane to bottompanel
		cytoidtargettextarea.setSize(7, 10);
		cytoidtargetscrollpane = new JScrollPane(cytoidtargettextarea);
		c.ipady = 100;
		c.ipadx = 100;
		c.gridx = 3;
		c.gridy = 4;
		c.gridwidth = 2;
		bottompanel.add(cytoidtargetscrollpane, c);
		/* ------------------------------------------------------ */
		// adding the subgraphbutton to bottompanel and adding action commands/listener
		c.gridwidth = 1;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridx = 4;
		c.gridy = 5;
		bottompanel.add(subgraphbutton, c);
		subgraphbutton.setActionCommand("Subgraph");
		subgraphbutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// Add top title panel into the main panel
		c.ipady = 10;
		c.ipadx = 10;
		c.gridx = 0;
		c.gridy = 0;
		mainpanel.add(toptitlepanel, c);
		/* ------------------------------------------------------ */
		// Add top panel into the main panel
		c.ipady = 10;
		c.ipadx = 10;
		c.gridx = 0;
		c.gridy = 1;
		mainpanel.add(toppanel, c);
		/* ------------------------------------------------------ */
		// Add separator into the main panel
		c.ipady = 10;
		c.ipadx = 10;
		c.gridx = 0;
		c.gridy = 2;
		mainpanel.add(new JSeparator(JSeparator.HORIZONTAL), c);
		/* ------------------------------------------------------ */
		// Add middle title panel into the main panel
		c.ipady = 10;
		c.ipadx = 10;
		c.gridx = 0;
		c.gridy = 3;
		mainpanel.add(middletitlepanel, c);
		/* ------------------------------------------------------ */
		// Add middle panel into the main panel
		c.ipady = 10;
		c.ipadx = 10;
		c.gridx = 0;
		c.gridy = 4;
		mainpanel.add(middlepanel, c);
		/* ------------------------------------------------------ */
		// Add separator into the main panel
		c.ipady = 10;
		c.ipadx = 10;
		c.gridx = 0;
		c.gridy = 5;
		mainpanel.add(new JSeparator(JSeparator.HORIZONTAL), c);
		/* ------------------------------------------------------ */
		// Add bottom title panel into the main panel
		c.ipady = 10;
		c.ipadx = 10;
		c.gridx = 0;
		c.gridy = 6;
		mainpanel.add(bottomtitlepanel, c);
		/* ------------------------------------------------------ */
		// Add bottom title panel into the main panel
		c.ipady = 10;
		c.ipadx = 10;
		c.gridx = 0;
		c.gridy = 7;
		mainpanel.add(bottompanel, c);
		/* ------------------------------------------------------ */
		// add the main panel to the content pane
		getContentPane().add(mainpanel);
		getContentPane().setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public boolean isExpandChecked() {
		return expandcheckbox.isSelected();
	}

	public boolean isAffymetrixChecked() {
		return affymetrixcheckbox.isSelected();
	}

	public boolean isIlluminaChecked() {
		return illuminacheckbox.isSelected();
	}

	public boolean isIncludeComplexesChecked() {
		return includecomplexescheckbox.isSelected();
	}

	public String getInputTextfieldText() {
		return this.inputtextfield.getText();
	}

	public String getOutputTextfieldText() {
		return this.outputtextfield.getText();
	}

	public void setOutputTextfieldText(String s) {
		this.outputtextfield.setText(s);
	}

	public void setOutputFilenameLabelstring(String s) {
		outputfilenamelabel.setText(s);
	}

	public void setLabelstring(String s) {
		inputfilenamelabel.setText(s);
	}

	public String getTextFromTextArea() {
		return inputtextfield.getText();
	}

	public void setTextToOutputText(String arg0) {
		this.convertbutton.setText(arg0);
	}

	public JTextField getInputtext() {
		return inputtextfield;
	}

	public void setInputFileText(String inputtext) {
		this.inputtextfield.setText(inputtext);
	}

	public JTextArea getCytoidSourceTextArea() {
		return cytoidsourcetextarea;
	}

	public void setCytoidSourceTextAreaText(String cytoidsourcetextareatext) {
		this.cytoidsourcetextarea.setText(cytoidsourcetextareatext);
	}

	public JTextArea getCytoidTargetTextArea() {
		return cytoidtargettextarea;
	}

	public void setCytoidTargetTextAreaText(String arg) {
		this.cytoidtargettextarea.setText(arg);
	}

	public JTextField getGenesourcetextfield() {
		return genesourcetextfield;
	}

	public void setGenesourcetextfieldText(String arg) {
		this.genesourcetextfield.setText(arg);
	}

	public JTextField getGenetargettextfield() {
		return genetargettextfield;
	}

	public void setGenetargettextfieldText(String arg) {
		this.genetargettextfield.setText(arg);
	}

	public JTextField getSigmolsourcetextfield() {
		return sigmolsourcetextfield;
	}

	public void setSigmolsourcetextfieldText(String arg) {
		this.sigmolsourcetextfield.setText(arg);
	}

	public JTextField getSigmoltargettextfield() {
		return sigmoltargettextfield;
	}

	public void setSigmoltargettextfieldText(String arg) {
		this.sigmoltargettextfield.setText(arg);
	}
}
