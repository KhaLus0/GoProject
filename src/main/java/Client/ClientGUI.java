package Client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.lang.String;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ClientGUI
{
	static JFrame mainFrame = new JFrame("Go Game");
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
	static JMenuBar mainFrameMenuBar = new JMenuBar();
	static JMenu mainFrameMenu = new JMenu();
	static JButton passTurn = new JButton("Pass turn");
	static JButton surrender = new JButton("Surrender");

	static int boardSize;
	static gamemodeType currentGamemodeType;
	static final int frameHeight = 820;
	static final int frameWidth = 820;

	public int getFrameHeight()
	{
		return frameHeight;
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
	
	public void clearBoard()
	{
		for(int i=0;i<pawns.size();i++)
		{
			if(pawns.get(i)!=null)
			mainFrame.remove(pawns.get(i));
		}
		for(int i=0;i<rect.size();i++)
		{
			mainFrame.remove(rect.get(i));
		}
		for(int i=0;i<nodes.size();i++)
		{
			if(nodes.get(i)!=null)
			mainFrame.remove(nodes.get(i));
		}
		mainFrame.repaint();
		mainFrame.revalidate();
	}

	public void updateBoard(String[][] boardFields)
	{
		clearBoard();
		paintPawns(boardFields);
		mainFrame.repaint();
		mainFrame.revalidate();	
		drawRectangles();
		mainFrame.repaint();
		mainFrame.revalidate();
		drawNodes();
		mainFrame.repaint();
		mainFrame.revalidate();
	}
	
	static ArrayList<DrawNode> nodes = new ArrayList<DrawNode>();
	
	
	
	public void drawNodes()
	{
		nodes.clear();
		for(int i=0;i<boardSize;i++)
		{
			for(int j=0;j<boardSize;j++)
			{
				if(pawns.get(i*boardSize+j)==null)
				{
					DrawNode node = new DrawNode(28+j*((getFrameWidth()-40)/boardSize),13+i*((getFrameHeight()-40)/boardSize),4,4,i,j);
					nodes.add(node);
					mainFrame.add(node);
					mainFrame.repaint();
					mainFrame.revalidate();
				}
				else
					nodes.add(null);
			}
		}
	}

	static ArrayList<DrawRectangle> rect = new ArrayList<DrawRectangle>();
	
	public void drawRectangles()
	{
		for(int i=0;i<boardSize-1;i++)
		{
			for(int j=0;j<boardSize-1;j++)
			{
				DrawRectangle drawRect = new DrawRectangle(30+j*((getFrameWidth()-40)/boardSize),15+i*((getFrameHeight()-40)/boardSize),((getFrameWidth()-40)/boardSize),((getFrameHeight()-40)/boardSize));
				rect.add(drawRect);
				mainFrame.add(drawRect);
				mainFrame.repaint();
				mainFrame.revalidate();
			}
		}
		mainFrame.repaint();
		mainFrame.revalidate();
		
	}
	
	public void statUpdateBoard()
	{
		String[][] boardFields = new String[boardSize][boardSize];
		for(int i =0;i<boardSize;i++)
		{
			for(int j=0;j<boardSize;j++)
			{
				if(i==3&&j==3)
					boardFields[i][j]="B";
				else if(i==4&&j==4)
					boardFields[i][j]="W";
					else
					boardFields[i][j]="";
			}
		}
		updateBoard(boardFields);
	}
	
	public void statUpdateBoardTest()
	{
		String[][] boardFields = new String[boardSize][boardSize];
		for(int i =0;i<boardSize;i++)
		{
			for(int j=0;j<boardSize;j++)
			{
				if(i==6&&j==7)
					boardFields[i][j]="B";
				else if(i==2&&j==1)
					boardFields[i][j]="W";
					else
					boardFields[i][j]="";
			}
		}
		updateBoard(boardFields);
	}

	static ArrayList<PaintPawn> pawns = new ArrayList<PaintPawn>();

	public void paintPawns(String[][] boardFields)
	{
		pawns.clear();
		for(int i=0;i<boardSize;i++)
		{
			for(int j=0;j<boardSize;j++)
			{
				if(boardFields[i][j]=="B")
					{
					PaintPawn paintPawn = new PaintPawn(15+j*((getFrameWidth()-40)/boardSize),i*((getFrameHeight()-40)/boardSize),30,30,"Black");
					//pawns.set(i*boardSize+j, paintPawn);
					pawns.add(paintPawn);
					mainFrame.add(paintPawn);
					mainFrame.repaint();
					mainFrame.revalidate();
					}
				else if(boardFields[i][j]=="W")
					{
					PaintPawn paintPawn = new PaintPawn(15+j*((getFrameWidth()-40)/boardSize),i*((getFrameHeight()-40)/boardSize),30,30,"White");
					pawns.add(paintPawn);
					mainFrame.add(paintPawn);
					mainFrame.repaint();
					mainFrame.revalidate();
					}
				else
					pawns.add(null);
			}
		}
		drawRectangles();
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
		
		ActionListener testing = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				statUpdateBoardTest();
			}
		};
		ActionListener testing2 = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				statUpdateBoard();
			}
		};
		passTurn.addActionListener(testing);
		surrender.addActionListener(testing2);

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
				statUpdateBoard();
			}
		};

		setGamemodeTypePvP.addActionListener(setGamemodeType);
		setGamemodeTypePvB.addActionListener(setGamemodeType);
		passTurn.setPreferredSize( new Dimension(10,20));
		surrender.setPreferredSize( new Dimension(10,20));
	}

	public void initFrames()
	{
		chooseSize.setSize(400,160);
		chooseSize.setResizable(false);
		chooseSize.setLocation(640,300);
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
		chooseGamemodeType.setLocation(640,300);
		chooseGamemodeTypePanel.add(setGamemodeTypePvP);
		chooseGamemodeTypePanel.add(setGamemodeTypePvB);
		mainFrame.setSize(frameWidth, frameHeight );
		mainFrameMenuBar.add(passTurn);
		mainFrameMenuBar.add(surrender);
		mainFrame.setJMenuBar(mainFrameMenuBar);
		mainFrame.setLocation(380,0);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrameMenu.setVisible(true);
		mainFrameMenu.setVisible(true);
		mainFrame.setVisible(true);
		chooseSize.setVisible(true);
	}

}
