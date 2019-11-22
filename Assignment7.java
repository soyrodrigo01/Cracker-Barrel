//used code from https://github.com/rationalcoder/peg-game/blob/master/Cracker.java
// As a guide for the Move(s), board, and stepping classes.

import java.util.*;

class Move{

	public int f;
	public int o;
	public int t;

	//defines a move
	public Move(int f, int o, int t){
		this.f = f;
		this.o = o;
		this.t = t;
	}

	//reverses the moves
	public Move backwards(){
		return new Move(t, o, f);
	}

	//returns a string with the moves
	@Override
	public String toString(){
		return "(" + f + ", " + o + ", " + t + ")";
	}
}

class Moves implements Iterable<Move>{
	public static final Move[] moves = {
		new Move(0,1,3), new Move(0,2,5), new Move(1,3,6), new Move(1,4,8),
		new Move(2,4,7), new Move(2,5,9), new Move(3,6,10), new Move(3,7,12),
		new Move(4,7,11), new Move(4,8,13), new Move(5,8,12), new Move(5,9,14),
		new Move(3,4,5), new Move(6,7,8), new Move(7,8,9), new Move(10,11,12),
		new Move(11,12,13), new Move(12,13,14)
	};

	@Override
	public Stepping iterator(){
		return new Stepping(moves);
	}
}

class Stepping implements Iterator<Move>{
	private Move backwards;
	private int p;
	private Move[] moves;

	public Stepping(Move[] moves){
		this.moves = moves;
		this.p = 0;
	}

	@Override
	public Move next(){
		if(backwards != null){
			Move temp = backwards;
			backwards = null;
			return temp;
		}

		Move temp = moves[p++];
		backwards = temp.backwards();

		return temp;
	}

	@Override
	public boolean hasNext(){
		return p < moves.length || (p == moves.length && backwards != null);
	}

}



class Board{
	public int[] spots;
	public int count;

	//initializes an empty board
	public Board(int nothing){
		count = 14;
		spots = new int[15];
		for(int i = 0; i < 15; i++){
			spots[i] = i == nothing ? 0 : 1;
		}
	}

	public Board(int count, int[] spots){
		this.count = count;
		this.spots = spots.clone();
	}

	public Board move(Move m){
		if(spots[m.o] == 1 && spots[m.f] == 1 && spots[m.t] == 0){
			Board newBoard = new Board(count-1, spots.clone());
			newBoard.spots[m.f] = 0;
			newBoard.spots[m.o] = 0;
			newBoard.spots[m.t] = 1;

			return newBoard;
		}

		return null;
	}
}

public class Assignment7{
	static Moves moves(){
		return new Moves();
	}

	static ArrayList<LinkedList<Move>> solve(Board b){
		ArrayList<LinkedList<Move>> temp = new ArrayList<LinkedList<Move>>();
		solve(b, temp, 0);
		return temp;
	}

	static LinkedList<Move> sol(Board b){
		ArrayList<LinkedList<Move>> temp = new ArrayList<LinkedList<Move>>();
		solve(b, temp, 1);

		if(temp.size() == 0){
			return null;
		}

		return temp.get(0);
	}


	// generator that yields all possible solutions
	// given a cell configuration
	static void solve(Board kd, ArrayList<LinkedList<Move>> sols, int i){
		if(kd.count == 1){
			sols.add(new LinkedList<Move>());
			return;
		}

		for(Move m : moves()){
			Board temp = kd.move(m);
			if(temp == null){
				continue;
			}

			ArrayList<LinkedList<Move>> tail = new ArrayList<LinkedList<Move>>();
			solve(temp, tail, i);

			for(LinkedList<Move> sol : tail){
				sol.add(0, m);
				sols.add(sol);

				if(sols.size() == i){
					return;
				}
			}
		}
	}

	static void print(Board kd){
		System.out.print("(" + kd.count + ", [");
		for(int i = 0; i < kd.spots.length; i++){
			if(i < kd.spots.length-1){
				System.out.print(kd.spots[i] + ", ");
			}
			else{
				System.out.print(kd.spots[i] + "])");
			}
		}
		System.out.println();
	}

	//shows the result by printing out successive states
	static void show(Board kd){
		int[][] lines = {{4,0,0},{3,1,2},{2,3,5},{1,6,9},{0,10,14}};
		for( int[] l : lines){
			int spaces = l[0];
			int b = l[1];
			int end = l[2];

			String space = new String();
			for(int i = 0; i < spaces; i++){
				space += " ";
			}

			System.out.print(space);
			for(int i = b; i <= end; i++){
				if(kd.spots[i] == 0){
					System.out.print(". ");
				}
				else{
					System.out.print("x ");
				}
			}

			System.out.println();
		}
	}

	//replay a sequence of moves, showing the state of cells 
	static void replay(List<Move> ms, Board kd)
	{
		show(kd);
		for(Move m : ms){
			kd = kd.move(m);
			show(kd);
		}
	}

	//prints out a terse view of solutions for each missing peg 
	static void terse(){
		for(int i = 0; i < 15; i++){
			Board kd = new Board(i);
			print(kd);
			List<Move> ms = sol(kd);
			for(Move m : ms){
				System.out.println(m);
				kd = kd.move(m);
			}
			print(kd);
			System.out.println();
		}
	}

	// visualizes a solution for each first 5 positions
	// others look the same after 120 degrees rotations
	static void go(){
		for(int i = 0; i < 5; i++){
			System.out.println("=== " + i + " ===");
			Board kd = new Board(i);
			replay(sol(kd), kd);
			System.out.println();
		}
	}

	public static void main(String[] args){
		go();
		terse();
	}
}


