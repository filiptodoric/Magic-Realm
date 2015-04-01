package ControlFlow;

import View.CombatSystemGUI;
import java.util.ArrayList;
import java.util.HashSet;

import ObjectClasses.Chit;

public class CombatSystem{
	CombatSystemGUI gui;
	public CombatSystem() {
		gui = new CombatSystemGUI();
	}
	
	public void resolveFight(ArrayList<Chit> side1, ArrayList<Chit> side2){
		gui.addCharacters(side1, side2);
	}
}
