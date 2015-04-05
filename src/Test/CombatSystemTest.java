package Test;

import java.util.ArrayList;

import ControlFlow.CombatSystem;
import ObjectClasses.Character;
import ObjectClasses.Chit;

public class CombatSystemTest {

	static Character testCharacter;
	
	public static void main(String[] args) {
		CombatSystem sys = new CombatSystem();
		testCharacter = new Character("Amazon", null);
		ArrayList<Chit> characters = new ArrayList<Chit>();
		characters.add(new Chit("Amazon", "L"));
		ArrayList<Chit> enemies = new ArrayList<Chit>();
		enemies.add(new Chit("Black Knight", "L"));
		sys.initFight(characters, enemies, testCharacter);
	}

}
