package ListsAndLogic;

import java.util.HashSet;
import java.util.Iterator;

import ObjectClasses.Chit;
import ObjectClasses.MapChit;

public class BoxOfMapChits {
	private HashSet<MapChit> warningChits;
	private HashSet<MapChit> siteChits;
	private HashSet<MapChit> soundChits;
	
	public BoxOfMapChits(){
		warningChits = new HashSet<MapChit>();
		for (String ltr : new String[]{"V", "W", "C", "M"}){
			warningChits.add(new MapChit("BONES", ltr, MapChit.Type.WARNING));
			warningChits.add(new MapChit("DANK", ltr, MapChit.Type.WARNING));
			warningChits.add(new MapChit("RUINS", ltr, MapChit.Type.WARNING));
			warningChits.add(new MapChit("SMOKE", ltr, MapChit.Type.WARNING));
			warningChits.add(new MapChit("STINK", ltr, MapChit.Type.WARNING));
		}
		System.out.println(warningChits.toString());
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
	
	
}
