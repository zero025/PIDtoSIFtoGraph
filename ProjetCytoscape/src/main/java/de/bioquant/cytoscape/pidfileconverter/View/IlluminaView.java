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

public class IlluminaView extends JFrame
{
	private JPanel mainpanel = new JPanel();
	private JPanel titlepanel = new JPanel();
	private JPanel toppanel = new JPanel();
	private JLabel titlelabel = new JLabel();
	private JLabel condition1label = new JLabel("Condition 1");
	private JLabel condition2label = new JLabel("Condition 2");
	private JTextField inputcondition1field = new JTextField(20);
	private JTextField inputcondition2field = new JTextField(20);
	private JButton browsecondition1button = new JButton("Browse");
	private JButton browsecondition2button = new JButton("Browse");
	private JButton applyfilterbutton = new JButton("Apply Filter");
	private JButton helpbutton = new JButton("?");
	
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
		titlelabel.setText("Import normalized file and 2 conditions.");
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
	    // adding the condition1label label to toppanel
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.ipady = 5;
		c.ipadx = 5;
		c.gridx = 0;
		c.gridy = 0;
		toppanel.add(condition1label, c);
		/* ------------------------------------------------------ */
		// adding the inputcondition1field to toppanel
		c.gridx = 1;
		c.gridy = 0;
		toppanel.add(inputcondition1field, c);
		/* ------------------------------------------------------ */
		// adding the browsecondition1button to toppanel and adding action commands/listener
		c.gridx = 2;
		c.gridy = 0;
		toppanel.add(browsecondition1button, c);
		browsecondition1button.setActionCommand("Browse Condition 1");
		browsecondition1button.addActionListener(controller);
		/* ------------------------------------------------------ */
		// adding the condition2label label to toppanel
		c.gridx = 0;
		c.gridy = 1;
		toppanel.add(condition2label, c);
		/* ------------------------------------------------------ */
		// adding the inputcondition2field label to toppanel
		c.gridx = 1;
		c.gridy = 1;
		toppanel.add(inputcondition2field, c);
		/* ------------------------------------------------------ */
		// adding the browsecondition2button to toppanel and adding action commands/listener
		c.gridx = 2;
		c.gridy = 1;
		toppanel.add(browsecondition2button, c);
		browsecondition2button.setActionCommand("Browse Condition 2");
		browsecondition2button.addActionListener(controller);
		/* ------------------------------------------------------ */
		// adding the applyfilterbutton to toppanel and adding action commands/listener
		c.gridx = 2;
		c.gridy = 2;
		toppanel.add(applyfilterbutton, c);
		applyfilterbutton.setActionCommand("Apply Filter");
		applyfilterbutton.addActionListener(controller);
		/* ------------------------------------------------------ */
		// adding the browsecondition2button to toppanel and adding action commands/listener
		c.gridx = 3;
		c.gridy = 2;
		toppanel.add(helpbutton, c);
		helpbutton.setActionCommand("Help");
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
}