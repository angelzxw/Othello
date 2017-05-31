package GUI;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Player {
	/*
	 * gm_mode: PvsP = 0; P vs AI = 1; AI vs AI = 2;
	 * ply_mode: Black = 0; White = 1; 
	 */
	
	// 0->1  0+1 = 1 % 2 = 1  1->0 1+1 = 2%2 =0 
	final static int pt_table[][] = new int[game.WIDTH][game.HEIGHT];
	int last_move;
	final int CORNER = 50;
	final int DIAGONAL = -10;
	final int SECOND = -1;
	final int THIRD = 5;
	final int FOURTH = 2;
	final int COMMON = 1;
	final int STARTER = 0;
	
	//weights
	final double POSITIONWEIGHT = 5;
	final double MOBILITYWEIGHT = 15;
	final double ENDWEIGHT = 300;
	
	final double INFINITE = Double.MAX_VALUE;
	final double NEG_INFINITE = Double.MIN_VALUE;
	
	final int max_time = 10;
	protected int gm_mode, ply_mode, score;
	double tm_mode;

	boolean end_game;
	
	long search_time;
	int search_depth;
	public move prev_move;
	
	public Player() {
	}
	
	public int get_last_move(){
		return last_move;
	}
	public game getState(game gm, boolean finish, int color){
		return search(gm, finish, color);
	}
	public game search(game gm, boolean finish, int color){
		return gm;
	}
	
	public Player(int g, int t, int p) {
		//NextMoves.add(new cell());
		gm_mode = g;
		tm_mode = t;
		ply_mode = p;
		score = 2;
	}
	
	public int getPlyMode(){
		return ply_mode;
	}
	
	public int getScore(){
		return score;
	}
	
	public void setPlyMode(int t){
		score = score + t;
	}
	
	public int get_search_depth(){
		return search_depth;
	}
	
	public long get_search_time(){
		return search_time;
	}
	
	public void refill_points(){}
	
	public static void main(String[] args) {
	}

	public void refill_points_2() {
	}

}
