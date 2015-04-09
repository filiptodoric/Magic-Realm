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
	
	public boolean isAllied(){
		return isAllied;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	public int getGallopTime(){
		return gallopTime;
	}

}
