package View;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;

import ListsAndLogic.ListOfMonsters;
import ObjectClasses.ActionChit;
import ObjectClasses.Character;
import ObjectClasses.Chit;
import ObjectClasses.Weapon;

public class CombatSystemGUI{
	
	public JFrame window;
	private ImageLookup lookup;
	private JPanel combatPanel;
	private JPanel optionPanel;
	private GridBagConstraints constraints;
	public JLabel infoText;
	private ListOfMonsters monsterList;
	
	public CombatSystemGUI() {
		lookup = new ImageLookup();
		monsterList = new ListOfMonsters();
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
		combatPanel.removeAll();
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
		}
		combatPanel.repaint();
	}
	
	public void setupOptions(ArrayList<Chit> side2) {
		infoText = new JLabel();
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
		window.pack();
	}

	public void flee(Character playerCharacter, ArrayList<Chit> enemies) {
		int maxMoveTime = 999;
		for (Chit enemy : enemies){
			HashMap<String, String> values = monsterList.monsters.get(enemy.getName());
			if (maxMoveTime > Integer.parseInt(values.get("moveSpeed"))){
				maxMoveTime = Integer.parseInt(values.get("moveSpeed"));
			}
		}
		for (ActionChit chit : playerCharacter.activeActionChits){
			if (chit.getTime() < maxMoveTime){
				JOptionPane.showMessageDialog(null, "You successfully managed to flee!");
				close();
			}
		}
		JOptionPane.showMessageDialog(null, "You can't flee! (No move chits fast enough)");
	}

	public void close() {
		window.dispose();
		window = null;
	}

	public int getTarget(ArrayList<Chit> enemies) {
		ArrayList<String> temp = new ArrayList<String>();
		for (Chit chit : enemies){
			temp.add(chit.getName());
		}
		Object[] enemyList = temp.toArray();
		
		Object selectedEnemy = JOptionPane.showInputDialog(window, 
		        "Select the enemy to attack:",
		        "Target",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        enemyList, 
		        enemyList[0]);
		
		for(int i = 0; i < enemyList.length; i++){
			if (enemyList[i].equals(selectedEnemy)){
				return i;
			}
		}
		return 0;
	}

	public Object getEncounterAction() {
		Object[] options = {"Alert Weapon", "Flee", "Activate/Deactivate Belongings", "Abandon belongings", "Order Natives to Attack (I'm too lazy to fight)"};
		return JOptionPane.showInputDialog(window, 
		        "Perform an action:",
		        "Action Turn",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        options, 
		        options[0]);
	}

	public void activateDeactivateItems(Character playerCharacter) {
		// TODO Auto-generated method stub
		
	}

	public void abandonItems(Character playerCharacter) {
		// TODO Auto-generated method stub
		
	}

	public ActionChit getFightChit(Character playerCharacter, ArrayList<Chit> enemies, boolean isEncounterStep, Chit prevFightChit, int effortAsterisks) {
		int maxMoveTime = 999;
		boolean cantFight = true;
		for (ActionChit chit : playerCharacter.activeActionChits){
			if (isEncounterStep){
				if ((chit.getTime() < maxMoveTime) && (chit.getName().equals("FIGHT") && (chit.numAsterisks() + effortAsterisks <= 2))){
					cantFight = false;
				}
			}
			else{
				if ((chit.getName().equals("FIGHT") && (chit.numAsterisks() + effortAsterisks <= 2))){
					cantFight = false;
				}
			}
		}
		if(cantFight && !isEncounterStep){
			JOptionPane.showMessageDialog(null, "You are too fatigued to fight, and can only fight with L 5.");
			return new ActionChit("FIGHT", "L", 5, 0);
		}
		for (Chit enemy : enemies){
			HashMap<String, String> values = monsterList.monsters.get(enemy.getName());
			if (maxMoveTime > Integer.parseInt(values.get("moveSpeed"))){
				maxMoveTime = Integer.parseInt(values.get("moveSpeed"));
			}
		}
		ArrayList<String> availableChits = new ArrayList<String>();
		if (prevFightChit != null){
			ArrayList<ActionChit> newChits = new ArrayList<ActionChit>();
			for (ActionChit chit : playerCharacter.activeActionChits){
				newChits.add(chit);
			}
			newChits.remove(prevFightChit);
			for (ActionChit chit : newChits){
				if (isEncounterStep){
					if ((chit.getTime() < maxMoveTime) && (chit.getName().equals("FIGHT"))){
						availableChits.add(chit.toString());
					}
				}
				else{
					if ((chit.getName().equals("FIGHT"))){
						availableChits.add(chit.toString());
					}
				}
				
			}
		}
		else{
			for (ActionChit chit : playerCharacter.activeActionChits){
				if (isEncounterStep){
					if ((chit.getTime() < maxMoveTime) && (chit.getName().equals("FIGHT"))){
						availableChits.add(chit.toString());
					}
				}
				else{
					if ((chit.getName().equals("FIGHT"))){
						availableChits.add(chit.toString());
					}
				}
				
			}
		}
		Object[] options = availableChits.toArray();
		if (options.length > 0){
			while(true){
				String selectedValue = (String) JOptionPane.showInputDialog(window, 
				        "Select a fight chit:",
				        "Fight Chit",
				        JOptionPane.QUESTION_MESSAGE, 
				        null, 
				        options, 
				        options[0]);
				for (ActionChit chit : playerCharacter.activeActionChits){
					if (chit.toString().equals(selectedValue)){
						// 2 effort limit as per 21.3/1b
						if ((chit.numAsterisks() + effortAsterisks) <= 2){
							return chit;
						}
						else{
							JOptionPane.showMessageDialog(null, "You can't select this chit...not enough energy! (Asterisk limit)");
						}
					}
				}
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "You can't alert your weapon! (No fight chits fast enough)");
		}
		return null;
	}

	public String[] getDirections(Character playerCharacter, int effortAsterisks) {
		String[] directions = new String[4];
		Object[] options = {"Thrust Ahead", "Swing to Side", "Smash Down"};
		directions[0] =  (String) JOptionPane.showInputDialog(window, 
		        "Select an attack direction:",
		        "Weapon Direction",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        options, 
		        options[0]);
		ArrayList<String> availableChits = new ArrayList<String>();
		for (ActionChit chit : playerCharacter.activeActionChits){
			if ((chit.getName().equals("MOVE"))){
					availableChits.add(chit.toString());
			}
		}
		options = availableChits.toArray();
		boolean cantMove = true;
		for (ActionChit chit : playerCharacter.activeActionChits){
			if (chit.numAsterisks() + effortAsterisks <= 2){
					cantMove = false;
			}
		}
		if (options.length > 0 && cantMove == false){
				while(true){
					boolean flag = false;
					directions[1] = (String) JOptionPane.showInputDialog(window, 
					        "Select a move chit for maneuvering:",
					        "Move Chit",
					        JOptionPane.QUESTION_MESSAGE, 
					        null, 
					        options, 
					        options[0]);
					for (ActionChit chit : playerCharacter.activeActionChits){
						if (chit.toString().equals(directions[1])){
							// 2 effort limit as per 21.3/1b
							if ((chit.numAsterisks() + effortAsterisks) <= 2){
								flag = true;
							}
							else{
								JOptionPane.showMessageDialog(null, "You can't select this chit...not enough energy! (Asterisk limit)");
							}
						}
					}
					if (flag == true){
						break;
					}
				}
				options = new Object[]{"Charge Ahead", "Dodge To Side", "Duck Down"};
				directions[2] =  (String) JOptionPane.showInputDialog(window, 
				        "Select a maneuver direction:",
				        "Maneuver Direction",
				        JOptionPane.QUESTION_MESSAGE, 
				        null, 
				        options, 
				        options[0]);
		}
		else{
			directions[1] = "MOVE L 5";
			options = new Object[]{"Charge Ahead", "Dodge To Side", "Duck Down"};
			directions[2] =  (String) JOptionPane.showInputDialog(window, 
			        "Select a maneuver direction (you are over-fatigued and can only deal Light damage:",
			        "Maneuver Direction",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        options, 
			        options[0]);
		}
		for (Chit item : playerCharacter.getInventory()){
			if (item.getName().contains("Shield")){
				options = new Object[]{"Thrust", "Swing", "Smash"};
				directions[3] =  (String) JOptionPane.showInputDialog(window, 
				        "Select a shield direction (Protect against):",
				        "Shield Direction",
				        JOptionPane.QUESTION_MESSAGE, 
				        null, 
				        options, 
				        options[0]);
			}
		}
		return directions;
	}

	public String getWeaponToAlert(Character playerCharacter) {
		ArrayList<String> availableChits = new ArrayList<String>();
		for (Chit chit : playerCharacter.getInventory()){
			if (chit instanceof Weapon){
				availableChits.add(chit.getName());
			}
		}
		Object[] options = availableChits.toArray();
		return (String) JOptionPane.showInputDialog(window, 
		        "Select a weapon to alert:",
		        "Weapon Selection",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        options, 
		        options[0]);
	}

	public void showVictorious() {
		JOptionPane.showMessageDialog(null, "You are victorious!");
	}
	
	public void showFlee() {
		JOptionPane.showMessageDialog(null, "You managed to flee successfully!");
	}
	
	public void showNoFlee() {
		JOptionPane.showMessageDialog(null, "You failed to flee successfully!");
	}

	public void showDefeat() {
		JOptionPane.showMessageDialog(null, "You were defeated!");
	}

	public ActionChit getSelectedChit(Character player, String category, boolean isFatigue) {
		ArrayList<String> availableChits = new ArrayList<String>();
		String prompt = "";
		if (isFatigue){
			prompt = "Select a chit to fatigue:";
		}
		else{
			prompt = "Select a chit to wound:";
		}
		for (Chit chit : player.activeActionChits){
			if (category.equals("FIGHT/MOVE")){
				String[] fightMove = category.split("/");
				if (chit.getName().contains(fightMove[0]) || chit.getName().contains(fightMove[1])){
					availableChits.add(chit.toString());
				}
			}
			else{
				if (chit.getName().contains(category)){
					availableChits.add(chit.toString());
				}
			}
		}
		Object[] options = availableChits.toArray();
		String chitToRemove = (String) JOptionPane.showInputDialog(window, 
		        prompt,
		        "Chit Selection",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        options, 
		        options[0]);
		for(ActionChit chit : player.activeActionChits){
			if (chit.toString().equals(chitToRemove)){
				return chit;
			}
		}
		return null;
	}

	public int getMonsterDieRoll(String monsterName) {
			String[] options = {"Charge and Thrust", "Dodge and Swing", "Duck and Smash"};
		    return JOptionPane.showOptionDialog(null, "Select the maneuver for " + monsterName, "*CHEAT MODE* - Monster Maneuver",
		        JOptionPane.DEFAULT_OPTION, 0, null, options, options[0]);
	}
	
	public void displayMessage(String message, String header) {
		JOptionPane.showMessageDialog(null, message, header, JOptionPane.PLAIN_MESSAGE);
	}
}
