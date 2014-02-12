package de.bioquant.cytoscape.pidfileconverter.View;

import java.awt.ComponentOrientation;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SplashFrame extends JFrame {
	private JPanel panel = new JPanel();
	private JLabel label = new JLabel("Please wait for processing. This window closes automatically.");

	public SplashFrame() {
		panel.add(label);
		getContentPane().add(panel);
		getContentPane().setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
