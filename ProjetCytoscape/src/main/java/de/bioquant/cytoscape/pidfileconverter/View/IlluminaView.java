package de.bioquant.cytoscape.pidfileconverter.View;

import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class IlluminaView extends JFrame
{
	private JPanel mainpanel = new JPanel();
	private JPanel titlepanel = new JPanel();
	private JPanel toppanel = new JPanel();
	private JLabel titlelabel = new JLabel();
	private JLabel fileLabel = new JLabel("File : ");
	private JLabel conditionsLabel = new JLabel("Conditions : ");
	private JTextField inputFileField = new JTextField(20);
	private JTextField inputconditionsField = new JTextField(20);
	private JButton browseFileButton = new JButton("Browse");
	private JButton applyfilterbutton = new JButton("Apply Filter");
	private JButton helpbutton = new JButton("?");
	private JScrollPane conditionsscrollpane;
	private JTextArea conditionsTextArea = new JTextArea();
	
	//TODO: this view has yet to be defined -> Ask carito!
	/**
	 * This constructor is responsible to build the frame of this class,
	 * as well as setting the labels, text areas and the buttons,
	 * as well as their actioncommands&listeners.
	 * @param controller
	 */
	public IlluminaView(Controller controller)
	{
		// set the controller
		controller = new Controller(this);
		
		// set the layouts for the different panels
		mainpanel.setLayout(new GridBagLayout());
		titlepanel.setLayout(new GridBagLayout());
		toppanel.setLayout(new GridBagLayout());
		
		// call a new set of gridbagconstraints
		GridBagConstraints c = new GridBagConstraints();
		/* ------------------------------------------------------ */
		// setting the titlelabel and adding it to titlepanel
		titlelabel.setText("Import normalized file and conditions.");
		titlelabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		c.fill = GridBagConstraints.HORIZONTAL;
	    c.ipady = 30;      //make this component tall
	    c.ipadx = 20;
	    c.weightx = 0.0;
	    c.gridwidth = 4;
	    c.gridx = 0;
	    c.gridy = 0;
	    titlepanel.add(titlelabel, c);
		/* ------------------------------------------------------ */
	    // adding the filelabel label to toppanel
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.ipady = 5;
		c.ipadx = 5;
		c.gridx = 0;
		c.gridy = 0;
		toppanel.add(fileLabel, c);
		/* ------------------------------------------------------ */
		// adding the inputFileField to toppanel
		c.gridx = 1;
		c.gridy = 0;
		toppanel.add(inputFileField, c);
		/* ------------------------------------------------------ */
		// adding the browseFileButton to toppanel and adding action commands/listener
		c.gridx = 2;
		c.gridy = 0;
		toppanel.add(browseFileButton, c);
		browseFileButton.setActionCommand("Illumina Browse File");
		browseFileButton.addActionListener(controller);
		/*------------------------------------------------------- */
		// adding the conditions text area to toppannel 
		conditionsTextArea.setSize(7, 50);
	    conditionsscrollpane = new JScrollPane(conditionsTextArea);
	    //conditionsTextArea.setRows(3);
		c.ipady = 50;
	    c.gridx = 1;
		c.gridy = 1;
		toppanel.add(conditionsscrollpane, c);
		
		/* ------------------------------------------------------ */
		// adding the conditionslabel label to toppanel
		c.ipady = 15;
		c.ipadx = 15;
		c.gridx = 0;
		c.gridy = 1;
		toppanel.add(conditionsLabel, c);
		/* ------------------------------------------------------ */
		// adding the inputconditionsField label to toppanel
		c.gridx = 1;
		c.gridy = 1;
		toppanel.add(inputconditionsField, c);
		/* ------------------------------------------------------ */
		// adding the applyfilterbutton to toppanel and adding action commands/listener
		c.gridx = 2;
		c.gridy = 3;
		toppanel.add(applyfilterbutton, c);
		applyfilterbutton.setActionCommand("Illumina Apply Filter");
		applyfilterbutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// adding the help button to toppanel and adding action commands/listener
		c.gridx = 3;
		c.gridy = 3;
		toppanel.add(helpbutton, c);
		helpbutton.setActionCommand("Illumina Help");
		helpbutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// Add top title panel into the main panel
	    c.ipady = 10;
	    c.ipadx = 10;
	    c.gridx = 0;
	    c.gridy = 0;
		mainpanel.add(titlepanel, c);
		/* ------------------------------------------------------ */
		// Add top panel into the main panel
	    c.ipady = 10;
	    c.ipadx = 10;
	    c.gridx = 0;
	    c.gridy = 1;
		mainpanel.add(toppanel, c);
	    /* ------------------------------------------------------ */
		
		// add the main panel to the content pane
		getContentPane().add(mainpanel);
		getContentPane().setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
	}
	
	public void setInputFileFieldText(String arg)
	{
		this.inputFileField.setText(arg);
	}

	public void setInputConditionsFieldText(String arg)
	{
		this.inputconditionsField.setText(arg);
	}

	public JTextField getInputFileField() {
		return inputFileField;
	}

	public void setInputFileField(JTextField inputFileField) {
		this.inputFileField = inputFileField;
	}

	public JTextField getInputconditionsField() {
		return inputconditionsField;
	}

	public void setInputconditionsField(JTextField inputconditionsField) {
		this.inputconditionsField = inputconditionsField;
	}
	
	public JTextArea getConditionsTextArea()
	{
		return conditionsTextArea;
	}
}