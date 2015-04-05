package ControlFlow;

import View.CombatSystemGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

import ObjectClasses.Chit;
import ObjectClasses.Character;

public class CombatSystem{
	CombatSystemGUI gui;
	Character playerCharacter;
	public CombatSystem() {
		gui = new CombatSystemGUI();
	}
	
	public void initFight(ArrayList<Chit> side1, ArrayList<Chit> side2, Character player){
		playerCharacter = player;
		gui.addCharacters(side1, side2);
		gui.setupOptions(side2);
		setActionListeners();
	}
	
	public void setActionListeners(){
		gui.fleeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (checkFlee()){
					gui.flee();
					fatigueChit();
					gui.close();
				}
			}
		});
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
