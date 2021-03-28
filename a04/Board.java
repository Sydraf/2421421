package a04;

import edu.princeton.cs.algs4.Queue;
/**
 * 
 * @author Mathew Merkley, Mike Kennedy
 * Board saves components to a 8 puzzle 
 */
public final class Board {
	private final int size;
	private final int[] block;
	private final int x;
	private final int y;

	/**
	 * Constructor
	 * @param blocks int[][]
	 */
	public Board(int[][] blocks) {
		size = blocks.length;
		block = new int[size * size];
		int temp1 = 0;
		int temp2 = 0;
		
		//fill 1d array with the 2d array passed
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				block[conv(i, j)] = blocks[i][j];
				if (block[conv(i, j)] == 0) {
					temp1 = i;
					temp2 = j;
				}
			}
		}

		x = temp1;
		y = temp2;
	}

	/**
	 * size of array 
	 * @return int 
	 */
	public int size() {
		return size;
	}

	/**
	 * How many blocks out of place
	 * @return int 
	 */
	public int hamming() {
		int dist = 0;
		int position = 1;

		for (int i = 0; i < block.length; i++) {
			if (block[i] != 0 && block[i] != position) {
				dist++;
			}

			position++;
		}

		return dist;
	}

	/**
	 * sum of Manhattan distances between blocks and goal
	 * @return int
	 */
	public int manhattan() {
		int distTotal = 0;
		int position = 1;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (block[conv(i, j)] != 0 && block[conv(i, j)] != position) {
					int row = (block[conv(i, j)] - 1) / size;
					int column = (block[conv(i, j)] - 1) % size;
					distTotal += Math.abs(row - i) + Math.abs(column - j);
				}

				position++;
			}
		}

		return distTotal;
	}

	/**
	 * is goal board?
	 * @return boolean
	 */
	public boolean isGoal() {
		return hamming() == 0;
	}

	/**
	 * is board solveable?
	 * @return boolean
	 */
	public boolean isSolvable() {
		int inversions = 0;

		for (int n = 1; n <= size * size - 1; n++) {
			for (int i = 0; i < block.length; i++) {
				if (block[i] > n) {
					inversions++;
				} else if (block[i] == n) {
					break;
				}
			}
		}

		if (size % 2 == 0) {
			return (inversions + x) % 2 != 0;
		} else {
			return inversions % 2 == 0;
		}
	}

	/**
	 * all neighboring boards
	 * @return Iterable<Board>
	 */
	public Iterable<Board> neighbors() {
		Queue<Board> neighbors = new Queue<>();

		if (x > 0) {
			neighbors.enqueue(new Board(swap(x - 1, y)));
		}

		if (x < size - 1) {
			neighbors.enqueue(new Board(swap(x + 1, y)));
		}

		if (y > 0) {
			neighbors.enqueue(new Board(swap(x, y - 1)));
		}

		if (y < size - 1) {
			neighbors.enqueue(new Board(swap(x, y + 1)));
		}

		return neighbors;
	}

	/**
	 * swap boards 
	 * @param row 
	 * @param column
	 * @return int[][]
	 */
	private int[][] swap(int row, int column) {
		int[][] copy = copyBoard();

		copy[x][y] = copy[row][column];
		copy[row][column] = 0;

		return copy;
	}
	
	/**
	 * copy of board
	 * @return int[][]
	 */
	private int[][] copyBoard() {
		int[][] copy = new int[size][size];

		for (int i = 0; i < block.length; i++) {
			copy[i / size][i % size] = block[i];
		}

		return copy;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return int
	 */
	private int conv(int x, int y) {
		return (size * x) + y;
	}
	
	/**
	 * is board = to y?
	 * @return boolean
	 */
	public boolean equals(Object y) {
		if (y == this)
			return true;
		if (y == null)
			return false;
		if (y.getClass() != this.getClass())
			return false;

		Board that = (Board) y;

		if (that.size() != this.size())
			return false;

		for (int i = 0; i < block.length; i++) {
			if (this.block[i] != that.block[i]) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(size + "\n");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				sb.append(String.format("%2d ", block[conv(i, j)]));
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}