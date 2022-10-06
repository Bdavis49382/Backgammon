package main;

import java.util.ArrayList;

import pieces.Piece;

public class Gameboard {
	//public List<List<String>> stripes;
	public ArrayList<Piece>[] stripes;	//an array of stripes, which are lists of Strings either white or black
	public int[] dice = new int[4];
	Master m;
	public boolean turn=true;	//if true then it's whites turn, false it's black's
	public boolean whiteLocked=false; //if a player has a piece on the bar, they are locked from moving any other pieces
	public boolean blackLocked=false;
	public int whiteScore=0;
	public int blackScore=0;
	public Gameboard(Master m) {
		this.m=m;
		defaultBoard();
		

	}
	@SuppressWarnings("unchecked")
	public void defaultBoard() {
		//stripes = new ArrayList<List<String>>(24);
		stripes= (ArrayList<Piece>[]) new ArrayList[28];
		for(int i=0;i<stripes.length;i++) {
			stripes[i] = new ArrayList<Piece>();
		}
		addPieces(1,2,"black");
		addPieces(6,5,"white");
		addPieces(8,3,"white");
		addPieces(12,5,"black");
		addPieces(13,5,"white");
		addPieces(17,3,"black");
		addPieces(19,5,"black");
		addPieces(24,2,"white");

		
	}
	public void addPieces(int stripe,int num,String color) {
		for(int i=0;i<num;i++) {
			stripes[stripe].add(new Piece(stripe,color,false,0));
		}
	}
	public void eraseGhosts() {
		for(ArrayList<Piece> s:stripes) {
			for(Piece p:s) {
				if(p.ghost) {
					s.remove(p);
					break;
				}
			}
			
			
		}
	}
	public void forecastMovement(int stripe,int amount) {
		if(amount !=0) {
			
			if(stripes[27].size()>0) {
				whiteLocked=true;
			}
			if(stripes[26].size()>0) {
				blackLocked=true;
			}
			int target = stripe-amount;
			if(stripe==27) {
				target-=2;
				
			}
			String color="";
			if(target>-1&&turn) {							//WHITE MOVEMENT
				if(stripes[target].size()>0) {
				 color = stripes[target].get(0).color;
				}
				if(turn&&target>-1&&((stripes[target].size()<=1)||color.equals("white"))&&(!whiteLocked||stripe==27)) {
					
					if(target!=0||checkPosition("white"))
						stripes[target].add(new Piece(target,"white",true,amount));
						
				}
			}
			if(turn&&target<0&&nonHigher(stripe,"white")&&checkPosition("white")) {
				stripes[0].add(new Piece(0,"white",true,amount));
				
			}
			
			target=stripe+amount;
			if(stripe==26) {
				target-=26;
				blackLocked=true;
			}
			if(target<26&&!turn) {							//BLACK MOVEMENT
				if(stripes[target].size()>0) {
					 color = stripes[target].get(0).color;
					}
				
				if(!turn&&target<26&&((stripes[target].size()<=1)||color.equals("black"))&&(!blackLocked||stripe==26)) {
					
					if(target!=25||checkPosition("black")) {
						
						stripes[target].add(new Piece(target,"black",true,amount));
					}
					
				}
			}
			if(!turn&&target>25&&nonHigher(stripe,"black")&&checkPosition("black")) {
				
				stripes[25].add(new Piece(25,"black",true,amount));
			}
				//stripes[stripe-amount].stream().forEach(p -> System.out.println(p.ghost));
			
		}
	}
	public void move(Piece p,int stripe) {
		
		stripes[p.stripe].remove(p);
		if(stripe==0||stripe==25) {
			if(p.color.equals("black"))
				blackScore++;
			if(p.color.equals("white"))
				whiteScore++;
			
			return;
		}
		stripes[stripe].add(p);
		p.stripe=stripe;
		
	}
	public void rollDice() {		//rolls two dice and returns the 2 values in an int array
		dice[0]=0;
		dice[3]=0;
		dice[1]=(int)(Math.random()*6+1);
		dice[2]=(int)(Math.random()*6+1);
		if(dice[1]==dice[2]) {
			dice[0]=dice[1];
			dice[3]=dice[1];
		}
			
	}
	public boolean checkForGhosts() {
		
		for(ArrayList<Piece> s:stripes) {
			for(Piece p:s) {
				if(p.ghost) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean checkPosition(String color) {	//checks if all pieces for a color are in winning position
		int distance;
		for(ArrayList<Piece> s:stripes) {
			for(Piece p:s) {
				
				distance= p.color.equals("white")?p.stripe:25-p.stripe;
				
				if(distance>6&&p.color.equals(color)) {
					System.out.println("false");
					return false;
				}
			}
		}
		
		return true;
	}
	public boolean nonHigher(int stripe,String color) {
			if(color.equals("white")) {
				
				for(int i=stripe+1;i<7;i++) {
					
					
					if(!stripes[i].isEmpty())
						return false;
				}
			}
			else if(color.equals("black")) {
				for(int i=stripe-1;i>18;i--) {
					if(!stripes[i].isEmpty())
						return false;
				}
			}
		return true;
	}
	public int firstDice() {
		for(int d:dice) {
			if(d!=0) {
				return d;
			}
		}
		return 0;
	}
}
