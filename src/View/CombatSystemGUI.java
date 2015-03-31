package View;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.*;

import ObjectClasses.Chit;

public class CombatSystemGUI{
	
	private JFrame window;
	private ImageLookup lookup;
	private JPanel universalPanel;
	private JPanel combatPanel;
	private JPanel optionPanel;
	private GridBagConstraints c;
	private ArrayList<JLabel> protagonistLabels;
	private ArrayList<JLabel> enemyLabels;
	
	public CombatSystemGUI() {
		lookup = new ImageLookup();
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
		window.setResizable(false);
		combatPanel = new JPanel();
		combatPanel.setSize(640, 360);
		optionPanel = new JPanel();
		optionPanel.setSize(640, 120);
		combatPanel.setLayout(null);
		optionPanel.setLayout(new GridBagLayout());
		window.add(combatPanel);
		window.add(optionPanel);
	}

	public void addCharacters(ArrayList<Chit> side1, ArrayList<Chit> side2) {
		int x1 = 50;
		int y1 = 250;
		int x2 = 500;
		int y2 = 80;
		for (Chit chit : side1){
			ImageIcon imageIcon = 
					new ImageIcon(getClass().getResource(lookup.getValue(chit.getName())));
			JLabel label = new JLabel(imageIcon);
			label.setBounds(x1,y1,90,90);
			label.setOpaque(true);
			label.setName(chit.getName());
			//protagonistLabels.add(label);
			combatPanel.add(label);
			combatPanel.repaint();
		}
		
		for (Chit chit : side2){
			ImageIcon imageIcon = 
					new ImageIcon(getClass().getResource(lookup.getValue(chit.getName())));
			JLabel label = new JLabel(imageIcon);
			label.setBounds(x2,y2,90,90);
			label.setOpaque(true);
			label.setName(chit.getName());
			//protagonistLabels.add(label);
			combatPanel.add(label);
			combatPanel.repaint();
		}
	}
}
