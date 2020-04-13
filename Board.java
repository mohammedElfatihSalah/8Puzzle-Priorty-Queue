import java.util.Iterator;
public class Board{

	//private final static int [][] goal;
	private final int[][] tiles;
	private int n;

	public Board(int[][] tiles){
		this.n 		= tiles.length;

		this.tiles = new int[n][n];
		

		
		for(int i = 0 ; i < n; i++){
			for(int j =0 ; j < n; j++){
				this.tiles[i][j] = tiles[i][j];
			}
		}

		
	}

	public int dimension(){
		return tiles.length;
	}

	public int hamming(){

		int result = 0;
		for(int i = 0 ; i < n ; i++){
			for(int j = 0 ; j < n ; j++){
				if(tiles[i][j] == 0) continue;
				if(tiles[i][j] != i*tiles.length + j + 1) result++;
			}
		}

		return result;
	}

	public int manhattan(){
		int result = 0;

		int tile= 0;
		int ig  =0;
		int jg =0; 
		for(int i = 0 ; i < n ; i++){
			for(int j = 0 ; j < n ; j++){
				tile = tiles[i][j];
				if(tile == 0) continue;
				ig   = (tile - 1)/n;
				jg   = (tile-1)%n; 
				result += Math.abs(ig-i) + Math.abs(jg - j);
			}
		}

		return result;
	}


	public String toString(){
		StringBuilder result = new StringBuilder();

		result.append(n).append("\n");
		for(int i = 0 ; i < n ; i++){
			for(int j = 0 ; j < n ; j++){
				result.append(tiles[i][j]).append(" ");
			}
			result.append("\n");
		}

		return result.toString();
	}

	public boolean isGoal(){

		boolean correspondingEntryEqual = true;
		for(int i = 0 ; i < n ; i++){
			for(int j = 0 ; j < n ; j++){
				if(tiles[i][j] == 0) continue;
				if(tiles[i][j] != i*tiles.length + j + 1){
					correspondingEntryEqual = false;
					break;
				}
			}
			if(!correspondingEntryEqual) break;
		}
		return correspondingEntryEqual;
	}

	public boolean equals(Object y){


		if(y == null) return false;
		if(!(y instanceof Board)) return false;
		Board board = (Board) y;

		return this.toString().equals(board.toString());
	}

	public Iterable<Board> neighbors(){
		return new Neighbors(tiles);
	}

	public Board twin(){
		int[][] newTiles =  new int[n][n];

		int firstTile= 1;
		int i0 = 0;
		int j0 = 0;
		for(int i = 0; i < tiles.length;i++){
			for(int j =0 ; j < tiles.length;j++){
				newTiles[i][j] = tiles[i][j];
				if(tiles[i][j] == firstTile){
					i0 = i;
					j0 = j;
				}
			}
		}

		boolean out = false;
		for(int i = 0; i < tiles.length;i++){
			for(int j =0 ; j < tiles.length;j++){

				if(newTiles[i][j] !=0 && newTiles[(i+1)%tiles.length][j] !=0 ){
					int temp = newTiles[i][j];
					newTiles[i][j] = newTiles[(i+1)%tiles.length][j];
					newTiles[(i+1)%tiles.length][j] = temp;
					out = true;
					break;
				}
			}

			if(out) break;
		}

		
		return new Board(newTiles);
	}


	

	private class Neighbors implements Iterable<Board>{
		private int [][] tiles;
		public Neighbors(int [][] tiles){
			this.tiles = tiles;

		}

		public Iterator<Board> iterator(){
			return new CustomBoardIterator(tiles);
		}

		private class CustomBoardIterator implements Iterator<Board>{
			private int [][] tiles;
		//up right down left
			private boolean [] moves;
			private int current = 0;
			private int i0, j0;
			private int n;
			//private int current = 0;

			public CustomBoardIterator(int [][] tiles){
				this.tiles = tiles;
				this.n  = tiles.length;
				moves  = new boolean[5];

				for(int i = 0; i < 5 ; i++) moves[i] = true;

				for(int i = 0; i < tiles.length;i++){
					for(int j =0 ; j < tiles.length;j++){
						if(tiles[i][j] == 0){
							i0 = i;
							j0 = j;
							break;
						} 
					}
				}

				if(i0 == 0) moves[0] = false;
				if(i0 == n-1) moves[2] = false;
				if(j0 == 0) moves[3] = false;
				if(j0 == n-1)moves[1] = false;

				//for(int i = 0; i < 4 ; i++) System.out.print(moves[i] +" ");
				//System.out.println();
			}

			public Board next(){
				while((moves[current] == false) && (current <= 4)) ++current;
				//System.out.println("current >> " + current + " value >> " + moves[current]);

				int[][] newTiles  = new int[n][n];
				for(int i = 0; i < tiles.length;i++){
					for(int j =0 ; j < tiles.length;j++){
						
						newTiles[i][j] = tiles[i][j]; 
					}
				}

				int temp = newTiles[i0][j0];
				if(current == 0){
					newTiles[i0][j0] = newTiles[i0-1][j0];
					newTiles[i0-1][j0] = temp;
				}else if(current==1){
					newTiles[i0][j0] = newTiles[i0][j0+1];
					newTiles[i0][j0+1] = temp;
				}else if(current==2){
					newTiles[i0][j0] = newTiles[i0+1][j0];
					newTiles[i0+1][j0] = temp;
				}else if(current==3){
					newTiles[i0][j0] = newTiles[i0][j0-1];
					newTiles[i0][j0-1] = temp;
				}

				current++;
				//System.out.println("current >> " + current + " value >> " + moves[current]);

				while(moves[current] == false && current <= 4) ++current;

				return new Board(newTiles);
			}

			public boolean hasNext(){
				return current < 4;
			}

		}

	}



	public static void main(String [] args){
		int[][] test1 = {{1,2,3},{4,6,0},{7,8,5}};
		int[][] test2 = {{1,2,3},{4,6,0},{7,8,5}};

		Board board1 = new Board(test1);
		Board board2 = new Board(test2);

		System.out.println(board1);
		System.out.println(board1.hamming());
		System.out.println(board1.manhattan());

		System.out.println("the two board are equal: " + board1.equals(board2));
		System.out.println("is the board is the goal: " + board1.isGoal());

		for(Board b : board1.neighbors()){
			System.out.println(b);
		}

		System.out.println(board1.twin());
	}


}
