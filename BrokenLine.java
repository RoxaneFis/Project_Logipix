import java.util.LinkedList;

public class BrokenLine {
	public LinkedList<Cell> bk;
	
	public BrokenLine (LinkedList<Cell> list) {
		bk = list;
	}
	
	// displays all BrokenLine cells
	public void Affichage() {
		System.out.println(bk.size());
		for (Cell k : bk) {
			System.out.println(k);
		}
	}
	
	// marks all the cells of the BrokenLine
	public void mark() {
		for(Cell k : this.bk) {
			k.mark();
		}
	}
	
	// unmarks all the cells of the BrokenLine
	public void unmark() {
		for(Cell k : this.bk) {
			k.unmark();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		BrokenLine that = (BrokenLine) o;
		if(this.bk.size() != that.bk.size()) {
			return false;
		}
		for(int i = 0; i < this.bk.size(); i ++) {
			if (! this.bk.get(i).equals(that.bk.get(i))) {
				return false;
			}
		}
		return true;
		
	}
}
