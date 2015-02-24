package ObjectClasses;

import java.awt.Rectangle;
import java.util.ArrayList;
/**
 * The clearing class, used to contain information about a clearing on the map.
 */
public class Clearing {
	private Rectangle locationRect;
	private String name;
	private ArrayList<String> adjacentClearings;
	private ArrayList<Chit> chits;
	private int treasure;
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
	 * Returns true if a clearing is connected to the current clearing.
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
		chits.add(chit);
	}
	
	public void removeChit(Chit chit) {
		chits.remove(chit);
	}
	
	public void generateTreasure(){
		// ~30% chance of deploying gold
		if ((Math.random()*10) > 7){
			// Random amount of treasure from 0 to 10
			treasure = (int)(Math.random()*10);
		}
		else{
			treasure = 0;
		}
	}
	
	public int plunderTreasure(){
		int treasureHold = treasure;
		treasure = 0;
		return treasureHold;
	}
}
