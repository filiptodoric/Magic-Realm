package ObjectClasses;

import java.util.ArrayList;

public class Player {
	Character character;
	String playerName;
	
	public Player(String inpName, String selectedCharacter, ArrayList<Chit> startClearings){
		playerName = inpName;
		character = new Character(selectedCharacter, startClearings);
	}
	
	public Character getCharacter(){
		return character;
	}
	
	public String getName(){
		return playerName;
	}
}
