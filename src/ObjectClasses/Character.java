package ObjectClasses;

import java.util.ArrayList;
import java.util.HashSet;

public class Character extends Chit{
	private ArrayList<Chit> inventory = new ArrayList<Chit>();
	private String currentClearing;
	private boolean isHidden;
	private HashSet<String> discoveries;
	private int gold;
	private int notority;
	private int fame;
	public ArrayList<Chit> startClearings;
	public ArrayList<ActionChit> activeActionChits;
	public ArrayList<ActionChit> woundedActionChits;
	public ArrayList<ActionChit> fatiguedActionChits;
	private ArrayList<Native> allies;
	
	public Character(String selectedCharacter, ArrayList<Chit> dwellingLocations) {
		super(selectedCharacter,null);
		currentClearing = null;
		isHidden = false;
		discoveries = new HashSet<String>();
		startClearings = new ArrayList<Chit>();
		activeActionChits = new ArrayList<ActionChit>();
		woundedActionChits = new ArrayList<ActionChit>();
		fatiguedActionChits = new ArrayList<ActionChit>();
		allies = new ArrayList<Native>();
		initInventory();
		initStartClearings(dwellingLocations);
		initStartChits();
		gold = 10;
	}
	
	private void initInventory(){
		inventory.clear();
		switch(name){
		case "Captain":
			inventory.add(new Weapon("Short Sword", "L", false, 0, false, 1));
			inventory.add(new Armour("Breastplate", "M"));
			inventory.add(new Armour("Shield", "M"));
			break;
		case "Swordsman":
			inventory.add(new Weapon("Thrusting Sword", "L", false, 0, false, 1));
			break;
		case "Amazon":
			inventory.add(new Weapon("Short Sword", "H", false, 0, false, 1));
			inventory.add(new Armour("Helmet", "M"));
			inventory.add(new Armour("Breastplate", "M"));
			inventory.add(new Armour("Shield", "M"));
			break;
		case "Dwarf":
			inventory.add(new Weapon("Great Axe", "H", false, 4, false, 1));
			inventory.add(new Armour("Helmet", "M"));
			break;
		case "Elf":
			inventory.add(new Weapon("Light Bow", "L", false, 1, false, 2));
			break;
			// Spells? :O
		case "Black Knight":
			inventory.add(new Weapon("Mace", "M", false, 3, false, 0));
			inventory.add(new Armour("Suit of Armor", "T"));
			inventory.add(new Armour("Shield", "M"));
			break;
		}
	}
	
	private void initStartClearings(ArrayList<Chit> dwellings){
		if (dwellings != null){
			for (Chit dwelling : dwellings){
				switch(name){
				case "Captain":
					if ((dwelling.getName().equals("Inn")) || (dwelling.getName().equals("House")) || (dwelling.getName().equals("Guard"))){
						startClearings.add(dwelling);
					}
					break;
				case "Swordsman":
					if ((dwelling.getName().equals("Inn"))){
						startClearings.add(dwelling);
					}
					break;
				case "Amazon":
					if ((dwelling.getName().equals("Inn"))){
						startClearings.add(dwelling);
					}
					break;
				case "Dwarf":
					if ((dwelling.getName().equals("Inn")) || (dwelling.getName().equals("Guard"))){
						startClearings.add(dwelling);
					}
					break;
				case "Elf":
					if (dwelling.getName().equals("Inn")){
						startClearings.add(dwelling);
					}
					break;
				case "Black Knight":
					if (dwelling.getName().equals("Inn")){
						startClearings.add(dwelling);
					}
					break;
				}
			}
		}
	}
	
