package ListsAndLogic;

import java.util.HashSet;

public class ListOfSecretRoutes {
	private static HashSet<String> hiddenPaths;
	private static HashSet<String> hiddenPassages;
	
	public ListOfSecretRoutes(){
		hiddenPaths = new HashSet<String>();
		hiddenPassages = new HashSet<String>();
		hiddenPaths.add("RUINS C5,RUINS C1");
		hiddenPaths.add("DW C6,DW C3");
		hiddenPaths.add("DW C1,DW C4");
		hiddenPassages.add("CRAG C1,CRAG C6");
		hiddenPaths.add("CRAG C3,CRAG C2");
		hiddenPaths.add("MOUNTAIN C4,MOUNTAIN C6");
		hiddenPassages.add("CLIFF C6,CLIFF C3");
		hiddenPaths.add("CLIFF C2,CLIFF C5");
		hiddenPaths.add("LEDGES C1,LEDGES C3");
		hiddenPaths.add("LEDGES C6,LEDGES C4");
		hiddenPassages.add("CAVERN C1,CAVERN C4");
		hiddenPassages.add("CAVES C2,CAVES C3");
		hiddenPassages.add("CAVERN C3,CAVERN C5");
		hiddenPassages.add("BORDERLAND C4,BORDERLAND C5");
	}
	
	public boolean isSecret(String str1, String str2){
		HashSet<String> allRoutes = new HashSet<String>();
		allRoutes.addAll(hiddenPassages);
		allRoutes.addAll(hiddenPaths);
		for (String path : allRoutes){
			if (path.contains(str1) && path.contains(str2)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isSecretPath(String str1, String str2){
		for (String path : hiddenPaths){
			if (path.contains(str1) && path.contains(str2)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isSecretPassage(String str1, String str2){
		for (String path : hiddenPassages){
			if (path.contains(str1) && path.contains(str2)){
				return true;
			}
		}
		return false;
	}
}
