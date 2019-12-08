package Client;

import java.awt.*;

import javax.swing.JComponent;

public class PaintPawn extends JComponent{
	
	Color color = null;
	int x,y,width,height;
	
	public PaintPawn(int x,int y,int width, int height,String type)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		if(type=="Black")
		{
			color = new Color(1,1,1);
		}
		if(type=="White")
		{
			color = new Color(255,255,255);
		}
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(color);
		g.fillOval(x, y, width, height);
	}
	
}
