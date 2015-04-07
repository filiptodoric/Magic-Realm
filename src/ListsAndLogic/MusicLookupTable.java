package ListsAndLogic;

import java.util.HashMap;

public class MusicLookupTable {
	public HashMap<String, String> table = new HashMap<String, String>();
	
	public MusicLookupTable(){
		table.put("Wolf", "dogGrowling.mp3");
		table.put("Viper", "snakeHiss.mp3");
	}
}
