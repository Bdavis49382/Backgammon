package main;


public class Master {
	Gameboard gm = new Gameboard(this);
	public UI ui = new UI(this);
	
	public static void main(String[] args) {
		new Master();
		
	}
	
	public Master() {	}
}
