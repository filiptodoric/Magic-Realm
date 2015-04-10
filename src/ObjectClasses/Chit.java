package ObjectClasses;

import java.io.Serializable;

/**
 * The base class for chits.
 */
public class Chit implements Serializable{
	String name = null;
	String letter = null;
	
	public Chit(String inputName, String inpLetter){
		name = inputName;
		letter = inpLetter;
	}
	
	public String getName(){
		return name;
	}
	
	public String getLetter(){
		return letter;
	}
	
	public int getChitNumber(){
		return Integer.parseInt(letter);
	}
}
