package ObjectClasses;

import java.awt.Color;
import java.io.Serializable;

/**
 * The map chit class. Implements a "Type" enum for each type of map chit, use this
 * for defining the type of map chit being created.
 */
public class MapChit extends Chit implements Serializable{
	Color colour;
	Type type;
	
	public enum Type{
		SOUND,
		WARNING,
		SITE
	}
	
	public MapChit(String inputName, String inpLetter, Type inpType) {
		super(inputName, inpLetter);
		type = inpType;
		if (type == Type.SOUND){
			colour = Color.RED;
		}
		else if (type == Type.WARNING){
			colour = Color.YELLOW;
		}
		else{
			// 255-215-0 = Gold
			colour = new Color(255,215,0);
		}
	}

	public Color getColour(){
		return colour;
	}
	
	public Type getType(){
		return type;
	}
}
