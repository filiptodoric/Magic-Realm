package View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import ObjectClasses.ActionChit;
import ObjectClasses.TurnsTableModel;
import ObjectClasses.Chit;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;

public class MagicRealmGUI {
		
	public static JFrame      window;
	JDesktopPane 	        desktopPane;
	public JButton	        startGameButton;
	public JButton 	        tradeButton;
	public JButton 	        restButton;
	public JButton 	        searchButton;
	public JButton 	        moveButton;
	public JButton 	        hideButton;
	public JButton 	        showCardButton;
	public JButton 	        showCheatButton;
	public JButton 	        setupVRButton;
	public JTable             turnsTable;
	public JTextArea          playerInfoArea;
	public JLayeredPane       map;
	public MapBrain           mapBrain;
	public static ImageLookup lookup;
	private JScrollPane       turnsTableScrollPane;
	
	
/****************************************************************************************
* CONSTRUCTOR
****************************************************************************************/
	public MagicRealmGUI() {
		
		System.out.println("-- In MagicRealmGUI constructor.");
		lookup = new ImageLookup();
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
	public JLayeredPane getMap(){
		map      = new JLayeredPane();
		mapBrain = new MapBrain(map);
		map.addMouseListener(mapBrain);
		map.setLayout(new BorderLayout());
		MouseMotionAdapter scroll = new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
            	map.scrollRectToVisible(new Rectangle(
            			e.getXOnScreen(), 
            			e.getYOnScreen(),
            			(int)map.getVisibleRect().getHeight(),
            			(int)map.getVisibleRect().getWidth()));
            }
        };
        map.addMouseMotionListener(scroll);
		return map;
	}
	
	public void addImage(String imageName, int x, int y, int width, int height, 
			int layer, boolean opaque){
		ImageIcon imageIcon = 
				new ImageIcon(getClass().getResource(lookup.getValue(imageName)));
		JLabel label = new JLabel(imageIcon);
		label.setBounds(x,y,width,height);
		label.setOpaque(opaque);
		label.setName(imageName);
		map.add(label);
	}
	
	public void removeImage(String imageName){
		for (Component component : map.getComponents()){
			if (component.getName().equals(imageName)){
				map.remove(component);
			}
		}
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
		JScrollPane mapScrollPane = new JScrollPane(getMap());
		mapScrollPane.setName("Map");
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
	
	public void refreshMapInternalFrame() {
		for (Component component : desktopPane.getComponents()){
			if (component.getName() != null && component.getName().equals("Magic Realm Map")){
				desktopPane.remove(component);
				JScrollPane mapScrollPane = new JScrollPane(getMap());
				JInternalFrame mapInternalFrame = new JInternalFrame("Magic Realm Map");
				mapInternalFrame.setSize(700, 725);
				mapInternalFrame.setMaximizable(true);
				mapInternalFrame.setIconifiable(true);
				mapInternalFrame.setResizable  (true);
				mapInternalFrame.setVisible    (true);
				mapInternalFrame.add(mapScrollPane);
				desktopPane.add(mapInternalFrame);
			}
		}
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
		
		// "Start Game" Button
		startGameButton = new JButton("Start Game");
		constraints.gridx = 0;
		constraints.gridy = 1;
		startGameButton.setToolTipText("Click to start game.");
		startGameButton.setEnabled(false);
		startGameButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO Place function here.
			}
		});
		playersInternalFrame.add(startGameButton, constraints);
		
		
		
		desktopPane.add(playersInternalFrame);
	}
	
	
	
	
	
/****************************************************************************************
* FUNCTION: openChooseCharDialog()
* CONTEXT:  - Pressing "New Character" button calls this function.
****************************************************************************************/
	public String openChooseCharacterDialog(ArrayList<String> characters) {
		
		System.out.println("-- In newCharButtonClicked()");
		
		String this_character = (String)JOptionPane.showInputDialog(
		                    	window,
		                    	"Please choose your character\n",
		                    	"Choose Character Dialog",
		                    	JOptionPane.PLAIN_MESSAGE,
		                    	null,
		                    	characters.toArray(),
		                    	"Amazon");

		if ((this_character != null) && (characters.contains(this_character))) {
		    System.out.println("You chose " +this_character+ " as your character!");
		    buildCharInternalFrame(this_character);
		    characters.remove(this_character);
		    return this_character;
		}
		System.out.println("-- Did not choose a character.");
		return null;
	}
	
	public String getServerAddress() {
        return JOptionPane.showInputDialog(
            desktopPane,
            "Enter IP Address of the Server:",
            "Magic Realm v0.1",
            JOptionPane.QUESTION_MESSAGE);
    }
	
	public String getName() {
        return JOptionPane.showInputDialog(
            desktopPane,
            "Enter your name:",
            "Magic Realm v0.1",
            JOptionPane.QUESTION_MESSAGE);
    }
	
	
	
	
	
