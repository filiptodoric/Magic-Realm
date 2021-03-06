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
		lookupTable.put("Order 1", "knight.gif");
		lookupTable.put("Order 2", "knight.gif");
		lookupTable.put("Order 3", "knight.gif");
		lookupTable.put("Order HQ", "knight.gif");
		lookupTable.put("Soldier 1", "pikeman.gif");
		lookupTable.put("Soldier 2", "pikeman.gif");
		lookupTable.put("Soldier 3", "crossbowman.gif");
		lookupTable.put("Soldier HQ", "great_swordsman.gif");
		lookupTable.put("Rogue 1", "great_axeman.gif");
		lookupTable.put("Rogue 2", "great_axeman.gif");
		lookupTable.put("Rogue 3", "short_swordsman.gif");
		lookupTable.put("Rogue 4", "archer.gif");
		lookupTable.put("Rogue 5", "assassin.gif");
		lookupTable.put("Rogue 6", "short_swordsman.gif");
		lookupTable.put("Rogue 7", "short_swordsman.gif");
		lookupTable.put("Rogue HQ", "assassin.gif");
		lookupTable.put("Wolf", "wolf.gif");
		lookupTable.put("Viper", "serpent.gif");
		lookupTable.put("Tremendous Dragon", "dragon.gif");
		lookupTable.put("Tremendous Flying Dragon", "dragon_flying.gif");
		lookupTable.put("Heavy Dragon", "firedrake.gif");
		lookupTable.put("Ogre", "orc_sword.gif");
		lookupTable.put("Tremendous Octopus", "octopus.gif");
		lookupTable.put("Tremendous Troll", "troll.gif");
		lookupTable.put("Heavy Troll", "troll.gif");
		lookupTable.put("Serpent", "serpent.gif");
		lookupTable.put("Heavy Serpent", "serpent.gif");
		lookupTable.put("Giant", "giant.gif");
		lookupTable.put("Tremendous Spider", "spider.gif");
		lookupTable.put("Heavy Spider", "spider.gif");
		lookupTable.put("Goblin with Spear", "goblin_spear.gif");
		lookupTable.put("Goblin with Axe", "goblin_axe.gif");
		lookupTable.put("Giant Bat", "bat.gif");
		for(int i = 1; i < 5; i++){
			lookupTable.put("Warhorse " + i, "warhorse.gif");
		}
		for(int i = 1; i < 7; i++){
			lookupTable.put("Workhorse " + i, "horse.gif");
		}
		for(int i = 1; i < 8; i++){
			lookupTable.put("Pony " + i, "pony.gif");
		}
	}
	
	public String getValue(String name){
		return lookupTable.get(name);
	}
}
