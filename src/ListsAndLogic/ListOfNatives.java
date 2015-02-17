package ListsAndLogic;

import java.util.HashMap;
/**
 * A list class of monsters and their size.
 */
public class ListOfNatives {
	/**
	 * The list of all natives, organized in the form of "(name, (variable, value))"
	 */
	public static HashMap<String, HashMap<String, String>> natives = new HashMap<String, HashMap<String, String>>();
	private static HashMap<String, String> values = new HashMap<String,String>();
	
	public ListOfNatives(){
		initNative("Knight", "Broadsword", "Tremendous and armored", "8", 
				"12 NOTORIETY and 4 GOLD", "Heavy", "Heavy");
		initNative("Lancer", "Spear", "Light", "2", 
				"4 NOTORIETY and 2 GOLD", "Light", "Light");
		initNative("Raider", "Short Sword", "Light", "2", "4 NOTORIETY and 2 GOLD"
				   , "Light", "Light");
		initNative("Great Axeman", "Great Axe", "Heavy", "4", "6 NOTORIETY and 4 GOLD"
				   , "Heavy", "Heavy");
		initNative("Great Swordsman", "Great Sword", "Heavy and armored", "4", "6 NOTORIETY and 4 GOLD"
				   , "Heavy", "Heavy");
		initNative("Archer", "Light Bow", "Medium", "2", 
				"4 NOTORIETY and 2 GOLD", "Medium", "Medium");
		initNative("Pikeman", "Spear", "Medium and armored", "2", 
				"3 NOTORIETY and 2 GOLD", "Medium", "Medium");
		initNative("Swordsman", "Thrusting Sword", "Medium", "1", 
				"2 NOTORIETY and 1 GOLD", "Medium", "Medium");
		initNative("Short Swordsman", "Short Sword", "Medium and armored", "2", 
				"3 NOTORIETY and 2 GOLD", "Medium", "Medium");
		initNative("Assassin", "Short Sword", "Medium", "1", 
				"2 NOTORIETY and 1 GOLD", "Medium", "Medium");
		initNative("Crossbowman", "Crossbow", "Medium and armored", "2", 
				"4 NOTORIETY and 2 GOLD", "Medium", "Medium");
	}
	
	private void initNative(String str1, String str2, String str3, String str4, 
			String str5, String str6, String str7){
		values.put("weapon", str2);
		values.put("VUL", str3);
		values.put("wage", str4);
		values.put("bounty", str5);
		values.put("move", str6);
		values.put("weight", str7);
		natives.put(str1, values);
		values.clear();
	}
}