/****************************************************************************************
* FUNCTION: buildCharInternalFrame()
* CONTEXT:  - Called after a character is chosen.
****************************************************************************************/
	public void buildCharInternalFrame(String charName) {
		
		System.out.println("-- In buildCharInternalFrame().");
		
		JInternalFrame charInternalFrame = new JInternalFrame(charName);
		charInternalFrame.setLayout(new GridBagLayout());
		charInternalFrame.setSize(450, 500);
		charInternalFrame.setMaximizable(true);
		charInternalFrame.setIconifiable(true);
		charInternalFrame.setResizable  (true);
		charInternalFrame.setVisible    (true);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		
		JLabel charNameLabel = new JLabel(charName);
		charNameLabel.setFont(new Font("Serif", Font.BOLD, 28));
		//constraints.anchor = GridBagConstraints.BOTH;
		constraints.gridx  = 0;
		constraints.gridy  = 0;
		charInternalFrame.add(charNameLabel, constraints);
		
		// Setup Victory Requirements Button
		setupVRButton = new JButton("Setup VPs");
		constraints.weightx = 0.5;
		constraints.weighty = 8.0;
		constraints.gridx   = 4;
		constraints.gridy   = 4;
		charInternalFrame.add(setupVRButton, constraints);
		
		// Show Card Button
		showCardButton = new JButton("Show Card");
		showCardButton.setToolTipText("Show Character's Card");
		constraints.gridx = 2;
		constraints.gridy = 4;
		charInternalFrame.add(showCardButton, constraints);
		
		// Cheat Mode Button
		showCheatButton = new JButton("Cheat Mode");
		showCheatButton.setToolTipText("Enable cheat mode!");
		constraints.gridx = 0;
		constraints.gridy = 4;
		charInternalFrame.add(showCheatButton, constraints);
		
		// Player Info Area
		playerInfoArea = new JTextArea();
		playerInfoArea.setEditable(false);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.fill = constraints.BOTH;
		constraints.gridheight = 2;
		constraints.gridwidth = 5;
		JScrollPane infoScroll = new JScrollPane(playerInfoArea);
		charInternalFrame.add(infoScroll, constraints);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.fill = constraints.NONE;
		
		showCheatButton.setToolTipText("Enable cheat mode!");
		constraints.gridx = 0;
		constraints.gridy = 4;
		charInternalFrame.add(showCheatButton, constraints);
		
		// Hide Button
		hideButton = new JButton();
		constraints.gridx = 0;
		constraints.gridy = 3;
		hideButton.setToolTipText("Hide");
		hideButton.setIcon(new ImageIcon(getClass().getResource("hide.gif")));
		hideButton.setEnabled(false);
		charInternalFrame.add(hideButton, constraints);
				
				
		// Move Button
		moveButton = new JButton();
		constraints.gridx = 1;
		constraints.gridy = 3;
		moveButton.setToolTipText("Move");
		moveButton.setIcon(new ImageIcon(getClass().getResource("move.gif")));
		moveButton.setEnabled(false);
		charInternalFrame.add(moveButton, constraints);
				
		// Search Button
		searchButton = new JButton();
		constraints.gridx = 2;
		constraints.gridy = 3;
		searchButton.setToolTipText("Search");
		searchButton.setIcon(new ImageIcon(getClass().getResource("search.gif")));
		searchButton.setEnabled(false);
		charInternalFrame.add(searchButton, constraints);
				
		// Rest Button
		restButton = new JButton();
		constraints.gridx = 3;
		constraints.gridy = 3;
		restButton.setToolTipText("Rest");
		restButton.setIcon(new ImageIcon(getClass().getResource("rest.gif")));
		restButton.setEnabled(false);
		charInternalFrame.add(restButton, constraints);
				
		// Trade Button
		tradeButton = new JButton();
		constraints.gridx = 4;
		constraints.gridy = 3;
		tradeButton.setToolTipText("Trade");
		tradeButton.setIcon(new ImageIcon(getClass().getResource("trade.gif")));
		tradeButton.setEnabled(false);
		charInternalFrame.add(tradeButton, constraints);
		
		
		turnsTable = new JTable(new TurnsTableModel());
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		//turnsTable.setPreferredScrollableViewportSize(new Dimension(380, 50));
		renderer.setPreferredSize(new Dimension(380, 60));
		turnsTableScrollPane = new JScrollPane();
		turnsTableScrollPane.setViewportView(turnsTable);
		constraints.weightx = 10;
		constraints.gridwidth = 5;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		//charInternalFrame.add(turnsTableScrollPane, constraints);
		//constraints.gridx = 0;
		//constraints.gridy = 4;
		//constraints.fill = GridBagConstraints.HORIZONTAL;
		//charInternalFrame.add(turnsTableScrollPane);
		
		
		desktopPane.add(charInternalFrame);
	}
	
	public MapBrain getMapBrain(){
		return mapBrain;
	}
	
	
	
	
	
