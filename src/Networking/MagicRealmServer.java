package Networking;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import View.MapBrain;

/**
 * The main controller for the server side of the game. Spawns Handler objects 
 * (aka threads) for each new client that connects, and also does the processing 
 * for determining a winner at the end of each game. The server also holds a couple 
 * of client-independent variables to keep one central copy of some important 
 * resources. 
 */
public class MagicRealmServer implements Runnable{

    /**
     * The set of all names of players on the server.  Maintained
     * so that we can check that new clients are not registering name
     * already in use.
     */
    private static HashSet<String> names = new HashSet<String>();
    
    /**
     * The set of all names of players on the server.  Maintained
     * so that we can check that new clients are not registering name
     * already in use.
     */
    private static MapBrain centralMap;

    /**
     * The set of all the print writers for all the clients.  This
     * set is kept so we can easily broadcast messages.
     */
    private static HashSet<ObjectOutputStream> writers = new HashSet<ObjectOutputStream>();
    
    /**
     * A variable for tracking game status; restricts players from
     * joining active games.
     */
    private static boolean gameInProgress = false;
    
    /**
     * The counter for the current day in the Magic Realm.
     */
    private static int day;
    
    /**
     * The boolean to check to see if the current player has finished their turn.
     */
    private static boolean playerFinished;
    
    /**
     * The server's copy of playable characters.
     */
    static ArrayList<String> playableCharacters;
    
    /**
     * A tracker to see if the map has been recently synced.
     */
    private static boolean isSynced;

    /**
     * The application main method, which just listens on a port and
     * spawns handler threads.
     */
    public static void main(String[] args) throws Exception {
        MagicRealmServer server = new MagicRealmServer();
		playableCharacters = new ArrayList<String>();
		playableCharacters.add("Amazon");
		playableCharacters.add("Swordsman");
		playableCharacters.add("Captain");
		playableCharacters.add("Dwarf");
		playableCharacters.add("Elf");
		playableCharacters.add("Black Knight");
        server.run();
    }

    /**
     * The thread class for the server, which is a nested class in the YahtzeeServer class. 
     * Incorporates Object I/O streams for client-server communication, and manages most of 
     * the control flow of the game.
     */
    private static class Handler extends Thread {
        private String name;
        private String ownerName;
        private String charName;
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Services this thread's client by repeatedly requesting a
         * screen name until a unique one has been submitted, then
         * acknowledges the name and registers the output stream for
         * the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        public void run() {
            try {
            	if (names.size() >= config.MAX_PLAYERS){
            		shutdown("Server currently has max amount of players!");
            	}
            	else if (gameInProgress){
            		shutdown("Cannot join a game in progress!");
            	}
                // Create character streams for the socket.
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                writers.add(out);
                in = new ObjectInputStream(socket.getInputStream());

                // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                    out.writeObject("SUBMITNAME");
                    name = (String) in.readObject();
                    synchronized (names) {
                        if (!names.contains(name) && name.length() != 0) {
                            names.add(name);
                            break;
                        }
                        else{
                        	out.writeObject("INVALIDNAME");
                        }
                    }
                }

                // When the first player joins the game, they set the map for all players,
                // and subsequent players receive the map from the server...
                if (centralMap == null){
                	out.writeObject("SENDMAP:"+name);
                    Object objIn = in.readObject();
                    if (objIn != null){
                    	centralMap = (MapBrain) objIn;
                    }
                }
                else{
                	out.writeObject(centralMap);
                	out.reset();
                }


                while (true) {
                    out.writeObject("CHOOSECHARACTER:" + getAvailableCharacterString());
                    charName = (String) in.readObject();
                    synchronized (names) {
                        if (playableCharacters.contains(charName)) {
                            playableCharacters.remove(charName);
                            break;
                        }
                        else{
                        	out.writeObject("INVALIDNAME");
                        }
                    }
                }
                System.out.println("User " + name + " connected and communicating on port " + socket.getPort());
                for (ObjectOutputStream writer : writers) {
                	writer.writeObject("MESSAGE " + name + " has now joined the server, playing as " + charName);
                	writer.writeObject("Players Active: " + names.toString());
                }
                if (names.size() >= config.MIN_PLAYERS){
                	System.out.println("Game can now be initiated!");
                    for (ObjectOutputStream writer : writers) {
                        writer.writeObject("GAMECANSTART");
                    }
                }
                // Main loop for processing commands sent from the client!
                while (true) {
                	Object objIn = in.readObject();
                	if (objIn instanceof MapBrain){
                		syncMap(objIn);
                	}
                	else{
                        String input = (String)objIn;
                        if (input.equals("STARTGAME")){
                        	for (ObjectOutputStream writer : writers) {
                                writer.writeObject("GAMESTART");
                                gameInProgress = true;
                                day = 1;
                                newRound();
                            }
                        }
                        else if (input.startsWith("MESSAGE:")){
                            for (ObjectOutputStream writer : writers) {
                                writer.writeObject("MESSAGE:" + name + ": " + input.substring(8));
                            }
                        }
                        else if (input.startsWith("COMPLETE")){
                            playerFinished = true;
                        }
                	}
            	}
            } catch (IOException e) {
                System.out.println(e);
            } catch (ClassNotFoundException e) {
				e.printStackTrace();
            } catch (Exception e) {
            	e.printStackTrace();
            	e.getMessage();
			} finally {
                shutdown("Player disconnected.");
            }
        }

