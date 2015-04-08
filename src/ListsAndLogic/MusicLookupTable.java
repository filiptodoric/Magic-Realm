package ListsAndLogic;

import java.util.HashMap;

public class MusicLookupTable {
	public HashMap<String, String> table = new HashMap<String, String>();
	
	public MusicLookupTable(){
		table.put("Wolf", "music/dogGrowling.mp3");
		table.put("Viper", "music/snakeHiss.mp3");
		table.put("battleSuccess", "music/battleSuccess.mp3");
		table.put("mainTheme", "music/mainTheme.mp3");
		table.put("combatTheme", "music/combatTheme.mp3");
	}
}
