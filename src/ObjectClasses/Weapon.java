package ObjectClasses;
/**
 * The weapon chit class.
 */
public class Weapon extends Chit{
	int sharpnessStarCount;
	boolean alerted;
	int attackTime;
	boolean valuable;
	
	public Weapon(String inputName, String inpLetter, boolean inpAlerted,
			int inpAtkTime, boolean inpValuable, int sharpnessStars) {
		super(inputName, inpLetter);
		alerted = inpAlerted;
		attackTime = inpAtkTime;
		valuable = inpValuable;
		sharpnessStarCount = sharpnessStars;
	}
	
	public int numSharpnessStar(){
		return sharpnessStarCount;
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
