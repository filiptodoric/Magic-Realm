package ObjectClasses;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.awt.event.*;



/**
 * The clearing class, used to contain information about a clearing on the map.
 */
public class Clearing extends MouseAdapter implements Serializable{
	private Rectangle locationRect;
	private String name;
	private ArrayList<String> adjacentClearings;
	public ArrayList<Chit> chits;
	private int treasure;
	private int notority;
	private int fame;
	public Clearing(Rectangle inpLocationRect, String inpName, String adjacentClearingsStr){
		chits = new ArrayList<Chit>();
		adjacentClearings = new ArrayList<String>();
		locationRect = inpLocationRect;
		name = inpName;
		generateTreasure();
		for (String input : adjacentClearingsStr.split(",")){
			adjacentClearings.add(input);
		}
	}
	/**
	 * @return Returns true if a clearing is connected to the current clearing.
	 */
	public boolean isAdjacentTo(String locationName){
		if (adjacentClearings.contains(locationName)){
			return true;
		}
		return false;
	}
	
	public String getName(){
		return name;
	}
	
	public Rectangle getArea(){
		return locationRect;
	}
	
	public ArrayList<Chit> getChits(){
		return chits;
	}
	
	public ArrayList<String> getAdjacentClearings(){
		return adjacentClearings;
	}
	
	public void addChit(Chit chit){
		if (chit.getName().equals("LOST CITY") || chit.getName().equals("LOST CASTLE")){
			treasure = (int)(Math.random()*10) + 40;
		}
		chits.add(chit);
	}
	
	public void addChits(ArrayList<Horse> arrayList){
		chits.addAll(arrayList);
	}
	
	public void removeChit(Chit chit) {
		chits.remove(chit);
	}
	
	public void generateTreasure(){
		treasure = (int)(Math.random()*29) + 10;
		notority = (int)(Math.random()*19) + 1;
		fame = (-5) + (int)(Math.random()*35);
	}
	
	public int[] plunderTreasure(){
		int treasureHold = treasure;
		int notorityHold = notority;
		int fameHold = fame;
		int[] returnArray = {treasureHold, notorityHold, fameHold};
		treasure = 0;
		fame = 0;
		notority = 0;
		return returnArray;
	}
	/**
	 * For use in CHEAT MODE only!
	 */
	public void wipeTreasure(){
		treasure = 0;
		fame = 0;
		notority = 0;
	}
	
	
	
	
/**************************************************************************************************
* FUNCTION: hasChits
* @return - boolean (true/false)
* PURPOSE:  - Checks if a particular clearing has chits. 
**************************************************************************************************/
	public boolean hasChits(){
		if(!(chits.isEmpty())){
			return true;
		}
		return false; 
	}
	

	

} /* CLOSES CLASS */




































