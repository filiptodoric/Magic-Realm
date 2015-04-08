package ObjectClasses;
/**
 * The armor chit class.
 */
public class Armour extends Chit{
	
	boolean damaged;
	String protectsAgainst;
	boolean isEnabled;
	
	public Armour(String inputName, String inpLetter) {
		super(inputName, inpLetter);
		setProtectAgainst();
		isEnabled = true;
	}
	
	public boolean isDamaged(){
		return damaged;
	}
	
	public void setDamaged(){
		damaged = true;
	}
	
	public void setEnabled(boolean enabled){
		isEnabled = enabled;
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

	public boolean isEnabled() {
		return isEnabled;
	}
}