		private String getAvailableCharacterString() {
			String temp = "";
			for (String character : playableCharacters){
				temp += character + ",";
			}
			System.out.println(playableCharacters);
			return temp.substring(0, temp.length()-1);
		}

		private void newRound() throws IOException, ClassNotFoundException, InterruptedException{
			for (String currName : names) {
            	for (ObjectOutputStream writer : writers) {
            		// This uses the player name, not the character name
					writer.writeObject("ROUNDSTART:"+currName);
            	}
            	if (currName.equals(name)){
            		Object objIn = in.readObject();
            	}
            	else{
            		playerFinished = false;
            		while(!playerFinished){
            			Thread.sleep(1000);
            		}
            	}
            	isSynced = false;
            	for (ObjectOutputStream writer : writers) {
					writer.writeObject("SENDMAP:"+currName);
            	}
            	if (currName.equals(name)){
            		Object objIn = in.readObject();
            		syncMap(objIn);
            	}
            	else{
                	while(!isSynced){
            			Thread.sleep(1000);
            		}
            	}
            	System.out.println(currName + " has completed their round!");
			}
			if (day != 28){
				day++;
				newRound();
			}
		}

		private void syncMap(Object objIn) throws IOException {
    		centralMap = (MapBrain) objIn;
        	for (ObjectOutputStream writer : writers) {
        		if (centralMap != null){
    				writer.writeObject(centralMap);
    				writer.reset();
        		}
        	}
        	isSynced = true;
		}

		private void handleGameOver(){
			// Insert game over actions (from the server) here!
		}
		
        public void shutdown(String reason){
        	// This client is going down!  Remove its name and its print
            // writer from the sets, and close its socket.
        	try {
	            if (name != null) {
	                names.remove(name);
	            }
	            if (out != null) {
	                writers.remove(out);
	            }
	            playableCharacters.add(charName);
                for (ObjectOutputStream writer : writers) {
                	if (name != null){
                        writer.writeObject("REMOVE:"+name);
                        writer.writeObject("Players Active: " + names.toString());
                	}
                }
                if ((names.size() < config.MIN_PLAYERS) && !gameInProgress){
                	System.out.println("Minimum number of players are no longer on the server!");
                    for (ObjectOutputStream writer : writers) {
                        writer.writeObject("GAMECAN'TSTART");
                    }
                }
            	if (names.size() == 0){
            		gameInProgress = false;
            		day = 0;
            		centralMap = null;
            	}
	            if (reason != null && reason != ""){
	            	System.out.println("Client shutting down: " + reason);
	            }
	            	socket.close();
	        	}
        	catch (IOException ie){
        		
        	}
        }
    }

	public void run() {
		System.out.println("Magic Realm host server is now running on port #" + config.PORT);
        ServerSocket listener = null;
		try {
			listener = new ServerSocket(config.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
            while (true) {
            	if (!gameInProgress && (names.size() < config.MAX_PLAYERS)){
                	new Handler(listener.accept()).start();
            	}
            }
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
				listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
}
