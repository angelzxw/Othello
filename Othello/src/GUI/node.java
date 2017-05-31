package GUI;
import java.util.ArrayList;

public class node {

	move last;
	game state;
	int turn;
	
	int position = 0;
	
	int mobility = 0;
	
	/*
	 * -1: not over
	 * 0: tie
	 * 1: black wins
	 * 2: white wins
	 */
	int end = -1;
	
	// Parent and children
	node parent;
	ArrayList<node> children = new ArrayList<node>();

	public node(move last, game state, int turn){
		this.last = last;
		this.state = state;
		this.turn = turn;
	}
	
	public game get_game(){
		return state;
	}
	
	public int get_turn(){
		return turn;
	}
	
	
	public void addChild(node newChild)
	{
		if (children == null){
			////System.out.println("Yes");
		}
		else{
			children.add(newChild);
		}
	}

}
