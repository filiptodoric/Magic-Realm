package ListsAndLogic;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import ObjectClasses.Chit;
import ObjectClasses.HexTile;
import ObjectClasses.MapChit;

public class BoxOfMapChits implements Serializable{
	private HashSet<MapChit> warningChits;
	private HashSet<MapChit> siteSoundChits;
	boolean lostCastlePlaced = false;
	boolean lostCityPlaced = false;
	
	public BoxOfMapChits(){
		warningChits = new HashSet<MapChit>();
		siteSoundChits = new HashSet<MapChit>();
		for (String ltr : new String[]{"V", "W", "C", "M"}){
			warningChits.add(new MapChit("BONES", ltr, MapChit.Type.WARNING));
			warningChits.add(new MapChit("DANK", ltr, MapChit.Type.WARNING));
			warningChits.add(new MapChit("RUINS", ltr, MapChit.Type.WARNING));
			warningChits.add(new MapChit("SMOKE", ltr, MapChit.Type.WARNING));
			warningChits.add(new MapChit("STINK", ltr, MapChit.Type.WARNING));
		}
		siteSoundChits.add(new MapChit("HOWL", "4", MapChit.Type.SOUND));
		siteSoundChits.add(new MapChit("FLUTTER", "1", MapChit.Type.SOUND));
		siteSoundChits.add(new MapChit("ROAR", "6", MapChit.Type.SOUND));
		siteSoundChits.add(new MapChit("PATTER", "2", MapChit.Type.SOUND));
		siteSoundChits.add(new MapChit("SLITHER", "3", MapChit.Type.SOUND));
		siteSoundChits.add(new MapChit("HOWL", "4", MapChit.Type.SOUND));
		siteSoundChits.add(new MapChit("FLUTTER", "1", MapChit.Type.SOUND));
		siteSoundChits.add(new MapChit("ROAR", "6", MapChit.Type.SOUND));
		siteSoundChits.add(new MapChit("PATTER", "2", MapChit.Type.SOUND));
		siteSoundChits.add(new MapChit("SLITHER", "3", MapChit.Type.SOUND));
		siteSoundChits.add(new MapChit("STATUE", "2", MapChit.Type.SITE));
		siteSoundChits.add(new MapChit("ALTAR", "1", MapChit.Type.SITE));
		siteSoundChits.add(new MapChit("VAULT", "3", MapChit.Type.SITE));
		siteSoundChits.add(new MapChit("POOL", "6", MapChit.Type.SITE));
		siteSoundChits.add(new MapChit("LOST CITY", "3", MapChit.Type.SITE));
		siteSoundChits.add(new MapChit("LOST CASTLE", "1", MapChit.Type.SITE));
		siteSoundChits.add(new MapChit("HOARD", "6", MapChit.Type.SITE));
		siteSoundChits.add(new MapChit("LAIR", "3", MapChit.Type.SITE));
		siteSoundChits.add(new MapChit("CAIRNS", "5", MapChit.Type.SITE));
		siteSoundChits.add(new MapChit("SHRINE", "4", MapChit.Type.SITE));
	}
	
	public MapChit getRandomWarningChit(String ltr){
		MapChit tempChit = null;
		while (tempChit == null){
			Iterator<MapChit> iter = warningChits.iterator();
			while(iter.hasNext()){
				MapChit chit = iter.next();
				if (chit.getLetter().equals(ltr) && Math.random() <= 0.25){
					tempChit = chit;
					iter.remove();
					break;
				}
			}
		}
		return tempChit;
	}
	
	
/**************************************************************************************************
* FUNCTION: getRandomSoundWarningChit                                                       Apr. 09
* @return MapChit
**************************************************************************************************/
	public MapChit getRandomSoundWarningChit(){
			
		MapChit randomChit = null;
		Iterator<MapChit> iter;
		Random randGen = new Random();
			
		while (randomChit == null){
			if (randGen.nextInt(100) < 49){
				iter = warningChits.iterator();
			} else {
				iter = siteSoundChits.iterator();
			}
			while (iter.hasNext()){
				MapChit chit = iter.next();
				if (Math.random() <= 0.25 && !chit.getType().equals(MapChit.Type.SITE)){
					randomChit = chit;
					iter.remove();
					break;
				}
			}
		}
		return randomChit;
	}
	
	
	public MapChit getRandomSiteSoundChit(HexTile tile){
		MapChit tempChit = null;
		if ((tile.getWarningChit().getLetter().equals("C") ||
				tile.getWarningChit().getLetter().equals("M"))){
			while (tempChit == null){
				Iterator<MapChit> iter = siteSoundChits.iterator();
				while(iter.hasNext()){
					MapChit chit = iter.next();
					int i = 1;
					// Yet, make sure Lost City and Lost Castle end up on a clearing, so we don't have to hack it ;)
					if ((tile.getWarningChit().getLetter().equals("C") && chit.getName().equals("LOST CITY")) ||
							(tile.getWarningChit().getLetter().equals("M") && chit.getName().equals("LOST CASTLE"))){
						if(chit.getName().equals("LOST CITY")){
							lostCityPlaced = true;
						}
						if(chit.getName().equals("LOST CASTLE")){
							lostCastlePlaced = true;
						}
						tempChit = chit;
						System.out.println(chit.getName() + " placed!");
						iter.remove();
						break;
					}
					else if (Math.random() <= 0.25 && (lostCityPlaced || !tile.getWarningChit().getLetter().equals("C")) && (lostCastlePlaced || !tile.getWarningChit().getLetter().equals("M"))){
						if (!chit.getName().equals("LOST CASTLE") && !chit.getName().equals("LOST CITY")){
							tempChit = chit;
							iter.remove();
							break;
						}
					}
				}
			}
			return tempChit;
		}
		else{
			return null;
		}
	}
}
