package View;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;

public class GUI extends JFrame{

	private ImageIcon image1;
	private JLabel label1 = new JLabel();
	//private JTextArea textarea;
	private JScrollPane jsp = new JScrollPane();
	
	
	public GUI(){
		//setLayout(new FlowLayout());
		
		//private ScrollablePicture picture;
		//image1 = new ImageIcon(getClass().getResource("theBoard.png"));
		
		label1.setIcon(new ImageIcon(getClass().getResource("theBoard.png")));
		//jsp = new JScrollPane(label1);
		jsp.setViewportView(label1);
		getContentPane().add(jsp, BorderLayout.CENTER);
		setSize(600, 600);
		setVisible(true);
		
		validate();
	}
	
	
	public static void main(String[] args){
		GUI testGUI = new GUI();
		testGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//testGUI.setVisible(true);
		//testGUI.pack();
		//testGUI.setSize(600, 900);
		//testGUI.setTitle("Map Test");
	}
}