package View;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import ListsAndLogic.BoxOfMapChits;
import ObjectClasses.Chit;
import ObjectClasses.Clearing;
import ObjectClasses.Denizen;
import ObjectClasses.HexTile;
import ObjectClasses.MapChit;
import ObjectClasses.Native;

public class MapBrain extends MouseAdapter{

	JLayeredPane label;
	ArrayList<HexTile> tiles;
	int counter;
	String hexName;
	String inpAdjTiles;
	// Demo variable!
	Clearing currentClearing;
	BoxOfMapChits mapChits;
	
	public MapBrain(JLayeredPane mapImage){
		this.label = mapImage;
		tiles = new ArrayList<HexTile>();
		mapChits = new BoxOfMapChits();
		counter = 0;
		initClearings();	
	}
	
	public ArrayList<HexTile> getTiles(){
		return tiles;
	}
	
	private void initClearings() {
		/* CLIFF HOTSPOTS */
		HexTile tile = new HexTile("CLIFF");
		tile.addChit(mapChits.getRandomWarningChit("M"));
		tile.addChit(mapChits.getRandomSiteSoundChit(tile));
		tile.addClearing(new Clearing(new Rectangle(560, 355, 100, 80), "CLIFF C1", "CLIFF C6,EV C2"));
		tile.addClearing(new Clearing(new Rectangle(560, 355, 100, 80), "CLIFF C1", "CLIFF C6,EV C2"));
		tile.addClearing(new Clearing(new Rectangle(690, 355, 100, 80), "CLIFF C2", "LEDGES C3,CLIFF C3,CLIFF C5"));
		tile.addClearing(new Clearing(new Rectangle(625, 240, 100, 80), "CLIFF C3", "CLIFF C5,CLIFF C2,CLIFF C6"));
		tile.addClearing(new Clearing(new Rectangle(560, 120, 100, 80), "CLIFF C4", "CLIFF C6"));
		tile.addClearing(new Clearing(new Rectangle(700, 125, 100, 80), "CLIFF C5", "CLIFF C3,CLIFF C2"));
		tile.addClearing(new Clearing(new Rectangle(500, 245, 100, 80), "CLIFF C6", "CLIFF C1,CLIFF C4,CLIFF C3"));
		tiles.add(tile);
 		
 		/* EVIL VALLEY HOTSPOTS */
		tile = new HexTile("EV");
		tile.addChit(mapChits.getRandomWarningChit("V"));
		tile.addClearing(new Clearing(new Rectangle(350, 500, 100, 80), "EV C1", "EV C4"));
		tile.addClearing(new Clearing(new Rectangle(475, 495, 100, 80), "EV C2", "CLIFF C1,EV C5"));
		tile.addClearing(new Clearing(new Rectangle(530, 680, 100, 80), "EV C4", "EV C1,LEDGES C2,BORDERLAND C2"));
		tile.addClearing(new Clearing(new Rectangle(350, 720, 100, 80), "EV C5", "EV C2,HP C6"));
		exchangeValleyChits(tile);
		tiles.add(tile);
		
		/* LEDGES HOTSPOTS */
		tile = new HexTile("LEDGES");
		tile.addChit(mapChits.getRandomWarningChit("M"));
		tile.addChit(mapChits.getRandomSiteSoundChit(tile));
		tile.addClearing(new Clearing(new Rectangle(867,522,100,80), "LEDGES C1", "LEDGES C6,LEDGES C4,LEDGES C3"));
		tile.addClearing(new Clearing(new Rectangle(707,611,100,80), "LEDGES C2", "LEDGES C5,EV C4"));
		tile.addClearing(new Clearing(new Rectangle(753,507,100,80), "LEDGES C3", "LEDGES C6,CLIFF C2,LEDGES C1"));
		tile.addClearing(new Clearing(new Rectangle(831,626,100,80), "LEDGES C4", "LEDGES C1,BORDERLAND C4,LEDGES C6"));
		tile.addClearing(new Clearing(new Rectangle(871,742,100,80), "LEDGES C5", "LEDGES C2,OW C2"));
		tile.addClearing(new Clearing(new Rectangle(970,536,100,80), "LEDGES C6", "LEDGES C1,LEDGES C3,LEDGES C4"));
		tiles.add(tile);
		
		/* HIGH PASS HOTSPOTS */
		tile = new HexTile("HP");
		tile.addChit(mapChits.getRandomWarningChit("C"));
		tile.addChit(mapChits.getRandomSiteSoundChit(tile));
		tile.addClearing(new Clearing(new Rectangle(156,1081,100,80), "HP C1", "HP C5,HP C4"));
		tile.addClearing(new Clearing(new Rectangle(324,988,100,80), "HP C2", "HP C4,BORDERLAND C1"));
		tile.addClearing(new Clearing(new Rectangle(259,1095,100,80), "HP C3", "HP C6,CAVERN C5"));
		tile.addClearing(new Clearing(new Rectangle(178,960,100,80), "HP C4", "HP C1,HP C2"));
		tile.addClearing(new Clearing(new Rectangle(65,994,100,80), "HP C5", "HP C1"));
		tile.addClearing(new Clearing(new Rectangle(262,875,100,80), "HP C6", "EV C5,HP C3"));
		tiles.add(tile);
		
		/* BORDERLAND HOTSPOTS */
		tile = new HexTile("BORDERLAND");
		tile.addChit(mapChits.getRandomWarningChit("C"));
		tile.addChit(mapChits.getRandomSiteSoundChit(tile));
		tile.addClearing(new Clearing(new Rectangle(484,1073,100,80), "BORDERLAND C1", "HP C2,BV C5,BORDERLAND C6"));
		tile.addClearing(new Clearing(new Rectangle(611,856,100,80), "BORDERLAND C2", "EV C4,OW C2,BORDERLAND C3"));
		tile.addClearing(new Clearing(new Rectangle(503,926,100,80), "BORDERLAND C3", "BORDERLAND C2,BORDERLAND C5,BORDERLAND C6"));
		tile.addClearing(new Clearing(new Rectangle(770,1070,100,80), "BORDERLAND C4", "BORDERLAND C6,BORDERLAND C5,LEDGES C4"));
		tile.addClearing(new Clearing(new Rectangle(678,1055,100,80), "BORDERLAND C5", "BORDERLAND C3,CAVERN C2"));
		tile.addClearing(new Clearing(new Rectangle(588,1019,100,80), "BORDERLAND C6", "BORDERLAND C1,BORDERLAND C3,BORDERLAND C4"));
		tiles.add(tile);
		
		/* OAK WOODS HOTSPOTS */
		tile = new HexTile("OW");
		tile.addChit(mapChits.getRandomWarningChit("W"));
		tile.addClearing(new Clearing(new Rectangle(951,945,100,80), "OW C2", "BORDERLAND C2,LEDGES C5, OW C4"));
		tile.addClearing(new Clearing(new Rectangle(1182,991,100,80), "OW C4", "OW C2,DW C1"));
		tile.addClearing(new Clearing(new Rectangle(1052,1108,100,80), "OW C5", "MW C5, BV C1"));
		tiles.add(tile);
		
		/* CRAG CLEARINGS */
		tile = new HexTile("CRAG");
		tile.addChit(mapChits.getRandomWarningChit("M"));
		tile.addChit(mapChits.getRandomSiteSoundChit(tile));
		tile.addClearing(new Clearing(new Rectangle(1208,477,100,80), "CRAG C1","CRAG C6,CRAG C4"));
		tile.addClearing(new Clearing(new Rectangle(1330,731,100,80), "CRAG C2","CRAG C3,CRAG C5,DW C1"));
		tile.addClearing(new Clearing(new Rectangle(1352,615,100,80), "CRAG C3","CRAG C6,CRAG C5,CRAG C2"));
		tile.addClearing(new Clearing(new Rectangle(1166,592,100,80), "CRAG C4","CRAG C1,CRAG C6"));
		tile.addClearing(new Clearing(new Rectangle(1222,688,100,80), "CRAG C5","CRAG C3,CRAG C2"));
		tile.addClearing(new Clearing(new Rectangle(1306,520,100,80), "CRAG C6","CRAG C1,CRAG C4,CRAG C3"));
		tiles.add(tile);
		
		/* CAVERN CLEARINGS */
		tile = new HexTile("CAVERN");
		tile.addChit(mapChits.getRandomWarningChit("C"));
		tile.addChit(mapChits.getRandomSiteSoundChit(tile));
		tile.addClearing(new Clearing(new Rectangle(549,1359,100,80), "CAVERN C1","BV C4,CAVERN C3,CAVERN C4"));
		tile.addClearing(new Clearing(new Rectangle(474,1245,100,80), "CAVERN C2","BORDERLAND C4,CAVERN C3"));
		tile.addClearing(new Clearing(new Rectangle(420,1332,100,80), "CAVERN C3","CAVERN C5,CAVERN C6,CAVERN C1,CAVERN C2"));
		tile.addClearing(new Clearing(new Rectangle(339,1492,100,80), "CAVERN C4","CAVERN C5,CAVERN C6,CAVERN C1"));
		tile.addClearing(new Clearing(new Rectangle(341,1250,100,80), "CAVERN C5","HP C3,CAVERN C4,CAVERN C3"));
		tile.addClearing(new Clearing(new Rectangle(420,1434,100,80), "CAVERN C6","CAVERN C4,CAVERN C3"));
		tiles.add(tile);
		
		/* MOUNTAIN CLEARINGS */
		tile = new HexTile("MOUNTAIN");
		tile.addChit(mapChits.getRandomWarningChit("M"));
		tile.addChit(mapChits.getRandomSiteSoundChit(tile));
		tile.addClearing(new Clearing(new Rectangle(578,1815,100,80), "MOUNTAIN C1","MOUNTAIN C3"));
		tile.addClearing(new Clearing(new Rectangle(697,1864,100,80), "MOUNTAIN C2","MOUNTAIN C4,MOUNTAIN C5,PW C4"));
		tile.addClearing(new Clearing(new Rectangle(694,1748,100,80), "MOUNTAIN C3","MOUNTAIN C1,MOUNTAIN C6"));
		tile.addClearing(new Clearing(new Rectangle(477,1735,100,80), "MOUNTAIN C4","MOUNTAIN C2,MOUNTAIN C6"));
		tile.addClearing(new Clearing(new Rectangle(695,1613,100,80), "MOUNTAIN C5","MOUNTAIN C2,MOUNTAIN C6,BV C4"));
		tile.addClearing(new Clearing(new Rectangle(568,1642,100,80), "MOUNTAIN C6","MOUNTAIN C4,MOUNTAIN C5,MOUNTAIN C3"));
		tiles.add(tile);
		
		/* CAVES CLEARINGS */
		tile = new HexTile("CAVES");
		tile.addChit(mapChits.getRandomWarningChit("C"));
		tile.addChit(mapChits.getRandomSiteSoundChit(tile));
		tile.addClearing(new Clearing(new Rectangle(980,1852,100,80), "CAVES C1","PW C5,CAVES C6"));
		tile.addClearing(new Clearing(new Rectangle(984,1623,100,80), "CAVES C2","BV C2,CAVES C3,CAVES C4"));
		tile.addClearing(new Clearing(new Rectangle(1046,1718,100,80), "CAVES C3","CAVES C2,CAVES C5"));
		tile.addClearing(new Clearing(new Rectangle(916,1767,100,80), "CAVES C4","CAVES C6,CAVES C2"));
		tile.addClearing(new Clearing(new Rectangle(1110,1622,100,80), "CAVES C5","CAVES C3,MW C4"));
		tile.addClearing(new Clearing(new Rectangle(1179,1799,100,80), "CAVES C6","CAVES C4,CAVES C1"));
		tiles.add(tile);
		
		/* RUINS CLEARINGS */
		tile = new HexTile("RUINS");
		tile.addChit(mapChits.getRandomWarningChit("C"));
		tile.addChit(mapChits.getRandomSiteSoundChit(tile));
		tile.addClearing(new Clearing(new Rectangle(1545,1645,100,80), "RUINS C1","NW C4,RUINS C4,RUINS C2,RUINS C5"));
		tile.addClearing(new Clearing(new Rectangle(1606,1809,100,80), "RUINS C2","RUINS C1,AV C1,LW C4"));
		tile.addClearing(new Clearing(new Rectangle(1408,1858,100,80), "RUINS C3","RUINS C6,RUINS C5"));
		tile.addClearing(new Clearing(new Rectangle(1435,1724,100,80), "RUINS C4","RUINS C1,RUINS C6"));
		tile.addClearing(new Clearing(new Rectangle(1365,1633,100,80), "RUINS C5","RUINS C1,MW C2,RUINS C3"));
		tile.addClearing(new Clearing(new Rectangle(1510,1815,100,80), "RUINS C6","RUINS C4,RUINS C3"));
		tiles.add(tile);
		
		/* DEEP WOODS CLEARINGS */
		tile = new HexTile("DW");
		tile.addChit(mapChits.getRandomWarningChit("M"));
		tile.addChit(mapChits.getRandomSiteSoundChit(tile));
		tile.addClearing(new Clearing(new Rectangle(1349,939,100,80), "DW C1","CRAG C2,OW C4,DW C4,DW C6"));
		tile.addClearing(new Clearing(new Rectangle(1593,927,100,80), "DW C2","DV C5,CV C2,DW C3"));
		tile.addClearing(new Clearing(new Rectangle(1593,1082,100,80), "DW C3","DW C2,DW C6,DW C5"));
		tile.addClearing(new Clearing(new Rectangle(1344,1075,100,80), "DW C4","DW C5,DW C6,DW C1"));
		tile.addClearing(new Clearing(new Rectangle(1446,1135,100,80), "DW C5","MW C5,DW C3,DW C4"));
		tile.addClearing(new Clearing(new Rectangle(1487,1017,100,80), "DW C6","DW C1,DW C3,DW C4"));
		tiles.add(tile);
		
		/* PINE WOODS CLEARINGS */
		tile = new HexTile("PW");
		tile.addChit(mapChits.getRandomWarningChit("W"));
		tile.addClearing(new Clearing(new Rectangle(713,2154,100,80), "PW C2","PW C4"));
		tile.addClearing(new Clearing(new Rectangle(757,2003,100,80), "PW C4","PW C2,MOUNTAIN C2"));
		tile.addClearing(new Clearing(new Rectangle(931,2049,100,80), "PW C5","CAVES C1"));
		tiles.add(tile);
		
		/* LINDEN WOODS CLEARINGS */
		tile = new HexTile("LW");
		tile.addChit(mapChits.getRandomWarningChit("W"));
		tile.addClearing(new Clearing(new Rectangle(1670,2162,100,80), "LW C2","LW C4"));
		tile.addClearing(new Clearing(new Rectangle(1625,2000,100,80), "LW C4","RUINS C2,LW C2"));
		tile.addClearing(new Clearing(new Rectangle(1782,2053,100,80), "LW C5","AV C2"));
		tiles.add(tile);
		
		/* AWFUL VALLEY CLEARINGS */
		tile = new HexTile("AV");
		tile.addChit(mapChits.getRandomWarningChit("V"));
		tile.addClearing(new Clearing(new Rectangle(1773,1742,100,80), "AV C1","RUINS C2,AV C4"));
		tile.addClearing(new Clearing(new Rectangle(1835,1848,100,80), "AV C2","LW C5,AV C5"));
		tile.addClearing(new Clearing(new Rectangle(1977,1741,100,80), "AV C4","AV C1"));
		tile.addClearing(new Clearing(new Rectangle(1841,1627,100,80), "AV C5","AV C2,NW C2"));
		exchangeValleyChits(tile);
		tiles.add(tile);
		
		/* NUT WOODS CLEARINGS */
		tile = new HexTile("NW");
		tile.addChit(mapChits.getRandomWarningChit("W"));
		tile.addClearing(new Clearing(new Rectangle(1802,1424,100,80), "NW C2","AV C5,NW C4"));
		tile.addClearing(new Clearing(new Rectangle(1644,1480,100,80), "NW C4","NW C2,RUINS C1"));
		tile.addClearing(new Clearing(new Rectangle(1666,1300,100,80), "NW C5","MW C2,CV C4"));
		tiles.add(tile);
		
		/* CURST VALLEY CLEARINGS */
		tile = new HexTile("CV");
		tile.addChit(mapChits.getRandomWarningChit("V"));
		tile.addClearing(new Clearing(new Rectangle(1842,878,100,80), "CV C1","DV C1,CV C4"));
		tile.addClearing(new Clearing(new Rectangle(1775,990,100,80), "CV C2","CV C5,DW C2"));
		tile.addClearing(new Clearing(new Rectangle(1940,1053,100,80), "CV C4","NW C5,CV C1"));
		tile.addClearing(new Clearing(new Rectangle(1973,881,100,80), "CV C5","CV C2"));
		exchangeValleyChits(tile);
		tiles.add(tile);
		
		/* DARK VALLEY CLEARINGS */
		tile = new HexTile("DV");
		tile.addChit(mapChits.getRandomWarningChit("V"));
		tile.addClearing(new Clearing(new Rectangle(1759,737,100,80), "DV C1","DV C4,CV C1"));
		tile.addClearing(new Clearing(new Rectangle(1824,624,100,80), "DV C2","DV C5"));
		tile.addClearing(new Clearing(new Rectangle(1659,557,100,80), "DV C4","DV C1"));
		tile.addClearing(new Clearing(new Rectangle(1630,730,100,80), "DV C5","DV C2,DW C2"));
		exchangeValleyChits(tile);
		tiles.add(tile);
		
		/* MAPLE WOODS CLEARINGS */
		tile = new HexTile("MW");
		tile.addChit(mapChits.getRandomWarningChit("W"));
		tile.addClearing(new Clearing(new Rectangle(1366,1431,100,80), "MW C2","MW C4,NW C5,RUINS C5"));
		tile.addClearing(new Clearing(new Rectangle(1209,1480,100,80), "MW C4","MW C2,CAVES C5"));
		tile.addClearing(new Clearing(new Rectangle(1266,1253,100,80), "MW C5","OW C5,DW C5"));
		tiles.add(tile);
		
		/* BAD VALLEY CLEARINGS */
		tile = new HexTile("BV");
		tile.addChit(mapChits.getRandomWarningChit("V"));
		tile.addClearing(new Clearing(new Rectangle(902,1247,100,80), "BV C1","OW C5,BV C4"));
		tile.addClearing(new Clearing(new Rectangle(904,1476,100,80), "BV C2","CAVES C2,BV C5"));
		tile.addClearing(new Clearing(new Rectangle(725,1431,100,80), "BV C4","CAVERN C1,MOUNTAIN C5,BV C1"));
		tile.addClearing(new Clearing(new Rectangle(773,1255,100,80), "BV C5","BORDERLAND C1,BV C2"));
		exchangeValleyChits(tile);
		tiles.add(tile);
	}
	
