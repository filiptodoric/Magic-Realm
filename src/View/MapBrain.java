package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MapBrain extends MouseAdapter{

	JLabel label;
	Rectangle[] hotspot;
	
	public MapBrain(JLabel label){
		this.label = label;
	}
	
	private void initHotSpots() {
		hotspot = new Rectangle[50];
		
		/* CLIFF HOTSPOTS */
		hotspot[1]  = new Rectangle(560, 355, 100, 80); // CLIFF C1 
		hotspot[2]  = new Rectangle(690, 355, 100, 80); // CLIFF C2
		hotspot[3]  = new Rectangle(625, 240, 100, 80); // CLIFF C3
 		hotspot[4]  = new Rectangle(560, 120, 100, 80); // CLIFF C4
 		hotspot[5]  = new Rectangle(700, 125, 100, 80); // CLIFF C5
 		hotspot[6]  = new Rectangle(500, 245, 100, 80); // CLIFF C6
 		
 		/* EVIL VALLEY HOTSPOTS */
 		hotspot[7]  = new Rectangle(350, 500, 100, 80); // EV C1 
		hotspot[8]  = new Rectangle(475, 495, 100, 80); // EV C2
		hotspot[9]  = new Rectangle(530, 680, 100, 80); // EV C4
 		hotspot[10] = new Rectangle(350, 720, 100, 80); // EV C5
		
	}
	
	public void mousePressed(MouseEvent e){
		if (hotspot == null)
			initHotSpots();
		Point p = e.getPoint();
		if (hotspot[1].contains(p)){
			System.out.println("CLIFF - Clearing 1");
		} else if (hotspot[2].contains(p)){
			System.out.println("CLIFF - Clearing 2");
		} else if (hotspot[3].contains(p)){
			System.out.println("CLIFF - Clearing 3");
		} else if (hotspot[4].contains(p)){
			System.out.println("CLIFF - Clearing 4");
		} else if (hotspot[5].contains(p)){
			System.out.println("CLIFF - Clearing 5");
		} else if (hotspot[6].contains(p)){
			System.out.println("CLIFF - Clearing 6");
		} else if (hotspot[7].contains(p)){
			System.out.println("EVIL VALLEY - Clearing 1");
		}else if (hotspot[8].contains(p)){
			System.out.println("EVIL VALLEY - Clearing 2");
		}else if (hotspot[9].contains(p)){
			System.out.println("EVIL VALLEY - Clearing 4");
		}else if (hotspot[10].contains(p)){
			System.out.println("EVIL VALLEY - Clearing 5");
		}
	}
}
