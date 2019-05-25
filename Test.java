
public class Test {
	
	public static void main(String[] args) throws Exception {
		
		Logipix m = new Logipix("TeaCup.txt");			// crée une nouvelle instance du logipix
		
		long startTime = System.nanoTime();			// chronomètre donnant le temps nécessaire pour résoudre le logipix
		System.out.println(m.supersolve());			// appel à la méthode supersolve pour résoudre le logipix
		long endTime = System.nanoTime();
		System.out.print("Computed in " + (endTime-startTime)/1000000000.0 + " s\n");

	}
}
