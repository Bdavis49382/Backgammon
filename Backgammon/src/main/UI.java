package main;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pieces.Piece;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.Arrays;


public class UI {

	JFrame window;
	Master m;
	
	public JLabel title;
	public JPanel menuOptions;
	public JPanel board;
	public JButton start;
	public JButton singlePlayer;
	public JButton settings;
	public JLabel background;
	public Piece clickedOn;
	//the Color of the Opening Screen
	Color chill = new Color(25,79,166);
	public UI(Master m) {
		this.m=m;
		window = new JFrame();
		window.setSize(810,628);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createMenuScreen();
		m.gm.rollDice();

		
		
		
		window.setVisible(true);
	}
	
	public void createMenuScreen() {
		
		//CREATE WINDOW
		
		JPanel backSplash = new JPanel();
		backSplash.setBackground(chill);
		backSplash.setLayout(new BorderLayout(20,20));
		window.add(backSplash);
		
		
		//CREATE OPTIONS
		title = new JLabel("Backgammon");
		title.setFont(new Font("Trebuchet MS", Font.BOLD,35));
		title.setHorizontalAlignment(JLabel.CENTER);
		
		start = new JButton("Multiplayer(2 Players, 1 Computer)");
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				backSplash.setVisible(false);
				createGameScreen();
				updateBoard();
			}
			
		});
		
		settings = new JButton("Settings");
		
		singlePlayer = new JButton("Singleplayer(coming soon!)");
		
		//CREATE OPTIONS LIST
		menuOptions = new JPanel();
		menuOptions.setLayout(new BoxLayout(menuOptions,BoxLayout.PAGE_AXIS));
		menuOptions.setBounds(350,50,700,150);
		menuOptions.setBackground(chill);
		menuOptions.setForeground(Color.black);
		
		menuOptions.add(title);
		menuOptions.add(Box.createRigidArea(new Dimension(0,45)));
		menuOptions.add(start);
		menuOptions.add(Box.createRigidArea(new Dimension(0,10)));
		menuOptions.add(singlePlayer);
		menuOptions.add(Box.createRigidArea(new Dimension(0,10)));
		menuOptions.add(settings);
		
		//ADD OPTIONS LIST TO WINDOW
		backSplash.add(Box.createRigidArea(new Dimension(0,30)),BorderLayout.PAGE_START);
		backSplash.add(Box.createRigidArea(new Dimension(280,15)),BorderLayout.LINE_START);
		backSplash.add(Box.createRigidArea(new Dimension(200,15)),BorderLayout.LINE_END);

		backSplash.add(menuOptions,BorderLayout.CENTER);
		
	}
	public void createGameScreen() {
		
		
		board = new JPanel();
		board.setBounds(0,0,800,593);
		board.setBackground(Color.blue);
		board.setLayout(null);
		window.add(board);
		
		background = new JLabel();
		background.setBounds(0,0,800,593);
		
		ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("board 800 x 593.jpg"));
		background.setIcon(bgIcon);
		board.add(background);
		
	}
	public void createEndScreen() {
		JPanel back= new JPanel();
		back.setBackground(chill);
		back.setLayout(new BorderLayout(20,20));
		back.setBounds(0,0,800,593);
		JLabel endText = new JLabel(m.gm.whiteScore==15?"Player White Won!":"Player Black Won!");
		endText.setHorizontalAlignment(JLabel.CENTER);
		endText.setFont(new Font("Trebuchet MS", Font.BOLD,35));
		back.add(endText,BorderLayout.PAGE_START);
		window.add(back);
	}
	public void createObject(int x,int y,int size,String fileName,Piece p) {
		JLabel objectLabel = new JLabel();
		objectLabel.setBounds(x,y,size,size);
		
		ImageIcon objectIcon = new ImageIcon(getClass().getClassLoader().getResource(fileName));
		objectLabel.setIcon(objectIcon);
		if(p!=null) {
			objectLabel.addMouseListener(new MouseListener() {


				public void mouseClicked(MouseEvent e) {
					
				}
	
				@Override
				public void mousePressed(MouseEvent e) {
					if(!p.ghost&&SwingUtilities.isLeftMouseButton(e)&&((p.color.equals("black")&&!m.gm.turn)||(p.color.equals("white")&&m.gm.turn))) {	//if you click on a piece, creates movement ghosts
						m.gm.eraseGhosts();
						if(m.gm.dice[3]!=0) {
							m.gm.forecastMovement(p.stripe, m.gm.firstDice());
						}
						else {
							m.gm.forecastMovement(p.stripe, m.gm.dice[1]);
							m.gm.forecastMovement(p.stripe, m.gm.dice[2]);
						}
						if((p.stripe==26||p.stripe==27)&&!m.gm.checkForGhosts()&&((p.color.equals("white")&&m.gm.whiteLocked)||(p.color.equals("black")&&m.gm.blackLocked))) {
							m.gm.turn = m.gm.turn?false:true;
							m.gm.rollDice();
							updateBoard();
						}
						clickedOn=p;
						updateBoard();
						background.repaint();
						m.gm.whiteLocked=false;
						m.gm.blackLocked=false;
					}
					else if(p.ghost&&SwingUtilities.isLeftMouseButton(e)&&((p.color.equals("black")&&!m.gm.turn)||(p.color.equals("white")&&m.gm.turn))) {	//if you click on a ghost, it moves the piece
						
						
						int s = p.stripe;
						for(int i=0;i<4;i++) {
							
							//if(Math.abs(s-clickedOn.stripe)==m.gm.dice[i]||Math.abs(s-25)==m.gm.dice[i]||Math.abs(s-0)==m.gm.dice[i]) {
							if(p.diceNum==m.gm.dice[i]) {
								m.gm.dice[i]=0;
								m.gm.eraseGhosts();
								break;
							}
						}
						
						m.gm.stripes[s].remove(p);
						if(m.gm.stripes[s].size()>0) {
							if(!m.gm.stripes[s].get(0).color.equals(p.color)) {
								m.gm.move(m.gm.stripes[s].get(0),p.color.equals("white")?26:27);
							}
						}
						m.gm.move(clickedOn, s);
						clickedOn=null;
						updateBoard();
						background.repaint();
					}
	
				}
	
				public void mouseReleased(MouseEvent e) {
					
				}
				public void mouseEntered(MouseEvent e) {
					
				}
				public void mouseExited(MouseEvent e) {
				
				}});
		}
		if(p!=null) {
			p.setPlatform(objectLabel);
		}
		
		background.add(objectLabel);
	}
	public void drawPiece(int pos, int ind,Piece p) {
		String color=p.color;
		String fileName="";
		//if(ind>4) {
		//	ind=4;
		//}
		switch(color) { 
		case "black":
			fileName=p.ghost?"ghostBlackPiece.png":"blackPiece.png";
			break;
		case "white":
			fileName=p.ghost?"ghostWhitePiece.png":"whitePiece.png";
			break;
		
		}
		int spacer=0;
		for(int i=1;i<13;i++) {
			if(i>=7)
				spacer=29;
			if(i==pos) {
				if(ind<5)
					createObject(669-(i-1)*51-spacer,520-ind*51,52,fileName,p);
				else if(ind<10)
					createObject(663-(i-1)*51-spacer,316+(ind-5)*51,52,fileName,p);
			}
			spacer=0;
		}
		for(int i=13;i<25;i++) {
			if(i>=19)
				spacer=29;
			if(i==pos) {
				if(ind<5)
					createObject(80+(i-13)*51+spacer,20+ind*51,52,fileName,p);
				else if(ind<10)
					createObject(74+(i-13)*51+spacer,224-(ind-5)*51,52,fileName,p);
			}
			spacer=0;
		}
		switch(pos) {
		case 26:
			createObject(375,350,52,fileName,p);
			break;
		case 0:
			createObject(735,400,52,fileName,p);
			break;
		
		case 25:
			createObject(735,140,52,fileName,p);
			break;
		case 27:
			createObject(375,190,52,fileName,p);
			break;
		}
	}
	public void updateBoard() {
		background.removeAll();
		for(int i=0;i<m.gm.stripes.length;i++) {
			for(int t=m.gm.stripes[i].size()-1;t>=0;t--) {
				drawPiece(i,t,m.gm.stripes[i].get(t));
				
			}
		}
		
		if(Arrays.stream(m.gm.dice).allMatch(x -> x==0)) {
			m.gm.turn=m.gm.turn?false:true;
			m.gm.rollDice();
		}
		
		for(int i=0;i<m.gm.whiteScore;i++) {
			createObject(735,537-i*7,53,"whiteSidePiece.png",null);
		}
		for(int i=0;i<m.gm.blackScore;i++) {
			createObject(735,4+i*7,53,"blackSidePiece.png",null);
		}
		
		updateDice(m.gm.turn?"":"b");
		if(m.gm.whiteScore==15||m.gm.blackScore==15) {
			board.setVisible(false);
			createEndScreen();
			
		}
		
	}
	
	public void updateDice(String color) {
		
		for(int i=0;i<4;i++) {
			if(m.gm.dice[i]!=0||i==1||i==2)
				createObject(260+i*80,275,40,color+"dice_"+m.gm.dice[i]+".png",null);
		}
		
		
	}
}
