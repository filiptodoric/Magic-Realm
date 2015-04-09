package ListsAndLogic;

import java.io.Serializable;
import java.util.ArrayList;

import ObjectClasses.Horse;

public class StableOfHorses implements Serializable{
	private ArrayList<Horse> horses;
	
	public StableOfHorses(){
		horses = new ArrayList<Horse>();
		horses.add(new Horse("Warhorse", "T", 7, 0, 25, 4));
		horses.add(new Horse("Warhorse", "T", 7, 0, 22, 5));
		horses.add(new Horse("Warhorse", "T", 5, 0, 20, 3));
		horses.add(new Horse("Warhorse", "H", 6, 0, 18, 4));
		
		horses.add(new Horse("Workhorse", "T", 7, 0, 12, 4));
		horses.add(new Horse("Workhorse", "H", 7, 0, 11, 5));
		horses.add(new Horse("Workhorse", "H", 5, 0, 11, 3));
		horses.add(new Horse("Workhorse", "M", 6, 0, 10, 4));
		horses.add(new Horse("Workhorse", "M", 5, 0, 9, 3));
		horses.add(new Horse("Workhorse", "L", 5, 0, 8, 4));
		
		horses.add(new Horse("Pony", "L", 4, 0, 16, 4));
		horses.add(new Horse("Pony", "M", 5, 0, 16, 5));
		horses.add(new Horse("Pony", "M", 4, 0, 15, 3));
		horses.add(new Horse("Pony", "M", 5, 0, 14, 4));
		horses.add(new Horse("Pony", "M", 4, 0, 14, 3));
		horses.add(new Horse("Pony", "M", 5, 0, 12, 4));
		horses.add(new Horse("Pony", "M", 5, 0, 12, 4));
	}
	
	public ArrayList<Horse> getDwellingHorses(){
		// 17/4 = 4 horses each (and we'll leave one in the stable for diversity)
		ArrayList<Horse> returnHorses = new ArrayList<Horse>();
		for(int i = 0; i < 4; i++){
			int index = (int)Math.random()*horses.size();
			if (index == horses.size()){
				index = index - 1;
			}
			returnHorses.add(horses.get(index));
			horses.remove(index);
		}
		return returnHorses;
	}
}
