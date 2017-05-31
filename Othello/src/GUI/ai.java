package GUI;

import java.util.ArrayList;
import java.util.Random;

public class ai extends Player{

	final static int pt_table[][] = new int[game.WIDTH][game.HEIGHT];
	final static int BLACK = 1;
	final static int WHITE = 2;
	final int CORNER = 50;
	final int DIAGONAL = -10;
	final int SECOND = -1;
	final int THIRD = 5;
	final int FOURTH = 2;
	final int COMMON = 1;
	final int STARTER = 0;
	int position = 0;
	int mobility = 0;
	//weights
	int POSITIONWEIGHT = 5;
	int MOBILITYWEIGHT = 15;
	//double ENDWEIGHT = 300;
	int counter = 0;
	final double INFINITE = Double.MAX_VALUE;
	final double NEG_INFINITE = Double.MIN_VALUE;
	final int max_time = 10;
	long search_time;
	int search_depth;
	int emy;
	int chosen_move = -1;
	int search_move = 0;
	
	int last_move;
	//int chosen_move;
	public ai(int g, int t, int p) {
		gm_mode = g;
		tm_mode = t * 0.98;
		ply_mode = p;
		if (p == 0){
			emy = BLACK;
		}else{
			emy = WHITE;
		}
	}

	public ai() {
		fill_points();
	}
	
	public void fill_points(){
		/*
		 * Assigning initial points to different positions on the board
		 * each quarter follows the following:
		 * [0] [0]  [0]   [0] [0]
		 * [0] [50] [-1]  [5] [2]
		 * [0] [-1] [-10] [1] [1]
		 * [0] [5]  [1]   [1] [1]
		 * [0] [2]  [1]   [1] [0]
		 */
		pt_table[1][1] = CORNER;
		pt_table[2][2] = DIAGONAL;
		pt_table[1][2] = pt_table[2][1] = SECOND;
		pt_table[1][3] = pt_table[3][1] = THIRD;
		pt_table[1][4] = pt_table[4][1] = FOURTH;
		pt_table[2][3] = pt_table[3][2] = pt_table[2][4] = pt_table[4][2] = pt_table[3][3] = COMMON;
		pt_table[4][4] = STARTER;
		
		//top
		for (int i = 5; i < 9; i++){
			for (int j = 1; j< 5; j++){
				pt_table[j][i] = pt_table[j][9-i];
			}
		}
		//bottom
		for (int i = 1; i < 9; i++){
			for (int j = 5; j< 9; j++){
				pt_table[j][i] = pt_table[9-j][i];
			}
		}
		//outer boundary
		for (int j = 0; j < 10; j++){
			pt_table[j][0] = pt_table[j][9] = pt_table[0][j] = pt_table[9][j] = STARTER;
		}
	}
	
