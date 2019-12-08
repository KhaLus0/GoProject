package Client;

import java.awt.*;

import javax.swing.JComponent;

public class PaintPawn extends JComponent{
	
	Color color = null;
	int x,y,width,height;
	
	public void setSize(int x,int y,int width, int height)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public void setType(String type)
	{
		if(type=="Black")
		{
			color = new Color(255,255,255);
		}
		if(type=="White")
		{
			color = new Color(0,0,0);
		}
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(color);
		g.fillOval(x, y, width, height);
	}
	
}
