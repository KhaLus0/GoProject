package GoGame;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.lang.String;

public class ClientGUI
{
	static JFrame mainFrame = new JFrame();
	static JPanel mainFramePanel = new JPanel();
	static JDialog chooseSize = new JDialog(mainFrame, "Please choose size of the board");
	static JPanel chooseSizePanel = new JPanel();
	static Dimension setPreferredSize = new Dimension(300,30);
	static JButton setSize9 = new JButton("9  x  9");
	static JButton setSize13 = new JButton("13 x 13");
	static JButton setSize19 = new JButton("19 x 19");
	static JDialog chooseGamemodeType = new JDialog(mainFrame,"Please choose gamemode type");
	static JPanel chooseGamemodeTypePanel = new JPanel();
	static JButton setGamemodeTypePvP = new JButton("Player vs Player");
	static JButton setGamemodeTypePvB = new JButton("Player vs Bot");

	static int boardSize;
	static gamemodeType currentGamemodeType;
	static int frameHieght;
	static int frameWidth;

	public int getFrameHeight()
	{
		return frameHieght;
	}

	public int getFrameWidth()
	{
		return frameWidth;
	}

	public enum gamemodeType
	{
		PVP , PVB;
	}

	public ClientGUI()
	{
		initButtons();
		initFrames();
	}

	public void updateBoard(String[][] boardFields)
	{

	}

	public void paintBoard(String[][] boardFields)
	{
		mainFramePanel.removeAll();
		drawLines();
		paintPawns(boardFields);
	}

	public void drawLines()
	{
		DrawLine drawLine = new DrawLine();
		for(int i=0;i<boardSize;i++)
		{
			for(int j=0;j<boardSize;j++)
			{
				drawLine.setSize(20+j*getFrameWidth(),20+i*getFrameHeight(),20,20);
				mainFramePanel.add(drawLine);
				System.out.println("i: "+i+" j: "+j);
			}
		}
		mainFrame.repaint();
		mainFrame.revalidate();
	}

	public void paintPawns(String[][] boardFields)
	{
		PaintPawn paintPawn = new PaintPawn();
		JComponent[][] pawns = new JComponent[boardSize][boardSize];
		for(int i=0;i<boardSize;i++)
		{
			for(int j=0;j<boardSize;j++)
			{
				if(boardFields[i][j]=="B")
					{
					paintPawn.setType("Black");
					paintPawn.setSize(20+j*getFrameWidth(),20+i*getFrameHeight(),20,20);
					pawns[i][j] = paintPawn;
					}
				else if(boardFields[i][j]=="W")
					{
					paintPawn.setType("White");
					paintPawn.setSize(20+j*getFrameWidth(),20+i*getFrameHeight(),20,20);
					pawns[i][j] = paintPawn;
					}
			}
		}
	}



	public void initButtons()
	{
		ActionListener setSize = new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						chooseSize.setVisible(false);
						String name = ((JButton) e.getSource()).getText().substring(0,2).replaceAll("\\s","");
						boardSize = Integer.parseInt(name);
						chooseGamemodeType.setVisible(true);
					}
				};
		setSize9.addActionListener(setSize);
		setSize13.addActionListener(setSize);
		setSize19.addActionListener(setSize);

		ActionListener setGamemodeType = new ActionListener()
		{
			public void actionPerformed(ActionEvent f)
			{
				chooseGamemodeType.setVisible(false);
				String name = ((JButton) f.getSource()).getText();
				if(name==("Player vs Player"))
					currentGamemodeType = gamemodeType.PVP;
				if(name==("Player vs Bot"))
					currentGamemodeType = gamemodeType.PVB;
				drawLines();
			}
		};

		setGamemodeTypePvP.addActionListener(setGamemodeType);
		setGamemodeTypePvB.addActionListener(setGamemodeType);
	}

	public void initFrames()
	{
		chooseSize.setSize(400,160);
		chooseSize.setResizable(false);
		chooseSize.setLocation(530,300);
		chooseSize.add(chooseSizePanel);
		setSize9.setPreferredSize(setPreferredSize);
		setSize13.setPreferredSize(setPreferredSize);
		setSize19.setPreferredSize(setPreferredSize);
		chooseSizePanel.add(setSize9);
		chooseSizePanel.add(setSize13);
		chooseSizePanel.add(setSize19);
		setGamemodeTypePvP.setPreferredSize(setPreferredSize);
		setGamemodeTypePvB.setPreferredSize(setPreferredSize);
		chooseGamemodeType.add(chooseGamemodeTypePanel);
		chooseGamemodeType.setSize(400,120);
		chooseGamemodeType.setResizable(false);
		chooseGamemodeType.setLocation(530,300);
		chooseGamemodeTypePanel.add(setGamemodeTypePvP);
		chooseGamemodeTypePanel.add(setGamemodeTypePvB);
		//mainFrame.add(mainFramePanel);
		mainFrame.setSize(1300,800);
		mainFrame.setLocation(120,0);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		chooseSize.setVisible(true);
	}

}
