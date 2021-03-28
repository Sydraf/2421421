package a04;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public final class Solver {

	private final static class SearchNode implements Comparable<SearchNode> {
		
		final Board value;
		final int moves;
		final SearchNode previous;
		final int priority;
			
		private SearchNode(Board board, SearchNode previous) {
			int weight = 1; 
							
			this.value = board;
			this.previous = previous;
			
			if (previous == null) {
				this.moves = 0;
			} else {
				this.moves = previous.moves + 1;
			}

			this.priority = (board.manhattan() * weight) + moves;
		}
		
		public int compareTo(SearchNode other) {
			return this.priority - other.priority;
		}
	}

	private static SearchNode result;

	public Solver(Board initial) {
		if (initial.equals(null)) {
			throw new NullPointerException("Board is null");
		}
		if (!initial.isSolvable()) {
			throw new IllegalArgumentException("Not solvable");
		}
		
		SearchNode root = new SearchNode(initial, null);
		MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
		minPQ.insert(root);
		
    	while(!minPQ.isEmpty()) {
    		SearchNode current = minPQ.delMin();
    		if(current.value.isGoal()) {
    			result = current;
    			break;
    		}
    		
			for (Board board : current.value.neighbors()) {
    			SearchNode s = new SearchNode(board, current);
    			if (current.previous != null) {
  					if ((s.priority < current.priority)     						
    					|| board.equals(current.previous.value)) {				
    						continue;
    				} 
    			}
    			minPQ.insert(s);
			}
    	}
	}

	public int moves() {
		return result.moves;
	}

	public Iterable<Board> solution() {
		Stack<Board> solution = new Stack<>();

		while (result != null) {
			solution.push(result.value);
			result = result.previous;
		}
		return solution;
	}

	public static void main(String[] args) {

	    In in = new In(args[0]);
	    int N = in.readInt();
	    int[][] blocks = new int[N][N];
	    for (int i = 0; i < N; i++)
	        for (int j = 0; j < N; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);

	    if (initial.isSolvable()) {
	        Solver solver = new Solver(initial);
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }

	    else {
	        StdOut.println("Unsolvable puzzle");
	    }
	}
}
