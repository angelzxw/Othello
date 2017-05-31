package GUI;

import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class human extends Player{

final static int pt_table[][] = new int[game.WIDTH][game.HEIGHT];
	
	final int max_time = 10;
	int last_move = -1;
	public human() {
		// TODO Auto-generated constructor stub
	}
	/*
	public human(int g, int t, int p) {
		gm_mode = g;
		tm_mode = t;
		ply_mode = p;
		
		// TODO Auto-generated constructor stub
	}*/
	public int get_last_move(){
		return last_move;
	}
	
	public human(int num){
		ply_mode = num;
	}
	
	public human(int g, int t, int p) {
		super(g, t, p);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int m = 17;
		int k = m/10;
		int j = m%10;
		System.out.println(k);
		System.out.println(j);
	}
	
	public game getState(game gm, boolean finish, int color, int x, int y){
	    
		return search(gm, finish, color, x, y);
	}

	public game search(game gm, boolean finish, int color, int x, int y){

		if (!finish)
			gm.board[y][x] = color;
			
		return gm;
	}
		
	private int end_check(game gm) {
		
		int result = -1; //default is unfinished
		int black_pt = 0;
		int white_pt = 0;
		
		for (int j = 1; j < gm.HEIGHT-1; j++){
			for (int i = 1; i < gm.WHITE-1; i++){
				if (gm.legal_move(i, j, gm.BLACK, false) || gm.legal_move(i, j, gm.WHITE, false)){
					result = -1;
					return result;
				}
				
				if (gm.board[j][i] == gm.BLACK){
					black_pt ++;
				}else if (gm.board[j][i] == gm.WHITE){
					white_pt++;
				}
			}
		}
		
		if (black_pt > white_pt){
			result = gm.BLACK;
		}else if (black_pt < white_pt){
			result = gm.WHITE;
		}else{
			result = 0; //tie
		}
		return result;
	}

	private int mobility_check(game gm, int color) {
		int result = 0;
		
		for (int j = 0; j < gm.HEIGHT-1 ;j++){
			for (int i = 0; i < gm.WIDTH -1; i++){
				if (gm.legal_move(i, j, color, false)){
					result ++;
				}
				
			}
			
		}
		return result;
	}
	
	public game make_move(game gm, boolean finish, int color, move mv){
		
		if (!finish){
			if (mv.legal){
				gm.point_move(mv.x, mv.y, color, true, pt_table);
				gm.board[mv.y][mv.x] = color;
			}
		}
		return gm;
	} 

}
