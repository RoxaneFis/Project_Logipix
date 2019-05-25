import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Logipix {
	
	public static Cell[][] grid; // grid that contains all the cells of the Logipix
	public static LogipixFrame frame; // object allowing the display of the grid
	static public HashMap<Integer, LinkedList<Cell>> buckets; // HashMap containing all clues grouped by values
	static public PriorityQueue<Cell> Q; // Priority queue containing the clues that are not yet marked
	private static final int step = 20;

	
	
	public Logipix(String file) throws Exception { // reading the text file
		Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));
		int width = Integer.parseInt(sc.nextLine()); // read grid width
		int height = Integer.parseInt(sc.nextLine()); // read grid height
		grid = new Cell[height][width];
		buckets = new HashMap<Integer, LinkedList<Cell>>(); // creation HashMap buckets
		Q = new PriorityQueue<Cell>(); // creation of a priority queue that will contain the clues
		while (sc.hasNextLine()) { // priority given to clues of small values
			for (int i = 0; i < grid.length; i++) {
				String[] line = sc.nextLine().trim().split(" ");
				for (int j = 0; j < line.length; j++) {
					grid[i][j] = new Cell(i, j); // grid filling
					grid[i][j].clue = Integer.parseInt(line[j]);
					int val = grid[i][j].clue;
					if (val > 0) { // if we are dealing with a clue (i.e. not an empty box), we add it
						grid[i][j].markok = new LinkedList<Cell>(); // at the tail and HashMap
						Q.add(grid[i][j]);
						if (buckets.containsKey(val)) {
							buckets.get(val).add(grid[i][j]);
						} else {
							buckets.put(val, new LinkedList<Cell>());
							buckets.get(val).add(grid[i][j]);
						}
					}
				}
			}
		}
		sc.close();
		frame = new LogipixFrame(grid, grid.length, grid[0].length, step); // creation of the object to display the grid

	}


	
	public static boolean isValid(Cell c) { // allows to check that a cell is well contained in the grid
		if (c == null) {
			return false;
		} else {
			return c.x >= 0 && c.x < grid.length && c.y >= 0 && c.y < grid[0].length;
		}
	}

	public static LinkedList<Cell> neighbors(Cell c) { // returns a list containing the neighbors of a cell
		LinkedList<Cell> l = new LinkedList<Cell>(); // (i.e. cells sharing an edge with the cell)
		Cell w = west(c);
		if (isValid(w)) {
			l.add(w);
		}
		Cell s = south(c);
		if (isValid(s)) {
			l.add(s);
		}
		Cell e = east(c);
		if (isValid(e)) {
			l.add(e);
		}
		Cell n = north(c);
		if (isValid(n)) {
			l.add(n);
		}
		return l;
	}
	
	
	


	

	

	public static Cell west(Cell c) {		// returns the left neighbor of the cell if it exists, otherwise null
		if (c.x > 0) {
			return grid[c.x - 1][c.y];
		} else {
			return null;
		}
	}

	public static Cell south(Cell c) {		// The same for the neighbor to the south
		if (c.y < grid[0].length - 1) {
			return grid[c.x][c.y + 1];
		} else {
			return null;
		}
	}

	public static Cell east(Cell c) {		// The same goes for the right-hand neighbor
		if (c.x < grid.length - 1) {
			return grid[c.x + 1][c.y];
		} else {
			return null;
		}
	}

	public static Cell north(Cell c) {		// The same goes for the neighbour to the north
		if (c.y > 0) {
			return grid[c.x][c.y - 1];
		} else {
			return null;
		}
	}



	
	
	// determines recursively if there is a path of length distance between marlin and nemo
	public static boolean DFS(Cell marlin, Cell nemo, int distance) {	
		marlin.mark(); // we mark marlin, the starting square
		if (distance == 1 && marlin.equals(nemo)) {
			return true; // STOP: if the right number of cells have been traversed (i.e. distance == 1)
		} // and that marlin is the same cell as nemo, we return true
		if (distance == 1 && !marlin.equals(nemo)) {
			marlin.unmark(); // otherwise, we send false reports
			return false;
		}
		for (Cell k : neighbors(marlin)) { // RECURSIVITY : for each marlin neighbor :

			if ( (!k.blue() || nemo.markok.contains(k)) // - not part of another combination (blue - Part III.Combination)
				&& k != null && isValid(k) // - being valid,
				&& (!k.marked()) // - not being permanently marked,
				&& (k.clue == 0 || 
				(k.clue == nemo.clue && distance == 2))) { // - not being a clue, or a clue of the same value as nemo :
				if (DFS(k, nemo, distance - 1)) { // DFS is recalled on this neighbor, with a distance decreased by 1
					k.parent = marlin; // we mark where we arrived on this neighbour (i.e. his parent is marlin)
					return true;
				}
			}
		}
		marlin.unmark(); // we mark marlin
		return false; // we return false if there is no path
	}

	
	
	
	
	//returns, if available, the BrokenLine connecting marlin to nemo
	public static BrokenLine backtrack(Cell marlin, Cell nemo) {
		LinkedList<Cell> l = new LinkedList<Cell>();
		marlin.parent = marlin;
		if (DFS(marlin, nemo, marlin.clue)) { // if there is a path between marlin and nemo
			Cell current = nemo; // we start from nemo (arrival cell)
			l.add(nemo);
			while (!(current.parent.equals(current))) { // we go up, to the start square using the "parent" field of each cell 
				current.unmark();							
				current = current.parent;
				l.add(current);
			}
			current.unmark();
		}
		return new BrokenLine(l);
	}
	
	
	
	
	

	// generates the list of BrokenLine connecting marlin to nemo, if any, returns an empty list otherwise
	public static LinkedList<BrokenLine> generates(Cell marlin, Cell nemo, LinkedList<BrokenLine> l) {
		int abs = Math.abs(marlin.x - nemo.x); // we check that nemo is at a reachable distance for marlin
		int ord = Math.abs(marlin.y - nemo.y);
		if (ord + abs >= nemo.clue) {
			return l; // if this is not the case, an empty list is returned
		}
		BrokenLine line = backtrack(marlin, nemo); // otherwise we generate A line between marlin and nemo thanks to backtrack
		if (!line.bk.isEmpty() && !l.contains(line)) { // if this line has not already been added to the final BrokenLine list
			l.add(line); // it is added
			for (Cell c: line.bk) { // we will successively mark each cell of the line (except nemo and marlin)
				if (!c.equals(marlin) && !c.equals(nemo)) { // recall the generate function to find the other lines connecting marlin
					c.mark(); // to nemo
					generates(marlin, nemo, l); // if any, they are added to the final BrokenLine list
					c.unmark(); // mark the marked cell and start again with another cell of the path
				}
			}
		}
		return l;
	}

	

	// returns, for a cell c, the list of unmarked clues of the same value and located at a distance reachable from c
	public static LinkedList<Cell>candidat(Cell c) {				
		LinkedList<Cell> list = new LinkedList<Cell>();			
		for (Cell candidate: buckets.get(c.clue)) { // for each clue of the same value
			if (!candidate.equals(c) && !candidate.marked()) { // if not marked
				int abs = Math.abs(candidate.x - c.x); // we check that it is potentially reachable from c
				int ord = Math.abs(candidate.y - c.y);
				if (ord + abs < c.clue) {
					list.add(candidate);
				}
			}
		}
		return list;
	}


	
	// Combination
	// returns the list of cells common to all paths connecting marlin to nemo and marks them in Blue
	// Updates the marlin and nemo "markok" field with this list
	public static LinkedList<Cell> compatible(Cell marlin, Cell nemo, LinkedList<BrokenLine> L){
		LinkedList<Cell> list =new LinkedList<Cell>();			
		BrokenLine l1 = L.poll(); // take a path from marlin to nemo
		int n = L.size();
		for(Cell k : l1.bk) { // for each cell of this path
			int ind = 0;
			for(BrokenLine l : L) {
				if(l.bk.contains(k)) { // it is checked that it is in all other paths connecting marlin to nemo
					ind ++;
				}
				else {
					break;
				}
			}
			if (ind == n) {
				list.add(k); // if this is the case, the cell is added to the final list
				k.blued(); // and we mark it in blue
			}
		}
		marlin.markok = list;
		nemo.markok = list;
		return list;	
	}
	

	// Goal: to mark all the cells that we are sure should be marked
	public boolean FirstlyBool() {	
		
		int a = 0; // if a is different from 0 at the end, we will know that the method has marked something
		LinkedList<Cell> aReinsere = new LinkedList<Cell>();
		
		while (!Q.isEmpty()) { // we want to consider all the clues of the grid
			Cell heart = (Cell) Q.poll(); // we take a clue
			
			while (heart.clue == 1 || heart.clue == 2) { // if it is a 1 or a 2, it can be marked directly
				a = 1;
				heart.mark();
				heart = Q.poll(); // we take another clue (heart) of the tail
			} // When leaving the loop, clue > 2
			
			LinkedList<Cell>listCandidates = candidat(heart); // you look at your candidates
			LinkedList<BrokenLine> Ltrue = new LinkedList<BrokenLine>();
			int n = 0;
			Cell ind = null;
			for (Cell c: listCandidates) { // we look at the number of candidates for whom there is at least 1 path
				LinkedList<BrokenLine> L = new LinkedList<BrokenLine>();
				if (!generates(heart, c, L).isEmpty()) {
					Ltrue = L;
					n++;
					ind = c;
					if(n > 1) {
						break;
					}
				}
			}
			if (n == 1) { // if there is only one candidate C for whom it is true
				Q.remove(ind); // it is removed from Q
				if (Ltrue.size() == 1) { // if there is only one possible path with C
					Ltrue.getFirst().mark(); // it can be marked immediately
					a = 1;
				} 
				else {
					compatible (heart, ind, Ltrue); // otherwise, we will mark the common squares of the different 
					aReinsere.add(heart); // paths with C, and note that the "heart" key will have to be reinserted  
				} // at the queue for later processing
			} 
			
			else {
				aReinsere.add(heart); // otherwise, we indicate that we will have to reinsert heart into the tail
			}}
		
		
		for (Cell k : aReinsere) { // for all keys to be reinserted
			if (!k.marked()) { // we check that they have not been marked in the meantime
				Q.add(k); // they are added to the queue
			}
		}
		
		return (a != 0); // we return true if the method has not marked any additional clue during 
	} /// its execution

		
	
	
	static void slow() {
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
		}
	}
		
		
	// Goal: complete the logipix
	public boolean solve() {
		slow();
		
		frame.repaint();
		if (Q.isEmpty()) { // if the queue is empty, we have finished and we return true
			return true;
		}
		Cell heart = (Cell) Q.poll(); // we take a heart key from the tail
		for (Cell candidate: candidat(heart)) { // for each of its candidates C
			Q.remove(candidate); // remove C from the queue
			LinkedList<BrokenLine> L = new LinkedList<BrokenLine>();

			L = generates (heart, candidate, L); // we generate the possible paths with C
			
			if (!L.isEmpty()) { // if any
				for (BrokenLine line : L) { // for each of these paths
					line.mark(); // it is marked
					if (solve()) { // if lelogipix has a solution including this path
						return true; // true is returned
						} 
					else {
						line.unmark(); // otherwise, we mark the path
					}
				}
			}
			Q.add(candidate); // and put candidate C back in line
		}
		return false; // if we couldn't return true at one time, we return false
	}


	//Reset the logipix
	public boolean supersolve() { 
		while (FirstlyBool()) {		
			continue; // as long as FirstlyBool() marks additional cells, it is called
		}
		return solve(); // then we call solve to oocupy cells not yet marked and finish the logipix

	}


}
