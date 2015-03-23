package ObjectClasses;

import java.io.Serializable;

/**
 * The denizen class. Extends chit for common variables and classes.
 */
public class Denizen extends Chit implements Serializable{
	boolean sharpnessStar;
	int moveTime;
	int attackTime;
	
	public Denizen(String inputName, String inpLetter, boolean isSharp,
			int inpMoveTime, int inpAtkTime) {
		super(inputName, inpLetter);
		sharpnessStar = isSharp;
		moveTime = inpMoveTime;
		attackTime = inpAtkTime;
	}
	
	public boolean hasSharpnessStar(){
		return sharpnessStar;
	}
	
	public int getMoveTime(){
		return moveTime;
	}
	
	public int getAttackTime(){
		return attackTime;
	}

}
