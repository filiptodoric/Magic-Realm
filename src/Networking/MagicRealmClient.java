package Networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import ListsAndLogic.ListOfSecretRoutes;
import ListsAndLogic.ListOfSecretRoutes;
import ObjectClasses.Chit;
import ObjectClasses.Clearing;
import ObjectClasses.HexTile;
import ObjectClasses.MapChit;
import ObjectClasses.Player;
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
    /**A variable for checking the previous use of a character's special ability (move, phase, etc).*/
    boolean playerSpecialAbility;
    /**A variable for checking the enabling of cheat mode.*/
    boolean cheatMode;
    /**The client's list of available characters to display for the player's selection.*/
    ArrayList<String> playableCharacters;
    /** A quick reference for the client to have dwelling chits on hand.*/
    ArrayList<Chit> dwellingChits;
    
    public MagicRealmClient() {
    	gui = new MagicRealmGUI();
    	secretRoutes = new ListOfSecretRoutes();
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
    	    
    	    HexTile currentTile = gui.mapBrain.getCurrentTile();
    	    
    	    String warningChitLetter = currentTile.getWarningChit().getLetter();
    	    
    	    if (currentTile.hasSoundChit()){
    		    chitName = currentTile.getSoundChit().getName();
    	    }else{
    		    chitName = currentTile.getWarningChit().getName();
    	    }
    	   
    	    switch(dice){
    			case 1:
    				
    			case 2:
    				
    			case 3:
    				
    			case 4:
    			
    			case 5:
    				
    			case 6:	
    	    }
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
				// TODO Place function here.
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
			}
		});
		
		gui.restButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO Place function here.
			}
		});
		
		gui.searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				turns--;
				int searchChoice = gui.getSearchType();
				String temp = "";
				int dice = rollDice();
				summonMonster(dice);
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
						int[] foundTreasure = getPlayerClearing().plunderTreasure();
						player.getCharacter().gainGold(foundTreasure[0]);
						player.getCharacter().gainNotority(foundTreasure[1]);
						player.getCharacter().gainFame(foundTreasure[2]);
						 gui.playerInfoArea.append("\nYou found " + foundTreasure[0] + " gold, gained " +
						foundTreasure[1] + " notority, and gained " + foundTreasure[2] + " fame! You now have " + 
									player.getCharacter().getGold() + " gold, " + player.getCharacter().getNotority() +
									" notority, and " + player.getCharacter().getFame() + " fame.");
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
				if (turns == 0){
					gui.disableButtons();
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
					gui.playerInfoArea.append("\nMoved to " + gui.getMapBrain().getCurrentClearing().getName());
					placeCharacter(gui.getMapBrain().getCurrentClearing().getName());
					if (player.getCharacter().getName().equals("Amazon") && playerSpecialAbility == false){
						playerSpecialAbility = true;
					}
					else{
						turns--;
					}
					if (turns == 0){
						gui.disableButtons();
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
				if ((Math.random()*6)+1 > 5.0){
					gui.playerInfoArea.append("\nCouldn't hide!");
				}
				else{
					player.getCharacter().setHidden(true);
					gui.playerInfoArea.append("\nManaged to hide!");
				}
				if (turns == 0){
					gui.disableButtons();
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
            		try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            		gui.playerInfoArea.setText("");
                	gui.playerInfoArea.append("Day " + day + ": It's your turn...");
                	day++;
                	playerSpecialAbility = false;
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
