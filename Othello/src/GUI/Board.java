package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;

public class Board extends JFrame  implements ActionListener{
	final static int BLACK = 1;          // Declare state of each square
    final static int WHITE = 2;
    final static int EMPTY = 0;
    final static int OFFBOARD = -1;

    Player ply_1 = new Player();       // The players
    Player ply_2 = new Player();

    private game gm = new game();     // Game state
    private Timer timer;
    private static int time_window;
    private static long start_time, stop_time, run_time = 0;
    private int turn = BLACK; 
    private boolean black_finish = false; 
    private boolean white_finish = false;
    
	private Container CT;
	private JButton btn, btn2, btn3;
	private JButton[][] btns;
	private JLabel ply1, ply2, ply1_sc, ply2_sc, result, no_legal;
    private int count = 4;
	private int row=gm.HEIGHT-2;
	private int col=gm.WIDTH-2;
	private JPanel p,p1,p2, p3, all;
	private ImageIcon W, B, R, Y;
	private Image white, black, red, yellow;
	boolean ingame = false;
	boolean canmove = false;
	boolean End_Game = false;
	int gm_mode, tm_mode, ply_mode;
	private JTextArea log;
	private String prev_moves;
	private JScrollPane p4;
	int ini_pos_x, ini_pos_y;
	boolean refill = true, refill_2 = true;
	
	/*
	 * gm_mode: 1-p vs ai; 2-ai vs ai
	 * ply_mode: 0-black; 1-white
	 */
	
	public Board(int x, int y) {
		super("Let's Play Othello");
		ini_pos_x = x;
		ini_pos_y = y;
		////System.out.println("x: "+ini_pos_x+"; "+ ini_pos_y);
		
		init_game(gm);		
		CT=getContentPane();
		setSize(297,377);
		this.setBounds(400, 100, 400, 520);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initGUI();
	}
	
	public void init_game(game gm) {
		
		turn = BLACK;//always starts from BLACK
		
		//outer boundary
		for (int j = 0; j < gm.WIDTH; j++){
			gm.board[j][0] = gm.board[j][gm.WIDTH -1] = gm.board[0][j] 
					= gm.board[gm.WIDTH -1][j] = OFFBOARD;	
		}
		//game board
		for (int j = 1; j < gm.HEIGHT-1; j++){
			for (int i = 1; i < gm.WIDTH-1; i++){
				gm.board[j][i] = EMPTY;
			}
		}
		//default start position4,5; 5, 4; 4, 4; 5, 5
		gm.board[ini_pos_y+1][ini_pos_x+2] = gm.board[ini_pos_y+2][ini_pos_x+1] = BLACK;
		gm.board[ini_pos_y+1][ini_pos_x+1] = gm.board[ini_pos_y+2][ini_pos_x+2] = WHITE;
	}
	
