package ObjectClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class HexTile implements Serializable{
	private ArrayList<Clearing> clearings;
	private ArrayList<MapChit> chits;
	private String name;
	public HexTile(String inpName){
		name = inpName;
		clearings = new ArrayList<Clearing>();
		chits = new ArrayList<MapChit>();
	}
	
	public void addClearing(Clearing input){
		clearings.add(input);
	}
	
	public ArrayList<Clearing> getClearings(){
		return clearings;
	}
	
	public ArrayList<MapChit> getChits(){
		return chits;
	}
	
	public MapChit getWarningChit(){
		for (MapChit chit : chits){
			if(chit.getType().equals(MapChit.Type.WARNING)){
				return chit;
			}
		}
		return null;
	}
	
	public MapChit getSiteSoundChit(){
		for (MapChit chit : chits){
			if(chit.getType().equals(MapChit.Type.SOUND) || chit.getType().equals(MapChit.Type.SITE)){
				return chit;
			}
		}
		return null;
	}

	public MapChit getSiteChit(){
		for (MapChit chit : chits){
			if(chit.getType().equals(MapChit.Type.SITE)){
				return chit;
			}
		}
		return null;
	}
	
	public int getSiteSoundNumber()	{
		MapChit soundChit = getSiteSoundChit();
		int soundChitNumber = Integer.parseInt(soundChit.getLetter());
		return soundChitNumber;
	}
	
	public String getName(){
		return name;
	}
	
	public void addChit(MapChit chit){
		chits.add(chit);
	}
	
	
/**************************************************************************************************
* FUNCTION: hasSoundChit()
**************************************************************************************************/
		public boolean hasSiteSoundChit(){
			if (getSiteSoundChit() == null){
				return false;
			}
			else
				return true;
		}

		public void setSiteSoundChit(MapChit chit) {
			Iterator<MapChit> iter = chits.iterator();
			while(iter.hasNext()){
				MapChit curChit = iter.next();
				if(curChit.getType().equals(MapChit.Type.SOUND) || curChit.getType().equals(MapChit.Type.SITE)){
					iter.remove();
				}
			}
			chits.add(chit);
		}

		public void setWarningChit(MapChit mapChit) {
			Iterator<MapChit> iter = chits.iterator();
			while(iter.hasNext()){
				MapChit curChit = iter.next();
				if(curChit.getType().equals(MapChit.Type.WARNING)){
					iter.remove();
				}
			}
			chits.add(mapChit);
		}
		
	}

