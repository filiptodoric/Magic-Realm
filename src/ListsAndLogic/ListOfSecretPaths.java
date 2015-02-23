package ListsAndLogic;

import java.util.HashSet;

public class ListOfSecretPaths {
	private static HashSet<String> hiddenPaths;
	
	public ListOfSecretPaths(){
		hiddenPaths = new HashSet<String>();
		hiddenPaths.add("RUINS C5,RUINS C1");
		hiddenPaths.add("DW C6,DW C3");
		hiddenPaths.add("DW C1,DW C4");
		hiddenPaths.add("CRAG C1,CRAG C2");
		hiddenPaths.add("CRAG C1,CRAG C2");
		hiddenPaths.add("MOUNTAIN C4,MOUNTAIN C6");
		hiddenPaths.add("CLIFF C6,CLIFF C3");
		hiddenPaths.add("CLIFF C2,CLIFF C5");
		hiddenPaths.add("LEDGES C1,LEDGES C3");
		hiddenPaths.add("LEDGES C6,LEDGES C4");
	}
	
	public boolean isSecretPath(String str1, String str2){
		for (String path : hiddenPaths){
			if (path.contains(str1) && path.contains(str2)){
				return true;
			}
		}
		return false;
	}
}
