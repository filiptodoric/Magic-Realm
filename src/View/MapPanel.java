package View;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class MapPanel extends JPanel
{
  public static BufferedImage image;
 
  public MapPanel()
  {
    super();
    try
    {               
      image = ImageIO.read(new File("board.png"));
    }
    catch (IOException e)
    {
    	
    }
  }

  public void paintComponent(Graphics g)
  {
    g.drawImage(image, 0, 0, null);
    repaint();
  }
}
