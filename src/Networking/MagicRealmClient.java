package Networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import ControlFlow.CombatSystem;
import ListsAndLogic.ListOfNatives;
import ListsAndLogic.ListOfSecretRoutes;
import ListsAndLogic.ListOfSecretRoutes;
import ListsAndLogic.ListOfMonsters;
import ListsAndLogic.ListOfWeapons;
import ListsAndLogic.MusicLookupTable;
import ObjectClasses.ActionChit;
import ObjectClasses.Armour;
import ObjectClasses.Chit;
import ObjectClasses.Clearing;
import ObjectClasses.HexTile;
import ObjectClasses.Horse;
import ObjectClasses.MapChit;
import ObjectClasses.Native;
import ObjectClasses.Player;
import ObjectClasses.Weapon;
import View.MagicRealmGUI;
import View.MapBrain;


/**
 * The main controller for the client side of the game. Incorporates networking 
 * for the client, sends/receives messages with the server and manipulates the view.
 * Original code accessed from: http://cs.lmu.edu/~ray/notes/javanetexamples/#chat
 */
public class MagicRealmClient implements Runnable {
    ObjectInputStream in;
    ObjectOutputStream out;
    MagicRealmGUI gui;
    private MusicLookupTable musicLookup;
    ListOfMonsters monsterList;
    ListOfNatives nativesList;
    ListOfWeapons weaponsList;
    /** A specialized object of secret routes on the map.*/
    ListOfSecretRoutes secretRoutes;
    /** An object representing the player associated with this client.*/
    Player player;
    /** The player's name.*/
    String name;
    /** Name of the player's character.*/
    String character;
    /** The client's communication socket.*/
    Socket socket;
    /** An integer count of the player's number of turns.*/
    int turns;
    /** The current day in the game.*/
    int day;
    /** Integer array for victory conditions.*/
    int[] victoryConditions;
    /**A variable for checking the previous use of a character's special ability (move, phase, etc).*/
    boolean playerSpecialAbility;
    /**A variable for checking the previous use of a horse special move ability.*/
    boolean horseSpecialAbility;
    /**A variable for checking the enabling of cheat mode.*/
    boolean cheatMode;
    /**The client's list of available characters to display for the player's selection.*/
    ArrayList<String> playableCharacters;
    /** A quick reference for the client to have dwelling chits on hand.*/
    ArrayList<Chit> dwellingChits;
    /** Combat system for fighting.*/
    CombatSystem combatSystem;
    /** Music! */
	Media mainSong;
	MediaPlayer mediaPlayer;
    
    
    public MagicRealmClient() {
    	gui = new MagicRealmGUI();
    	JFXPanel fxPanel = new JFXPanel();
    	combatSystem = new CombatSystem(cheatMode);
    	musicLookup = new MusicLookupTable();
    	mainSong = new Media(Paths.get(musicLookup.table.get("mainTheme")).toUri().toString());
    	mediaPlayer = new MediaPlayer(mainSong);
    	monsterList = new ListOfMonsters();
    	nativesList = new ListOfNatives();
    	secretRoutes = new ListOfSecretRoutes();
    	weaponsList = new ListOfWeapons();
    	setActionListeners();
    	day = 1;
    }

    private void setActionListeners() {
    	// Note that actionListeners are set here, so that the controller
    	// can change the view behaviour based on the actions performed in 
    	// the view, without the view knowing about the model.
    	gui.startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					out.writeObject("STARTGAME");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        });
	}
    
    private void refreshMap(){
    	gui.map.removeAll();
    	for (HexTile tile : gui.getMapBrain().getTiles()){
    		for (Clearing clearing : tile.getClearings()){
    			int offset = 0;
    			for (Chit chit : clearing.getChits()){
    				gui.addImage(chit.getName(), 
    						(int)clearing.getArea().getX() - offset, 
    						(int)clearing.getArea().getY() - offset, 
    						(int)clearing.getArea().getWidth(), 
    						(int)clearing.getArea().getHeight(), 0, true);
    				offset += 4;
    			}
    		}
    	}
    	gui.addImage("board.png",0,0,2221,2439,1, false);
    	gui.refreshMapInternalFrame();
    }
    
    private void placeCharacter(String targetClearing){
    	if (player.getCharacter().getClearing() == null){
    		player.getCharacter().setClearing(targetClearing);
    	}
    	else{
        	// Remove the player's chit(s) from any clearing that isn't the target clearing,
    		// and add it to the target clearing
        	for (HexTile tile : gui.getMapBrain().getTiles()){
        		for (Clearing clearing : tile.getClearings()){
        			if (clearing.getName().equals(targetClearing)){
        				clearing.addChit(player.getCharacter());
        			}
        			else if (!clearing.getName().equals(targetClearing)){
        				Iterator<Chit> iter = clearing.getChits().iterator();
        				while (iter.hasNext()){
        					if(iter.next().getName().equals(player.getCharacter().getName())){
        						iter.remove();
        					}
        				}
        			}
        		}
        	}
        	// Repeat for all player allies
        	for (Chit ally : player.getCharacter().getAllies()){
        		for (HexTile tile : gui.getMapBrain().getTiles()){
            		for (Clearing clearing : tile.getClearings()){
            			if (clearing.getName().equals(targetClearing)){
            				clearing.addChit(ally);
            			}
            			else if (!clearing.getName().equals(targetClearing)){
            				Iterator<Chit> iter = clearing.getChits().iterator();
            				while (iter.hasNext()){
            					if(iter.next().getName().equals(ally.getName())){
            						iter.remove();
            					}
            				}
            			}
            		}
            	}
        	}
        	// Repeat for any horses (need them represented in-game)
        	for (Chit item : player.getCharacter().getInventory()){
        		if(item instanceof Horse){
            		for (HexTile tile : gui.getMapBrain().getTiles()){
                		for (Clearing clearing : tile.getClearings()){
                			if (clearing.getName().equals(targetClearing)){
                				clearing.addChit(item);
                			}
                			else if (!clearing.getName().equals(targetClearing)){
                				Iterator<Chit> iter = clearing.getChits().iterator();
                				while (iter.hasNext()){
                					if(iter.next().getName().equals(item.getName())){
                						iter.remove();
                					}
                				}
                			}
                		}
                	}
        		}
        	}
    	}
    	// Set the player's new clearing
    	player.getCharacter().setClearing(targetClearing);
    	refreshMap();
    }
    
    
    
    
