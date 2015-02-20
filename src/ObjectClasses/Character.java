package ObjectClasses;

import java.util.ArrayList;

public class Character {
	private String name;
	private ArrayList<Chit> inventory = new ArrayList<Chit>();
	public Character(String selectedCharacter) {
		name = selectedCharacter;
		initInventory();
	}
	
	private void initInventory(){
		inventory.clear();
		switch(name){
		case "Captain":
			inventory.add(new Weapon("Short Sword", "L", false, 0, false, 1));
			inventory.add(new Armour("Breastplate", "M"));
			inventory.add(new Armour("Shield", "M"));
		case "Swordsman":
			inventory.add(new Weapon("Thrusting Sword", "L", false, 0, false, 1));
		case "Amazon":
			inventory.add(new Weapon("Short Sword", "L", false, 0, false, 1));
			inventory.add(new Armour("Helmet", "M"));
			inventory.add(new Armour("Breastplate", "M"));
			inventory.add(new Armour("Shield", "M"));
		case "Dwarf":
			inventory.add(new Weapon("Great Axe", "L", false, 4, false, 1));
			inventory.add(new Armour("Helmet", "M"));
		case "Elf":
			inventory.add(new Weapon("Light Bow", "L", false, 1, false, 2));
			// Spells? :O
		case "Black Knight":
			inventory.add(new Weapon("Mace", "L", false, 3, false, 0));
			inventory.add(new Armour("Suit of Armor", "T"));
			inventory.add(new Armour("Shield", "M"));
		}
	}
	
	// Parent class function to be overridden
	public void hide(){
		System.out.println("Character Class - hide");
	}
	
	public void move(){
		System.out.println("Character Class - move");
	}
	
	public void search(){
		System.out.println("Character Class - search");
	}
	
	public void rest(){
		System.out.println("Character Class - rest");
	}
	
	public void trade(){
		System.out.println("Character Class - trade");
	}

}
