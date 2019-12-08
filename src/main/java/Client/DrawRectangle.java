package Client;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class DrawRectangle extends JComponent
{
	int x,y,width,height;
	
	public DrawRectangle(int x,int y,int width, int height)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
	}
}
