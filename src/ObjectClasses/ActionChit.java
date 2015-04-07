package ObjectClasses;

public class ActionChit extends Chit{
	private int time;
	private int asteriskCount;
	
	public ActionChit(String inputName, String inpStrength, int inpTime,
			int effortAsterisk) {
		super(inputName, inpStrength);
		time = inpTime;
		asteriskCount = effortAsterisk;
	}
	
	public int getTime(){
		return time;
	}
	
	public int numAsterisks(){
		return asteriskCount;
	}
	
	public String toString(){
		String chitString = getName() + " " + getLetter() + " " + getTime() + " ";
		for (int i = 0; i < numAsterisks(); i++){
			chitString += "*";
		}
		return chitString;
	}

}
