package GUI;

public class move {

	boolean legal = false;
	int points = 0;
	int x = -1;
	int y = -1;

	public move() {}
	public move(move input) {
		move output = new move();
		this.legal = input.legal;
		this.points = input.points;
		this.x = input.x;
		this.y = input.y;
	}
	public int get_x(){
		return x;
	}
	
	public int get_y(){
		return y;
	}

}
