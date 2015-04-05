package View;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import ObjectClasses.Chit;

public class CombatSystemGUI{
	
	private JFrame window;
	private ImageLookup lookup;
	private JPanel universalPanel;
	private JPanel combatPanel;
	private JPanel optionPanel;
	private GridBagConstraints constraints;
	private ArrayList<JLabel> protagonistLabels;
	private ArrayList<JLabel> enemyLabels;
	public JButton fleeButton;
	
	public CombatSystemGUI() {
		lookup = new ImageLookup();
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill   = GridBagConstraints.BOTH;
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		buildWindow();
	}
	
	public static void main(String[] args) {
		CombatSystemGUI combatSystemGui = new CombatSystemGUI();
	}
	
	public void buildWindow() {
		window = new JFrame("Combat");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(640, 480);
		window.setVisible(true);
		window.setResizable(true);
		window.setLayout(new GridBagLayout());
		combatPanel = new JPanel();
		combatPanel.setSize(640, 360);
		combatPanel.setBackground(Color.WHITE);
		combatPanel.setOpaque(true);
		optionPanel = new JPanel();
		optionPanel.setSize(640, 120);
		combatPanel.setLayout(new GridBagLayout());
		optionPanel.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		window.add(combatPanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		window.add(optionPanel, constraints);
	}

	public void addCharacters(ArrayList<Chit> side1, ArrayList<Chit> side2) {
		int characterCount = 0;
		for (Chit chit : side1){
			ImageIcon imageIcon = 
					new ImageIcon(getClass().getResource(lookup.getValue(chit.getName())));
			JLabel label = new JLabel(imageIcon);
			label.setOpaque(true);
			label.setName(chit.getName());
			constraints.gridx = characterCount;
			characterCount++;
			constraints.gridy = 1;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			combatPanel.add(label, constraints);
			combatPanel.repaint();
		}
		characterCount = 0;
		for (Chit chit : side2){
			ImageIcon imageIcon = 
					new ImageIcon(getClass().getResource(lookup.getValue(chit.getName())));
			JLabel label = new JLabel(imageIcon);
			label.setOpaque(true);
			label.setName(chit.getName());
			constraints.gridx = characterCount + 1;
			characterCount++;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			combatPanel.add(label, constraints);
			combatPanel.repaint();
		}
	}
	
	public void setupOptions(ArrayList<Chit> side2) {
		JLabel infoText = new JLabel();
		String enemies = "A hostile encounter: ";
		for (Chit enemy : side2){
			enemies += enemy.getName() + ", ";
		}
		enemies += " appeared!";
		infoText.setText(enemies);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		optionPanel.add(infoText, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		JButton fightButton = new JButton("Fight!");
		optionPanel.add(fightButton, constraints);
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		fleeButton = new JButton("Flee!");
		optionPanel.add(fleeButton, constraints);
		window.pack();
	}

	public void flee() {
		JOptionPane.showMessageDialog(null, "You successfully managed to flee!");
	}

	public void close() {
		window.dispose();
	}
}
