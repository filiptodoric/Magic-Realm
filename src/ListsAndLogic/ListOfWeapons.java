package ListsAndLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ListOfWeapons {
	/**
	 * The list of all monsters, organized in the form of "(name, (variable, value))"
	 */
	public static HashMap<String, HashMap<String, String>> weapons = new HashMap<String, HashMap<String, String>>();
	
	public ListOfWeapons(){
		initWeapon("Spear", "Striking", "10", "6", "M");
		initWeapon("Great Sword", "Striking", "8", "10", "H");
		initWeapon("Power Of The Pit", null, "17", null, "T");
		initWeapon("Axe", "Striking", "2", "4", "M");
		initWeapon("Broadsword", "Striking", "7", "8", "M");
		initWeapon("Short Sword", "Striking", "3", "4", "L");
		initWeapon("Great Axe", "Striking", "5", "8", "H");
		initWeapon("Light Bow", "Missile", "14", "6", "L");
		initWeapon("Thrusting Sword", "Striking", "4", "6", "L");
		initWeapon("Crossbow", "Missile", "12", "10", "H");
		initWeapon("Medium Bow", "Missile", "16", "8", "M");
		initWeapon("Morning Star", "Striking", "6", "8", "H");
		initWeapon("Staff", "Striking", "9", "1", "L");
		initWeapon("Mace", "Striking", "1", "6", "M");
		initWeapon("Dagger", "Striking", "0", null, "N");
		initWeapon("Tooth/Claw", "Striking", "0", null, "M");
		
		// Include armor in here to avoid having to make a LIST OF ARMOURS too (just for trading)!
		// Note that "atkMethod" is used for the item's damaged value
		initWeapon("Suit of Armor", "12", null, "17", "T");
		initWeapon("Breastplate", "6", null, "9", "M");
		initWeapon("Shield", "5", null, "7", "M");
		initWeapon("Helmet", "3", null, "5", "M");
	}
	
	private void initWeapon(String str1, String str2, String str3, String str4, String str5){
		HashMap<String, String> values = new HashMap<String,String>();
		values.put("atkMethod", str2);
		values.put("length", str3);
		values.put("price", str4);
		values.put("stength", str5);
		weapons.put(str1, values);
	}
	
	public ArrayList<String> getAllValidWeapons(){
		ArrayList<String> armList = new ArrayList<String>();
		Iterator<Entry<String, HashMap<String, String>>> iter = weapons.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, HashMap<String, String>> x = iter.next();
			if(x.getValue().get("length") != null && x.getValue().get("price") != null){
				armList.add(x.getKey());
			}
		}
		return armList;
	}
	
	public ArrayList<String> getAllValidArmour(){
		ArrayList<String> armList = new ArrayList<String>();
		Iterator<Entry<String, HashMap<String, String>>> iter = weapons.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, HashMap<String, String>> x = iter.next();
			if(x.getValue().get("length") == null){
				armList.add(x.getKey());
			}
		}
		return armList;
	}
}
