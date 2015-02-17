package ListsAndLogic;

import java.util.HashMap;

public class ListOfWeapons {
	/**
	 * The list of all monsters, organized in the form of "(name, (variable, value))"
	 */
	public static HashMap<String, HashMap<String, String>> weapons = new HashMap<String, HashMap<String, String>>();
	private static HashMap<String, String> values = new HashMap<String,String>();
	
	public ListOfWeapons(){
		initMonster("Spear", "Striking", "10");
		initMonster("Great Sword", "Striking", "8");
		initMonster("Power Of The Pit", null, "17");
		initMonster("Axe", "Striking", "2");
		initMonster("Broadsword", "Striking", "7");
		initMonster("Short Sword", "Striking", "3");
		initMonster("Great Axe", "Striking", "5");
		initMonster("Light Bow", "Missile", "14");
		initMonster("Thrusting Sword", "Striking", "4");
		initMonster("Crossbow", "Missile", "12");
		
	}
	
	private void initMonster(String str1, String str2, String str3){
		values.put("atkMethod", str2);
		values.put("length", str3);
		weapons.put(str1, values);
		values.clear();
	}
}
