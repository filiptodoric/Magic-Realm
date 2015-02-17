package ObjectClasses;
/**
 * The armor chit class.
 */
public class Armour extends Chit{
	
	boolean damaged;
	String protectsAgainst;
	
	public Armour(String inputName, String inpLetter) {
		super(inputName, inpLetter);
		setProtectAgainst();
	}
	
	public boolean isDamaged(){
		return damaged;
	}
	
	public void setDamaged(){
		damaged = true;
	}
	
	public void setProtectAgainst(){
		if (name.equals("Suit of Armor")){
			protectsAgainst = "All";
		}
		else if (name.equals("Breastplate")){
			protectsAgainst = "Thrust and Swing";
		}
		else if (name.equals("Helmet")){
			protectsAgainst = "Smash";
		}
		else{
			protectsAgainst = "Any One Direction";
		}
	}
}
