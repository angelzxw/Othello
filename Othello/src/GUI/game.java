package GUI;

import java.util.ArrayList;

public class game {
	final static int BLACK = 1;
	final static int WHITE = 2;
	final static int EMPTY = 0;
	final static int WIDTH = 10;
	final static int HEIGHT = 10;
	final int board[][] = new int[WIDTH][HEIGHT];
	public game() {}
	public int search_depth;
	public long search_time;
	public move prev_move = new move();
	
	public game (game copy){
		for (int j = 0; j < HEIGHT; j++){
			for (int i = 0; i < WIDTH; i++){
				this.board[j][i] = copy.board[j][i];
			}
		}
	}

	public boolean legal_move(int x, int y, int color, boolean flip){
		boolean legal = false;
		if(board[y][x] == EMPTY){
			int pos_x;
			int pos_y;
			boolean found;
			int cur;
			
			//search in  directions
			for (int x_dir = -1; x_dir <= 1; x_dir++){
				for (int y_dir = -1; y_dir <= 1; y_dir++){
					pos_x = x + x_dir;
					pos_y = y + y_dir;
					found = false;
					cur = board[pos_y][pos_x];
					
					/*
					 * -1: out of boundary
					 */
					
					if (cur == -1 || cur == EMPTY || cur == color){
						continue;
					}
					
					while (!found){
						pos_x += x_dir;
						pos_y += y_dir;
						cur = board[pos_y][pos_x];
						
						if (cur == color){
							found = true;
							legal = true;
						
							if (flip){
								pos_x -= x_dir;
								pos_y -= y_dir;
								cur = board[pos_y][pos_x];
								
								while (cur != EMPTY){
									board[pos_y][pos_x] = color;
									pos_x -= x_dir;
									pos_y -= y_dir;
									cur = board[pos_y][pos_x];
								}
							}
						}else if (cur == -1 || cur == EMPTY){
							found = true;
						}
					}
				}
			}
		}
		return legal;
	}
	
	public move point_move(int x, int y, int color, boolean flip, int[][] point){
		move new_move = new move();
		
		if (board[y][x] == EMPTY){
			int pos_x;
			int pos_y;
			boolean found;
			int cur;
			int sum;
			
			//search in  directions
			for (int x_dir = -1; x_dir <= 1; x_dir++){
				for (int y_dir = -1; y_dir <= 1; y_dir++){
					pos_x = x + x_dir;
					pos_y = y + y_dir;
					found = false;
					cur = board[pos_y][pos_x];
					sum = 0;

					//-1: out of boundary
					if (cur == -1 || cur == EMPTY || cur == color){
						continue;
					}else{
						sum += point[pos_y][pos_x];
					}
					
					while (!found){
						pos_x += x_dir;
						pos_y += y_dir;
						cur = board[pos_y][pos_x];
						
						if (cur == color){
							found = true;
							new_move.legal = true;
							new_move.x = x;
							new_move.y = y;
							new_move.points += point[y][x];
						
							if (flip){
								pos_x -= x_dir;
								pos_y -= y_dir;
								cur = board[pos_y][pos_x];
								
								while (cur != EMPTY){
									board[pos_y][pos_x] = color;
									pos_x -= x_dir;
									pos_y -= y_dir;
									cur = board[pos_y][pos_x];
								}
							}
						}else if (cur == -1 || cur == EMPTY){
							//reset variables for searching in another direction
							sum = 0;
							found = true;
						}
					}
					new_move.points += sum;
				}
			}
		}
		return new_move;
	}
	
	public ArrayList<move> legal_moves(int color){
		ArrayList<move> moves = new ArrayList<move>();
		for (int j = 1; j < HEIGHT-2; j++){
			for (int i = 1; i <= WIDTH-2; i++){
				if (legal_move(i, j, color, false)){
					move tmp = new move();
					tmp.legal = true;
					tmp.x = i;
					tmp.y = j;
					moves.add(tmp);
				}
			}
		}
		return moves;
	}
	
	public void update(move tmp, int color){
		board[tmp.get_y()][tmp.get_x()] = color;
	}
	
	public int get_score(){
		int blk_sc = 0;
		int wht_sc = 0;
		for (int j = 1; j < HEIGHT-2; j++){
			for (int i = 1; i <= WIDTH-2; i++){
				if (board[j][i] == BLACK){
					blk_sc++;
				}else if (board[j][i] == WHITE){
					wht_sc++;
				}
			}
		}
		int score = blk_sc*100+wht_sc;
		return score;
	}
	
	
	public void print_board(){
		for (int j = 1; j < HEIGHT-2; j++){
			for (int i = 1; i <= WIDTH-2; i++){
				//System.out.print("[" + board[j][i] + "]");
			}
			//System.out.println();
		}
	}
}
