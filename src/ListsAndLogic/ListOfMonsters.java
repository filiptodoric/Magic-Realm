package ListsAndLogic;

import java.util.HashMap;
/**
 * A list class of monsters and their size.
 */
public class ListOfMonsters {
	public static HashMap<String, Integer> monsters = new HashMap<String, Integer>();
	
	public ListOfMonsters(){
		monsters.put("Little Monster", 1);
		// Add the rest of the monsters here
	}
}