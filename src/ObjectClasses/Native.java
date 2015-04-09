package ObjectClasses;

import java.io.Serializable;

public class Native extends Denizen implements Serializable{
	String id;
	String characterType;
	public Native(String inputName, String inpLetter, boolean isSharp,
			int inpMoveTime, int inpAtkTime, String inpID, String charType) {
		super(inputName, inpLetter, isSharp, inpMoveTime, inpAtkTime);
		id = inpID;
		characterType = charType;
	}
	
	public String getID(){
		return id;
	}

	public String getCharType(){
		return characterType;
	}
}