	public void exchangeValleyChits(HexTile tile){
		MapChit warning = tile.getWarningChit();
		if (warning.getLetter().equals("V")){
			for (Clearing clearing : tile.getClearings()){
				if (clearing.getName().contains("C5")){
					if (warning.getName().equals("SMOKE")){
						clearing.addChit(new Chit("House", clearing.getName()));
						clearing.addChit(new Native("Soldier 1", "H", true, 6, 4, "S"));
						clearing.addChit(new Native("Soldier 2", "H", true, 6, 4, "S"));
						clearing.addChit(new Native("Soldier 3", "H", false, 6, 5, "S"));
						clearing.addChit(new Native("Soldier HQ", "T", true, 4, 6, "S"));
					}
					else if (warning.getName().equals("BONES")){
						clearing.addChit(new Denizen("Ghost", "H", false, 4, 4));
						clearing.addChit(new Denizen("Ghost", "H", false, 4, 4));
					}
					else if (warning.getName().equals("RUINS")){
						clearing.addChit(new Chit("Guard", clearing.getName()));
						clearing.addChit(new Native("Guard 1", "H", true, 5, 5, "G"));
						clearing.addChit(new Native("Guard 2", "H", true, 5, 5, "G"));
						clearing.addChit(new Native("Guard HQ", "H", true, 5, 5, "G"));
					}
					else if (warning.getName().equals("STINK")){
						clearing.addChit(new Chit("Inn", clearing.getName()));
						clearing.addChit(new Native("Rogue 1", "H", true, 5, 4, "R"));
						clearing.addChit(new Native("Rogue 2", "H", true, 5, 4, "R"));
						clearing.addChit(new Native("Rogue 3", "M", true, 5, 3, "R"));
						clearing.addChit(new Native("Rogue 4", null, true, 0, 2, "R"));
						clearing.addChit(new Native("Rogue 5", "M", true, 3, 4, "R"));
						clearing.addChit(new Native("Rogue 6", "M", true, 3, 5, "R"));
						clearing.addChit(new Native("Rogue 7", "M", true, 3, 5, "R"));
						clearing.addChit(new Native("Rogue HQ", "H", true, 6, 4, "R"));
					}
					else if (warning.getName().equals("DANK")){
						clearing.addChit(new Chit("Chapel", clearing.getName()));
						clearing.addChit(new Native("Order 1", "H", true, 4, 6, "G"));
						clearing.addChit(new Native("Order 2", "H", true, 5, 5, "G"));
						clearing.addChit(new Native("Order 3", "H", true, 4, 6, "G"));
						clearing.addChit(new Native("Order HQ", "H", true, 6, 4, "G"));
					}
				}
			}
		}
	}
	