	public game getState(game gm, boolean finish, int color){
		return search(gm, finish, color);
	}
	
	
	public node make_tree(node parent, int depth){
		if (depth > 0 && parent.end == -1){
			depth --;
			int next;
			if (parent.turn == game.BLACK){
				next = game.WHITE;
			}else{
				next = game.BLACK;
			}
			
			//search for legal move
			
			for (int j = 0; j < 9; j++){
				for (int i = 0; i < 9; i++){
					move cur_move = parent.state.point_move(i, j, parent.turn, false, pt_table);
					if (cur_move.legal){
						//create a copy of the game object for attempting the cur_move
						game gm_copy = new game(parent.state);
						
						gm_copy = make_move(gm_copy, false, parent.turn, cur_move);
						
						//holds cur_move and gm_copy
						node new_node = new node(cur_move, gm_copy, next);
						
						new_node.position = cur_move.points;
						
						new_node.mobility = mobility_check(gm_copy, next);
						
						new_node.end = end_check(gm_copy);
						
						parent.addChild(new_node);
						new_node.parent = parent;
					}	
				}
			}
			if (depth >0){
				for (node n: parent.children){
					n = make_tree(n, depth);
				}
			}
			parent.mobility = parent.children.size();
		}
		return parent;
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
	
	public game search(game gm, boolean finish, int color){
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		move best_move;
		ArrayList<move> moves = gm.legal_moves(color);
		int tot = (gm.get_score()/100+gm.get_score()%100);
		//System.out.println("tot"+tot);
		if (tot >= 50){
			if (!finish){
				node cur_state = new node(new move(), gm, color);
				cur_state = make_tree(cur_state, 5);
				//cur_state = make_tree(cur_state, 20);
				best_move = minimax(cur_state, color);
				if (best_move.legal){
					gm.point_move(best_move.x, best_move.y, color, true, pt_table);
					gm.board[best_move.y][best_move.x] = color;
					last_move = best_move.x + 10 * best_move.y;
					search_depth = 5;
					endTime = System.currentTimeMillis();
					search_time = endTime - startTime;
				}
			}
		}else if (tot < 50){
			if (moves.size()==1){
				best_move = moves.get(0);
				if (best_move.legal){
					gm.point_move(best_move.x, best_move.y, color, true, pt_table);
					gm.board[best_move.y][best_move.x] = color;
					last_move = best_move.x + 10 * best_move.y;
					search_depth = 0;
					endTime = System.currentTimeMillis();
					search_time = endTime - startTime;
				}
			}else if (moves.size() < 1){
				return gm;
			}else if (moves.size()>1){
					int depth = 1;
					chosen_move = -1;
					search_move = 0;
					
					int max_depth = (64 - (gm.get_score()/100+gm.get_score()%100));
					
					long elpTime = endTime - startTime;
					
					while (elpTime < tm_mode && depth <= max_depth){
						boolean cut_off = false;
						search_move = mini_max(gm, depth, NEG_INFINITE, INFINITE, true, search_move, tm_mode, startTime, true, cut_off, color);
						counter++;
						//System.out.println("color:" +color);
						if (!cut_off){
							chosen_move = search_move;
							depth++;
						}else{
							depth--;
						}
						endTime = System.currentTimeMillis();
						elpTime = endTime - startTime;
					}
					//System.out.println(chosen_move);
					best_move = moves.get(chosen_move);
					if (best_move.legal){
						gm.point_move(best_move.x, best_move.y, color, true, pt_table);
						gm.board[best_move.y][best_move.x] = color;
						last_move = best_move.x + 10 * best_move.y;
						search_depth = depth;
						search_time = elpTime;
						//System.out.println("check pos 3: "+last_move);
					}	
			}
		}
		//System.out.println("color: "+ color + " D "+search_depth +" T " + search_time);
		return gm;
	}
	
	public int mini_max(game gm, int depth, double alpha, double beta, boolean max_ai, int chosen_move,
			double tm_mode, long startTime, boolean is_top, boolean cut_off, int color) {
		long endTime = System.currentTimeMillis();
		long elpTime = endTime - startTime;
		if (elpTime > tm_mode){
			cut_off = true;
			return Integer.MIN_VALUE;
		}
		int bad = -1;

		ArrayList<move> ply_moves = gm.legal_moves(color);
		ArrayList<move> emy_moves = gm.legal_moves(color);
		
		if (depth == 0){
			return eval_fun(gm, color, ply_moves.size(), emy_moves.size());
		}else if ((ply_moves.size() < 1) && (emy_moves.size() < 1)){
			return eval_fun(gm, color, ply_moves.size(), emy_moves.size());
		}
		//go to next depth
		if ((max_ai && ply_moves.size()<1) || (max_ai && emy_moves.size() < 1)){
			return mini_max(gm, depth -1, alpha, beta, !max_ai, bad,
					 tm_mode, startTime, false, cut_off, color);
		}
		int result;
		ArrayList<Integer> poss_moves = new ArrayList<Integer>();
 		if(max_ai){
 			result = Integer.MIN_VALUE;
 			for (int i = 0; i < ply_moves.size(); i++){
 				game gm_child = new game(gm);
 				make_move(gm_child, false, color, ply_moves.get(i));
 				
 				int tmp = mini_max(gm_child, depth -1, alpha, beta, false, bad,
 					 tm_mode, startTime, false, cut_off, color);
 				if (tmp > result){
 					result = tmp;
 					if (is_top){
 						poss_moves.clear();
 						poss_moves.add(i);
 					}
 				}else if (is_top && (tmp == result)){
 					poss_moves.add(i);
 				}
 				if (result > beta){
 					break;
 				}
 				if (result > alpha){
 					alpha = result;
 				}
 			}
 			if (is_top && poss_moves.size() > 0){
 				int randInt = (int) (Math.random() * poss_moves.size());
 				chosen_move = poss_moves.get(randInt);
 			}
 			if (is_top && poss_moves.size() > 0){
 				int randInt = (int) (Math.random() * poss_moves.size());
 				chosen_move = poss_moves.get(randInt);
 			}
 		}else{
 			result = Integer.MAX_VALUE;
 			for (int i = 0; i < emy_moves.size(); i++){
 				game gm_child = new game(gm);
 				make_move(gm_child, false, emy, ply_moves.get(i));
 				int tmp = mini_max(gm_child, depth -1, alpha, beta, true, bad,
 	 					 tm_mode, startTime, false, cut_off, color);
 				if (tmp < result){
 					result = tmp;
 				}
 				if (result < beta){
 					beta = result;
 				}
 				if (result < alpha){
 					break;
 				}
 			}
 		}
 		return result;
 }

	private int eval_fun(game gm, int color, int ply_moves, int emy_moves) {
		int her_val = MOBILITYWEIGHT * (ply_moves-emy_moves);
		int ply_num = 0;
		int emy_num = 0;
		for (int j = 1; j < 9 ;j++){
			for (int i = 1; i < 9; i++){
				if (gm.board[j][i] != gm.EMPTY){
					if (gm.board[j][i] == color){
						ply_num++;
						her_val += pt_table[j][i]*POSITIONWEIGHT;
					}else{
						emy_num++;
						her_val -= pt_table[j][i]*POSITIONWEIGHT;
					}
				}
			}
		}
		her_val += (ply_num-emy_num);
		return 0;
	}

	public int get_search_depth(){
		return search_depth;
	}
	
	public long get_search_time(){
		return search_time;
	}
	
	public int get_last_move(){
		return last_move;
	}
	
	public move minimax(node root, int color){
		double max = NEG_INFINITE;
		node best_node = null;
		
		for (node n: root.children){
			double tmp_min = getMin(n, color);
			if (best_node == null){
				max = tmp_min;
				best_node = n;
			}else if (tmp_min > max){
				max = tmp_min;
				best_node = n;
			}
		}
		return best_node.last;
	} 
	
	private double getMin(node n, int color) {
		double min = INFINITE;
		
		//if the node is a leaf -> potential end of game
		if (n.children.size() == 0){
			if (n.end == -1){
				//calculate the value
				min = n.position*POSITIONWEIGHT-n.mobility*MOBILITYWEIGHT;
			}else if (n.end == color){//win the game
				return min;
			}else if (n.end != color){//lose the game
				min = -ENDWEIGHT; //zero-sum
				return min;
			}
		}
		
		for(node k: n.children){
			double tmp_min = getMax(k, color);
			if (tmp_min < min){
				min = tmp_min;
			}
		}
		return min;
	}

	private double getMax(node n, int color) {
		double max = NEG_INFINITE;
		if (n.children.size() == 0){
			if (n.end == -1){
				//calculate the value
				max = -n.position*POSITIONWEIGHT-n.mobility*MOBILITYWEIGHT;
			}else if (n.end == color){//win the game
				return max;
			}else if (n.end != color){//lose the game
				max = -ENDWEIGHT; //zero-sum
				return max;
			}
		}
		
		for(node k: n.children){
			double tmp_max = getMin(k, color);
			if (tmp_max > max){
				max = tmp_max;
			}
		}
		return max;
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
	/*
	public game pt_sys(game gm, boolean finish, int color){
		
		if (!finish){
			move best_move = new move();
			move cur_move = new move();
			
			for (int j = 1; j < 9 ;j++){
				for (int i = 1; i < 9; i++){
					cur_move = gm.point_move(i, j, color, false, pt_table);
					if (cur_move.legal){
						if (cur_move.points > best_move.points || !best_move.legal){
							best_move = cur_move;
						}
					}
				}
				
			}
			if (best_move.legal){
				gm.point_move(best_move.x, best_move.y, color, true, pt_table);
				gm.board[best_move.y][best_move.x] = color;
			}
		}
		return gm;
	}
	
	public game random_sys(game gm, boolean finish, int color){
		
		int y = (int)(Math.random()*(gm.HEIGHT-2))+1;
		int x = (int)(Math.random()*(gm.WIDTH-2))+1;
		while (!finish && !gm.legal_move(x, y, color, true)){
			y = (int)(Math.random()*(gm.HEIGHT-2))+1;
			x = (int)(Math.random()*(gm.WIDTH-2))+1;
		}
		if (!finish){
			gm.board[y][x] = color;
		}
		return gm;
	}*/
	
	public void refill_points_2(){
		pt_table[1][1] = 99;
		pt_table[2][2] = -25;
		pt_table[1][2] = pt_table[2][1] = -25;
		pt_table[1][3] = pt_table[3][1] = 20;
		pt_table[1][4] = pt_table[4][1] = 10;
		pt_table[2][3] = pt_table[3][2] = 1;
		pt_table[2][4] = pt_table[4][2] = 1;
		pt_table[3][3] = COMMON;
		pt_table[4][4] = STARTER;
		
		POSITIONWEIGHT = 8;
		MOBILITYWEIGHT = 20;
		
		//top
		for (int i = 5; i < 9; i++){
			for (int j = 1; j< 5; j++){
				pt_table[j][i] = pt_table[j][9-i];
			}
		}
		//bottom
		for (int i = 1; i < 9; i++){
			for (int j = 5; j< 9; j++){
				pt_table[j][i] = pt_table[9-j][i];
			}
		}
		//outer boundary
		for (int j = 0; j < 10; j++){
			pt_table[j][0] = pt_table[j][9] = pt_table[0][j] = pt_table[9][j] = STARTER;
		}
		
		//System.out.println("switched 1");
	}
	public void refill_points() {
		pt_table[1][1] = 99;
		pt_table[2][2] = -24;
		pt_table[1][2] = pt_table[2][1] = -8;
		pt_table[1][3] = pt_table[3][1] = 8;
		pt_table[1][4] = pt_table[4][1] = 6;
		pt_table[2][3] = pt_table[3][2] = -4;
		pt_table[2][4] = pt_table[4][2] = -3;
		pt_table[3][3] = COMMON;
		pt_table[4][4] = STARTER;
		
		POSITIONWEIGHT = 10;
		MOBILITYWEIGHT = 15;

		//top
		for (int i = 5; i < 9; i++){
			for (int j = 1; j< 5; j++){
				pt_table[j][i] = pt_table[j][9-i];
			}
		}
		//bottom
		for (int i = 1; i < 9; i++){
			for (int j = 5; j< 9; j++){
				pt_table[j][i] = pt_table[9-j][i];
			}
		}
		//outer boundary
		for (int j = 0; j < 10; j++){
			pt_table[j][0] = pt_table[j][9] = pt_table[0][j] = pt_table[9][j] = STARTER;
		}
		//System.out.println("switched 2");
	}
	
	
}
