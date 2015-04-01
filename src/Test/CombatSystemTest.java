package Test;

import java.util.ArrayList;

import ControlFlow.CombatSystem;
import ObjectClasses.Chit;

public class CombatSystemTest {

	public static void main(String[] args) {
		CombatSystem sys = new CombatSystem();
		ArrayList<Chit> characters = new ArrayList<Chit>();
		characters.add(new Chit("Amazon", "L"));
		ArrayList<Chit> enemies = new ArrayList<Chit>();
		enemies.add(new Chit("Black Knight", "L"));
		sys.resolveFight(characters, enemies);
	}

}
