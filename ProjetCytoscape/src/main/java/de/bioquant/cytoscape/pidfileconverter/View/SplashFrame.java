/**
 * Contributor Yamei & Thomas
 */

package de.bioquant.cytoscape.pidfileconverter.View;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import de.bioquant.cytoscape.pidfileconverter.FileReader.AbstractProcess;

@SuppressWarnings("serial")
public class SplashFrame extends JFrame {
	private JProgressBar bar;
	private JLabel label;
	private JButton start;
	private static SplashFrame myself;
	private AbstractProcess process;
	private JButton stop;

	/**
	 * Default constructor, without "start" and "stop" buttons
	 */
	public SplashFrame() {
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);	
		setTitle("Please Wait a Moment...");
		setLocationRelativeTo(null); 
		setSize(500, 200);

		label = new JLabel("Please wait for processing. This window closes automatically.");
		add(label,  BorderLayout.CENTER);

		bar = new JProgressBar();
		bar.setMaximum(100);
		bar.setMinimum(0);
		bar.setStringPainted(true);
		add(bar, BorderLayout.SOUTH);
		
       	setVisible(true);
       	
		// setfocus on this frame
		requestFocus();
		
	}
		
	/**
	 * Constructor linked to a processus, with a "sart" and a "stop" buttons.
	 * @param processus
	 * @param controller
	 */
	public SplashFrame(String processus, Controller controller) {
		
		SplashFrame.myself=this;
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);	
		setTitle("Please Wait a Moment...");
		setLocationRelativeTo(null); 
		setSize(500, 200);

		label = new JLabel("Please wait for processing. This window closes automatically.");
		add(label,  BorderLayout.NORTH);

		bar = new JProgressBar();
		bar.setMaximum(100);
		bar.setMinimum(0);
		bar.setStringPainted(true);
		add(bar, BorderLayout.SOUTH);
		
		JPanel centralPanel = new JPanel();
		start = new JButton("Start");
		start.setSize(100, 50);
		centralPanel.add(start);
		add(centralPanel, BorderLayout.CENTER);
		start.setActionCommand("Start "+processus);
		start.addActionListener(controller);
		
		stop = new JButton("Stop");
		stop.setSize(100, 50);
		centralPanel.add(stop);
		centralPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		centralPanel.add(stop);
		stop.setActionCommand("Stop "+processus);
		stop.addActionListener(controller);
		
		// Stop the thread on close
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				if (myself.process != null){
					myself.process.setContinueThread(false);
				}
				myself.dispose();
			}
		} );
		
		pack();
		setVisible(true);
	}
	public AbstractProcess getProcess() {
		return process;
	}

	public void setProcess(AbstractProcess process) {
		this.process = process;
	}

	public JProgressBar getBar() {
		return bar;
	}
	
	public JButton getStart() {
		return start;
	}
}
