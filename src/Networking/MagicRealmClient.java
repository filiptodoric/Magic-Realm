package Networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import ListsAndLogic.ListOfSecretRoutes;
import ListsAndLogic.ListOfSecretRoutes;
import ObjectClasses.Chit;
import ObjectClasses.Clearing;
import ObjectClasses.HexTile;
import ObjectClasses.Player;
import View.MagicRealmGUI;

/**
 * The main controller for the client side of the game. Incorporates networking 
 * for the client, sends/receives messages with the server and manipulates the view.
 * Original code accessed from: http://cs.lmu.edu/~ray/notes/javanetexamples/#chat
 */
public class MagicRealmClient implements Runnable {
    ObjectInputStream in;
    ObjectOutputStream out;
    MagicRealmGUI gui;
    ListOfSecretRoutes secretRoutes;
    Player player;
    String name;
    String character;
    Socket socket;
    int turns;
    ArrayList<String> playableCharacters;
    
    public MagicRealmClient() {
    	gui = new MagicRealmGUI();
    	secretRoutes = new ListOfSecretRoutes();
    	setActionListeners();
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
    
    private void placeCharacter(){
    	for (HexTile tile : gui.getMapBrain().getTiles()){
    		for (Clearing clearing : tile.getClearings()){
    			if (clearing.getName().equals(player.getCharacter().getClearing()) &&
    					!clearing.getChits().contains(player.getCharacter())){
    				clearing.addChit(player.getCharacter());
    			}
    			else if (!clearing.getName().equals(player.getCharacter().getClearing()) &&
    					clearing.getChits().contains(player.getCharacter())){
    				clearing.removeChit(player.getCharacter());
    			}
    		}
    	}
    	refreshMap();
    }
    
    private void setCharacterActionListeners(){
		gui.tradeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO Place function here.
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
				if (searchChoice == 0){
					switch((int)(Math.random()*6+1)){
					case 1:
						System.out.println("Pick any category!");
						break;
					case 2:
						System.out.println("Clues and Paths");
						for (String adjClearing : getPlayerClearing().getAdjacentClearings()){
							if (secretRoutes.isSecretPath(getPlayerClearing().getName(), adjClearing)){
								player.getCharacter().addDiscovery(getPlayerClearing().getName() + "," + adjClearing);
								System.out.println("You found a secret route: " + getPlayerClearing().getName() + "," + adjClearing + "!");
							}
						}
						break;
					case 3:
						System.out.println("Hidden enemies and Paths");
						for (String adjClearing : getPlayerClearing().getAdjacentClearings()){
							if (secretRoutes.isSecretPath(getPlayerClearing().getName(), adjClearing)){
								player.getCharacter().addDiscovery(getPlayerClearing().getName() + "," + adjClearing);
								System.out.println("You found a secret route: " + getPlayerClearing().getName() + "," + adjClearing + "!");
							}
						}
						break;
					case 4:
						System.out.println("Hidden enemies");
						break;
					case 5:
						System.out.println("Clues");
						break;
					case 6:
						System.out.println("You didn't find anything.");
						break;
					default:
						System.out.println("You didn't find anything.");
						break;
					}
				}
				else{
					switch((int)(Math.random()*6+1)){
					case 1:
						System.out.println("Pick any category!");
						break;
					case 2:
						System.out.println("You discovered passages and clues!");
						for (String adjClearing : getPlayerClearing().getAdjacentClearings()){
							if (secretRoutes.isSecretPassage(getPlayerClearing().getName(), adjClearing)){
								player.getCharacter().addDiscovery(getPlayerClearing().getName() + "," + adjClearing);
								System.out.println("You found a secret route: " + getPlayerClearing().getName() + "," + adjClearing + "!");
							}
						}
						break;
					case 3:
						System.out.println("You discovered passages!");
						for (String adjClearing : getPlayerClearing().getAdjacentClearings()){
							if (secretRoutes.isSecretPassage(getPlayerClearing().getName(), adjClearing)){
								player.getCharacter().addDiscovery(getPlayerClearing().getName() + "," + adjClearing);
								System.out.println("You found a secret route: " + getPlayerClearing().getName() + "," + adjClearing + "!");
							}
						}
						break;
					case 4:
						System.out.println("You discovered chits (treasure)!");
						int foundGold = getPlayerClearing().plunderTreasure();
						player.getCharacter().gainGold(foundGold);
						if (foundGold > 0){
							System.out.println("You found " + foundGold + " gold! You now have " + 
						player.getCharacter().getGold() + " gold...");
						}
						break;
					case 5:
						System.out.println("You didn't find anything.");
						break;
					case 6:
						System.out.println("You didn't find anything.");
						break;
					default:
						System.out.println("You didn't find anything.");
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
					System.out.println("Day completed, waiting for others...");
				}
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
					System.out.println("Moved to " + gui.getMapBrain().getCurrentClearing().getName());
					player.getCharacter().setClearing(gui.getMapBrain().getCurrentClearing().getName());
					placeCharacter();
					turns--;
					if (turns == 0){
						gui.disableButtons();
						try {
							out.writeObject("COMPLETE");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						System.out.println("Day completed, waiting for others...");
					}
				}
				else{
					System.out.println("Can't travel there!");
				}
			}
		});
		
		gui.hideButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				turns--;
				if (((int)Math.random()*6)+1 > 5){
					System.out.println("Couldn't hide!");
				}
				else{
					player.getCharacter().setHidden(true);
					System.out.println("Managed to hide!");
				}
				if (turns == 0){
					gui.disableButtons();
					try {
						out.writeObject("COMPLETE");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println("Day completed, waiting for others...");
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
        	try {
        		line = (String) in.readObject();
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
            	player = new Player(name, character);
            	name = null;
            	setCharacterActionListeners();
            	placeCharacter();
            	refreshMap();
            } else if (line.startsWith("INVALIDNAME")){
            } else if (line.startsWith("NAMEACCEPTED")){
            } else if (line.startsWith("GAMECANSTART")){
            	gui.startGameButton.setEnabled(true);
            } else if (line.startsWith("GAMECAN'TSTART")){
            	gui.startGameButton.setEnabled(false);
            } else if (line.startsWith("GAMESTART")) {
            	System.out.println("The game is now starting!");
            } else if (line.startsWith("ROUNDSTART:")){
            	if (line.contains(player.getName())){
                	System.out.println("It's your turn...");
                	turns = 3;
                	gui.enableButtons();
                	player.getCharacter().setHidden(false);
            	}
            	else{
            		System.out.println("Waiting...");
            	}
            	
            } else if (line.startsWith("MESSAGE")){
            } else if (line.startsWith("Score:")){
            } else if (line.startsWith("Players Active:")){
            } else if (line.startsWith("DENIED")){
            } else if (line.startsWith("LOCKCATEGORY:")){
            } else if (line.startsWith("GAMEOVER")){
            } else if (line.startsWith("WINNER:")){
            } else if (line.startsWith("REMOVE:")){
            	
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
