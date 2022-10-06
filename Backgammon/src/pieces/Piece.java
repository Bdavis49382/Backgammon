package pieces;

import javax.swing.JLabel;

public class Piece {
	public JLabel platform;
	public int stripe;
	public String color;
	public boolean ghost;
	public int diceNum;			//just for ghosts, the dice value that led to them being created
	public Piece(int stripe,String color,boolean ghost,int diceNum) {
		this.stripe=stripe;
		this.color=color;
		this.ghost=ghost;
		this.diceNum=diceNum;
	}
	public void setPlatform(JLabel platform) {
		this.platform=platform;
	}
}