	private void initStartChits(){
		switch(name){
		case "Captain":
			activeActionChits.add(new ActionChit("MOVE", "M", 4, 1));
			activeActionChits.add(new ActionChit("MOVE", "M", 5, 0));
			activeActionChits.add(new ActionChit("FIGHT", "H", 5, 1));
			activeActionChits.add(new ActionChit("FIGHT", "M", 5, 0));
			activeActionChits.add(new ActionChit("FIGHT", "M", 3, 2));
			activeActionChits.add(new ActionChit("MOVE", "M", 3, 2));
			activeActionChits.add(new ActionChit("MOVE", "M", 4, 1));
			activeActionChits.add(new ActionChit("FIGHT", "H", 5, 1));
			activeActionChits.add(new ActionChit("FIGHT", "M", 4, 1));
			activeActionChits.add(new ActionChit("MOVE", "M", 4, 1));
			activeActionChits.add(new ActionChit("FIGHT", "H", 6, 0));
			activeActionChits.add(new ActionChit("MOVE", "M", 4, 1));
			break;
		case "Swordsman":
			activeActionChits.add(new ActionChit("MOVE", "L", 4, 0));
			activeActionChits.add(new ActionChit("MOVE", "L", 3, 1));
			activeActionChits.add(new ActionChit("FIGHT", "L", 3, 1));
			activeActionChits.add(new ActionChit("MOVE", "L", 3, 1));
			activeActionChits.add(new ActionChit("FIGHT", "L", 2, 2));
			activeActionChits.add(new ActionChit("MOVE", "L", 2, 2));
			activeActionChits.add(new ActionChit("MOVE", "M", 4, 1));
			activeActionChits.add(new ActionChit("FIGHT", "M", 4, 1));
			activeActionChits.add(new ActionChit("FIGHT", "M", 3, 2));
			activeActionChits.add(new ActionChit("FIGHT", "L", 4, 0));
			activeActionChits.add(new ActionChit("MOVE", "M", 5, 0));
			activeActionChits.add(new ActionChit("FIGHT", "L", 2, 2));
			break;
		case "Amazon":
			activeActionChits.add(new ActionChit("MOVE", "M", 4, 0));
			activeActionChits.add(new ActionChit("MOVE", "M", 3, 1));
			activeActionChits.add(new ActionChit("FIGHT", "L", 4, 0));
			activeActionChits.add(new ActionChit("MOVE", "M", 4, 0));
			activeActionChits.add(new ActionChit("FIGHT", "M", 5, 0));
			activeActionChits.add(new ActionChit("FIGHT", "M", 4, 1));
			activeActionChits.add(new ActionChit("MOVE", "M", 3, 1));
			activeActionChits.add(new ActionChit("FIGHT", "M", 3, 2));
			activeActionChits.add(new ActionChit("FIGHT", "H", 4, 2));
			activeActionChits.add(new ActionChit("FIGHT", "M", 4, 1));
			activeActionChits.add(new ActionChit("FIGHT", "M", 3, 2));
			activeActionChits.add(new ActionChit("MOVE", "M", 3, 1));
			break;
		case "Dwarf":
			activeActionChits.add(new ActionChit("DUCK", "T", 3, 1));
			activeActionChits.add(new ActionChit("MOVE", "H", 6, 0));
			activeActionChits.add(new ActionChit("FIGHT", "H", 5, 1));
			activeActionChits.add(new ActionChit("MOVE", "T", 6, 1));
			activeActionChits.add(new ActionChit("FIGHT", "H", 6, 0));
			activeActionChits.add(new ActionChit("FIGHT", "H", 4, 2));
			activeActionChits.add(new ActionChit("MOVE", "H", 5, 1));
			activeActionChits.add(new ActionChit("FIGHT", "T", 6, 1));
			activeActionChits.add(new ActionChit("FIGHT", "H", 4, 2));
			activeActionChits.add(new ActionChit("MOVE", "T", 5, 2));
			activeActionChits.add(new ActionChit("FIGHT", "T", 5, 2));
			activeActionChits.add(new ActionChit("FIGHT", "T", 5, 2));
			break;
		case "Elf":
			activeActionChits.add(new ActionChit("MAGIC", "III", 3, 1));
			activeActionChits.add(new ActionChit("MAGIC", "III", 4, 1));
			activeActionChits.add(new ActionChit("MAGIC", "VII", 4, 1));
			activeActionChits.add(new ActionChit("MAGIC", "VII", 3, 1));
			activeActionChits.add(new ActionChit("MAGIC", "III", 3, 1));
			activeActionChits.add(new ActionChit("MAGIC", "III", 2, 1));
			activeActionChits.add(new ActionChit("MOVE", "L", 3, 1));
			activeActionChits.add(new ActionChit("FIGHT", "L", 3, 1));
			activeActionChits.add(new ActionChit("MOVE", "L", 2, 1));
			activeActionChits.add(new ActionChit("MOVE", "M", 4, 0));
			activeActionChits.add(new ActionChit("FIGHT", "M", 3, 1));
			activeActionChits.add(new ActionChit("FIGHT", "M", 4, 0));
			break;
		case "Black Knight":
			activeActionChits.add(new ActionChit("MOVE", "M", 5, 0));
			activeActionChits.add(new ActionChit("MOVE", "H", 5, 1));
			activeActionChits.add(new ActionChit("FIGHT", "H", 5, 1));
			activeActionChits.add(new ActionChit("MOVE", "H", 6, 0));
			activeActionChits.add(new ActionChit("MOVE", "M", 4, 1));
			activeActionChits.add(new ActionChit("FIGHT", "H", 6, 0));
			activeActionChits.add(new ActionChit("FIGHT", "M", 4, 1));
			activeActionChits.add(new ActionChit("FIGHT", "M", 4, 1));
			activeActionChits.add(new ActionChit("FIGHT", "M", 5, 0));
			activeActionChits.add(new ActionChit("MOVE", "H", 4, 2));
			activeActionChits.add(new ActionChit("FIGHT", "H", 4, 2));
			activeActionChits.add(new ActionChit("FIGHT", "M", 3, 2));
			break;
		}
	}
	
