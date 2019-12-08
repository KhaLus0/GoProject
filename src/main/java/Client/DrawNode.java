package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class DrawNode extends JComponent
{
	public int x,y,width,height,i,j;
	
	public DrawNode(int x,int y,int width, int height,final int i,int j)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.i=i;
		this.j=j;
	}
	
	public String getI()
	{
		String a ="cos";
		return a;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
	}
}