	public void generateHexTileCode(Point p){
		if (counter > 0 && counter < 7){
			inpAdjTiles = JOptionPane.showInputDialog("Input connected tiles:");
			System.out.println("tile.addClearing(new Clearing(new Rectangle("+
					(int)(p.getX()-50) + "," + (int)(p.getY()-40) + ",100,80), \"" + hexName + " " +
					"C" + counter + "\",\"" + inpAdjTiles + "\"));");
			counter++;
		}
		else if (counter == 0){
			System.out.println("Creating new HexTile");
			hexName = JOptionPane.showInputDialog("Please input tile name:");
			System.out.println("/* " + hexName + " CLEARINGS */");
			System.out.println("tile = new HexTile(\"" + hexName + "\");");
			counter++;
		}
		else if (counter == 7){
			System.out.println("tiles.add(tile);");
			counter = 0;
		}
	}
	
	public void mousePressed(MouseEvent e){
		Point p = e.getPoint();
		for (HexTile tile : tiles){
			for (Clearing clearing : tile.getClearings()){
				if (clearing.getArea().contains(p)){
					currentClearing = clearing;
					System.out.println("Clearing " + clearing.getName() + " selected");
				}
			}
		}
	}
	
	public ArrayList<Chit> findDwellings(){
		ArrayList<Chit> dwellings = new ArrayList<Chit>();
		for (HexTile tile : tiles){
			for (Clearing clearing : tile.getClearings()){
				for (Chit chit : clearing.getChits()){
					if (chit.getName().equals("Inn") || chit.getName().equals("House") || chit.getName().equals("Guard") ||
							chit.getName().equals("Chapel")){
						dwellings.add(chit);
					}
				}
			}
		}
		return dwellings;
	}
	
	public Clearing getCurrentClearing(){
		return currentClearing;
		
	}

	public void clearCurrentClearing() {
		currentClearing = null;
	}



}