	public String getClearing(){
		return currentClearing;
	}
	
	public ArrayList<Chit> getInventory(){
		return inventory;
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
	
	public void setClearing(String clearing){
		currentClearing = clearing;
	}
	
	public void addDiscovery(String discovery){
		discoveries.add(discovery);
	}
	
	public boolean hasFoundDiscovery(String discovery){
		String[] clearings = discovery.split(",");
		for (String foundRoute : discoveries){
			if (foundRoute.contains(clearings[0]) && foundRoute.contains(clearings[1])){
				return true;
			}
		}
		return false;
	}

	public void loseGold(int value) {
		gold -= value;
	}
	
	public void gainGold(int value) {
		gold += value;
	}
	
	public int getGold() {
		return gold;
	}

	public void setHidden(boolean b) {
		isHidden = b;
	}

	public ArrayList<Chit> getStartLocations() {
		return startClearings;
	}

	public void gainNotority(int i) {
		notority += i;
	}

	public void gainFame(int i) {
		fame += i;
	}

	public int getNotority() {
		return notority;
	}
	
	public int getFame() {
		return fame;
	}
	
	public void removeItem(Chit item){
		for (Chit chit : inventory){
			if (chit.getName().equals(item.getName())){
				inventory.remove(chit);
			}
		}
	}
	
	public void addItem(Chit item){
		inventory.add(item);
	}

	public void addAlly(Native nativeToHire) {
		allies.add(nativeToHire);
	}
	
	public void removeAlly(Native nativeToHire) {
		allies.remove(nativeToHire);
	}
	
	public ArrayList<Native> getAllies(){
		return allies;
	}

	public ArrayList<String> getStats() {
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("Gold");
		stats.add("Fame");
		stats.add("Notority");
		return stats;
	}

	public ArrayList<Integer> getStatVals() {
		ArrayList<Integer> statVals = new ArrayList<Integer>();
		statVals.add(gold);
		statVals.add(fame);
		statVals.add(notority);
		return statVals;
	}

	public boolean hasActiveHorse() {
		for(Chit chit : inventory){
			if (chit instanceof Horse && ((Horse)chit).isActive()){
				return true;
			}
		}
		return false;
	}
}
