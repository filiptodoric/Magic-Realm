package View;

import javax.swing.*;
//import java.awt.*;

public class MagicRealmGUI {
		
	private JFrame window;
	
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
		
		// JFrame
		window = new JFrame("Magic Realm Window");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1200, 800);
		
		// JDesktop Pane: desktopPane1
		JDesktopPane desktopPane1 = new JDesktopPane();
		
		// JInternalFrame: internalFrame1
		JInternalFrame internalFrame1 = new JInternalFrame("Magic Realm Map");
		internalFrame1.setSize(700, 750);
		internalFrame1.setMaximizable(true);
		internalFrame1.setIconifiable(true);
		internalFrame1.setResizable(true);
		internalFrame1.setVisible(true);
		desktopPane1.add(internalFrame1);
		window.add(desktopPane1);
		
		// JLabel: map
		JLabel map = new JLabel();
		map.setIcon(new ImageIcon(getClass().getResource("board.png")));
		
		// JScrollPane - holds map
		JScrollPane mapScrollPane = new JScrollPane();
		mapScrollPane.setViewportView(map);
		internalFrame1.add(mapScrollPane);
		
		window.setVisible(true);
	}
}






























































































































































































