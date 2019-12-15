package Client;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class DrawChooseOutline  extends JComponent
{
	Color color = null;
	int x,y,width,height;
	
	public DrawChooseOutline(int x,int y,int width, int height,String type)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		if(type=="Green")
		{
			color = new Color(0,255,0);
		}
		
		if(type=="Red")
		{
			color = new Color(255,0,0);
		}
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(color);
		g.fillOval(x, y, width, height);
	}
}

