package Client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;

import java.lang.String;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

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
	static JLabel stateLabel = new JLabel(" Your turn!");

	static int boardSize;
	static gamemodeType currentGamemodeType;
	static turn currentTurn;
	static final int frameHeight = 820;
	static final int frameWidth = 820;
	
	Socket socket;
	Scanner in;
	PrintWriter out;
	
	public enum turn
	{
		YOU, OPPONENT;
	}

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

	public ClientGUI() throws UnknownHostException, IOException
	{
		socket = new Socket("localhost",58901);
		in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
		initButtons();
		initFrames();
		listenServer();
	}
	
	public void listenServer()
	{
		boolean check = true;
		while(check)
		{
			if(in.hasNextLine())
			{
				String response = in.nextLine();
				System.out.println(response);
				if(response.startsWith("BOARD"))
				{
					response.substring(6);
					String[][] boardFields = new String[boardSize][boardSize];
					int index=0;
					for(int i=0;i<boardSize;i++)
					{
						for(int j=0;j<boardSize;j++)
						{
							boardFields[i][j]=response.substring(index,index);
							index++;
						}
					}
				updateBoard(boardFields);
				currentTurn = turn.OPPONENT;
				stateLabel.setText(" Opponent turn!");
				}	
			}		
		}
	}
	
	public void stateSetter()
	{
		
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
	}
	
	static ArrayList<DrawNode> nodes = new ArrayList<DrawNode>();
	
	public void clearNodes()
	{
		for(int i=0;i<nodes.size();i++)
		{
			if(nodes.get(i)!=null)
			mainFrame.remove(nodes.get(i));
		}
	}
	
	public void drawNodes(int x1,int y1)
	{
		clearNodes();
		nodes.clear();
		if(x1>=0);
		{
			if(y1>=0)
			{
				if(x1<(boardSize))
				{
					if(y1<boardSize)
					{
						DrawNode node = new DrawNode(15+x1*((getFrameWidth()-40)/boardSize),y1*((getFrameHeight()-40)/boardSize),30,30);
						nodes.add(node);
						mainFrame.add(node);
						mainFrame.repaint();
						mainFrame.revalidate();
					}
					
				}
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

	static ArrayList<PaintPawn> pawns = new ArrayList<PaintPawn>();

	public void paintPawns(String[][] boardFields)
	{
		pawns.clear();
		for(int i=0;i<boardSize;i++)
		{
			for(int j=0;j<boardSize;j++)
			{
				if(boardFields[i][j]=="b")
					{
					PaintPawn paintPawn = new PaintPawn(15+j*((getFrameWidth()-40)/boardSize),i*((getFrameHeight()-40)/boardSize),30,30,"Black");
					//pawns.set(i*boardSize+j, paintPawn);
					pawns.add(paintPawn);
					mainFrame.add(paintPawn);
					mainFrame.repaint();
					mainFrame.revalidate();
					}
				else if(boardFields[i][j]=="w")
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
						out.println("SIZE "+name);
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
				currentTurn = turn.YOU;
				drawRectangles();
				mainFrame.repaint();
				mainFrame.revalidate();
				addMouse();
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
		mainFrameMenuBar.add(stateLabel);
		mainFrame.setJMenuBar(mainFrameMenuBar);
		mainFrame.setLocation(380,0);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrameMenu.setVisible(true);
		mainFrameMenu.setVisible(true);
		mainFrame.setVisible(true);
		chooseSize.setVisible(true);
	}
	
	MouseListener mouseClick = new MouseListener() {
		public void mouseClicked(MouseEvent e) {
			if(currentTurn == turn.YOU)
			{
				System.out.println(((e.getX()-15)/((getFrameWidth()-40)/boardSize))+" "+((e.getY())/((getFrameWidth()-40)/boardSize)-1));
				out.println("MOVE " + ((e.getX()-15)/((getFrameWidth()-40)/boardSize))+" "+((e.getY())/((getFrameWidth()-40)/boardSize)-1));
			}
			else
			{
				currentTurn = turn.YOU;
				stateLabel.setText(" Your Turn!");
			}
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	};
	
	MouseMotionListener mouseMove = new MouseMotionListener()
	{
		public void mouseDragged(MouseEvent e) {}
		public void mouseMoved(MouseEvent e) {
			drawNodes((e.getX()-15)/((getFrameWidth()-40)/boardSize),(e.getY())/((getFrameWidth()-40)/boardSize)-1);
			mainFrame.revalidate();
			mainFrame.repaint();
		}
	};
	
	public void addMouse()
	{
				mainFrame.addMouseListener(mouseClick);
				mainFrame.addMouseMotionListener(mouseMove);
	}
	
	ArrayList<Point> choosenPoint = new ArrayList<Point>();
	ArrayList<DrawChooseOutline> choosenOutlines = new ArrayList<DrawChooseOutline>();
	
	public void drawChooseOutline(int x,int y,String ccolor)
	{
		DrawChooseOutline drawOutline = new DrawChooseOutline(15+x*((getFrameWidth()-40)/boardSize),y*((getFrameHeight()-40)/boardSize),30,30,ccolor);
		mainFrame.add(drawOutline);
		choosenOutlines.add(drawOutline);
		Point point = new Point(x,y);
		choosenPoint.add(point);
	}
		
	
	public void chooseAlive()
	{
		clearNodes();
		mainFrame.removeMouseListener(mouseClick);
		mainFrame.removeMouseMotionListener(mouseMove);
		mainFrame.addMouseListener(mouseChooseAlive);
	}
	
	MouseListener mouseChooseAlive = new MouseListener()
	{
	public void mouseClicked(MouseEvent e) {
		drawChooseOutline((e.getX()-15)/((getFrameWidth()-40)/boardSize),(e.getY())/((getFrameWidth()-40)/boardSize)-1,"Green");
		mainFrame.repaint();
		mainFrame.revalidate();
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	};
}
