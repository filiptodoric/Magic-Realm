package ControlFlow;

import View.CombatSystemGUI;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import ListsAndLogic.ListOfMonsters;
import ListsAndLogic.ListOfWeapons;
import ListsAndLogic.MusicLookupTable;
import ObjectClasses.ActionChit;
import ObjectClasses.Armour;
import ObjectClasses.Chit;
import ObjectClasses.Character;
import ObjectClasses.Weapon;

public class CombatSystem{
	CombatSystemGUI gui;
	Character playerCharacter;
	ObjectInputStream in;
    ObjectOutputStream out;
    boolean AIopponent = false;
    boolean attacker = false;
    ArrayList<Chit> allies;
    ArrayList<Chit> enemies;
    ListOfMonsters monsterLookup;
    ListOfWeapons weaponsLookup;
	private MusicLookupTable musicLookup;
	private int round;
	private int kills;
	private boolean cheatMode;
	
	public CombatSystem(ObjectInputStream input, ObjectOutputStream output, boolean cheatEnabled) {
		in = input;
		out = output;
		round = 0;
		kills = 0;
		monsterLookup = new ListOfMonsters();
		weaponsLookup = new ListOfWeapons();
		musicLookup = new MusicLookupTable();
		cheatMode = cheatEnabled;
		JFXPanel fxPanel = new JFXPanel();
		gui = new CombatSystemGUI();
	}
	
	public void initFight(ArrayList<Chit> side1, ArrayList<Chit> side2, Character player, boolean isAI, boolean isAttacker){
		playerCharacter = player;
		AIopponent = isAI;
		attacker = isAttacker;
		allies = side1;
		enemies = side2;
		gui.addCharacters(allies, enemies);
		gui.setupOptions(enemies);
		setActionListeners();
	}
	
