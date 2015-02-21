package ObjectClasses;

import java.util.ArrayList;

public class HexTile {
	private ArrayList<Clearing> clearings;
	private ArrayList<Chit> chits;
	private String name;
	public HexTile(String inpName){
		name = inpName;
		clearings = new ArrayList<Clearing>();
	}
	
	public void addClearing(Clearing input){
		clearings.add(input);
	}
	
	public ArrayList<Clearing> getClearings(){
		return clearings;
	}
	
	public ArrayList<Chit> getChit() {
		return chits;
	}
	
	public String getName(){
		return name;
	}
}
