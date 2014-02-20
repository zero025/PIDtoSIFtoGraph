package de.bioquant.cytoscape.pidfileconverter.View;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class Step2 extends JFrame {
	private JPanel panel = new JPanel();
	private JPanel titlePanel = new JPanel();
	private JPanel topPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();

	private JLabel titleLabel = new JLabel("Step 2: Overlap Microarray Data");
	private JLabel text = new JLabel(
			"Import Barcode output or normalized file from 2 conditions");
	private JLabel affymetrixLabel = new JLabel("Affymetrix");
	private JLabel illuminaLabel = new JLabel("Illumina");

	private JButton chooseButton = new JButton("Choose");
	private JButton helpButton = new JButton("Help");
	private JButton back = new JButton("< Back");
	private JButton next = new JButton("> Next");
	private JButton quit = new JButton("Quit");
	
	private JRadioButton illuminaRadio = new JRadioButton();
	private JRadioButton affymetrixRadio = new JRadioButton();
	private ButtonGroup group = new ButtonGroup();

	public Step2(Controller controller) throws Exception {

		controller = new Controller(this);

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
		
		//The text
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(15, 0, 20, 0);
		topPanel.add(text, c);		
		
		// adding the affymetrixcRadio to topPanel
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 5);
		group.add(affymetrixRadio);
		topPanel.add(affymetrixRadio, c);
		
		// adding the affymetrixLabel to topPanel
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		topPanel.add(affymetrixLabel, c);
		
		// adding the illuminaRadio to topPanel
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0, 0, 0, 5);
		group.add(illuminaRadio);
		topPanel.add(illuminaRadio, c);
		
		// adding the illuminaLabel to topPanel
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		topPanel.add(illuminaLabel, c);
		
		// adding the chooseButton to topPanel 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 3;
		c.ipady = 10;
		c.ipadx = 10;
		c.insets = new Insets(30, 0, 30, 0);
		topPanel.add(chooseButton, c);

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
		c.insets = new Insets(0, 0, 15, 0);
		bottomPanel.add(back, c);
		
		// adding the next button to bottomPanel
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 5;
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
		chooseButton.setActionCommand("Choose");
		chooseButton.addActionListener(controller);
		helpButton.setActionCommand("Help Step 2");
		helpButton.addActionListener(controller);
		back.setActionCommand("Back 1");
		back.addActionListener(controller);
		next.setActionCommand("Next 2");
		next.addActionListener(controller);
		quit.setActionCommand("Quit");
		quit.addActionListener(controller);

		// add the main panel to the content pane
		setContentPane(panel);

		// Set the frame
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("PIDtoSIftoGraph");
		setSize(500, 300);
		setLocationRelativeTo(null);
		//setResizable(false); // To disable the possibility to change the size of the window
		setVisible(true);
		//pack(); //To have a reduced window
		
		chooseButton.setToolTipText("choose Illumina or Affymetrix files");
		helpButton.setToolTipText("To get information about how to use the plugin");
		back.setToolTipText("Step 1 : Generate network from PID/SIF");
		next.setToolTipText("step 3 : Subgraph Extraction");
		quit.setToolTipText("Quit the plugin");

	}

	public boolean isAffymetrixSelected() {
		return affymetrixRadio.isSelected();
	}

	public boolean isIlluminaSelected() {
		return illuminaRadio.isSelected();
	}
}
