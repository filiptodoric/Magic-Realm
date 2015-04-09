package ControlFlow;

import View.CombatSystemGUI;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

import javax.swing.JOptionPane;

import ListsAndLogic.ListOfMonsters;
import ListsAndLogic.ListOfNatives;
import ListsAndLogic.ListOfWeapons;
import ListsAndLogic.MusicLookupTable;
import ObjectClasses.ActionChit;
import ObjectClasses.Armour;
import ObjectClasses.Chit;
import ObjectClasses.Character;
import ObjectClasses.Native;
import ObjectClasses.Weapon;

/**
 * CombatSystem status:
 * - CAN fight against monsters
 * 		- Cheat mode enabled for monster attacks
 * 		- Can fight with multiple monsters across several turns! :D
 * - CAN'T fight PvP
 * - CAN fight alongside natives!
 * - CAN'T fight against natives
 */
public class CombatSystem{
	CombatSystemGUI cbGui;
	Character playerCharacter;
	Character enemyCharacter;
    ArrayList<Chit> allies;
    ArrayList<Chit> enemies;
    ListOfMonsters monsterLookup;
    ListOfWeapons weaponsLookup;
    ListOfNatives nativesLookup;
	private MusicLookupTable musicLookup;
	private int round;
	private int kills;
	private boolean cheatMode;
	private boolean iAmLazy;
	public boolean fightFinished;
	
	public CombatSystem(boolean cheatEnabled) {
		round = 0;
		kills = 0;
		monsterLookup = new ListOfMonsters();
		weaponsLookup = new ListOfWeapons();
		nativesLookup = new ListOfNatives();
		musicLookup = new MusicLookupTable();
		cheatMode = cheatEnabled;
		fightFinished = true;
		iAmLazy = false;
	}
	
