/**
 * @contributor Yamei & Thomas
 */

package de.bioquant.cytoscape.pidfileconverter.View;

import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class IlluminaView extends JFrame {
	private JPanel mainpanel = new JPanel();
	private JPanel filepanel = new JPanel();
	private JPanel conditionspanel = new JPanel();
	private JLabel insertNameslabel = new JLabel("Insert the names of 2 conditions.", SwingConstants.LEFT);
	private JLabel fileLabel = new JLabel("Select Normalized Illumina file (.CSV)");
	private JLabel condition1Label = new JLabel("Condition 1", SwingConstants.LEFT);
	private JLabel condition2Label = new JLabel("Condition 2", SwingConstants.LEFT);
	private JTextField inputFileField = new JTextField(20);
	private JTextField inputcondition1Field = new JTextField(20);
	private JTextField inputcondition2Field = new JTextField(20);
	private JButton browseFileButton = new JButton("Browse");
	private JButton applyfilterbutton = new JButton("Apply Filter");
	private JButton helpbutton = new JButton("?");

	/**
	 * This constructor is responsible to build the frame of this class, as well as setting the labels, text areas and the buttons, as well as their
	 * actioncommands&listeners.
	 * 
	 * @param controller
	 */
	public IlluminaView(Controller controller) {
		// set the controller
		controller = new Controller(this);

		// set the layouts for the different panels
		mainpanel.setLayout(new GridBagLayout());
		filepanel.setLayout(new GridBagLayout());
		conditionspanel.setLayout(new GridBagLayout());

		// call a new set of gridbagconstraints
		GridBagConstraints c = new GridBagConstraints();
		/* ------------------------------------------------------ */
		// adding the filelabel label to filepanel
		c.ipady = 5;
		c.ipadx = 5;
		c.gridx = 0;
		c.gridy = 0;
		filepanel.add(fileLabel, c);
		/* ------------------------------------------------------ */
		// adding the inputFileField to filepanel
		c.gridx = 1;
		c.gridy = 0;
		filepanel.add(inputFileField, c);
		/* ------------------------------------------------------ */
		// adding the browseFileButton to filepanel and adding action commands/listener
		c.gridx = 2;
		c.gridy = 0;
		filepanel.add(browseFileButton, c);
		browseFileButton.setActionCommand("Illumina Browse File");
		browseFileButton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// setting the insertNameslabel and adding it to conditionspanel
		insertNameslabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		// c.ipady = 30; //make this component tall
		// c.ipadx = 20;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		conditionspanel.add(insertNameslabel, c);
		/* ------------------------------------------------------ */
		// adding the condition1label label to conditionspanel
		// c.ipady = 15;
		// c.ipadx = 15;
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		conditionspanel.add(condition1Label, c);
		/* ------------------------------------------------------ */
		// adding the condition2label label to conditionspanel
		// c.ipady = 15;
		// c.ipadx = 15;
		c.gridx = 0;
		c.gridy = 2;
		conditionspanel.add(condition2Label, c);
		/* ------------------------------------------------------ */
		// adding the inputcondition1Field label to conditionspanel
		c.gridx = 1;
		c.gridy = 1;
		conditionspanel.add(inputcondition1Field, c);
		/* ------------------------------------------------------ */
		// adding the inputcondition1Field label to conditionspanel
		c.gridx = 1;
		c.gridy = 2;
		conditionspanel.add(inputcondition2Field, c);
		/* ------------------------------------------------------ */
		// adding the applyfilterbutton to conditionspanel and adding action commands/listener
		c.gridx = 3;
		c.gridy = 3;
		conditionspanel.add(applyfilterbutton, c);
		applyfilterbutton.setActionCommand("Illumina Apply Filter");
		applyfilterbutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// adding the help button to conditionspanel and adding action commands/listener
		c.gridx = 3;
		c.gridy = 4;
		conditionspanel.add(helpbutton, c);
		helpbutton.setActionCommand("Illumina Help");
		helpbutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// Add filepanel into the main panel
		c.ipady = 10;
		c.ipadx = 10;
		c.gridx = 0;
		c.gridy = 0;
		mainpanel.add(filepanel, c);
		/* ------------------------------------------------------ */
		// Add conditionspanel into the main panel
		c.gridx = 0;
		c.gridy = 1;
		mainpanel.add(conditionspanel, c);
		/* ------------------------------------------------------ */
		// add the main panel to the content pane
		getContentPane().add(mainpanel);
		getContentPane().setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void setInputFileFieldText(String arg) {
		this.inputFileField.setText(arg);
	}

	public void setInputCondition1FieldText(String arg) {
		this.inputcondition1Field.setText(arg);
	}

	public void setInputCondition2FieldText(String arg) {
		this.inputcondition2Field.setText(arg);
	}

	public JTextField getInputFileField() {
		return inputFileField;
	}

	public void setInputFileField(JTextField inputFileField) {
		this.inputFileField = inputFileField;
	}

	public JTextField getInputcondition1Field() {
		return inputcondition1Field;
	}

	public JTextField getInputcondition2Field() {
		return inputcondition2Field;
	}
}