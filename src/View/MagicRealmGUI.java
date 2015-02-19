package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MagicRealmGUI {
		
	private JFrame       window;
	private JDesktopPane desktopPane;
	private JButton      startGameButton;
	
/****************************************************************************************
* CONSTRUCTOR
****************************************************************************************/
	public MagicRealmGUI() {
		
		System.out.println("-- In MagicRealmGUI constructor.");
		buildWindow();
	}

	
	
	
	
/****************************************************************************************
* MAIN FUNCTION
****************************************************************************************/
	public static void main(String[] args) {
		
		System.out.println("-- In main().");
		@SuppressWarnings("unused")
		MagicRealmGUI magicRealmGui = new MagicRealmGUI();
	}
	
	
	
	
	
/****************************************************************************************
* FUNCTION: buildWindow()
* PURPOSE:  - Instantiates and arranges JComponents on the JFrame.
****************************************************************************************/
	public void buildWindow() {
		
		System.out.println("-- In buildWindow().");
		
		window = new JFrame("Magic Realm Window");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1200, 800);
		desktopPane = new JDesktopPane();
		
		buildMenuBar();
		buildPlayersInternalFrame();
		buildMapInternalFrame();
		
		window.add(desktopPane);
		window.setVisible(true);
	}

	
	
	
	
/****************************************************************************************
* FUNCTION: getMap() 
* PURPOSE:  - Grabs the map image (board.png) and puts it in a JLabel.
* @return:  - JLabel
* CONTEXT:  - buildWindow() calls this function. 
****************************************************************************************/
	public JLabel getMapLabel(){
		
		ImageIcon mapImageIcon = new ImageIcon(getClass().getResource("board.png"));
		JLabel mapImageLabel   = new JLabel(mapImageIcon);
		mapImageLabel.addMouseListener(new MapBrain(mapImageLabel));
		return mapImageLabel;
	}

	
	
	
	
/****************************************************************************************
* FUNCTION: buildMenuBar()
* PURPOSE:  - Adds a menu bar to the window (JFrame).
****************************************************************************************/
	public void buildMenuBar() {
		
		JMenuBar menuBar  = new JMenuBar();
		JMenu fileMenu    = new JMenu("File");
		JMenu networkMenu = new JMenu("Network");
		JMenu helpMenu    = new JMenu("Help");
		menuBar.add(fileMenu);
		menuBar.add(networkMenu);
		menuBar.add(helpMenu);
		window.setJMenuBar(menuBar);
	}
	
	
	
	
	
/****************************************************************************************
* FUNCTION: buildMapInternalFrame()
****************************************************************************************/
	public void buildMapInternalFrame() {
		
		// mapScrollPane - holds the mapImageLabel 
		JScrollPane mapScrollPane = new JScrollPane();
		mapScrollPane.setViewportView(getMapLabel());
		
		// mapInternalFrame - holds the mapScrollPane
		JInternalFrame mapInternalFrame = new JInternalFrame("Magic Realm Map");
		mapInternalFrame.setSize(700, 725);
		mapInternalFrame.setMaximizable(true);
		mapInternalFrame.setIconifiable(true);
		mapInternalFrame.setResizable  (true);
		mapInternalFrame.setVisible    (true);
		mapInternalFrame.add(mapScrollPane);
		desktopPane.add(mapInternalFrame);
	}
	
	
	
	
	
/****************************************************************************************
* FUNCTION: buildPlayersInternalFrame()
* PURPOSE:  - Builds the playersInternalFrame along with the components inside.
****************************************************************************************/
	public void buildPlayersInternalFrame() {
		
		JInternalFrame playersInternalFrame = new JInternalFrame("Players Frame");
		playersInternalFrame.setSize(450, 200);
		playersInternalFrame.setLayout(new GridBagLayout());
		playersInternalFrame.setMaximizable(true);
		playersInternalFrame.setIconifiable(true);
		playersInternalFrame.setResizable  (true);
		playersInternalFrame.setVisible    (true);
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		/* GridBag Column 1 */
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill   = GridBagConstraints.BOTH;
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		
		// "New Character" Button
		JButton newCharButton = new JButton("New Character");
		newCharButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				openChooseCharacterDialog();
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 0;
		playersInternalFrame.add(newCharButton, constraints);
		
		// "Start Game" Button
		startGameButton = new JButton("Start Game");
		startGameButton.setEnabled(false);
		constraints.gridx = 0;
		constraints.gridy = 1;
		playersInternalFrame.add(startGameButton, constraints);
			
		desktopPane.add(playersInternalFrame);
	}
	
	
	
	
	
/****************************************************************************************
* FUNCTION: openChooseCharDialog()
* CONTEXT:  - Pressing "New Character" button calls this function.
****************************************************************************************/
	public void openChooseCharacterDialog() {
		
		System.out.println("-- In newCharButtonClicked()");
		
		Object[] characters = { "Amazon", "Black Knight", "Captain", 
				            "Dwarf",  "Elf", "Swordsman" };
		
		String this_character = (String)JOptionPane.showInputDialog(
		                    	window,
		                    	"Please choose your character\n",
		                    	"Choose Character Dialog",
		                    	JOptionPane.PLAIN_MESSAGE,
		                    	null,
		                    	characters,
		                    	"Amazon");

		if ((this_character != null) && (this_character.length() > 0)) {
		    System.out.println("You chose " +this_character+ " as your character!");
		    startGameButton.setEnabled(true);
		    System.out.println("-- Start Game Button enabled.");
		    return;
		}
		System.out.println("-- Did not choose a character.");
	}
	
}






























































































































































