/****************************************************************************************
* FUNCTION: showCharacterCard()
* PURPOSE:  - Pops up a dialog box with the selected character's info and image. 
****************************************************************************************/
	public void showCharacterCard(String selectedCharacter) {
		switch(selectedCharacter){
			case "Amazon":
				JOptionPane.showMessageDialog(window, 
					new ImageIcon(getClass().getResource("amazon.jpg")));
				break;
			case "Black Knight":
				JOptionPane.showMessageDialog(window, 
					new ImageIcon(getClass().getResource("black_knight.jpg")));
				break;
			case "Captain":
				JOptionPane.showMessageDialog(window, 
					new ImageIcon(getClass().getResource("captain.jpg")));
				break;
			case "Dwarf":
				JOptionPane.showMessageDialog(window,
					new ImageIcon(getClass().getResource("dwarf.jpg")));
				break;
			case "Elf":
				JOptionPane.showMessageDialog(window, 
					new ImageIcon(getClass().getResource("elf.jpg")));
				break;
			case "Swordsman":
				JOptionPane.showMessageDialog(window, 
					new ImageIcon(getClass().getResource("swordsman.jpg")));
				break;
		}
	}

	public void enableButtons() {
		startGameButton.setEnabled(false);
		tradeButton.setEnabled(true);
		restButton.setEnabled(true);
		searchButton.setEnabled(true);
		moveButton.setEnabled(true);
		hideButton.setEnabled(true);
	}

	public void disableButtons() {
		startGameButton.setEnabled(false);
		tradeButton.setEnabled(false);
		restButton.setEnabled(false);
		searchButton.setEnabled(false);
		moveButton.setEnabled(false);
		hideButton.setEnabled(false);
	}

	public int getSearchType() {
		String[] buttons = {"Peer", "Locate"};
	    return JOptionPane.showOptionDialog(null, "What type of search do you want to perform?", "Search",
	        JOptionPane.DEFAULT_OPTION, 0, null, buttons, buttons[1]);
	}
	
	public String getStartLocation(ArrayList<Chit> dwellings) {
		ArrayList<String> dwellingNames = new ArrayList<String>();
		for (Chit dwelling : dwellings){
			dwellingNames.add(dwelling.getName());
		}
		String[] options = new String[dwellingNames.size()];
		for (int j = 0; j < options.length; j++){
			options[j] = dwellingNames.get(j);
		}
	    int i = JOptionPane.showOptionDialog(null, "Select your starting location...", "Starting Location",
	        JOptionPane.DEFAULT_OPTION, 0, null, options, options[0]);
	    for (Chit dwelling : dwellings){
			if (dwelling.getName().equals(options[i])){
				return dwelling.getLetter();
			}
		}
	    return null;
	}

	public void showCheatPanel() {
		// TODO Auto-generated method stub
		
	}
	
	public void setMapBrain(MapBrain newMapBrain){
		map.removeMouseListener(mapBrain);
		mapBrain = newMapBrain;
		map.addMouseListener(mapBrain);
	}

	public int getDieRoll() {
			String[] options = {"1", "2", "3", "4", "5", "6"};
		    return (JOptionPane.showOptionDialog(null, "Select the dice result!", "*CHEAT MODE* - Dice Roll",
		        JOptionPane.DEFAULT_OPTION, 0, null, options, options[0])) + 1;
	}
	
	public String getFatiguedChit(ArrayList<ActionChit> fatigued) {
		ArrayList<String> str = new ArrayList<String>();
		for (ActionChit chit : fatigued){
			str.add(chit.toString());
		}
		Object[] options = str.toArray();
		return (String) JOptionPane.showInputDialog(window, 
		        "Select a fatigued chit to rest to active status:",
		        "Rest",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        options, 
		        options[0]);
	}
	
	public String getWoundedChit(ArrayList<ActionChit> wounded) {
		ArrayList<String> str = new ArrayList<String>();
		for (ActionChit chit : wounded){
			str.add(chit.toString());
		}
		Object[] options = str.toArray();
		return (String) JOptionPane.showInputDialog(window, 
		        "Select a wounded chit to rest to fatigued status:",
		        "Rest",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        options, 
		        options[0]);
	}

	public int getWoundOrFatigue() {
		String[] options = {"Wound->Fatigue", "Fatigue->Active"};
	    return (JOptionPane.showOptionDialog(null, "Select a type of chit to rest:", "Rest",
	        JOptionPane.DEFAULT_OPTION, 0, null, options, options[0])) + 1;
	}

	public String getTradeType() {
		String[] options = {"Buy", "Sell", "Hire Native"};
		return (String) JOptionPane.showInputDialog(window, 
		        "Welcome! What 'er ya here for?",
		        "Dwelling Marketplace",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        options, 
		        options[0]);
	}





	public String getNativeToHire(Object[] objects) {
		return (String) JOptionPane.showInputDialog(window, 
		        "We've got a couple of fine warriors, who do you want?",
		        "Dwelling Marketplace",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        objects, 
		        objects[0]);
	}

	public void displayMessage(String message, String header) {
		JOptionPane.showMessageDialog(null, message, header, JOptionPane.PLAIN_MESSAGE);
	}

	public int confirmHire(String nativeToHire, int cost) {
		return JOptionPane.showConfirmDialog(null, "So " + nativeToHire + " is willing to join you, but it's gonna cost ya " + cost + " gold! You buying?",
				"Dwelling Marketplace", JOptionPane.YES_NO_OPTION);
	}
	
} /* CLOSES CLASS */






























































































































































