	public void setActionListeners(){
		//Media hit = new Media(Paths.get(musicLookup.table.get(enemies.get(0).getName())).toUri().toString());
		//MediaPlayer mediaPlayer = new MediaPlayer(hit);
		//mediaPlayer.play();
		gui.fleeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (checkFlee()){
					//mediaPlayer.stop();
					gui.flee(playerCharacter, enemies);
				}
			}
		});
		
		gui.fightButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				//mediaPlayer.stop();
				startCombat();
			}
		});
		
		if(!attacker){
			gui.fightButton.setEnabled(false);
		}
	}
	
	protected void startCombat() {
		// TODO Auto-generated method stub
		if (AIopponent){
			while((allies.size() > 0) && (enemies.size() > 0)){
				round++;
				ActionChit fightChit1 = null;
				int effortAsterisks = 0;
				// Select the enemy
				int index = gui.getTarget(enemies);
				// Play a fight chit, activate and/or deactivate one belonging, or abandon belongings
				String choice = (String) gui.getEncounterAction();
				if (choice.contains("Alert")){
					fightChit1 = gui.getFightChit(playerCharacter, enemies, true, null, effortAsterisks);
					if (fightChit1 != null){
						effortAsterisks += fightChit1.numAsterisks();
						String alertedWeapon = gui.getWeaponToAlert(playerCharacter);
						for (Chit chit : playerCharacter.getInventory()){
							if (chit.getName().contains(alertedWeapon)){
								((Weapon) chit).setAlerted(true);
							}
						}
					}
				}
				else if (choice.contains("Activate")){
					// Player can choose to activate armor here
					gui.activateDeactivateItems(playerCharacter);
				}
				else{
					// Player can abandon items here...not a priority since no weight cap
					gui.abandonItems(playerCharacter);
				}
				// Get fight chit for combat
				ActionChit fightChit2 = gui.getFightChit(playerCharacter, enemies, false, fightChit1, effortAsterisks);
				effortAsterisks += fightChit2.numAsterisks();
				// Place attack, maneuver, and shield directions (if applicable)
				String[] directions = gui.getDirections(playerCharacter, effortAsterisks);
				// Attack!
				gui.infoText.setText("Combat begins!");
				ArrayList<String> turns = getTurns(enemies, playerCharacter, fightChit2);
				ArrayList<Chit> allChits = new ArrayList<Chit>();
				ArrayList<Chit> deadChits = new ArrayList<Chit>();
				ArrayList<Integer> enemyManeuvers = initEnemyMoves(enemies);
				allChits.addAll(allies);
				allChits.addAll(enemies);
				for (String characterToPlay : turns){
					for (Chit chit : allChits){
						if (chit.getName().equals(characterToPlay)){
							if (chit.getName().equals(playerCharacter.getName())){
								playerAttack(fightChit2, index, directions[0], enemyManeuvers.get(index));
							}
							else if (enemies.contains(chit)){
								playEnemyAITurn(chit, allies, directions, enemyManeuvers.get(index));
							}
							else if(allies.contains(chit)){
								// This is incredibly optimistic, but here's the functionality for
								// allowing allies to fight!
								playAlliedAITurn(chit);
							}
						}
					}
				}
			}
			if (allies.size() == 0){
				gui.infoText.setText("You were defeated!");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else{
				gui.infoText.setText("You were victorious!");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		else{
			//PvP, whole other can of worms...
		}
	}
	
	private void playAlliedAITurn(Chit chit) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Returns a list of integers, where 0 - Charge and Thrust, 1 - Dodge and Swing,
	 * and 2 - Duck and Smash.
	 */
	private ArrayList<Integer> initEnemyMoves(ArrayList<Chit> enemies) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (Chit enemy : enemies){
			int i = (int)(Math.random()*3);
			if (i == 3){
				i = 2;
			}
			moves.add(i);
		}
		return moves;
	}

	private void playEnemyAITurn(Chit chit, ArrayList<Chit> targetChits, String[] directions, int maneuver) {
		int index = (int)(Math.random()*targetChits.size());
		// Check simple case first, player's more complex to kill
		if (!targetChits.get(index).getName().equals(playerCharacter.getName())){
			if ((monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0)) >= targetChits.get(index).getLetter().charAt(0)){
					gui.infoText.setText(targetChits.get(index).getName() + "was killed!");
					System.out.println(targetChits.get(index).getName() + "was killed!");
					allies.remove(index);
			}
			else{
				gui.infoText.setText(targetChits.get(index).getName() + "was attacked by " + chit.getName() + ", but the attack failed.");
				System.out.println(targetChits.get(index).getName() + "was attacked by " + chit.getName() + ", but the attack failed.");
			}
		}
		else{
			// Monster attacks player
			if (Integer.parseInt((monsterLookup.monsters.get(enemies.get(index).getName()).get("attackSpeed"))) >= targetChits.get(index).getLetter().charAt(0)){
				if (((monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0)) >= targetChits.get(index).getLetter().charAt(0)) &&
					(directions[3] == null) ){
					// Need to check for suits of armor!
					boolean armourProtection = checkArmour(chit, index, maneuver);
					if (!armourProtection){
						gui.infoText.setText(targetChits.get(index).getName() + "was killed!");
						System.out.println(targetChits.get(index).getName() + "was killed!");
						allies.remove(index);
					}
				}
				else{
					if (!isAligned(directions[3], maneuver)){
						// Missed
						gui.infoText.setText(chit.getName() + " tried to attack, but missed!");
						System.out.println(chit.getName() + " tried to attack, but missed!");
					}
					else{
						// Hit, but shield?
						boolean armourProtection = checkArmour(chit, index, maneuver);
						if (!armourProtection){
							gui.infoText.setText(targetChits.get(index).getName() + "was killed!");
							System.out.println(targetChits.get(index).getName() + "was killed!");
							allies.remove(index);
						}
					}
				}
			}
			else{
				// Lined up with the player's maneuver
				if(isAligned(directions[2], maneuver)){
					if (((monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0)) >= targetChits.get(index).getLetter().charAt(0)) &&
							(directions[3] == null || !isAligned(directions[3], maneuver)) ){
							boolean armourProtection = checkArmour(chit, index, maneuver);
							if (!armourProtection){
								gui.infoText.setText(targetChits.get(index).getName() + "was killed!");
								System.out.println(targetChits.get(index).getName() + "was killed!");
								allies.remove(index);
							}
					}
				}
				// Too slow, not lined up with the player...
				else{
					// Missed
					gui.infoText.setText(chit.getName() + " tried to attack, but missed!");
					System.out.println(chit.getName() + " tried to attack, but missed!");
				}
			}
		}
	}

	private boolean checkArmour(Chit chit, int index, int maneuver) {
		// Chit = attacker
		for(Chit item : playerCharacter.getInventory()){
			if (item.getName().equals("Suit of Armor") && (monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0) != 'T')){
				gui.infoText.setText(chit.getName() + " tried to attack you, but hit your suit of armour and did nothing.");
				System.out.println(chit.getName() + " tried to attack you, but hit your suit of armour and did nothing.");
				return true;
			}
			else if (item.getName().equals("Suit of Armor") && (monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0) == 'T')){
				if (((Armour)chit).isDamaged()){
					playerCharacter.removeItem(item);
					gui.infoText.setText(chit.getName() + " attacked you, destroyed your suit of armour and wounded you.");
					System.out.println(chit.getName() + " attacked you, destroyed your suit of armour and wounded you.");
					return true;
				}
				else{
					((Armour)chit).setDamaged();
					gui.infoText.setText(chit.getName() + " attacked you, damaged your suit of armour and did nothing.");
					System.out.println(chit.getName() + " attacked you, damaged your suit of armour and did nothing.");
					return true;
				}
			}
			else if (item.getName().equals("Breastplate") && (maneuver == 0 || maneuver == 1)){
				playerCharacter.removeItem(item);
				gui.infoText.setText(chit.getName() + " attacked you, destroyed your breastplate and wounded you.");
				System.out.println(chit.getName() + " attacked you, destroyed your breastplate and wounded you.");
				return true;
			}
			else if (item.getName().equals("Helmet") && (maneuver == 2)){
				playerCharacter.removeItem(item);
				gui.infoText.setText(chit.getName() + " attacked you, destroyed your breastplate and wounded you.");
				System.out.println(chit.getName() + " attacked you, destroyed your breastplate and wounded you.");
				return true;
			}
		}
		return false;
	}

	private ArrayList<String> getTurns(ArrayList<Chit> enemies, Character player, ActionChit charFightChit) {
		if (round == 1){
			// Sort by weapon strength
			int playerWeaponLength = 0;
			Map<String, Integer> map = new TreeMap<String, Integer>();
			ArrayList<String> characterOrder = new ArrayList<String>();
			for(Chit chit : playerCharacter.getInventory()){
				if ((chit instanceof Weapon) && (((Weapon)chit).isAlerted())){
					playerWeaponLength = Integer.parseInt(ListOfWeapons.weapons.get(chit.getName()).get("length"));
				}
			}
			map.put(player.getName(), playerWeaponLength);
			for (Chit enemy : enemies){
				String monsterWeapon = monsterLookup.monsters.get(enemy.getName()).get("weapon");
				if (ListOfWeapons.weapons.get(monsterWeapon).get("length") != null){
					map.put(enemy.getName(), Integer.parseInt(ListOfWeapons.weapons.get(monsterWeapon).get("length")));
				}
				else{
					// Some monsters attack with other "monsters" (i.e. heads), this hopefully handles that
					String monsterWeapon2 = monsterLookup.monsters.get(monsterWeapon).get("weapon");
					map.put(enemy.getName(), Integer.parseInt(ListOfWeapons.weapons.get(monsterWeapon).get("length")));
				}
			}
			for(Entry<String, Integer> entry : entriesSortedByValues(map)){
				characterOrder.add(entry.getKey());
			}
			
			Collections.reverse(characterOrder);
			
			return characterOrder;
		}
		else{
			// Sort by attack speed
			Map<String, Integer> map = new TreeMap<String, Integer>(Collections.reverseOrder());
			ArrayList<String> characterOrder = new ArrayList<String>();
			map.put(player.getName(), charFightChit.getTime());
			for (Chit enemy : enemies){
				map.put(enemy.getName(), Integer.parseInt(monsterLookup.monsters.get(enemy.getName()).get("attackSpeed")));
			}
			for(Entry<String, Integer> entry : entriesSortedByValues(map)){
				characterOrder.add(entry.getKey());
			}
			return characterOrder;
		}
	}

	private void playerAttack(ActionChit fightChit2, int index, String attackDirection, int enemyManeuver){
		boolean hasAttacked = false;
		// Compare player attack time with target move time (faster = undercut = hit)
		if (fightChit2.getTime() < Integer.parseInt(monsterLookup.monsters.get(enemies.get(index).getName()).get("moveSpeed"))){
			for(Chit chit : playerCharacter.getInventory()){
				if ((chit instanceof Weapon) && (((Weapon)chit).isAlerted()) &&
						(fightChit2.getLetter().charAt(0) >= monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0))){
						gui.infoText.setText("You killed the " + enemies.get(index).getName() + "!");
						System.out.println("You killed the " + enemies.get(index).getName() + "!");
						enemies.remove(index);
						hasAttacked = true;
				}
			}
			if(!hasAttacked){
				// Attack with dagger, no weapon alerted! If an enemy has armor, then it's absolutely
				// NOT Light, so only useful case is medium. It's "always" medium as per 23.2/1c.
				System.out.println("You attack with the dagger, no active weapon!");
				if (('M' >= monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0))){
						gui.infoText.setText("You killed the " + enemies.get(index).getName() + "!");
						System.out.println("You killed the " + enemies.get(index).getName() + "!");
						enemies.remove(index);
						hasAttacked = true;
				}
				
			}
			else{
				// Character attacked, but was not strong enough to kill
				System.out.println("You tried to attack, but were not strong enough.");
			}
		}
		else{
			if (isAligned(attackDirection, enemyManeuver)){
				for(Chit chit : playerCharacter.getInventory()){
					if ((chit instanceof Weapon) && (((Weapon)chit).isAlerted()) &&
							(fightChit2.getLetter().charAt(0) >= monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0))){
							gui.infoText.setText("You killed the " + enemies.get(index).getName() + "!");
							enemies.remove(index);
							hasAttacked = true;
					}
				}
				if(!hasAttacked){
					// Attack with dagger, no weapon alerted!
					System.out.println("You attack with the dagger, no active weapon!");
					if (('M' >= monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0))){
						gui.infoText.setText("You killed the " + enemies.get(index).getName() + "!");
						System.out.println("You killed the " + enemies.get(index).getName() + "!");
						enemies.remove(index);
						hasAttacked = true;
					}
				}
				else{
					// Character attacked, but was not strong enough to kill
					System.out.println("You tried to attack, but were not strong enough.");
				}
			}
			else{
				// Character attacked, but was not strong enough to kill
				System.out.println("You tried to attack, but missed!");
			}
		}
	}

	private boolean isAligned(String atkDirection, int monsterPosition) {
		if ((monsterPosition == 0 && atkDirection.contains("Thrust")) || 
		(monsterPosition == 1 && atkDirection.contains("Swing")) || 
		(monsterPosition == 2 && atkDirection.contains("Smash"))){
			return true;
		}
		return false;
	}

	private boolean checkFlee(){
		for (Chit chit: playerCharacter.activeActionChits){
			if (chit.getName().contains("MOVE")){
				return true;
			}
		}
		for (Chit chit: playerCharacter.getInventory()){
			if (chit.getName().contains("Horse") || chit.getName().contains("Boots")){
				return true;
			}
		}
		
		return false;
	}
	
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                int res = e1.getValue().compareTo(e2.getValue());
	                return res != 0 ? res : 1;
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
}