	public void initFight(ArrayList<Chit> side1, ArrayList<Chit> side2, Character player){
		cbGui = new CombatSystemGUI();
		fightFinished = false;
		playerCharacter = player;
		allies = side1;
		enemies = side2;
		cbGui.addCharacters(allies, enemies);
		cbGui.setupOptions(enemies);
		for(Chit enemy : enemies){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Media hit = new Media(Paths.get(musicLookup.table.get(enemy.getName())).toUri().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(hit);
			mediaPlayer.play();
		}
		try {
			startCombat();
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void initFightPlayers(Character player, Character targetPlayer){
		cbGui = new CombatSystemGUI();
		playerCharacter = player;
		enemyCharacter = targetPlayer;
		allies.add(playerCharacter);
		enemies.add(enemyCharacter);
		cbGui.addCharacters(allies, enemies);
		cbGui.setupOptions(enemies);
		Media hit = new Media(Paths.get(musicLookup.table.get(enemies.get(0).getName())).toUri().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
		try {
			startCombat();
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	protected void startCombat() throws ClassNotFoundException, IOException {
		while((allies.size() > 0) && (enemies.size() > 0)){
			round++;
			ActionChit fightChit1 = null;
			int effortAsterisks = 0;
			// Select the enemy
			int index = cbGui.getTarget(enemies);
			// Play a fight chit, activate and/or deactivate one belonging, or abandon belongings
			String choice = (String) cbGui.getEncounterAction();
			if (choice.contains("Alert")){
				fightChit1 = cbGui.getFightChit(playerCharacter, enemies, true, null, effortAsterisks);
				if (fightChit1 != null){
					effortAsterisks += fightChit1.numAsterisks();
					String alertedWeapon = cbGui.getWeaponToAlert(playerCharacter);
					for (Chit chit : playerCharacter.getInventory()){
						if (chit.getName().contains(alertedWeapon)){
							((Weapon) chit).setAlerted(true);
						}
					}
				}
			}
			else if (choice.contains("Activate")){
				// Player can choose to activate armor here
				cbGui.activateDeactivateItems(playerCharacter);
			}
			else if (choice.contains("Flee")){
				// Player can choose to flee
				if (checkFlee()){
					cbGui.showFlee();
					cbGui.close();
					return;
				}
				else{
					cbGui.showNoFlee();
				}
			}
			else if(choice.contains("lazy")){
				if(allies.size() > 1){
					iAmLazy = true;
				}
				else{
					cbGui.displayMessage("Gotta fight, nobody else here to help!", "Sorry!");
				}
			}
			else{
				// Player can abandon items here...not a priority since no weight cap
				cbGui.abandonItems(playerCharacter);
			}
			ActionChit fightChit2;
			String[] directions;
			// Get fight chit for combat
			if(!iAmLazy){
				fightChit2 = cbGui.getFightChit(playerCharacter, enemies, false, fightChit1, effortAsterisks);
				effortAsterisks += fightChit2.numAsterisks();
				// Place attack, maneuver, and shield directions (if applicable)
				directions = cbGui.getDirections(playerCharacter, effortAsterisks);
			}
			else{
				fightChit2 = null;
				directions = new String[]{"","","",""};
			}
			// Attack!
			cbGui.infoText.setText("Combat begins!");
			ArrayList<String> turns = getTurns(fightChit2);
			ArrayList<Chit> allChits = new ArrayList<Chit>();
			ArrayList<Chit> deadChits = new ArrayList<Chit>();
			ArrayList<Integer> enemyManeuvers = initEnemyMoves(enemies);
			allChits.addAll(allies);
			allChits.addAll(enemies);
			for (String characterToPlay : turns){
				if(allies.size() == 0 || enemies.size() == 0){
					// One side was defeated, fight's over!
					break;
				}
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
							playAlliedAITurn(((Native)chit), enemies);
						}
					}
				}
			}
			cbGui.addCharacters(allies, enemies);
			if(effortAsterisks == 2){
				if (fightChit1 != null){
					if((fightChit1.numAsterisks() + fightChit2.numAsterisks()) == 2){
						fatigueChit(playerCharacter, "FIGHT");
					}
					else if ((fightChit1.numAsterisks() + fightChit2.numAsterisks()) == 1){
						fatigueChit(playerCharacter, "FIGHT/MOVE");
					}
					else{
						fatigueChit(playerCharacter, "MOVE");
					}
				}
				else{
					if((fightChit2.numAsterisks()) == 2){
						fatigueChit(playerCharacter, "FIGHT");
					}
					else if ((fightChit2.numAsterisks()) == 1){
						fatigueChit(playerCharacter, "FIGHT/MOVE");
					}
					else{
						fatigueChit(playerCharacter, "MOVE");
					}
				}
			}
			iAmLazy = false;
		}
		if (allies.size() == 0){
			cbGui.infoText.setText("You were defeated!");
			cbGui.addCharacters(allies, enemies);
			cbGui.showDefeat();
			fightFinished = true;
			cbGui.close();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else{
			cbGui.infoText.setText("You were victorious!");
			cbGui.addCharacters(allies, enemies);
			Media hit = new Media(Paths.get(musicLookup.table.get("battleSuccess")).toUri().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(hit);
			mediaPlayer.play();
			cbGui.showVictorious();
			mediaPlayer.stop();
			fightFinished = true;
			cbGui.close();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void playAlliedAITurn(Native chit, ArrayList<Chit> targetChits) {
		int index = (int)(Math.random()*targetChits.size());
		if (index == targetChits.size()){
			index = index - 1;
		}
		if (successfulAttack(chit.getLetter().charAt(0), targetChits.get(index).getLetter().charAt(0))){
				cbGui.infoText.setText(targetChits.get(index).getName() + " was killed by " + chit.getName());
				System.out.println(targetChits.get(index).getName() + " was killed by "  + chit.getName());
				enemies.remove(index);
		}
		else{
			cbGui.infoText.setText(targetChits.get(index).getName() + " was attacked by " + chit.getName() + ", but the attack failed.");
			System.out.println(targetChits.get(index).getName() + " was attacked by " + chit.getName() + ", but the attack failed.");
		}
	}

	/*
	 * Returns a list of integers, where 0 - Charge and Thrust, 1 - Dodge and Swing,
	 * and 2 - Duck and Smash.
	 */
	private ArrayList<Integer> initEnemyMoves(ArrayList<Chit> enemies) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (Chit enemy : enemies){
			int i = (int)(Math.random()*3);
			if (cheatMode){
				i = cbGui.getMonsterDieRoll(enemy.getName());
			}
			else{
				if (i == 3){
					i = 2;
				}
			}
			moves.add(i);
		}
		return moves;
	}

	private void playEnemyAITurn(Chit chit, ArrayList<Chit> targetChits, String[] directions, int maneuver) {
		int index = (int)(Math.random()*targetChits.size());
		if (index == targetChits.size()){
			index = index - 1;
		}
		// Check simple case first, player's more complex to kill
		if (!targetChits.get(index).getName().equals(playerCharacter.getName())){
			if (successfulAttack(chit.getLetter().charAt(0), targetChits.get(index).getLetter().charAt(0))){
					cbGui.infoText.setText(targetChits.get(index).getName() + " was killed by " + chit.getName() + "!");
					System.out.println(targetChits.get(index).getName() + " was killed by " + chit.getName() + "!");
					playerCharacter.removeAlly((Native) allies.get(index));
					allies.remove(index);
			}
			else{
				cbGui.infoText.setText(targetChits.get(index).getName() + " was attacked by " + chit.getName() + ", but the attack failed.");
				System.out.println(targetChits.get(index).getName() + " was attacked by " + chit.getName() + ", but the attack failed.");
			}
		}
		else{
			// Monster attacks player
			// Get player's move chit
			ActionChit moveChit = null;
			for (ActionChit action : playerCharacter.activeActionChits){
				if (action.toString().equals(directions[1])){
					moveChit = action;
				}
			}
			if (Integer.parseInt((monsterLookup.monsters.get(chit.getName()).get("attackSpeed"))) > moveChit.getLetter().charAt(0)){
				if (successfulAttack(chit.getLetter().charAt(0), targetChits.get(index).getLetter().charAt(0))){
					// Need to check for suits of armor!
					boolean armourProtection = checkArmour(chit, index, maneuver, directions[3]);
					if (!armourProtection){
						cbGui.infoText.setText(targetChits.get(index).getName() + " was killed by " + chit.getName() + "!");
						System.out.println(targetChits.get(index).getName() + " was killed by " + chit.getName() + "!");
						allies.remove(index);
					}
				}
				else{
					// Hit, but shield?
					boolean armourProtection = checkArmour(chit, index, maneuver, directions[3]);
					if (!armourProtection){
						cbGui.infoText.setText(chit.getName() + " tried to attack, but was too weak to wound you.");
						System.out.println(chit.getName() + " tried to attack, but was too weak to wound you.");
					}
				}
			}
			else{
				// Lined up with the enemy's maneuver
				if(isAligned(directions[2], maneuver)){
					if (successfulAttack(chit.getLetter().charAt(0), targetChits.get(index).getLetter().charAt(0))){
						boolean armourProtection = checkArmour(chit, index, maneuver, directions[3]);
						if (!armourProtection){
							cbGui.infoText.setText(targetChits.get(index).getName() + " was killed by " + chit.getName() + "!");
							System.out.println(targetChits.get(index).getName() + " was killed by " + chit.getName() + "!");
							allies.remove(index);
						}
					}
					else{
						// Hit, but shield?
						boolean armourProtection = checkArmour(chit, index, maneuver, directions[3]);
						if (!armourProtection){
							cbGui.infoText.setText(chit.getName() + " tried to attack, but was too weak to wound you.");
							System.out.println(chit.getName() + " tried to attack, but was too weak to wound you.");
						}
					}
				}
				// Too slow, not lined up with the player...
				else{
					// Missed
					cbGui.infoText.setText(chit.getName() + " tried to attack, but missed!");
					System.out.println(chit.getName() + " tried to attack, but missed!");
				}
			}
		}
	}

	private boolean checkArmour(Chit chit, int index, int maneuver, String shieldDirection) {
		// Chit = attacker
		ArrayList<Chit> toRemove = new ArrayList<Chit>();
		boolean flag = false;
		for(Chit item : playerCharacter.getInventory()){
			if (item.getName().equals("Suit of Armor") && (monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0) != 'T')){
				cbGui.infoText.setText(chit.getName() + " tried to attack you, but hit your suit of armour and did nothing.");
				System.out.println(chit.getName() + " tried to attack you, but hit your suit of armour and did nothing.");
				flag = true;
				break;
			}
			else if (item.getName().equals("Shield") && (monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0) >= 'M')){
				toRemove.add(item);
				cbGui.infoText.setText(chit.getName() + " attacked you, destroyed your shield and wounded you.");
				System.out.println(chit.getName() + " attacked you, destroyed your shield and wounded you.");
				woundChit(playerCharacter, "FIGHT/MOVE");
				flag = true;
				break;
			}
			else if (item.getName().equals("Shield") && (monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0) < 'M')){
				cbGui.infoText.setText(chit.getName() + " tried to attack you, but hit your shield and did nothing.");
				System.out.println(chit.getName() + " tried to attack you, but hit your shield and did nothing.");
				flag = true;
				break;
			}
			else if (item.getName().equals("Suit of Armor") && (monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0) == 'T')){
				if (((Armour)chit).isDamaged()){
					toRemove.add(item);
					cbGui.infoText.setText(chit.getName() + " attacked you, destroyed your suit of armour and wounded you.");
					System.out.println(chit.getName() + " attacked you, destroyed your suit of armour and wounded you.");
					woundChit(playerCharacter, "FIGHT/MOVE");
					flag = true;
					break;
				}
				else{
					((Armour)chit).setDamaged();
					cbGui.infoText.setText(chit.getName() + " attacked you, damaged your suit of armour and did nothing.");
					System.out.println(chit.getName() + " attacked you, damaged your suit of armour and did nothing.");
					flag = true;
					break;
				}
			}
			else if (item.getName().equals("Breastplate") && (maneuver == 0 || maneuver == 1)){
				toRemove.add(item);
				cbGui.infoText.setText(chit.getName() + " attacked you, destroyed your breastplate and wounded you.");
				System.out.println(chit.getName() + " attacked you, destroyed your breastplate and wounded you.");
				woundChit(playerCharacter, "FIGHT/MOVE");
				flag = true;
				break;
			}
			else if (item.getName().equals("Helmet") && (maneuver == 2) && ((Armour)item).isEnabled()){
				toRemove.add(item);
				cbGui.infoText.setText(chit.getName() + " attacked you, destroyed your helmet and wounded you.");
				System.out.println(chit.getName() + " attacked you, destroyed your helmet and wounded you.");
				woundChit(playerCharacter, "FIGHT/MOVE");
				flag = true;
				break;
			}
		}
		for (Chit item : toRemove){
			playerCharacter.getInventory().remove(item);
		}
		return flag;
	}

	private ArrayList<String> getTurns(ActionChit charFightChit) {
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
			if(!iAmLazy){
				map.put(playerCharacter.getName(), playerWeaponLength);
			}
			// Add allies
			for (Chit ally : allies){
				if (ally instanceof Native){
					String nativeWeapon = nativesLookup.natives.get(((Native) ally).getCharType()).get("weapon");
					if (ListOfWeapons.weapons.get(nativeWeapon).get("length") != null){
						map.put(ally.getName(), Integer.parseInt(ListOfWeapons.weapons.get(nativeWeapon).get("length")));
					}
				}
			}
			// Add enemies
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
			if(charFightChit != null && !iAmLazy){
				map.put(playerCharacter.getName(), charFightChit.getTime());
			}
			for (Chit ally : allies){
				if (ally instanceof Native){
					map.put(ally.getName(), ((Native)ally).getAttackTime());
				}
			}
			for (Chit enemy : enemies){
				map.put(enemy.getName(), Integer.parseInt(monsterLookup.monsters.get(enemy.getName()).get("attackSpeed")));
			}
			for(Entry<String, Integer> entry : entriesSortedByValues(map)){
				characterOrder.add(entry.getKey());
			}
			return characterOrder;
		}
	}
	
	private void fatigueChit(Character character, String category){
		ActionChit chit = null;
		while (chit == null){
			chit = cbGui.getSelectedChit(character, category, true);
		}
		character.activeActionChits.remove(chit);
		character.fatiguedActionChits.add(chit);
	}
	
	private void woundChit(Character character, String category){
		ActionChit chit = null;
		while (chit == null){
			chit = cbGui.getSelectedChit(character, category, false);
		}
		character.activeActionChits.remove(chit);
		character.woundedActionChits.add(chit);
	}

	private void playerAttack(ActionChit fightChit2, int index, String attackDirection, int enemyManeuver){
		boolean hasActiveWeapon = false;
		// Compare player attack time with target move time (faster = undercut = hit)
		if (fightChit2.getTime() < Integer.parseInt(monsterLookup.monsters.get(enemies.get(index).getName()).get("moveSpeed"))){
			for(Chit chit : playerCharacter.getInventory()){
				if ((chit instanceof Weapon) && (((Weapon)chit).isAlerted())){
					hasActiveWeapon = true;
					if (successfulAttack(fightChit2.getLetter().charAt(0), monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0))){
						cbGui.infoText.setText("You killed the " + enemies.get(index).getName() + "!");
						System.out.println("You killed the " + enemies.get(index).getName() + "!");
						enemies.remove(index);
					}
					else{
						// Character attacked, but was not strong enough to kill
						System.out.println("You tried to attack, but were not strong enough to inflict any damage.");
					}
				}
			}
			if(!hasActiveWeapon){
				// Attack with dagger, no weapon alerted! If an enemy has armor, then it's absolutely
				// NOT Light, so only useful case is medium. It's "always" medium as per 23.2/1c.
				System.out.println("You attack with the dagger, no active weapon!");
				if (successfulAttack("M".charAt(0), monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0))){
						cbGui.infoText.setText("You killed the " + enemies.get(index).getName() + "!");
						System.out.println("You killed the " + enemies.get(index).getName() + "!");
						enemies.remove(index);
						hasActiveWeapon = true;
				}
				else{
					// Character attacked, but was not strong enough to kill
					System.out.println("You tried to attack, but were not strong enough to inflict any damage.");
				}
			}
		}
		else{
			if (isAligned(attackDirection, enemyManeuver)){
				for(Chit chit : playerCharacter.getInventory()){
					if ((chit instanceof Weapon) && (((Weapon)chit).isAlerted()) &&
							(successfulAttack(fightChit2.getLetter().charAt(0), monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0)))){
							cbGui.infoText.setText("You killed the " + enemies.get(index).getName() + "!");
							enemies.remove(index);
							hasActiveWeapon = true;
					}
				}
				if(!hasActiveWeapon){
					// Attack with dagger, no weapon alerted!
					System.out.println("You attack with the dagger, no active weapon!");
					if (successfulAttack("M".charAt(0), monsterLookup.monsters.get(enemies.get(index).getName()).get("size").charAt(0))){
						cbGui.infoText.setText("You killed the " + enemies.get(index).getName() + "!");
						System.out.println("You killed the " + enemies.get(index).getName() + "!");
						enemies.remove(index);
						hasActiveWeapon = true;
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
		if ((monsterPosition == 0 && (atkDirection.contains("Charge") || atkDirection.contains("Thrust"))) || 
		(monsterPosition == 1 && (atkDirection.contains("Dodge") || atkDirection.contains("Swing"))) || 
		(monsterPosition == 2 && (atkDirection.contains("Duck") || atkDirection.contains("Smash")))){
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
	
	public void setCheatMode(boolean b){
		cheatMode = b;
	}
	
	/**
	 * Returns true if attack strength is greater than defend vulnerability
	 */
	private boolean successfulAttack(char attack, char defend){
		if(attack == defend){
			return true;
		}
		else if(attack == 'M' && defend == 'L'){
			return true;
		}
		else if(attack == 'H' && defend == 'M'){
			return true;
		}
		else if(attack == 'T' && defend == 'H'){
			return true;
		}
		else{
			return false;
		}
	}
}