/**************************************************************************************************
* FUNCTION: summonMonster()                                                                 Apr. 06
* @param:   dice (int) 
**************************************************************************************************/
    private void summonMonster(int dice){
	    
	    System.out.println("-- In summonMonster().");
    	    
    	    String chitName;
    	    String onThisClearing;
    	    int soundNum;
    	    int counter = 0;
    	    MapChit tempMapChit;
    	    
    	    HexTile currentTile      = gui.mapBrain.getCurrentTile();
    	    
    	    String warningChitLetter = currentTile.getWarningChit().getLetter();
    	    System.out.println("Warning Letter: " + warningChitLetter);
    	    
    	    
    	    
    	    if (currentTile.hasSiteSoundChit()){
    		    System.out.println("-- Tile has sound chit!");
    		    chitName = currentTile.getSiteSoundChit().getName();
    		    System.out.println(chitName);
    		    
    		    /*   If LOST CITY or LOST CASTLE   */
    		    if (chitName.equals("LOST CITY") || chitName.equals("LOST CASTLE")){
    			    System.out.println("-- Switching chit for 5 new site/sound chits.");
    			    while (counter < 5){
    				    tempMapChit = gui.getMapBrain().mapChits.getRandomSiteSoundChit(currentTile);
    				    if (!tempMapChit.getName().equals("LOST CITY") || (!tempMapChit.getName().equals("LOST CASTLE"))){
    					    currentTile.addChit(tempMapChit);
    					    // put swich here
    					    counter++;
    				    }
    			    }
    		    }
    		    
    		    soundNum = currentTile.getSiteSoundNumber();
    		    System.out.println(soundNum);
    	    }else{
    		    System.out.println("-- Tile does NOT have sound chit!");
    		    chitName = currentTile.getWarningChit().getName();
    		    System.out.println(chitName);
    		    soundNum = 1;
    	    }
    	   
    	    onThisClearing = currentTile.getName() + " C" + soundNum;
    	    System.out.println("-- Monster being placed on: " + onThisClearing);
    	    
    	    /* Cases for Woods Monsters and Site Chits */
    	    switch(dice){ 
    			case 1:			
    				if (chitName.equals("HOARD")){
    					placeTheMonster("Tremendous Flying Dragon", onThisClearing);
    				}
    				else if(chitName.equals("LAIR")){
    					placeTheMonster("Heavy Dragon", onThisClearing);
    				}
    				break;
    			case 2:
    				if (warningChitLetter.equals("W") && chitName.equals("RUINS")){
    					placeTheMonster("Wolf", onThisClearing);
    				}
    				break;
    			case 3:
    				if (warningChitLetter.equals("W") && chitName.equals("BONES")){
    					placeTheMonster("Ogre", onThisClearing);
    				}
    				else if(warningChitLetter.equals("W") && chitName.equals("DANK")){
    					placeTheMonster("Viper", onThisClearing);
    				}
    				else if(chitName.equals("POOL")){
    					placeTheMonster("Tremendous Octopus", onThisClearing);
    				}
    				break;
    			case 4: 
    				if (chitName.equals("VAULT")){
    					placeTheMonster("Tremendous Troll", onThisClearing);
    				}
    				break;
    			case 5:
    				if (chitName.equals("CAIRNS")){
    					placeTheMonster("Tremendous Spider", onThisClearing);
    				}
    				break;
    			default:
    				break;
    	    	}
    }
        
        
        
        
