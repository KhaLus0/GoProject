package Client;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class DrawLine extends JComponent
{
	int x1,x2,y1,y2;
	
	public void setSize(int x1,int y1,int x2,int y2)
	{
		this.x1=x1;
		this.y1=y2;
		this.x2=x2;
		this.y2=y2;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawLine(x1, y1, x2, y2);
	}
}
