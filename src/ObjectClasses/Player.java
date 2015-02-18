package ObjectClasses;

public class Player {
	Character character;
	String name;
	
	public Player(String inpName, String selectedCharacter){
		name = inpName;
		character = new Character(selectedCharacter);
	}
}
