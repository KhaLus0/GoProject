package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class DrawNode extends JComponent
{
	public int x,y,width,height;
	
	public DrawNode(int x,int y,int width, int height)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.GREEN);
		g.drawOval(x, y, width, height);
	}
}
