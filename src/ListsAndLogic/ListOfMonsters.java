package ListsAndLogic;

import java.util.HashMap;
/**
 * A list class of monsters and their specifications.
 */
public class ListOfMonsters {
	/**
	 * The list of all monsters, organized in the form of "(name, (variable, value))"
	 */
	public static HashMap<String, HashMap<String, String>> monsters = new HashMap<String, HashMap<String, String>>();
	private static HashMap<String, String> values = new HashMap<String,String>();
	
	public ListOfMonsters(){
		initMonster("Tremendous Flying Dragon", "Tremendous and armored", 
				"12 FAME and 12 NOTORIETY", null, "True");
		initMonster("Heavy Dragon", "Heavy and armored", 
				"5 FAME and 5 NOTORIETY", null, "False");
		initMonster("Head", null, null, "Broadsword", "False");
		initMonster("Heavy Troll", "Heavy and armored", 
				"5 FAME and 5 NOTORIETY", null, "False");
		initMonster("Tremendous Dragon", "Tremendous and armored", 
				"10 FAME and 10 NOTORIETY", null, "False");
		initMonster("Heavy Serpent", "Heavy and armored", 
				"4 FAME and 4 NOTORIETY", null, "False");
		initMonster("HeadStaff", null, null, "Staff", "False");
		initMonster("Giant Bat", "Heavy", 
				"3 FAME and 3 NOTORIETY", null, "False");
		initMonster("Giant", "Tremendous", 
				"8 FAME and 8 NOTORIETY", null, "False");
		initMonster("Heavy Spider", "Heavy", 
				"3 FAME and 3 NOTORIETY", null, "False");
		initMonster("Club", null, null, "Great Sword", "False");
		initMonster("Imp", "Medium", 
				"2 FAME and 1 NOTORIETY", "Curse", "False");
		initMonster("Winged Demon", "Tremendous", 
				"8 FAME and 8 NOTORIETY", "Power Of The Pit", "True");
		initMonster("Goblin with Spear", "Medium", 
				"1 FAME and 1 NOTORIETY", "Spear", "False");
		initMonster("Demon", "Tremendous", 
				"8 FAME and 8 NOTORIETY", "Power Of The Pit", "False");
		initMonster("Goblin with Great Sword", "Medium", 
				"1 FAME and 1 NOTORIETY", "Great Sword", "False");
		initMonster("Tremendous Troll", "Tremendous and armored", 
				"8 FAME and 8 NOTORIETY", null, "False");
		initMonster("Goblin with Axe", "Medium", 
				"1 FAME and 1 NOTORIETY", "Axe", "False");
		initMonster("Tremendous Octopus", "Tremendous", 
				"8 FAME and 8 NOTORIETY", null, "False");
		initMonster("Viper", "Medium and armored", 
				"1 FAME and 2 NOTORIETY", null, "False");
		initMonster("Tremendous Serpent", "Tremendous", 
				"7 FAME and 7 NOTORIETY", null, "False");
		initMonster("Ghost", "Medium", 
				"0 FAME and 2 NOTORIETY", null, "False");
		initMonster("Tremendous Spider", "Tremendous", 
				"6 FAME and 6 NOTORIETY", null, "False");
		initMonster("Ogre", "Medium", 
				"0 FAME and 2 NOTORIETY", null, "False");
		initMonster("Heavy Flying Dragon", "Heavy and armored", 
				"5 FAME and 5 NOTORIETY", null, "True");
		initMonster("Wolf", "Medium", 
				"0 FAME and 1 NOTORIETY", null, "False");
		
	}
	
	private void initMonster(String str1, String str2, String str3, String str4,
			String str5){
		values.put("size", str2);
		values.put("bounty", str3);
		values.put("weapon", str4);
		values.put("flies", str5);
		monsters.put(str1, values);
		values.clear();
	}
}