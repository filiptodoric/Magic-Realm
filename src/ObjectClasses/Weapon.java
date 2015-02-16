package ObjectClasses;
/**
 * The weapon chit class.
 */
public class Weapon extends Chit{
	boolean sharpnessStar;
	boolean alerted;
	int attackTime;
	boolean valuable;
	
	public Weapon(String inputName, String inpLetter, boolean inpAlerted,
			int inpAtkTime, boolean inpValuable, boolean isSharp) {
		super(inputName, inpLetter);
		alerted = inpAlerted;
		attackTime = inpAtkTime;
		valuable = inpValuable;
		sharpnessStar = isSharp;
	}
	
	public boolean hasSharpnessStar(){
		return sharpnessStar;
	}
	
	public boolean isAlerted(){
		return alerted;
	}
	
	public int getAttackTime(){
		return attackTime;
	}
	
	public boolean isValuable(){
		return valuable;
	}

}
