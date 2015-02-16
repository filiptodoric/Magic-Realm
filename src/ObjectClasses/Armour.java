package ObjectClasses;
/**
 * The armour chit class.
 */
public class Armour extends Chit{
	
	boolean damaged;
	
	public Armour(String inputName, String inpLetter) {
		super(inputName, inpLetter);
	}
	
	public boolean isDamaged(){
		return damaged;
	}
	
	public void setDamaged(){
		damaged = true;
	}
}
