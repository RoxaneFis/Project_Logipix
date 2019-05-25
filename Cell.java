import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Cell implements Comparable<Cell> {
	private Set<Direction> dirs; // useful for creating the frame
	private boolean marked = false; // boolean which indicates if the cell is marked or not (i.e. if it is part of a path)
	public LinkedList<Cell>markok; // useful for part 3 : Combination
	private boolean blue = false; // 2nd type of marking used for cells common to different paths									// connecting two clues before they are permanently marked in this.marked	
	public Cell parent; // allows to backtrack a path once it is found by DFS
	public int clue; // stores the value of the clue
	public int x; // cell contact details
	public int y;

	public Cell(int i, int j) { // Builder
		dirs = new HashSet<Direction>();
		this.x = i;
		this.y = j;
	}

	public void addDirection(Direction dir) { // useful for creating the frame
		dirs.add(dir);
	}

	public void removeDirection(Direction dir) {
		dirs.remove(dir);
	}

	public boolean hasDirection(Direction dir) {
		return dirs.contains(dir);
	}

	public boolean marked() { // indicates whether or not a cell is marked
		return marked;
	}

	public boolean blue() { // indicates whether a cell is marked "blue" or not
		return blue;
	}

	public void blued() {
		blue = true;

	}

	public void mark() {
		marked = true;
	}

	public void unmark() {
		marked = false;
	}

	public String toString() {
		String s ="";
		s +="(" + x +"," + y +")";
		return s;
	}

	@Override
	public boolean equals(Object o) {
		Cell that = (Cell) o;
		return this.x == that.x && this.y == that.y;

	}

	@Override
	public int compareTo(Cell o) {
		Cell that = (Cell) o;
		return (this.clue - that.clue);
	}

}
