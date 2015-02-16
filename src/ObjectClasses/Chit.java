package ObjectClasses;
/**
 * The abstract base class for chits.
 */
public abstract class Chit {
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
}