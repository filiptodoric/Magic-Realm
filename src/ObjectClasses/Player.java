package ObjectClasses;

public class Player {
	Character character;
	String name;
	// Add technical variables here as needed!
	
	public Player(String inpName, String selectedCharacter){
		name = inpName;
		character = new Character(selectedCharacter);
	}
}
