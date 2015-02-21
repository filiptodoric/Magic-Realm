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
	public Clearing(Rectangle inpLocationRect, String inpName, String adjacentClearingsStr){
		adjacentClearings = new ArrayList<String>();
		locationRect = inpLocationRect;
		name = inpName;
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
	
	public void addChit(Chit chit){
		chits.add(chit);
	}
}
