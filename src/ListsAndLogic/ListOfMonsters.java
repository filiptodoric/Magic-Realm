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
	
	public ListOfMonsters(){
		initMonster("Tremendous Flying Dragon", "Tremendous and armored", 
				"12 FAME and 12 NOTORIETY", null, "True", "4", "6");
		initMonster("Heavy Dragon", "Heavy and armored", 
				"5 FAME and 5 NOTORIETY", null, "False", "4", "4");
		initMonster("Head", null, null, "Broadsword", "False", null, "3");
		initMonster("Heavy Troll", "Heavy and armored", 
				"5 FAME and 5 NOTORIETY", null, "False", "4", "4");
		initMonster("Tremendous Dragon", "Tremendous and armored", 
				"10 FAME and 10 NOTORIETY", null, "False", "6", "3");
		initMonster("Heavy Serpent", "Heavy and armored", 
				"4 FAME and 4 NOTORIETY", null, "False", "3", "4");
		initMonster("HeadStaff", null, null, "Staff", "False", null, "4");
		initMonster("Giant Bat", "Heavy", 
				"3 FAME and 3 NOTORIETY", null, "False", "3", "3");
		initMonster("Giant", "Tremendous", 
				"8 FAME and 8 NOTORIETY", null, "False", "4", "5");
		initMonster("Heavy Spider", "Heavy", 
				"3 FAME and 3 NOTORIETY", null, "False", "3", "4");
		initMonster("Club", null, null, "Great Sword", "False", null, "6");
		initMonster("Imp", "Medium", 
				"2 FAME and 1 NOTORIETY", "Curse", "False", "3", "4");
		initMonster("Winged Demon", "Tremendous", 
				"8 FAME and 8 NOTORIETY", "Power Of The Pit", "True", "3", "VI");
		initMonster("Goblin with Spear", "Medium", 
				"1 FAME and 1 NOTORIETY", "Spear", "False", "5", "5");
		initMonster("Demon", "Tremendous", 
				"8 FAME and 8 NOTORIETY", "Power Of The Pit", "False", "4", "VI");
		initMonster("Goblin with Great Sword", "Medium", 
				"1 FAME and 1 NOTORIETY", "Great Sword", "False", "5", "5");
		initMonster("Tremendous Troll", "Tremendous and armored", 
				"8 FAME and 8 NOTORIETY", null, "False", "4", "4");
		initMonster("Goblin with Axe", "Medium", 
				"1 FAME and 1 NOTORIETY", "Axe", "False", "4", "4");
		initMonster("Tremendous Octopus", "Tremendous", 
				"8 FAME and 8 NOTORIETY", null, "False", "3", "4");
		initMonster("Viper", "Medium and armored", 
				"1 FAME and 2 NOTORIETY", null, "False", "3", "2");
		initMonster("Tremendous Serpent", "Tremendous", 
				"7 FAME and 7 NOTORIETY", null, "False", "5", "4");
		initMonster("Ghost", "Medium", 
				"0 FAME and 2 NOTORIETY", null, "False", "2", "3");
		initMonster("Tremendous Spider", "Tremendous", 
				"6 FAME and 6 NOTORIETY", null, "False", "4", "5");
		initMonster("Ogre", "Medium", 
				"0 FAME and 2 NOTORIETY", null, "False", "4", "5");
		initMonster("Heavy Flying Dragon", "Heavy and armored", 
				"5 FAME and 5 NOTORIETY", null, "True", "4", "4");
		initMonster("Wolf", "Medium", 
				"0 FAME and 1 NOTORIETY", null, "False", "4", "5");
		
	}
	
	private void initMonster(String str1, String str2, String str3, String str4,
			String str5, String str6, String str7){
		HashMap<String, String> values = new HashMap<String,String>();
		values.put("size", str2);
		values.put("bounty", str3);
		values.put("weapon", str4);
		values.put("flies", str5);
		values.put("moveSpeed", str6);
		values.put("attackSpeed", str7);
		monsters.put(str1, values);
	}
}