	public void initGUI(){
		//map = new cell[8][8];
		try{
			white = ImageIO.read(getClass().getResource("Image/white.png"));
			black = ImageIO.read(getClass().getResource("Image/black.png"));
			red = ImageIO.read(getClass().getResource("Image/rred.png"));
			yellow = ImageIO.read(getClass().getResource("Image/yy.png"));
			W = new ImageIcon(white);
		    B = new ImageIcon(black);
			R = new ImageIcon(red);
			Y = new ImageIcon(yellow);
		}catch(IOException ex){
		}
		
		ply1 = new JLabel("Black:");
		ply1_sc = new JLabel();
		ply2 = new JLabel("White:");
		ply2_sc = new JLabel();
		

		btn = new JButton("START");

		btn.addActionListener(this);
		
		btn2 =new JButton("RESTART");
		btn2.setEnabled(false);
		btn2.addActionListener(this);
		
		btn3 =new JButton("Pass");
		btn3.setEnabled(false);
		btn3.addActionListener(this);
		
		result=new JLabel();
		result.setText("Black Turn");
		result.setVisible(false);
		//result.setVisible(true);
		
		no_legal=new JLabel();
		no_legal.setText("No Legal Move");
		no_legal.setVisible(false);
				
		btns=new JButton[row][col];
		
		p=new JPanel();
		p.setLayout(new BorderLayout());
		//CT.add(p);
		
		p1=new JPanel();
		p1.add(btn);
		p1.add(result);
		p1.add(btn2);
		
		p.add(p1,BorderLayout.CENTER);
		p2=new JPanel();
		p2.setLayout(new GridLayout(row,col,0,0));
		
		for(int j=0;j<row;j++){
			for(int i=0;i<col;i++){
				btns[j][i]=new JButton();
				btns[j][i].setMargin(new Insets(0,0,0,0));
				//btns[i][j].setText(String.valueOf(i*10+j));
				btns[j][i].setName(String.valueOf(j*10+i));
				btns[j][i].setEnabled(false);
				btns[j][i].addActionListener(this);
				btns[j][i].setPreferredSize(new Dimension(50, 50));
				p2.add(btns[j][i]);
			}
		}
		
		btns[ini_pos_y][ini_pos_x].setIcon(W);
		btns[ini_pos_y+1][ini_pos_x+1].setIcon(W);
		btns[ini_pos_y][ini_pos_x+1].setIcon(B);
		btns[ini_pos_y+1][ini_pos_x].setIcon(B);
		
		p3=new JPanel();
		p3.setLayout(new GridLayout(2,3,0,0));
		p3.add(ply1);
		p3.add(ply1_sc);
		p3.add(no_legal);
		//p3.add(btn3);
		p3.add(ply2);
		p3.add(ply2_sc);
		//p3.add(result);
		p3.add(btn3);
		
		log = new JTextArea();
        log.setEditable(false);

        prev_moves = "Activity Log:  \n";
        log.setText(prev_moves);

        p4 = new JScrollPane(log);
		//p4.setLayout(new GridLayout(2,3,0,0));
		p4.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		setPreferredSize(new Dimension(500, 520));
		
		//setSize(297,377);
		//this.setBounds(400, 100, 400, 520);
		
		CT.add(p, BorderLayout.NORTH);
		CT.add(p3, BorderLayout.CENTER);	
		CT.add(p2, BorderLayout.AFTER_LAST_LINE);
		CT.add(p4, BorderLayout.WEST);
		
		// closing the GUI
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
				// Closing client connection
			////System.out.println("GameGUI: Closing...");
			}
		});
		
	}
	
	public void player_move(){
		if (turn == BLACK){
			////System.out.println("chk pt 1");
			//no_legal.setVisible(false);
			black_move();
			////System.out.println("chk pt 3");
			turn = WHITE;
			result.setText("White Turn");
			//result.setVisible(false);
			if (gm_mode == 1 && ply_mode == 1){
				timer.stop();
			}
			
		}else{
			//.setVisible(false);
			white_move();
			turn = BLACK;
			//no_legal.setVisible(false);
			//clear_pos_moves();
			result.setText("Black Turn");
			//result.setVisible(false);
			if (gm_mode == 1 && ply_mode == 0){
				timer.stop();
				//result.setText("Your Turn");
				//result.setVisible(true);
			}
		}
	}

	public void black_move() {
		black_finish = true;
		for (int j = 1; j < gm.HEIGHT-1; j++){
			for (int i = 1; i < gm.WIDTH-1; i++){
				if (gm.legal_move(i, j, BLACK, false)){
					black_finish = false;
				}
			}
		}
		////System.out.println("chk pt 2: " + black_finish);
		if (black_finish){
			//no_legal.setVisible(true);
				if ((gm_mode == 1 && ply_mode == 0) || gm_mode == 0){
					btn3.setEnabled(true);
					timer.start();
					//result.setText("Black: No Legal Move");
					//result.setVisible(true);
				}else if (gm_mode == 2){
					//timer.stop();
					//result.setVisible(false);
					//result.setText("No Legal Move");
					////System.out.println("chk pt 2.2: " + result.getText());
					//timer.start();
					//result.setVisible(true);
				}
		}
		if (gm_mode == 2){
			ply_1.getState(gm, black_finish, BLACK);//ai
			int tmp = ply_1.get_last_move();
			int y = tmp/10;
			int x = tmp % 10;
			prev_moves += "Black Player: (" + (x-1) + "," + (y-1) +")\n"
					+ "Search Depth: " + ply_1.get_search_depth() +"\n"+"Search Time: " + ply_1.get_search_time() +" milliseconds\n";
			log.setText(prev_moves);
		}else if (gm_mode == 1 && ply_mode == 1) {
			ply_1.getState(gm, black_finish, BLACK);//ai
			int tmp = ply_1.get_last_move();;
			int y = tmp/10;
			int x = tmp % 10;
			prev_moves += "Black Player: (" + (y-1) + "," + (x-1) +")\n";
			////System.out.println("Search Depth: " + gm.search_depth +"\n"+"Search Time: " + gm.search_time +" milliseconds\n");
			log.setText(prev_moves);
		}
	}
	
	public void white_move() {
		white_finish = true;
		for (int j = 1; j < gm.HEIGHT-1; j++){
			for (int i = 1; i < gm.WIDTH-1; i++){
				if (gm.legal_move(i, j, WHITE, false)){
					white_finish = false;
				}
			}
		}
		////System.out.println("chk pt 4: " + white_finish);
		if (white_finish){
			//no_legal.setVisible(true);
			if ((gm_mode == 1 && ply_mode == 1) || gm_mode == 0){
				btn3.setEnabled(true);
				timer.start();
				//result.setText("No Legal Move");
				//result.setVisible(true);
			}else if (gm_mode == 2){
				//result.setText("No Legal Move");
				//result.setVisible(true);
			}
		}
		////System.out.println("white move: legal checked");
		if (gm_mode == 2 ){//|| (gm_mode == 1 && ply_mode == 0)){
			ply_2.getState(gm, white_finish, WHITE);//ai
			int tmp = ply_2.get_last_move();
			int y = tmp/10;
			int x = tmp % 10;
			prev_moves += "White Player: (" + (y-1) + "," + (x-1) +")\n";
			//System.out.println("Search Depth: " + gm.search_depth +"\n"+"Search Time: " + gm.search_time +" milliseconds\n");
			log.setText(prev_moves);
		}else if (gm_mode == 1 && ply_mode == 0){
			ply_2.getState(gm, white_finish, WHITE);//ai
			int tmp = ply_2.get_last_move();
			int y = tmp/10;
			int x = tmp % 10;
			prev_moves += "White Player: (" + (y-1) + "," + (x-1) +")\n";
			////System.out.println("Search Depth: "+ gm.search_depth +"\n"+"Search Time: " + gm.search_time +" milliseconds\n");
			log.setText(prev_moves);
		}
	}
	
	
	public void place_discs(){
       int black_sc = 0;                     
       int white_sc = 0; 
       //clear_pos_moves();
      
       //display_legal();
       
       //clear_pos_moves();
       for (int j=1; j<gm.HEIGHT-1; j++) {        
           for (int i=1; i < gm.WIDTH-1; i++) {
               //place the discs
               if (gm.board[j][i] == BLACK){
            	   //if (btns[j-1][i-1].getIcon() != B)
            	   btns[j-1][i-1].setIcon(B);
            	   btns[j-1][i-1].removeActionListener(this);
            	   black_sc++;
               }
               else if (gm.board[j][i] == WHITE){
            	  // if (btns[j-1][i-1].getIcon() != W)
            	   btns[j-1][i-1].setIcon(W);
            	   btns[j-1][i-1].removeActionListener(this);
            	   white_sc++;
               }else{
            	   btns[j-1][i-1].setIcon(null);
            	   btns[j-1][i-1].removeActionListener(this);
               }
               //Show the legal moves for the current player
               if (turn == BLACK && gm.legal_move(i,j,BLACK,false)) {
            	   btns[j-1][i-1].setIcon(R);
            	   btns[j-1][i-1].addActionListener(this);//only able to click the legal positions
               }
               // If other player cannot move, current player cleans up
               else if (turn == WHITE && gm.legal_move(i,j,WHITE,false)) {
            	   btns[j-1][i-1].setIcon(Y);
            	   btns[j-1][i-1].addActionListener(this);//only able to click the legal positions
               }
           }
       }
       
       ply1_sc.setText(new Integer(black_sc).toString());
       ply2_sc.setText(new Integer(white_sc).toString());
       
       
       //check anymore move
       End_Game = true;
       for (int j=1; j<gm.HEIGHT-1; j++) {        
           for (int i=1; i < gm.WIDTH-1; i++) {
        	   if (gm.legal_move(i, j, BLACK, false) || gm.legal_move(i, j, WHITE, false)){
        		   End_Game = false;
        	   }
           }
       }
       //end of game
       if (End_Game){
    	   int blk_gm =0;
    	   int blk_dis =0;
    	   int wht_gm =0;
    	   int wht_dis =0;
    	   
    	   if (black_sc > white_sc){
    		   result.setText("Black Player Won");
    	   }else if (black_sc == white_sc){
    		   result.setText("Tied Game");
    	   }else{
    		   result.setText("White Player Won");
    	   }
    	   //result.setVisible(true);
    	   for(int i=0;i<row;i++){
				for(int j=0;j<col;j++){
					if (gm.board[j+1][i+1] == WHITE){
						wht_gm++;
					}
					if (gm.board[j+1][i+1] == BLACK){
						blk_gm ++;
					}
					if (btns[j][i].getIcon() == W){
						wht_dis ++;
					}
					if (btns[j][i].getIcon() == B){
						blk_dis++;
					}
					//btns[i][j].setEnabled(false);
				}
    	   }
    	   //////System.out.println("blk" + (blk_gm-blk_dis));
    	   ////System.out.println("wht" + (wht_gm-wht_dis));
    	   timer.stop();
    	   
       }
	}

	public void go(int g, int t, int p){		
		setVisible(true);
		gm_mode = g;
		tm_mode = t * 1000;
		ply_mode = p;
		
		/*
		 * gm_mode: 1-p vs ai; 2-ai vs ai
		 * ply_mode: 0-black; 1-white
		 */
		
		//p vs ai
		if (gm_mode == 0){
			ply_1 = new human();
			ply_2 = new human();
		}
		else if (gm_mode == 1){
			if (ply_mode == 0){
				ply_1 = new human(); //black
				ply_2 = new ai(gm_mode, tm_mode, ply_mode); //white
			}else{
				ply_2 = new human(gm_mode, tm_mode, ply_mode);
				ply_1 = new ai(gm_mode, tm_mode, ply_mode);
			}
		}else{//ai vs ai
			ply_1 = new ai(gm_mode, tm_mode, 0);
			ply_2 = new ai(gm_mode, tm_mode, 1);
		}
		
		//ingame = false;
		//while (!End_Game){
			//if (ingame){
			//place_discs();
		////System.out.println("start");
		
		//while (!End_Game){
		timer =new Timer(tm_mode + 500, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//timer.stop();
				//clear_pos_moves();
				//display_legal();
				//timer.restart();
				int score = Integer.parseInt(ply1_sc.getText())+Integer.parseInt(ply2_sc.getText());
				if (score > 20 && refill){
					if (gm_mode == 2){
						ply_1.refill_points();
						ply_2.refill_points();
						refill = false;
					}else if (gm_mode == 1 && ply_mode == 0){
						ply_2.refill_points();
						refill = false;
					}else if (gm_mode == 1 && ply_mode == 1){
						ply_1.refill_points();
						refill = false;
					}
				}else if (score > 40 && refill_2){
					if (gm_mode == 2){
						ply_1.refill_points_2();
						ply_2.refill_points_2();
						refill_2 = false;
					}else if (gm_mode == 1 && ply_mode == 0){
						ply_2.refill_points_2();
						refill_2 = false;
					}else if (gm_mode == 1 && ply_mode == 1){
						ply_1.refill_points_2();
						refill_2 = false;
					}
				}
				////System.out.println(turn);
				player_move();
				place_discs();
				//timer.stop();
				//clear_pos_moves();
				//place_discs();
				////System.out.println(turn);
				//timer.restart();
			}
		});//}
			//clear_pos_moves();
			//place_discs();
			//}
		//
		
		//System.out.println("mode: "+gm_mode + "; "+ tm_mode+" sec; "+" player: "+ply_mode);
	}

	public static void main(String[] args){
		Board demo = new Board(3, 3);
		demo.go(2, 1, 0);
	}
	
	public void enable_btns(){
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				btns[i][j].addActionListener(this);
			}
		}
	}
	
	public void disable_btns(){
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				btns[i][j].removeActionListener(this);
			}
		}
	}
	public void actionPerformed(ActionEvent e) {	
		//start
		if(e.getSource()==btn){
			ingame = true;
			//setup initial positions
			ply1_sc.setText("2");
			ply2_sc.setText("2");
			
			for(int j=0;j<row;j++){
				for(int i=0;i<col;i++){
					btns[j][i].setEnabled(true);
				}
			}
			
			if (gm_mode == 1 && ply_mode == 1){
				enable_btns();
				timer.start();
			}else if (gm_mode == 0){
				enable_btns();
			}else{
				disable_btns();
				if (gm_mode == 2){
					timer.start();
				}else if (gm_mode == 1){
					//result.setText("Your Turn");
					//result.setVisible(true);
				}
			}
				
			btn.setEnabled(false);
			btn2.setEnabled(true);
			result.setVisible(true);
			//timer.start();
			
			//turn = BLACK;
			place_discs();
			////System.out.println("sstart");
			//gameloop();
		}
		//restart
		else if(e.getSource()==btn2){
			setVisible(false);
			timer.stop();
			new Lobby("Welcome To Othello").go();
		}else if(e.getSource()==btn3){
			if (turn == BLACK){
				turn = WHITE;
				place_discs();
				timer.start();
			}else{
				turn = BLACK;
				place_discs();
				timer.start();
			}
			//player_move();
			//result.setText("GAME MESSAGE");
			//result.setVisible(false);
		}else{
			//handle human movements
			int tmp = Integer.parseInt(((JButton) e.getSource()).getName());
			int y = tmp/10;
			int x = tmp % 10;
			
			////System.out.println(x+ " , " +y);
			////System.out.println("color: "+gm.board[y+1][x+1]);
			
			if (gm_mode == 0){
				if(turn == BLACK){
					if (!gm.legal_move(x+1, y+1, BLACK, true)){
						//no_legal.setVisible(true);
						//result.setText("Please Make a Legal Move.");
						//result.setVisible(true);
					}else{
						gm.board[y+1][x+1] = BLACK;
						////System.out.println("now color: "+gm.board[y+1][x+1]);
						prev_moves += "Black Player: (" + y + "," + x +")\n";
						log.setText(prev_moves);
	         		    ////System.out.println(prev_moves);
						//place_discs()
						black_finish = true;
						for (int j = 1; j < gm.HEIGHT-1; j++){
							for (int i = 1; i < gm.WIDTH-1; i++){
								if (gm.legal_move(i, j, BLACK, false)){
									black_finish = false;
								}
							}
						}
						turn = WHITE;
						btn3.setEnabled(false);
						//player_move();
						place_discs();
						white_move();
					}
				}else if (turn == WHITE){
					if (!gm.legal_move(x+1, y+1, WHITE, true)){
						//no_legal.setVisible(true);
						//result.setText("Please Make a Legal Move.");
						//result.setVisible(true);
					}else{
						gm.board[y+1][x+1] = WHITE;
						prev_moves += "White Player: (" + y + "," + x +")\n";
						log.setText(prev_moves);
						//place_discs();
						white_finish = true;
						for (int j = 1; j < gm.HEIGHT-1; j++){
							for (int i = 1; i < gm.WIDTH-1; i++){
								if (gm.legal_move(i, j, WHITE, false)){
									white_finish = false;
								}
							}
						}
						turn = BLACK;
						btn3.setEnabled(false);
						place_discs();
						black_move();
					}
				}
			}else{
				if(turn == BLACK && ply_mode == 0){
					if (!gm.legal_move(x+1, y+1, BLACK, true)){
						//no_legal.setVisible(true);
						//result.setText("Please Make a Legal Move.");
						//result.setVisible(true);
					}else{
						gm.board[y+1][x+1] = BLACK;
						////System.out.println("now color: "+gm.board[y+1][x+1]);
						prev_moves += "Black Player: (" + y + "," + x +")\n";
						log.setText(prev_moves);
	         		   ////System.out.println(prev_moves);
						//place_discs();
						black_finish = true;
						for (int j = 1; j < gm.HEIGHT-1; j++){
							for (int i = 1; i < gm.WIDTH-1; i++){
								if (gm.legal_move(i, j, BLACK, false)){
									black_finish = false;
								}
							}
						}
						turn = WHITE;
						btn3.setEnabled(false);
						long startTime = System.currentTimeMillis();
						place_discs();
						long endTime = System.currentTimeMillis();
						////System.out.println("That took " + (endTime - startTime) + " milliseconds");
	
						timer.start();
						//player_move();
						//place_discs();
						//white_move();
					}
				}else if (turn == WHITE && ply_mode == 1){
					if (!gm.legal_move(x+1, y+1, WHITE, true)){
						//no_legal.setVisible(true);
						//result.setText("Please Make a Legal Move.");
						//result.setVisible(true);
					}else{
						gm.board[y+1][x+1] = WHITE;
						prev_moves += "White Player: (" + y + "," + x +")\n";
						log.setText(prev_moves);
						//place_discs();
						white_finish = true;
						for (int j = 1; j < gm.HEIGHT-1; j++){
							for (int i = 1; i < gm.WIDTH-1; i++){
								if (gm.legal_move(i, j, WHITE, false)){
									white_finish = false;
								}
							}
						}
						turn = BLACK;
						btn3.setEnabled(false);
						place_discs();
						timer.start();
						//black_move();
					}
				}
			}
		}
	}

}
