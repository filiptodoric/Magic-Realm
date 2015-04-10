package ObjectClasses;

import java.io.Serializable;

public class Horse extends ActionChit implements Serializable{

	int cost;
	int gallopTime;
	boolean isAllied;
	boolean isActive;
	
	public Horse(String inputName, String inpStrength, int inpTime,
			int effortAsterisk, int inpCost, int inpGallopTime) {
		super(inputName, inpStrength, inpTime, effortAsterisk);
		cost = inpCost;
		gallopTime = inpGallopTime;
	}
	
	public int getCost(){
		return cost;
	}
	
	/**
	 * For making sure a horse is allied to a player and isn't open for sale...
	 */
	public boolean isAllied(){
		return isAllied;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	public void setActive(boolean active){
		isActive = active;
	}
	
	public int getGallopTime(){
		return gallopTime;
	}

	/**
	 * Make sure to set this when a horse gets assigned to a player!
	 */
	public void setAllied(){
		isAllied = true;
	}
}
