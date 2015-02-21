package View;

import java.util.HashMap;

public class ImageLookup {
	
	private HashMap<String, String> lookupTable;
	
	public ImageLookup(){
		lookupTable = new HashMap<String, String>();
		lookupTable.put("Amazon", "amazon.gif");
		lookupTable.put("Swordsman", "swordsman.gif");
		lookupTable.put("Ghost", "ghost.gif");
		lookupTable.put("Black Knight", "black_knight.gif");
		lookupTable.put("board.png", "board.png");
		lookupTable.put("Captain", "captain.gif");
		lookupTable.put("Chapel", "chapel.gif");
		lookupTable.put("Dwarf", "dwarf.gif");
		lookupTable.put("Elf", "elf.gif");
		lookupTable.put("Guard", "guard.gif");
		lookupTable.put("House", "house.gif");
		lookupTable.put("Inn", "inn.gif");
		lookupTable.put("Guard 1", "great_swordsman.gif");
		lookupTable.put("Guard 2", "great_swordsman.gif");
		lookupTable.put("Guard HQ", "great_swordsman.gif");
	}
	
	public String getValue(String name){
		return lookupTable.get(name);
	}
}
