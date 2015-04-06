package Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import ControlFlow.CombatSystem;
import Networking.config;
import ObjectClasses.Character;
import ObjectClasses.Chit;

public class CombatSystemTest {

	static Character testCharacter;
	static ObjectOutputStream out;
	static ObjectInputStream in;
	
	public static void main(String[] args) {
		String serverAddress = "";
        try {
        	// If left blank, serverAddress will simply be "localhost"
        	Socket socket = new Socket(serverAddress, config.PORT);
        	out = new ObjectOutputStream(socket.getOutputStream());
        	out.flush();
        	in = new ObjectInputStream(socket.getInputStream());
        }
    	catch (SocketException e){
    		
    	}
        catch (IOException ioe){
        	System.out.println("Failed to initialize I/O streams with socket!");
        }
		CombatSystem sys = new CombatSystem(in, out);
		testCharacter = new Character("Amazon", null);
		ArrayList<Chit> characters = new ArrayList<Chit>();
		characters.add(new Chit("Amazon", "L"));
		ArrayList<Chit> enemies = new ArrayList<Chit>();
		enemies.add(new Chit("Wolf", "L"));
		//enemies.add(new Chit("Viper", "L"));
		sys.initFight(characters, enemies, testCharacter, true, true);
	}

}
