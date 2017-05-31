package testing;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;

public class Board extends JFrame  implements ActionListener{
	
	private Container CT;
	private JButton btn;
	private JButton[][] btns;
	private JLabel ply1, ply2, ply1_sc, ply2_sc, result;
    private int count;

	private Timer timer;
	private int row=8;
	private int col=8;
	private int bon=10;
	private int[][] a;
	private int b;
	private int[] a1;
	private JPanel p,p1,p2, p3;
	
	public Board(){
		super("Let's Play Othello");
		CT=getContentPane();
		setSize(297,377);
		this.setBounds(400, 100, 400, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timer =new Timer(1000,(ActionListener) this);
		a = new int[row+2][col+2];
		initGUI();	
	}
	
	public void initGUI(){

		ply1=new JLabel("Player (Black)");
		ply1_sc=new JLabel("2");
		ply2=new JLabel("Player (White)");
		ply2_sc=new JLabel("2");
		
		btn =new JButton("START");
		btn.addActionListener(this);
		
		result=new JLabel("");
		
		btns=new JButton[row][col];
		
		p=new JPanel();
		p.setLayout(new BorderLayout());
		CT.add(p);
		
		p1=new JPanel();

		p1.add(btn);
		p1.add(result);
		
		p.add(p1,BorderLayout.CENTER);
		p2=new JPanel();
		p2.setLayout(new GridLayout(row,col,0,0));
		
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
			btns[i][j]=new JButton("");
			btns[i][j].setMargin(new Insets(0,0,0,0));
			btns[i][j].setBackground(Color.RED);
			//btns[i].setFont(new Font(null,Font.BOLD,25));
			btns[i][j].addActionListener(this);
			p2.add(btns[i][j]);
			}
		}
		
		p3=new JPanel();
		p3.add(ply1);
		p3.add(ply1_sc);
		p3.add(ply2);
		p3.add(ply2_sc);
		
		CT.add(p,BorderLayout.NORTH);
		CT.add(p2,BorderLayout.CENTER);	
		CT.add(p2,BorderLayout.SOUTH);		
	}
	
	public void go(){		
		setVisible(true);
	}
	
	public static void main(String[] args){
		Board demo = new Board();
		demo.go();
	}
	
	public void actionPerformed(ActionEvent e) {	
		System.out.println("gaming");
	}

}
