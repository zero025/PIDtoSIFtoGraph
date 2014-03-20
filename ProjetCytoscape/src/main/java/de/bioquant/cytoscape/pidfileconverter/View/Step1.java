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
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Step1 extends JFrame {

	private JPanel panel = new JPanel();
	private JPanel titlePanel = new JPanel();
	private JPanel topPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();

	private JLabel titleLabel = new JLabel(
			"Step 1: Generate Network from PID/SIF");
	private JLabel inputFilenameLabel = new JLabel("Input file (XML/SIF):");
	private JLabel outputFilenameLabel = new JLabel(
			"Output path/file (optional):");
	private JLabel showMore = new JLabel("Show family & complex members:");
	private JLabel hierachicalLabel = new JLabel("Hierarchical graph:");
	
	private JTextField inputTextField = new JTextField(30);
	private JTextField outputTextField = new JTextField(30);

	private JButton browseInputButton = new JButton("Browse");
	private JButton browseOutputButton = new JButton("Browse");
	private JButton convertButton = new JButton("Convert/Show");
	private JButton helpButton = new JButton("Help");
	private JButton next = new JButton(">Next");
	private JButton quit = new JButton("Quit");

	private JCheckBox showMoreCheckBox = new JCheckBox();
	private JCheckBox hierarchicalCheckBox = new JCheckBox();
	
	/**
	 * Constructor of the window
	 * @param controller
	 * @throws Exception
	 */
	public Step1(Controller controller) throws Exception {

		controller = new Controller(this);

		showMore.setFont(new Font("Arial", Font.ITALIC, 10));
		hierachicalLabel.setFont(new Font("Arial", Font.ITALIC, 10));
		titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
		
		panel.setLayout(new GridBagLayout());
		topPanel.setLayout(new GridBagLayout());
		bottomPanel.setLayout(new GridBagLayout());

		// setting the title label and adding it to topPanel
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 50, 0);
		titlePanel.add(titleLabel, c);
		
		// adding the inputFilenameLabel to topPanel
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 15, 0, 0);
		topPanel.add(inputFilenameLabel, c);
		
		// adding the inputTextField to topPanel
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.insets = new Insets(0, 0, 0, 10);
		topPanel.add(inputTextField, c);
		
		// adding the browseInputButton to topPanel
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 2;
		c.insets = new Insets(0, 0, 0, 15);
		topPanel.add(browseInputButton, c);

		// adding the outputFilename label to topPanel
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(10, 15, 0, 0);
		c.gridx = 0;
		c.gridy = 1;
		topPanel.add(outputFilenameLabel, c);
		
		// adding the outputTextField label to topPanel
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.insets = new Insets(10, 0, 0, 10);
		topPanel.add(outputTextField, c);
		
		// adding the browseOutputButton to topPanel
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 2;
		c.insets = new Insets(10, 0, 0, 15);
		c.gridx = 2;
		topPanel.add(browseOutputButton, c);
		
		// adding the showMore label to topPanel
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(15, 15, 0, 0);
		c.gridx = 0;
		c.gridy = 3;
		topPanel.add(showMore, c);

		// adding the showmore checkbox to topPanel
		c.gridx = 1;
		topPanel.add(showMoreCheckBox, c);

		// adding the hierarchical label to topPanel
		c.gridx = 0;
		c.gridy = 4;
		topPanel.add(hierachicalLabel, c);

		// adding the hierarchical checkbox to topPanel
		c.gridx = 1;
		topPanel.add(hierarchicalCheckBox, c);
		
		// adding the convertButton to topPanel
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 5;
		c.ipady = 10;
		c.ipadx = 10;
		c.insets = new Insets(30, 0, 30, 0);
		topPanel.add(convertButton, c);
				
		// adding the help button to bottomPanel
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridwidth = 1;
		c.gridy = 0;
		c.ipady = 0;
		c.ipadx = 0;
		c.weightx =1;
		c.insets = new Insets(0, 15, 15, 0);
		bottomPanel.add(helpButton, c);
		
		// adding the next button to bottomPanel
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 5;
		c.weightx =0;
		c.insets = new Insets(0, 5, 15, 0);
		bottomPanel.add(next, c);
		
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
		browseInputButton.setActionCommand("Browse");
		browseInputButton.addActionListener(controller);
		browseOutputButton.setActionCommand("Output");
		browseOutputButton.addActionListener(controller);
		convertButton.setActionCommand("Convert");
		convertButton.addActionListener(controller);
		helpButton.setActionCommand("Help Step 1");
		helpButton.addActionListener(controller);
		next.setActionCommand("Next 1");
		next.addActionListener(controller);
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
		
		next.setEnabled(false); //The user cannot go to step 2 or 3 without loading a file (PID or SIF).
		
		//tooltip help
		browseInputButton.setToolTipText("Click here to find a PID file, XML or SIF format");
		browseOutputButton.setToolTipText("Click here to find the directory where the files will be created (optionnal)");
		convertButton.setToolTipText("Create a graph from a PID and show it");
		helpButton.setToolTipText("To get information about how to use the plugin");
		next.setToolTipText("step 2 : Overlap Microarray Data");
		quit.setToolTipText("Quit the plugin");
	}

	public String getInputTextFieldText() {
		return inputTextField.getText();
	}

	public void setInputTextField(String inputTextFieldText) {
		this.inputTextField.setText(inputTextFieldText);
	}

	public String getOutputTextFieldText() {
		return outputTextField.getText();
	}

	public void setOutputTextField(String outputTextFieldText) {
		this.outputTextField.setText(outputTextFieldText);
	}

	public JButton getNext() {
		return next;
	}

	public boolean isExpandChecked() {
		return showMoreCheckBox.isSelected();
	}

	public boolean isHierarchicalChecked() {
		return hierarchicalCheckBox.isSelected();
	}
	
	public void setInputTextField(JTextField inputTextField) {
		this.inputTextField = inputTextField;
	}

	public void setOutputTextFieldText(String s) {
		this.outputTextField.setText(s);
	}

}
