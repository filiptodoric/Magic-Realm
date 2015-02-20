package Networking;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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
     * The set of all names of players that have selected categories for
     * the current round.
     */
    private static HashSet<String> namesSubmitted = new HashSet<String>();

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
     * The application main method, which just listens on a port and
     * spawns handler threads.
     */
    public static void main(String[] args) throws Exception {
        MagicRealmServer server = new MagicRealmServer();
        server.run();
    }

    /**
     * The thread class for the server, which is a nested class in the YahtzeeServer class. 
     * Incorporates Object I/O streams for client-server communication, and manages most of 
     * the control flow of the game.
     */
    private static class Handler extends Thread {
        private String name;
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

                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                out.writeObject("NAMEACCEPTED");
                writers.add(out);
                System.out.println("User " + name + " connected and communicating on port " + socket.getPort());
                for (ObjectOutputStream writer : writers) {
                	writer.writeObject("MESSAGE " + name + " has now joined the server");
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
                    String input = (String)objIn;
                    if (input == null) {
                        return;
                    }
                    if (input.equals("STARTGAME")){
                    	for (ObjectOutputStream writer : writers) {
                            writer.writeObject("GAMESTART");
                            gameInProgress = true;
                            day = 0;
                            day++;
                        }
                    }
                    else if (input.startsWith("MESSAGE:")){
                        for (ObjectOutputStream writer : writers) {
                            writer.writeObject("MESSAGE:" + name + ": " + input.substring(8));
                        }
                    }
                    else if (input.startsWith("CATEGORY:")){
                    	newRound();
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

		private void newRound() {
            try {
            	for (ObjectOutputStream writer : writers) {
					writer.writeObject("ROUNDSTART");
            	}
				namesSubmitted.clear();
				day++;
            } catch (IOException e) {
            	e.printStackTrace();
            }
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
