package Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javafx.embed.swing.JFXPanel;

import javax.swing.JOptionPane;

import ControlFlow.CombatSystem;
import Networking.config;
import ObjectClasses.Character;
import ObjectClasses.Chit;

public class CombatSystemTest {

	static Character testCharacter;
	static ObjectOutputStream out;
	static ObjectInputStream in;
	
	public static void main(String[] args) {
		CombatSystem sys = new CombatSystem(true);
		JFXPanel fxPanel = new JFXPanel();
		testCharacter = new Character("Amazon", null);
		ArrayList<Chit> characters = new ArrayList<Chit>();
		characters.add(new Chit("Amazon", "L"));
		ArrayList<Chit> enemies = new ArrayList<Chit>();
		enemies.add(new Chit("Wolf", "L"));
		//enemies.add(new Chit("Viper", "L"));
		sys.initFight(characters, enemies, testCharacter, true);
	}
}
