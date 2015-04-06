package ControlFlow;

import View.CombatSystemGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import ObjectClasses.Chit;
import ObjectClasses.Character;
import ObjectClasses.Weapon;

public class CombatSystem{
	CombatSystemGUI gui;
	Character playerCharacter;
	ObjectInputStream in;
    ObjectOutputStream out;
    boolean AIopponent = false;
    boolean attacker = false;
    ArrayList<Chit> allies;
    ArrayList<Chit> enemies;
	public CombatSystem(ObjectInputStream input, ObjectOutputStream output) {
		in = input;
		out = output;
		gui = new CombatSystemGUI();
	}
	
	public void initFight(ArrayList<Chit> side1, ArrayList<Chit> side2, Character player, boolean isAI, boolean isAttacker){
		playerCharacter = player;
		AIopponent = isAI;
		attacker = isAttacker;
		allies = side1;
		enemies = side2;
		gui.addCharacters(allies, enemies);
		gui.setupOptions(enemies);
		setActionListeners();
	}
	
	public void setActionListeners(){
		gui.fleeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (checkFlee()){
					gui.flee(playerCharacter, enemies);
				}
			}
		});
		
		gui.fightButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				startCombat();
			}
		});
		
		if(!attacker){
			gui.fightButton.setEnabled(false);
		}
	}
	
	protected void startCombat() {
		// TODO Auto-generated method stub
		if (AIopponent){
			// Select the enemy
			int index = gui.getTarget(enemies);
			// Play a fight chit, activate and/or deactivate one belonging, or abandon belongings
			String choice = (String) gui.getEncounterAction();
			if (choice.contains("Alert")){
				Chit fightChit1 = gui.getFightChit(playerCharacter, enemies, true);
				if (fightChit1 != null){
					String alertedWeapon = gui.getWeaponToAlert(playerCharacter);
					for (Chit chit : playerCharacter.getInventory()){
						if (chit.getName().contains(alertedWeapon)){
							((Weapon) chit).setAlerted(true);
						}
					}
				}
			}
			else if (choice.contains("Activate")){
				gui.activateDeactivateItems(playerCharacter);
			}
			else{
				gui.abandonItems(playerCharacter);
			}
			// Place attack, maneuver, and shield directions (if applicable)
			String[] directions = gui.getDirections(playerCharacter);
		}
		else{
			
		}
	}

	protected void fatigueChit() {
		// TODO Auto-generated method stub
		// Get chit to fatigue, fatigue it!
	}

	private boolean checkFlee(){
		for (Chit chit: playerCharacter.activeActionChits){
			if (chit.getName().contains("MOVE")){
				return true;
			}
		}
		for (Chit chit: playerCharacter.getInventory()){
			if (chit.getName().contains("Horse") || chit.getName().contains("Boots")){
				return true;
			}
		}
		
		return false;
	}
}
