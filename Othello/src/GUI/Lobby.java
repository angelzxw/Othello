package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Lobby extends JFrame  implements ActionListener{

	private Container CT;
	private JButton btn, btn_m1, btn_m2, btn_m3, btn_ply1, btn_ply2;
	private JButton[] btns;
	private JLabel g_mode, t_mode, errmsg, p_mode, x_pos, y_pos, instr_1, instr_2;
	private JTextField t1, tx, ty;
	private JPanel p, p0, p1, p2, p3;
	/*
	 * gm_mode: 0- p vs p; 1-p vs ai; 2-ai vs ai
	 * ply_mode: 0-black; 1-white
	 */
	int gm_mode, tm_mode = -1, ply_mode = 0;
	int ini_pos_x = 3, ini_pos_y = 3;
	boolean val_game = false, val_time = false;
	
	public Lobby(String title){
		super(title);
		CT=getContentPane();
		//setSize(100,300);
		this.setBounds(400, 300, 460, 270);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Timer(1000,(ActionListener) this);
		initGUI();	
	}
	
	public void initGUI(){

		g_mode= new JLabel("Game Mode: ", JLabel.RIGHT);
		btn_m1 =new JButton("P vs P");
		btn_m2 =new JButton("P vs AI");
		p_mode= new JLabel("Would You Be: ", JLabel.RIGHT);
		btn_ply1 = new JButton("Black Player");
		btn_ply2 = new JButton("White Player");
		btn_ply1.setEnabled(false);
		btn_ply2.setEnabled(false);
		btn_m3 =new JButton("AI vs AI");
		btn_m1.addActionListener(this);
		btn_m2.addActionListener(this);
		btn_ply1.addActionListener(this);
		btn_ply2.addActionListener(this);
		btn_m3.addActionListener(this);
		
		t_mode = new JLabel("Time Window (3-60 sec): ", JLabel.RIGHT);
		t1=new JTextField(2);
		t1.setEnabled(true);
		instr_1 = new JLabel("Optional: Specify Initial ", JLabel.RIGHT);
		instr_2 = new JLabel("Position of Upperleft White Stone", JLabel.LEFT);
		x_pos = new JLabel("x: ", JLabel.RIGHT);
		y_pos = new JLabel("y: ", JLabel.RIGHT);
		tx=new JTextField(2);
		ty=new JTextField(2);
		
		btn =new JButton("START");
		btn.addActionListener(this);
		btn.setPreferredSize(new Dimension(100, 30));
	    
		errmsg = new JLabel();
		
		p=new JPanel();
		p.setLayout(new BorderLayout());
		
		p0=new JPanel();
		p1=new JPanel();
		p2=new JPanel();
		p3=new JPanel();
		
		p0.add(errmsg);
		
		p1.setLayout(new GridLayout(2,3,0,0));
		p1.add(g_mode);
		p1.add(btn_m1);
		p1.add(btn_m2);
		p1.add(btn_m3);
		p1.add(p_mode);
		p1.add(btn_ply1);
		p1.add(btn_ply2);
		
		p2.setLayout(new GridLayout(4,1,0,0));
		p2.add(t_mode, BorderLayout.NORTH);
		p2.add(t1);
		p2.add(instr_1);
		p2.add(instr_2);
		p2.add(x_pos, BorderLayout.CENTER);
		p2.add(tx);
		p2.add(y_pos, BorderLayout.SOUTH);
		p2.add(ty);

		p3.add(btn);
		
		p.add(p1, BorderLayout.NORTH);
		p.add(p2, BorderLayout.CENTER);
		p.add(p3, BorderLayout.SOUTH);
		
		CT.add(p,BorderLayout.NORTH);
		CT.add(p0, BorderLayout.SOUTH);
		
		// closing the GUI
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();

				// Closing client connection
				//System.out.println("GameGUI: Closing client connection...");
			}
		});

	}
	public void go(){		
		setVisible(true);
	}
	public static void main(String[] args){
		new Lobby("Welcome To Othello").go();
	}

	public void actionPerformed(ActionEvent e) {	
		
		errmsg.setText("");
		btn_ply1.setBorderPainted(true);
		btn_ply2.setBorderPainted(true);
		if(e.getSource()==btn_m1){
			////System.out.println("m1");
			btn_m1.setBorderPainted(false);
			btn_m2.setBorderPainted(true);
			btn_m3.setBorderPainted(true);
			t1.setEnabled(false);
			gm_mode = 0;
			val_game = true;
		} else if (e.getSource()==btn_m2){
			////System.out.println("m2");
			btn_m2.setBorderPainted(false);
			//btn_m1.setBorderPainted(true);
			btn_m3.setBorderPainted(true);
			t1.setEnabled(true);
			btn_ply1.setEnabled(true);
			btn_ply2.setEnabled(true);
			gm_mode = 1;
			val_game = true;
		}else if (e.getSource()==btn_m3){
			////System.out.println("m3");
			btn_m3.setBorderPainted(false);
			//btn_ply1.setBorderPainted(true);
			//btn_ply2.setBorderPainted(true);
			t1.setEnabled(true);
			btn_ply1.setEnabled(false);
			btn_ply2.setEnabled(false);
			//btn_m1.setBorderPainted(true);
			btn_m2.setBorderPainted(true);
			gm_mode = 2;
			val_game = true;
		}
		if (gm_mode == 1){
			if (e.getSource()==btn_ply1){
				ply_mode = 0;
				btn_ply1.setBorderPainted(false);
				btn_ply2.setBorderPainted(true);
			}else if (e.getSource()==btn_ply2){
				ply_mode = 1;
				btn_ply1.setBorderPainted(true);
				btn_ply2.setBorderPainted(false);
			}
		}
		
		if (e.getSource()==btn){
			//errmsg.setText("");
			try {
				tm_mode = Integer.parseInt(t1.getText());
				ini_pos_x = Integer.parseInt(tx.getText());
				ini_pos_y = Integer.parseInt(ty.getText());
				} catch (NumberFormatException ex) {
					//tm_mode = -1;
			}
			if (!((ini_pos_x >= 0 && ini_pos_x <=6) && (ini_pos_y >= 0 && ini_pos_y <=6))){
					ini_pos_x = 3;
					ini_pos_y = 3;
			}
			//System.out.println("x: "+ini_pos_x+"; "+ ini_pos_y);
			if ((val_game && tm_mode >=3 && tm_mode <=60) || (val_game && gm_mode == 0)){
				//new Board(gm_mode, tm_mode, ply_mode);
				new Board(ini_pos_x, ini_pos_y).go(gm_mode, tm_mode, ply_mode);
				setVisible(false);
				//System.out.println("mode: "+gm_mode +"; "+ tm_mode+" sec; "+ " player: "+ply_mode);
			}else{
				errmsg.setText("Please Select a Game Mode and Enter a Valid Time");
			}
		}
	}
}
