package ObjectClasses;

public class Player {
	Character character;
	String playerName;
	
	public Player(String inpName, String selectedCharacter){
		playerName = inpName;
		character = new Character(selectedCharacter);
	}
	
	public Character getCharacter(){
		return character;
	}
	
	public String getName(){
		return playerName;
	}
}
