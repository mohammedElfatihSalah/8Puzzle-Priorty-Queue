import java.util.Iterator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver{

	private boolean solvable;
	private int numberOfMoves;
	private Node t;
	public Solver(Board board){

		if(board == null) throw new IllegalArgumentException("null board");

		//1 Board twin = board.twin();

		Node initialNode = new Node(null, board , 0);
		//2 Node initialTwinNode = new Node(null, twin, 0); 

		MinPQ<Node> heapOrigin = new MinPQ<Node>();
		//3 MinPQ<Node> heapTwin   = new MinPQ<Node>();


		heapOrigin.insert(initialNode);
		//4 heapTwin.insert(initialTwinNode);

		boolean solved = false;
		//int i = 0 ;
		Node next = null;
		//5  nextTwin = null;
		int i = 0;
		while(i < 1000000){
			i++;
			next = heapOrigin.delMin();
			//6 nextTwin = heapTwin.delMin();

			if(next.getStatus().isGoal()) {
				solved = true;
				break;
			}

			/* 7if(nextTwin.getStatus().isGoal()) {
				break;
			}*/

			for(Board b : next.getStatus().neighbors()){
				if(next.getParent()!= null){
					if(next.getParent().getStatus().equals(b)) continue;
				}

				heapOrigin.insert(new Node(next,b,next.getNumberOfMoves()+1));
			}

			/*8 for(Board b : nextTwin.getStatus().neighbors()){
				if(nextTwin.getParent() != null){
					if(nextTwin.getParent().getStatus().equals(b)) continue;
				}

				heapTwin.insert(new Node(next,b,next.getNumberOfMoves()+1));
			}*/

			
		}

		solvable = solved;
		if(solved){
			t = next;
			numberOfMoves = t.getNumberOfMoves();
		}
		
	}

	public boolean isSolvable(){
		return solvable;
	}

	public int moves(){

		if(!solvable) return -1;
		return numberOfMoves;
	}

	public Iterable<Board> solution(){
		if(!solvable) return null;
		return new BoardIterable(t);
	}

	private class Node implements Comparable<Node>{
		private Node parent;
		private Board board;
		private int numberOfMoves;
		private int manhanttan;
		private int hamming;

		public Node(Node parent , Board board , int numberOfMoves){
			this.parent = parent;
			this.board  = board;
			this.numberOfMoves = numberOfMoves;
			this.hamming = board.hamming();
			this.manhanttan = board.manhattan();
		}

		public int compareTo(Node node){
			if(this.getManhanttan() + this.getNumberOfMoves() == node.getManhanttan() + node.getNumberOfMoves()) return 0;
			else if(this.getManhanttan() + this.getNumberOfMoves() > node.getManhanttan() + node.getNumberOfMoves()) return 1;
			else return -1;
		}

		public int getNumberOfMoves(){
			return numberOfMoves;
		}

		public Node getParent(){
			return parent;
		}

		public Board getStatus(){
			return board;
		}

		public int getManhanttan(){
			return manhanttan;
		}

		public int getHamming(){
			return hamming;
		}


	}

	private class BoardIterable implements Iterable<Board>{

		Node node;
		public BoardIterable(Node node){
			this.node = node;
		}
		public Iterator<Board> iterator(){
			return new BoardIterator(node);
		}

		private class BoardIterator implements Iterator<Board>{

			Node node;
			int current = 0;
			int n = 0;
			Board[] board;
			public BoardIterator(Node node){
				this.node = node;
				n = node.getNumberOfMoves();
				board = new Board[n+1];

				for(int i = n; i >= 0; i--){
					board[i] = node.getStatus();
					node = node.getParent();
				}

			}

			public boolean hasNext(){
				return current <= n ;
			}

			public Board next(){
				return board[current++];
			}
		}
	}

	public static void main(String[] args){

		In in = new In(args[0]);
		int n = in.readInt();
		int[][] tiles = new int[n][n];
		

		for(int i = 0 ; i < n; i++){
			for(int j = 0; j< n; j++){
				tiles[i][j] = in.readInt();
			}
		}

		Board initial = new Board(tiles);
		Solver solver = new Solver(initial);

		if(solver.isSolvable()){
			StdOut.println("Minimum number of moves = "+solver.moves());
			for(Board b : solver.solution())
				StdOut.println(b);
		}else{
			StdOut.println("No solution possible");
		}
		
	}

}