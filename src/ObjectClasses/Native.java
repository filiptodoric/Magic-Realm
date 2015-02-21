package ObjectClasses;

public class Native extends Denizen{
	String id;
	public Native(String inputName, String inpLetter, boolean isSharp,
			int inpMoveTime, int inpAtkTime, String inpID) {
		super(inputName, inpLetter, isSharp, inpMoveTime, inpAtkTime);
		id = inpID;
	}
	
	public String getID(){
		return id;
	}

}
