package de.bioquant.cytoscape.pidfileconverter.View;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Step3 extends JFrame {

	private JPanel panel = new JPanel();
	private JPanel titlePanel = new JPanel();
	private JPanel topPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();

	private JLabel titleLabel = new JLabel("Step 3: Subgraph extraction");
	private JLabel sourceTitleLabel = new JLabel("Source:");
	private JLabel targetTitleLabel = new JLabel("Target:");
	private JLabel geneLabel = new JLabel("Genes (UniprotID/EntrezGeneID):");
	private JLabel sourceLabel = new JLabel(
			"Signaling molecules (UniprotID/EntrezGeneID):");
	private JLabel cytoLabel = new JLabel("CytoIDs:");
	private JLabel includeLabel = new JLabel(
			"Include signaling molecules within nodes of protein complex and protein families:");

	private JTextField geneTargetTextField = new JTextField(20);
	private JTextField sourceSourceTextField = new JTextField(20);
	private JTextField sourceTargetTextField = new JTextField(20);

	private JButton browseGeneTargetButton = new JButton("Browse");
	private JButton browseSourceSourceButton = new JButton("Browse");
	private JButton browseSourceTargetButton = new JButton("Browse");
	private JButton subgraphButton = new JButton("Subgraph it!");
	private JButton helpButton = new JButton("Help");
	private JButton back = new JButton("< Back");
	private JButton quit = new JButton("Quit");

	private JCheckBox includeCheckbox = new JCheckBox();

	private JTextArea cytoidSourceTextArea = new JTextArea();
	private JTextArea cytoidTargetTextArea = new JTextArea();
	private JScrollPane cytoidsourcescrollpane;
	private JScrollPane cytoidtargetscrollpane;

	/**
	 * Constructor of the window
	 * @param controller
	 * @throws Exception
	 */
	public Step3(Controller controller) throws Exception {

		controller = new Controller(this);

		titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
		includeLabel.setFont(new Font("Times New Roman", Font.ITALIC, 10));

		panel.setLayout(new GridBagLayout());
		topPanel.setLayout(new GridBagLayout());
		bottomPanel.setLayout(new GridBagLayout());

		// setting the title label and adding it to topPanel
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		/* ------------------------------------------------------ */
		// setting the title
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 30;
		c.ipadx = 20;
		c.weightx = 0.0;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		titlePanel.add(titleLabel, c);
		/* ------------------------------------------------------ */
		// setting the sourceTitleLabel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 0;
		topPanel.add(sourceTitleLabel, c);
		/* ------------------------------------------------------ */
		// setting the targetlabel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 2;
		c.gridx = 3;
		c.gridy = 0;
		topPanel.add(targetTitleLabel, c);
		/* ------------------------------------------------------ */
		// setting the geneLabel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		topPanel.add(geneLabel, c);
		/* ------------------------------------------------------ */
		// adding the geneTargetTextField
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 1;
		topPanel.add(geneTargetTextField, c);
		/* ------------------------------------------------------ */
		// adding the browseGeneTargetBbutton
		c.ipadx = 10;
		c.ipady = 10;
		c.gridwidth = 1;
		c.gridx = 4;
		c.gridy = 1;
		topPanel.add(browseGeneTargetButton, c);
		/* ------------------------------------------------------ */
		// setting the sourceSourceLabel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		topPanel.add(sourceLabel, c);
		/* ------------------------------------------------------ */
		// adding the sourceSourceTextField
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		topPanel.add(sourceSourceTextField, c);
		/* ------------------------------------------------------ */
		// adding the browseSourceSourceButton
		c.ipadx = 10;
		c.ipady = 10;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 2;
		topPanel.add(browseSourceSourceButton, c);
		/* ------------------------------------------------------ */
		// adding the browseSourceTargetTextField
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 2;
		topPanel.add(sourceTargetTextField, c);
		/* ------------------------------------------------------ */
		// adding the browseSourceTargetButton
		c.ipadx = 10;
		c.ipady = 10;
		c.gridwidth = 1;
		c.gridx = 4;
		c.gridy = 2;
		topPanel.add(browseSourceTargetButton, c);
		/* ------------------------------------------------------ */
		// setting the cytoLabel
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		topPanel.add(cytoLabel, c);
		/* ------------------------------------------------------ */
		// adding the cytoidSourceTextArea
		cytoidSourceTextArea.setSize(7, 10);
		cytoidsourcescrollpane = new JScrollPane(cytoidSourceTextArea);
		c.anchor = GridBagConstraints.CENTER;
		c.ipady = 100;
		c.ipadx = 100;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		topPanel.add(cytoidsourcescrollpane, c);
		/* ------------------------------------------------------ */
		// adding the cytoidTargetTextArea
		cytoidTargetTextArea.setSize(7, 10);
		cytoidtargetscrollpane = new JScrollPane(cytoidTargetTextArea);
		c.ipady = 100;
		c.ipadx = 100;
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 2;
		topPanel.add(cytoidtargetscrollpane, c);
		/* ------------------------------------------------------ */
		// adding the includeLabel
		c.ipady = 5;
		c.ipadx = 5;
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 4;
		topPanel.add(includeLabel, c);
		/* ------------------------------------------------------ */
		// setting the includeCheckbox
		c.ipady = 5;
		c.ipadx = 5;
		c.gridwidth = 2;
		c.gridx = 4;
		c.gridy = 4;
		topPanel.add(includeCheckbox, c);
		/* ------------------------------------------------------ */
		// adding the subgraphButton
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.NONE;
		c.ipady = 20;
		c.ipadx = 30;
		c.gridx = 0;
		c.gridy = 5;
		topPanel.add(subgraphButton, c);
		
		// adding the help button to bottomPanel		
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridwidth = 1;
		c.gridy = 0;
		c.ipady = 0;
		c.ipadx = 0;
		c.weightx =1;
		c.insets = new Insets(0, 15, 15, 0);
		bottomPanel.add(helpButton, c);

		// adding the back button to bottomPanel
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 4;
		c.weightx =0;
		c.insets = new Insets(0, 0, 15, 65);// adding a space to replace the next button on the previous steps
		bottomPanel.add(back, c);
		
		// adding the quit button to bottomPanel
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 6;
		c.insets = new Insets(0, 5, 15, 15);
		bottomPanel.add(quit, c);

		// Main panel
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy = 0;
		c.gridx = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		panel.add(titlePanel, c);

		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 1;
		c.weightx =1;
		c.weighty =1;
		c.fill = GridBagConstraints.BOTH;
		panel.add(topPanel, c);
		
		c.weightx =0;
		c.weighty =0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.PAGE_END;
		panel.add(bottomPanel, c);
		
		// Link to the controller
		browseGeneTargetButton.setActionCommand("Gene Target Browse");
		browseGeneTargetButton.addActionListener(controller);
		browseSourceSourceButton.setActionCommand("Source Source Browse");
		browseSourceSourceButton.addActionListener(controller);
		browseSourceTargetButton.setActionCommand("Source Target Browse");
		browseSourceTargetButton.addActionListener(controller);
		subgraphButton.setActionCommand("Subgraph");
		subgraphButton.addActionListener(controller);
		helpButton.setActionCommand("Help Step 3");
		helpButton.addActionListener(controller);
		back.setActionCommand("Back 2");
		back.addActionListener(controller);
		quit.setActionCommand("Quit");
		quit.addActionListener(controller);

		// add the main panel to the content pane
		setContentPane(panel);

		// Set the frame
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("PIDtoSIftoGraph");
		setLocationRelativeTo(null);
		//setResizable(false); // To disable the possibility to change the size of the window
		setVisible(true);
		pack(); //To have a reduced window

		//Tooltip help
		browseGeneTargetButton.setToolTipText("browse Gene Molecules-Targets");
		browseSourceSourceButton.setToolTipText("browse Source Molecules-Sources");
		browseSourceTargetButton.setToolTipText("browse Source Molecules-Targets");
		subgraphButton.setToolTipText("Find the shortest path between sources and targets");
		helpButton.setToolTipText("To get information about how to use the plugin");
		back.setToolTipText("Step 2 : Overlap Microarray Data");
		quit.setToolTipText("Quit the plugin");
		cytoidSourceTextArea.setToolTipText("put citoIDs of sources");
		cytoidTargetTextArea.setToolTipText("put citoIDs of targets");
	}

	public JTextField getSourceSourceTextField() {
		return sourceSourceTextField;
	}

	public JTextField getGeneTargetTextField() {
		return geneTargetTextField;
	}

	public JTextField getSourceTargetTextField() {
		return sourceTargetTextField;
	}

	public JTextArea getCytoidSourceTextArea() {
		return cytoidSourceTextArea;
	}

	public JTextArea getCytoidTargetTextArea() {
		return cytoidTargetTextArea;
	}

	public boolean isIncludeComplexesChecked() {
		return includeCheckbox.isSelected();
	}

	public void setGeneTargetTextFieldText(String arg) {
		this.geneTargetTextField.setText(arg);
	}

	public void setSourceSourceTextFieldText(String arg) {
		this.sourceSourceTextField.setText(arg);
	}

	public void setSourceTargetTextFieldText(String arg) {
		this.sourceTargetTextField.setText(arg);
	}

}
