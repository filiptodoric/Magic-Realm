package ControlFlow;

import View.MagicRealmGUI;

// template doesn't actually do anything

public class backendSetUp implements Runnable {
	
	MagicRealmGUI gui;
	
	public backendSetUp()	{
		gui = new MagicRealmGUI();
	}

	public void createCharacter(String character)	{
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		backendSetUp controller = new backendSetUp();
		controller.run();
	}
}
