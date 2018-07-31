import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConflictsSolver<E> {
	private static int numberVars = 0;

	private HashMap<E,Variable> mapEV = new HashMap<>();
	private HashMap<Variable,E> mapVE = new HashMap<>();
	private ArrayList<Variable> vars = new ArrayList<>();
	private ArrayList<Conflict> confl = new ArrayList<>();

	public ConflictsSolver() {}
	
	public void add(E e) {
		if(mapEV.containsKey(e)) return;
		Variable v = new Variable();
		mapEV.put(e,v);
		mapVE.put(v,e);
		vars.add(v);
	}
	
	public void addConflict(E e1, E e2) {
		add(e1);
		add(e2);
		Conflict c = new Conflict(mapEV.get(e1), mapEV.get(e2));
		if(confl.contains(c)) return;
		confl.add(c);
	}
	
	public boolean hasEdge(E e1, E e2) {
		return mapEV.get(e1).conflicts.contains(e2);
	}

	public boolean contains(Object arg0) {
		return mapEV.containsKey(arg0);
	}

	public boolean isEmpty() {
		return vars.isEmpty();
	}

	public boolean remove(Object arg0) {
		return false;
	}
	
	/*
	 * Marca un elemento. Tutti gli altri elementi che hanno un conflitto con l'elemento scelto 
	 * saranno oscurati. Gli elementi oscurati saranno inclusi nel valore di ritorno
	 */
	public List<E> choose(E chosen) {
		List<E> confl = new ArrayList<>();
		return confl;
	}
	
	public List<E> bestSolution() {
		return bestSolution(new ArrayList<>(), 0, 0);
	}
	
	private List<E> bestSolution(List<Variable> s, int z, int x) {
		return null;
	}

	public static void main(String[] args) {
		ConflictsSolver<String> g1 = new ConflictsSolver<>();
	    g1.addConflict("a1", "b1");
	    g1.addConflict("a1", "d1");
	    g1.addConflict("a2", "b2");
	    g1.addConflict("a2", "c1");
	    g1.addConflict("a2", "d1");
	    g1.addConflict("a2", "d2");
	    g1.addConflict("b1", "c1");
	    g1.addConflict("b1", "d1");
	    g1.addConflict("b2", "c2");
	    g1.addConflict("b2", "d2");
	    g1.addConflict("c1", "d1");
	    g1.addConflict("c2", "d2");
	}
	
	private class Conflict {
		Variable v1;
		Variable v2;
		
		Conflict(Variable v1,Variable v2) {
			this.v1 = v1;
			this.v2 = v2;
		}
		
		public boolean equals(Object o) {
			if(o instanceof ConflictsSolver<?>.Conflict) {
				@SuppressWarnings("unchecked")
				Conflict c = (Conflict) o;
				return (v1.equals(c.v1) && v2.equals(c.v2)) ||
						(v1.equals(c.v2) && v2.equals(c.v1));
			}
			return false;
		}
	}
	
	private class Variable implements Comparable<Variable> {
		int index;
		boolean chosen = false, obscurated = false;
		ArrayList<Variable> conflicts = new ArrayList<>();
		
		Variable() {
			index = ++numberVars;
		}
		
		void addConfl(Variable v) {
			if(conflicts.contains(v)) return;
			conflicts.add(v);
		}
		
		public boolean equals(Object o) {
			if(o instanceof ConflictsSolver<?>.Variable) {
				@SuppressWarnings("unchecked")
				Variable e = (Variable) o;
				return this.index == e.index;
			}
			return false;
		}

		public int compareTo(Variable other) {
			if(conflicts.size() == other.conflicts.size())
				return this.index - other.index;
			return conflicts.size() - other.conflicts.size();
		}
	}
}