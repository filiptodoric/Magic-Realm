package View;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import ObjectClasses.Clearing;

public class MapBrain extends MouseAdapter{

	JLabel label;
	ArrayList<Clearing> clearings;
	// Demo variable!
	Clearing currentClearing;
	
	public MapBrain(JLabel label){
		this.label = label;
		clearings = new ArrayList<Clearing>();
		initHotSpots();
	}
	
	private void initHotSpots() {
		/* CLIFF HOTSPOTS */
		clearings.add(new Clearing(new Rectangle(560, 355, 100, 80), "CLIFF C1", "CLIFF C6,EV C2"));
		clearings.add(new Clearing(new Rectangle(690, 355, 100, 80), "CLIFF C2", "LEDGES C3,CLIFF C3"));
		clearings.add(new Clearing(new Rectangle(625, 240, 100, 80), "CLIFF C3", "CLIFF C5,CLIFF C2,CLIFF C6"));
		clearings.add(new Clearing(new Rectangle(560, 120, 100, 80), "CLIFF C4", "CLIFF C6"));
		clearings.add(new Clearing(new Rectangle(700, 125, 100, 80), "CLIFF C5", "CLIFF C3"));
		clearings.add(new Clearing(new Rectangle(500, 245, 100, 80), "CLIFF C6", "CLIFF C1,CLIFF C4"));
 		
 		/* EVIL VALLEY HOTSPOTS */
		clearings.add(new Clearing(new Rectangle(350, 500, 100, 80), "EV C1", "EV C4"));
		clearings.add(new Clearing(new Rectangle(475, 495, 100, 80), "EV C2", "CLIFF C1,EV C5"));
		clearings.add(new Clearing(new Rectangle(530, 680, 100, 80), "EV C4", "EV C1,LEDGES C2,BORDERLAND C2"));
		clearings.add(new Clearing(new Rectangle(350, 720, 100, 80), "EV C5", "EV C2,HP C6"));
		
	}
	
	public void mousePressed(MouseEvent e){
		Point p = e.getPoint();
		for (Clearing clearing : clearings){
			if (clearing.getArea().contains(p)){
				if (e.getButton() == MouseEvent.BUTTON3){
					currentClearing = clearing;
					System.out.println(clearing.getName() + " set as current clearing");
				}
				else{
					if (currentClearing != null){
						if (clearing.isAdjacentTo(currentClearing.getName())){
							System.out.println("You can move from " + currentClearing.getName() + " to " + clearing.getName() + "!");
						}
						else{
							System.out.println("You CAN'T move from " + currentClearing.getName() + " to " + clearing.getName() + "!");
						}
					}
					else{
						System.out.println(clearing.getName());
					}
				}
				break;
			}
		}
	}
}