/**************************************************************************************************
* FUNCTION: placeTheMonster()                                                              Apr. 06
* @param:   dice (int) 
**************************************************************************************************/     
        private void placeTheMonster(String monsterName, String targetClearing){
      	  	System.out.println("-- In placeMonster().");
      	  	String playerClearing = player.getCharacter().getClearing();
      	    	if (player.getCharacter().getClearing() == null){
      	    		player.getCharacter().setClearing(targetClearing);
      	    	}
      	    	else{
      	        	for (HexTile tile : gui.getMapBrain().getTiles()){
      	        		for (Clearing clearing : tile.getClearings()){
      	        			if (clearing.getName().equals(targetClearing)){
      	        				//clearing.addChit(player.getCharacter());
      	        				clearing.addChit(new Chit(monsterName, monsterList.monsters.get(monsterName).get("size")));
      	        				refreshMap();
      	        			}
      	        			else if (!clearing.getName().equals(targetClearing)){
//      	        				Iterator<Chit> iter = clearing.getChits().iterator();
//      	        				while (iter.hasNext()){
//      	        					if(iter.next().getName().equals(player.getCharacter().getName())){
//      	        						iter.remove();
//      	        					}
//      	        				}
      	        				//System.out.println("in the else ----- shit");
      	        			}
      	        		}
      	        	}
      	    	}
      	    	// Set the player's new clearing
      	    	player.getCharacter().setClearing(playerClearing);
      	    	refreshMap();
      	    }
    
    private int rollDice(){
    	if (cheatMode){
    		return gui.getDieRoll();
    	}
    	else if (player.getCharacter().getName().equals("Dwarf")){
    		return (int)(Math.random()*5+1);
    	}
    	else{
    		return Math.max((int)(Math.random()*5+1), (int)(Math.random()*5+1));
    	}
    }
    
    
    private void setCharacterActionListeners(){
		gui.tradeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// Find if the player's on a dwelling
				for (Chit chit : gui.getMapBrain().findDwellings()){
        			if (chit.getLetter().equals(player.getCharacter().getClearing())){
        				mediaPlayer.pause();
        				conductTrade();
        				turns--;
        				ArrayList<String> playerStats = player.getCharacter().getStats();
                    	playerStats.add("Turns");
                    	ArrayList<Integer> statVals = player.getCharacter().getStatVals();
                    	statVals.add(turns);
                    	gui.updateStats(playerStats, statVals);
                    	mediaPlayer.play();
                    	if (turns == 0){
        					gui.disableButtons();
        					mediaPlayer.stop();
        					try {
        						out.writeObject("COMPLETE");
        					} catch (IOException e1) {
        						e1.printStackTrace();
        					}
        					gui.playerInfoArea.append("\nDay completed, waiting for others...");
        				}
        				return;
        			}
        		}
				gui.playerInfoArea.append("\nNo dwellings here, nowhere to trade!");
			}
		});
		
		gui.showCheatButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (cheatMode == false){
					cheatMode = true;
					gui.playerInfoArea.append("\nCheat mode enabled!");
				}
				else{
					cheatMode = false;
					gui.playerInfoArea.append("\nCheat mode disabled!");
				}
				combatSystem.setCheatMode(cheatMode);
			}
		});
		
		gui.restButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (player.getCharacter().fatiguedActionChits.size() == 0 && player.getCharacter().woundedActionChits.size() == 0){
					gui.playerInfoArea.append("\nNo wounded or fatigued chits to rest!");
				}
				else{
					turns--;
					if (player.getCharacter().fatiguedActionChits.size() != 0 && player.getCharacter().woundedActionChits.size() == 0){
						String chit = gui.getFatiguedChit(player.getCharacter().fatiguedActionChits);
						 ActionChit temp = null;
						 for (ActionChit action : player.getCharacter().fatiguedActionChits){
							 if (action.toString().equals(chit)){
								 temp = action;
								 break;
							 }
						 }
						 player.getCharacter().fatiguedActionChits.remove(temp);
						 player.getCharacter().activeActionChits.add(temp);
					}
					else if (player.getCharacter().woundedActionChits.size() != 0 && player.getCharacter().fatiguedActionChits.size() == 0){
						 String chit = gui.getWoundedChit(player.getCharacter().woundedActionChits);
						 ActionChit temp = null;
						 for (ActionChit action : player.getCharacter().woundedActionChits){
							 if (action.toString().equals(chit)){
								 temp = action;
								 break;
							 }
						 }
						 player.getCharacter().woundedActionChits.remove(temp);
						 player.getCharacter().fatiguedActionChits.add(temp);
					}
					else{
						int choice = gui.getWoundOrFatigue();
						String chit = "";
						if (choice == 0){
							 chit = gui.getWoundedChit(player.getCharacter().woundedActionChits);
							 ActionChit temp = null;
							 for (ActionChit action : player.getCharacter().woundedActionChits){
								 if (action.toString().equals(chit)){
									 temp = action;
									 break;
								 }
							 }
							 player.getCharacter().woundedActionChits.remove(temp);
							 player.getCharacter().fatiguedActionChits.add(temp);
						}
						else{
							 chit = gui.getFatiguedChit(player.getCharacter().fatiguedActionChits);
							 ActionChit temp = null;
							 for (ActionChit action : player.getCharacter().fatiguedActionChits){
								 if (action.toString().equals(chit)){
									 temp = action;
									 break;
								 }
							 }
							 player.getCharacter().fatiguedActionChits.remove(temp);
							 player.getCharacter().activeActionChits.add(temp);
						}
					}
				}
				ArrayList<String> playerStats = player.getCharacter().getStats();
            	playerStats.add("Turns");
            	ArrayList<Integer> statVals = player.getCharacter().getStatVals();
            	statVals.add(turns);
            	gui.updateStats(playerStats, statVals);
				if (turns == 0){
					gui.disableButtons();
					mediaPlayer.stop();
					try {
						out.writeObject("COMPLETE");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					gui.playerInfoArea.append("\nDay completed, waiting for others...");
				}
			}
		});
		
		gui.searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				turns--;
				int searchChoice = gui.getSearchType();
				String temp = "";
				int dice = rollDice();
				if (searchChoice == 0){
					switch(dice){
					case 1:
						gui.playerInfoArea.append("\nPick any category!");
						break;
					case 2:
						gui.playerInfoArea.append("\nClues and Paths");
						for (String adjClearing : getPlayerClearing().getAdjacentClearings()){
							if (secretRoutes.isSecretPath(getPlayerClearing().getName(), adjClearing)){
								player.getCharacter().addDiscovery(getPlayerClearing().getName() + "," + adjClearing);
								gui.playerInfoArea.append("\nYou found a secret route: " + getPlayerClearing().getName() + "," + adjClearing + "!");
							}
						}
						for (MapChit chit : getPlayerTile().getChits()){
							temp += (chit.getName() + ",");
						}
						gui.playerInfoArea.append("\n Clues:" + temp);
						break;
					case 3:
						gui.playerInfoArea.append("\nHidden enemies and Paths");
						for (String adjClearing : getPlayerClearing().getAdjacentClearings()){
							if (secretRoutes.isSecretPath(getPlayerClearing().getName(), adjClearing)){
								player.getCharacter().addDiscovery(getPlayerClearing().getName() + "," + adjClearing);
								gui.playerInfoArea.append("\nYou found a secret route: " + getPlayerClearing().getName() + "," + adjClearing + "!");
							}
						}
						break;
					case 4:
						gui.playerInfoArea.append("\nHidden enemies");
						break;
					case 5:
						gui.playerInfoArea.append("\nClues");
						for (MapChit chit : getPlayerTile().getChits()){
							temp += (chit.getName() + ",");
						}
						gui.playerInfoArea.append("\n Clues:" + temp);
						break;
					case 6:
						gui.playerInfoArea.append("\nYou didn't find anything.");
						break;
					default:
						gui.playerInfoArea.append("\nYou didn't find anything.");
						break;
					}
				}
				else{
					switch(dice){
					case 1:
						gui.playerInfoArea.append("\nPick any category!");
						break;
					case 2:
						gui.playerInfoArea.append("\nYou discovered passages and clues!");
						for (String adjClearing : getPlayerClearing().getAdjacentClearings()){
							if (secretRoutes.isSecretPassage(getPlayerClearing().getName(), adjClearing)){
								player.getCharacter().addDiscovery(getPlayerClearing().getName() + "," + adjClearing);
								gui.playerInfoArea.append("\nYou found a secret route: " + getPlayerClearing().getName() + "," + adjClearing + "!");
							}
						}
						for (MapChit chit : getPlayerTile().getChits()){
							temp += (chit.getName() + ",");
						}
						gui.playerInfoArea.append("\n Clues:" + temp);
						break;
					case 3:
						gui.playerInfoArea.append("\nYou discovered passages!");
						for (String adjClearing : getPlayerClearing().getAdjacentClearings()){
							if (secretRoutes.isSecretPassage(getPlayerClearing().getName(), adjClearing)){
								player.getCharacter().addDiscovery(getPlayerClearing().getName() + "," + adjClearing);
								gui.playerInfoArea.append("\nYou found a secret route: " + getPlayerClearing().getName() + "," + adjClearing + "!");
							}
						}
						break;
					case 4:
						gui.playerInfoArea.append("\nYou discovered chits (treasure)!");
						if(cheatMode){
							int[] cheatTreasure = gui.getCheatTreasure();
							getPlayerClearing().wipeTreasure();
							player.getCharacter().gainGold(cheatTreasure[0]);
							player.getCharacter().gainNotority(cheatTreasure[1]);
							player.getCharacter().gainFame(cheatTreasure[2]);
						}
						else{
							int[] foundTreasure = getPlayerClearing().plunderTreasure();
							player.getCharacter().gainGold(foundTreasure[0]);
							player.getCharacter().gainNotority(foundTreasure[1]);
							player.getCharacter().gainFame(foundTreasure[2]);
							if(!(foundTreasure[0] == 0 && foundTreasure[1] == 0 && foundTreasure[2] == 0)){
								gui.playerInfoArea.append("\nYou found " + foundTreasure[0] + " gold, gained " +
										foundTreasure[1] + " notority, and gained " + foundTreasure[2] + " fame! You now have " + 
													player.getCharacter().getGold() + " gold, " + player.getCharacter().getNotority() +
													" notority, and " + player.getCharacter().getFame() + " fame.");
							}
							else{
								gui.playerInfoArea.append("\nHate to break it to ya, but you only found fool's gold. You or someone else already dug up whatever was here...");
							}
						}
						 
						break;
					case 5:
						gui.playerInfoArea.append("\nYou didn't find anything.");
						break;
					case 6:
						gui.playerInfoArea.append("\nYou didn't find anything.");
						break;
					default:
						gui.playerInfoArea.append("\nYou didn't find anything.");
						break;
					}
				}
				ArrayList<String> playerStats = player.getCharacter().getStats();
            	playerStats.add("Turns");
            	ArrayList<Integer> statVals = player.getCharacter().getStatVals();
            	statVals.add(turns);
            	gui.updateStats(playerStats, statVals);
				if (turns == 0){
					gui.disableButtons();
					mediaPlayer.stop();
					try {
						out.writeObject("COMPLETE");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					gui.playerInfoArea.append("\nDay completed, waiting for others...");
				}
			}

			private HexTile getPlayerTile(){
				for (HexTile tile : gui.getMapBrain().getTiles()){
					if (tile.getClearings().contains(getPlayerClearing())){
						return tile;
					}
				}
				return null;
			}
		});
		
		gui.moveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// If the selected clearing's adjacent clearings contains the player's current clearing, AND
				// (If the pair of clearings is NOT in the list of secret routes, OR
				// If the player has found this secret route)
				if (gui.getMapBrain().getCurrentClearing().getAdjacentClearings().contains(getPlayerClearing().getName()) &&
						(!secretRoutes.isSecret(gui.getMapBrain().getCurrentClearing().getName(),getPlayerClearing().getName()) ||
						player.getCharacter().hasFoundDiscovery(gui.getMapBrain().getCurrentClearing().getName() + "," + getPlayerClearing().getName()))){
					// Rule 7.6 - A player cannot enter a cave on the same day he uses a "sunlight" phase
					// English translation - If a player has already moved for the day, they can't move to a cave clearing
					// (Note: Rule 7.7 says that players can use an extra move to enter the cave, so it is assumed that
					// if turns >= 3, you can move into a cave)
					if (turns < 3){
						boolean caveFlag = false;
						for(MapChit chit : gui.getMapBrain().getTile(player.getCharacter().getClearing()).getChits()){
							if(chit instanceof MapChit && ((MapChit)chit).getLetter().equals("C")){
								// Moving from cave clearings, we're fine!
								caveFlag = true;
							}
						}
						if(!caveFlag){
							// Moving from a different clearing into a cave clearing, gotta check!
							for(Chit chit : gui.getMapBrain().getCurrentTile().getChits()){
								if(chit instanceof MapChit && ((MapChit)chit).getLetter().equals("C")){
									gui.playerInfoArea.append("\nYou can't move into a cave clearing after sunlight! (Less than 3 turns left)");
									return;
								}
							}
						}
					}
					if (player.getCharacter().getName().equals("Amazon") && playerSpecialAbility == false){
						playerSpecialAbility = true;
						// Check if we're moving to a mountain clearing; still need to dock Amazon the extra move
						// Amazon can only lose 1 turn max though, so we treat it like a normal move
						for(Chit chit : gui.getMapBrain().getCurrentTile().getChits()){
							if(chit instanceof MapChit && ((MapChit)chit).getLetter().equals("M")){
								turns--;
								break;
							}
						}
					}
					else if(horseSpecialAbility == false && player.getCharacter().hasActiveHorse()){
						horseSpecialAbility = true;
						for(Chit chit : gui.getMapBrain().getCurrentTile().getChits()){
							if(chit instanceof MapChit && ((MapChit)chit).getLetter().equals("M")){
								turns--;
								break;
							}
						}
					}
					else{
						for(Chit chit : gui.getMapBrain().getCurrentTile().getChits()){
							if(chit instanceof MapChit && ((MapChit)chit).getLetter().equals("M")){
								if(turns == 1){
									// Not enough turns to move, notify player and exit the function completely
									gui.playerInfoArea.append("\nCan't move to this mountain clearing, only one turn left!");
									return;
								}
								else{
									// 2+ turns, dock the player the extra turn
									turns--;
									break;
								}
							}
						}
						turns--;
					}
					// Loop for disabling horse while in caves
					boolean isInCave = false;
					for(Chit chit : gui.getMapBrain().getCurrentTile().getChits()){
						if(chit instanceof MapChit && ((MapChit)chit).getLetter().equals("C")){
							isInCave = true;
						}
					}
					Iterator<Chit> iter = player.getCharacter().getInventory().iterator();
					while(iter.hasNext()){
						Chit item = iter.next();
						if(item instanceof Horse){
							System.out.println(isInCave);
							((Horse)item).setActive(!isInCave);
						}
					}
					gui.playerInfoArea.append("\nMoved to " + gui.getMapBrain().getCurrentClearing().getName());
					placeCharacter(gui.getMapBrain().getCurrentClearing().getName());
					summonMonster(rollDice());
					if(gui.getMapBrain().getCurrentClearing().getChits().size() != 1){
						for(Chit chit : gui.getMapBrain().getCurrentClearing().getChits()){
							// If it produces a result off the monster lookup table, it's a monster!
							if(monsterList.monsters.get(chit.getName()) != null){
								ArrayList<Chit> side1 = new ArrayList<Chit>();
								ArrayList<Chit> side2 = new ArrayList<Chit>();
								ArrayList<Chit> inventory = player.getCharacter().getInventory();
								// Add player
								side1.add(player.getCharacter());
								// Add native
								for (Native ally : player.getCharacter().getAllies()){
									side1.add(ally);
								}
								for(Chit enemy : gui.getMapBrain().getCurrentClearing().getChits()){
									if (!side1.contains(enemy)){
										side2.add(enemy);
									}
								}
								mediaPlayer.pause();
								combatSystem.initFight(side1, side2, player.getCharacter());
								// If the player died, make a treasure pile out of his inventory
								if (player.getCharacter() == null){
									for (Chit item : inventory){
										gui.getMapBrain().getCurrentClearing().addChit(item);
									}
								}
								side1.addAll(side2);
								// Remove all chits that were wiped out in combat!
								Iterator<Chit> clearingChits = gui.getMapBrain().getCurrentClearing().getChits().iterator();
								while (clearingChits.hasNext()){
									if (!side1.contains(clearingChits.next())){
										clearingChits.remove();
									}
								}
								refreshMap();
								mediaPlayer.play();
								break;
							}
						}
					}
					ArrayList<String> playerStats = player.getCharacter().getStats();
                	playerStats.add("Turns");
                	ArrayList<Integer> statVals = player.getCharacter().getStatVals();
                	statVals.add(turns);
                	gui.updateStats(playerStats, statVals);
					if (turns == 0){
						gui.disableButtons();
						mediaPlayer.stop();
						try {
							out.writeObject("COMPLETE");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						gui.playerInfoArea.append("\nDay completed, waiting for others...");
					}
				}
				else{
					gui.playerInfoArea.append("\nCan't travel there!");
				}
			}
		});
		
		gui.hideButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				turns--;
				if (rollDice() == 6){
					gui.playerInfoArea.append("\nCouldn't hide!");
				}
				else{
					player.getCharacter().setHidden(true);
					gui.playerInfoArea.append("\nManaged to hide!");
				}
				ArrayList<String> playerStats = player.getCharacter().getStats();
            	playerStats.add("Turns");
            	ArrayList<Integer> statVals = player.getCharacter().getStatVals();
            	statVals.add(turns);
            	gui.updateStats(playerStats, statVals);
				if (turns == 0){
					gui.disableButtons();
					mediaPlayer.stop();
					try {
						out.writeObject("COMPLETE");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					gui.playerInfoArea.append("\nDay completed, waiting for others...");
				}
			}
		});
		
		gui.showCardButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				gui.showCharacterCard(player.getCharacter().getName());
			}
		});
		
		gui.setupVRButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO Place function here.
			}
		});
    }

	protected void conductTrade() {
		Media hit = new Media(Paths.get(musicLookup.table.get("tradebgm")).toUri().toString());
		MediaPlayer tradeBGMPlayer = new MediaPlayer(hit);
		tradeBGMPlayer.play();
		String choice = gui.getTradeType();
		ArrayList<String> selectableInventory = new ArrayList<String>();
		if(choice == null){
			gui.displayMessage("A wise'r once told me, an 'unce of prevention's worth a pound o' cure. Maybe you'll learn the hard way.", 
					"Dwelling Marketplace");
			return;
		}
		if (choice.contains("Buy")){
			String category = gui.getBuySellTradeChoice(choice);
			if (category == null){gui.displayMessage("If you're not gonna buy, get outta here and stop wasting my time!", "Dwelling Marketplace");return;}
			if(category.contains("Weapons")){
				// Buy weapons
				String item = gui.getPlayerItem(weaponsList.getAllValidWeapons());
				String costString = weaponsList.weapons.get(item).get("price");
				int cost = customMeetingTable(Integer.parseInt(costString));
				if (cost == 0){
					gui.displayMessage("We don't sell to rascals like you.", 
							"Dwelling Marketplace");
					return;
				}
				else if (cost <= player.getCharacter().getGold()){
					int confirm = gui.confirmBuy(item, cost);
					if (confirm == JOptionPane.YES_OPTION){
						gui.displayMessage("Fantastic! It's yours.", "Dwelling Marketplace");
						player.getCharacter().addItem(new Weapon(item,weaponsList.weapons.get(item).get("stength"), false, 0, false, 0));
						player.getCharacter().loseGold(cost);
					}
					else{
						gui.displayMessage("Too cheap? Fine, get outta here!", "Dwelling Marketplace");
					}
				}
				else{
					gui.displayMessage("Ha! You're too poor to afford that weapon. Get outta here and don't waste my time!", "Dwelling Marketplace");
				}
				
			}
			else if(category.contains("Armour")){
				// Buy armor
				String item = gui.getPlayerItem(weaponsList.getAllValidArmour());
				String costString = weaponsList.weapons.get(item).get("price");
				int cost = customMeetingTable(Integer.parseInt(costString));
				if (cost == 0){
					gui.displayMessage("We don't sell to rascals like you.", 
							"Dwelling Marketplace");
					return;
				}
				else if (cost <= player.getCharacter().getGold()){
					int confirm = gui.confirmBuy(item, cost);
					if (confirm == JOptionPane.YES_OPTION){
						gui.displayMessage("Fantastic! It's yours.", "Dwelling Marketplace");
						player.getCharacter().addItem(new Armour(item,weaponsList.weapons.get(item).get("stength")));
						player.getCharacter().loseGold(cost);
					}
					else{
						gui.displayMessage("Too cheap? Fine, get outta here!", "Dwelling Marketplace");
					}
				}
				else{
					gui.displayMessage("Ha! You're too poor to afford that weapon. Get outta here and don't waste my time!", "Dwelling Marketplace");
				}
				
			}
			else{
				// Buy a horse!
				for(Chit item : getPlayerClearing().getChits()){
					if (item instanceof Horse && (!((Horse)item).isAllied())){
						selectableInventory.add(item.getName());
					}
				}
				if (selectableInventory.size() == 0){
					gui.displayMessage("Sorry, no horses here!", "Dwelling Marketplace");
				}
				String item = gui.getPlayerItem(selectableInventory);
				Horse horseToBuy = null;
				for(Chit chit : getPlayerClearing().getChits()){
					if (chit instanceof Horse && chit.getName().equals(item)){
						horseToBuy = (Horse) chit;
					}
				}
				int cost = customMeetingTable(horseToBuy.getCost());
				if (cost == 0){
					gui.displayMessage("You're not worthy of buying one of our horses.", 
							"Dwelling Marketplace");
					return;
				}
				else if (cost <= player.getCharacter().getGold()){
					int confirm = gui.confirmBuy(item, cost);
					if (confirm == JOptionPane.YES_OPTION){
						gui.displayMessage("Fantastic! It's yours.", "Dwelling Marketplace");
						horseToBuy.setAllied();
						player.getCharacter().addItem(horseToBuy);
						getPlayerClearing().removeChit(horseToBuy);
						player.getCharacter().loseGold(cost);
					}
					else{
						gui.displayMessage("Too cheap? Fine, get outta here!", "Dwelling Marketplace");
					}
				}
				else{
					gui.displayMessage("Ha! You're too poor to afford that weapon. Get outta here and don't waste my time!", "Dwelling Marketplace");
				}
			}
		}
		else if (choice.contains("Sell")){
			String category = gui.getBuySellTradeChoice(choice);
			if (category == null){gui.displayMessage("If you're not gonna sell anything, get outta here and stop wasting my time!", "Dwelling Marketplace");return;}
			if(category.contains("Weapons")){
				for(Chit item : player.getCharacter().getInventory()){
					if (item instanceof Weapon){
						selectableInventory.add(item.getName());
					}
				}
				if (selectableInventory.size() == 0){
					gui.displayMessage("Trying to sell when you have nothing to sell? Get outta here and don't waste my time!", "Dwelling Marketplace");
					return;
				}
				String item = gui.getPlayerItem(selectableInventory);
				String costString = weaponsList.weapons.get(item).get("price");
				int cost = customMeetingTable(Integer.parseInt(costString));
				if (cost == 0){
					gui.displayMessage("You're trying to sell me this piece of junk? I'm disgusted.", 
							"Dwelling Marketplace");
				}
				else{
					int confirm = gui.confirmSell(item, cost);
					if (confirm == JOptionPane.YES_OPTION){
						gui.displayMessage("Fantastic! Here's your gold.", "Dwelling Marketplace");
						player.getCharacter().gainGold(cost);
						Iterator<Chit> iter = player.getCharacter().getInventory().iterator();
						while(iter.hasNext()){
							Chit chit = iter.next();
							if (chit.getName().equals(item)){
								iter.remove();
							}
						}
					}
					else{
						gui.displayMessage("Too cheap? Fine, get outta here!", "Dwelling Marketplace");
					}
				}
			}
			else if(category.contains("Armour")){
				for(Chit item : player.getCharacter().getInventory()){
					if (item instanceof Armour){
						selectableInventory.add(item.getName());
					}
				}
				if (selectableInventory.size() == 0){
					gui.displayMessage("Trying to sell when you have nothing to sell? Get outta here and don't waste my time!", "Dwelling Marketplace");
					return;
				}
				String item = gui.getPlayerItem(selectableInventory);
				String costString = weaponsList.weapons.get(item).get("price");
				int cost = customMeetingTable(Integer.parseInt(costString));
				if (cost == 0){
					gui.displayMessage("You're trying to sell me this piece of junk? I'm disgusted.", 
							"Dwelling Marketplace");
				}
				else{
					int confirm = gui.confirmSell(item, cost);
					if (confirm == JOptionPane.YES_OPTION){
						gui.displayMessage("Fantastic! Here's your gold.", "Dwelling Marketplace");
						player.getCharacter().gainGold(cost);
						Iterator<Chit> iter = player.getCharacter().getInventory().iterator();
						while(iter.hasNext()){
							Chit chit = iter.next();
							if (chit.getName().equals(item)){
								iter.remove();
							}
						}
					}
					else{
						gui.displayMessage("Too cheap? Fine, get outta here!", "Dwelling Marketplace");
					}
				}
			}
			else{
				// Sell a horse!
				for(Chit item : player.getCharacter().getInventory()){
					if (item instanceof Horse){
						selectableInventory.add(item.getName());
					}
				}
				if (selectableInventory.size() == 0){
					gui.displayMessage("Trying to sell when you have nothing to sell? Get outta here and don't waste my time!", "Dwelling Marketplace");
					return;
				}
				String item = gui.getPlayerItem(selectableInventory);
				Horse horseToSell = null;
				for(Chit chit : player.getCharacter().getInventory()){
					if (chit instanceof Horse && chit.getName().equals(item)){
						horseToSell = (Horse) chit;
					}
				}
				int cost = customMeetingTable(horseToSell.getCost());
				if (cost == 0){
					gui.displayMessage("You're trying to sell me this piece of junk? I'm disgusted.", 
							"Dwelling Marketplace");
					return;
				}
				else{
					int confirm = gui.confirmSell(item, cost);
					if (confirm == JOptionPane.YES_OPTION){
						gui.displayMessage("Fantastic! Here's your gold.", "Dwelling Marketplace");
						player.getCharacter().gainGold(cost);
						Iterator<Chit> iter = player.getCharacter().getInventory().iterator();
						while(iter.hasNext()){
							Chit chit = iter.next();
							if (chit.equals(horseToSell)){
								iter.remove();
							}
						}
					}
					else{
						gui.displayMessage("Too cheap? Fine, get outta here!", "Dwelling Marketplace");
					}
				}
			}
		}
		else{
			// Get all natives
			ArrayList<String> nativeNames = new ArrayList<String>();
			for (Chit chit : getPlayerClearing().getChits()){
				if (chit instanceof Native){
					nativeNames.add(chit.getName());
				}
			}
			String nativeToHire = gui.getNativeToHire(nativeNames.toArray());
			// Have to get the native's character type for hiring
			String charType = null;
			for (Chit chit : getPlayerClearing().getChits()){
				if (chit instanceof Native && chit.getName().equals(nativeToHire)){
					charType = ((Native)chit).getCharType();
				}
			}
			if (charType == null){
				gui.displayMessage("We'll be seein' ya. Or maybe not.", 
						"Dwelling Marketplace");
				return;
			}
			// Get the native's wage, and roll on the meeting table for final cost
			String costString = nativesList.natives.get(charType).get("wage");
			int cost = customMeetingTable(Integer.parseInt(costString));
			if (cost == 0){
				gui.displayMessage("Turns out he's not interested. Better get movin' before others take an interest in ya...for reasons other than being hired.", 
						"Dwelling Marketplace");
			}
			else if (cost <= player.getCharacter().getGold()){
				int confirm = gui.confirmHire(nativeToHire, cost);
				if (confirm == JOptionPane.YES_OPTION){
					gui.displayMessage("Fantastic! He's yours.", "Dwelling Marketplace");
					// Add chit to player ally list
					for (Chit chit : getPlayerClearing().getChits()){
						if (chit.getName().equals(nativeToHire)){
							player.getCharacter().addAlly((Native) chit);
							player.getCharacter().loseGold(cost);
						}
					}
					Iterator<Chit> iter = getPlayerClearing().getChits().iterator();
					while(iter.hasNext()){
						Chit chit = iter.next();
						if(chit instanceof Native){
							if(player.getCharacter().getAllies().contains((Native)chit)){
								iter.remove();
							}
						}
					}
					refreshMap();
				}
				else{
					gui.displayMessage("Too cheap? Fine, get outta here!", "Dwelling Marketplace");
				}
			}
			else{
				gui.displayMessage("Ha! You're too poor to afford that guy. Get outta here and don't waste my time!", "Dwelling Marketplace");
			}
		}
		tradeBGMPlayer.stop();
	}
	
	/**
	 * A custom meeting table based off of the "Ally" relationship in the official meeting table.
	 * Die roll 1 produces no deal instead of boon!
	 */
	private int customMeetingTable(int cost){
		int result = rollDice();
		switch(result){
		case 1:
			return 0;
		case 2:
			return cost;
		case 3:
			return cost*2;
		case 4:
			return cost*3;
		case 5:
			return cost*4;
		case 6:
			return cost*4;
		default:
			return 0;
		}
	}

	private Clearing getPlayerClearing() {
		for (HexTile tile : gui.getMapBrain().getTiles()){
    		for (Clearing clearing : tile.getClearings()){
    			if (clearing.getName().equals(player.getCharacter().getClearing())){
    				return clearing;
    			}
    		}
    	}
		return null;
	}

	/**
     * Connects to the server then enters the processing loop.
     */
    public void run(){
        // Make connection and initialize streams
        String serverAddress = gui.getServerAddress();
        try {
        	// If left blank, serverAddress will simply be "localhost"
        	socket = new Socket(serverAddress, config.PORT);
        	out = new ObjectOutputStream(socket.getOutputStream());
        	out.flush();
        	in = new ObjectInputStream(socket.getInputStream());
        }
    	catch (SocketException e){
    		
    	}
        catch (IOException ioe){
        	System.out.println("Failed to initialize I/O streams with socket!");
        }
        
        // Process all messages from server, according to the protocol.
        while (true) {
        	String line = "";
        	Object obj;
        	try {
        		obj = in.readObject();
        		if (obj instanceof MapBrain){
        			// If we read in a MapBrain from the server at any time, we assign
        			// it to our map (update from another client), and refresh.
        			gui.setMapBrain((MapBrain) obj);
        			refreshMap();
        		}
        		else{
            		line = (String) obj;
        		}
        	}
        	catch (IOException ioe){
        		System.out.println("The server has been shut down unexpectedly! The game is now over.");
        		break;
        	} catch (ClassNotFoundException e) {
        		System.out.println("A string command was not passed to the client!");
        		break;
			}
        	catch (NullPointerException e){
        		System.out.println("Can't connect to server - may be full or unreachable!");
        		break;
        	}
            if (line.startsWith("SUBMITNAME")) {
            	name = gui.getName();
            	try {
					out.writeObject(name);
				} catch (IOException e) {
					e.printStackTrace();
				}
            } else if (line.startsWith("CHOOSECHARACTER:")){
            	String[] charStr = line.substring(16).split(",");
            	playableCharacters = new ArrayList<String>();
            	for (String character : charStr){
            		playableCharacters.add(character);
            	}
            	while (character == null || character.equals("")){
                	character = gui.openChooseCharacterDialog(playableCharacters);
            	}
            	try {
					out.writeObject(character);
				} catch (IOException e) {
					e.printStackTrace();
				}
            	dwellingChits = gui.getMapBrain().findDwellings();
            	player = new Player(name, character, dwellingChits);
            	setCharacterActionListeners();
            	placeCharacter(gui.getStartLocation(player.getCharacter().getStartLocations()));
            	refreshMap();
            } else if (line.startsWith("GAMECANSTART")){
            	gui.startGameButton.setEnabled(true);
            } else if (line.startsWith("GAMECAN'TSTART")){
            	gui.startGameButton.setEnabled(false);
            } else if (line.startsWith("GAMESTART")) {
            	gui.playerInfoArea.append("\nThe game is now starting! Please wait for your turn...");
            	gui.startGameButton.setEnabled(false);
            } else if (line.startsWith("ROUNDSTART:")){
            	if (line.contains(player.getName())){
            		mediaPlayer.play();
            		gui.playerInfoArea.setText("");
                	gui.playerInfoArea.append("Day " + day + ": It's your turn...");
                	day++;
                	playerSpecialAbility = false;
                	horseSpecialAbility = false;
                	// Special conditions for player turns
                	if (player.getCharacter().getName().equals("Captain")){
                		for (Chit chit : gui.getMapBrain().findDwellings()){
                			if (chit.getLetter().equals(player.getCharacter().getClearing())){
                				turns = 4;
                			}
                		}
                		if (turns != 4){
                			turns = 3;
                		}
					}
                	else if (player.getCharacter().getName().equals("Dwarf")){
                		turns = 2;
                	}
					else{
	                	turns = 3;
					}
                	/*
                	 * If a player starts in a cave, they automatically only get two phases for the day
                	 * (This applies to all characters, so it's checked after special conditions are
                	 * applied) - Players also never start in caves, so avoid checking on game start ;)
                	 */
                	if (gui.getMapBrain().getCurrentTile() != null){
                    	for(Chit chit : gui.getMapBrain().getTile(player.getCharacter().getClearing()).getChits()){
    						if(chit instanceof MapChit && ((MapChit)chit).getLetter().equals("C")){
    							turns = 2;
    						}
    					}
                	}
                	ArrayList<String> playerStats = player.getCharacter().getStats();
                	playerStats.add("Turns");
                	ArrayList<Integer> statVals = player.getCharacter().getStatVals();
                	statVals.add(turns);
                	gui.updateStats(playerStats, statVals);
                	gui.enableButtons();
                	player.getCharacter().setHidden(false);
            	}
            } else if (line.startsWith("MESSAGE")){
            } else if (line.startsWith("SENDMAP:")){
            	if (line.contains(name)){
            		try {
						out.writeObject(gui.getMapBrain());
						out.reset();
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
            }
        }
    }

	/**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
        MagicRealmClient client = new MagicRealmClient();
        client.run();
    }
}
