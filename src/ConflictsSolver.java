import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConflictsSolver<E> {
	private static int numberVars = 0;
	
	private HashMap<E,Variable> map = new HashMap<>();
	private ArrayList<Variable> vars = new ArrayList<>();

	public ConflictsSolver() {}
	
	public void add(E e) {
		if(!map.containsKey(e))
			map.put(e, new Variable());
	}
	
	public void addEdge(E e1, E e2) {
		if(!map.containsKey(e1))
			map.put(e1, new Variable());
		if(!map.containsKey(e2))
			map.put(e2, new Variable());
		Variable v1 = map.get(e1);
		Variable v2 = map.get(e2);
		v1.addConfl(v2.index);
		v2.addConfl(v1.index);
	}
	
	public boolean hasEdge(E e1, E e2) {
		return map.get(e1).conflicts.contains(e2);
	}

	public boolean contains(Object arg0) {
		return map.containsKey(arg0);
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
	
	public void bestSolution() {
		
	}

	public static void main(String[] args) {
		ConflictsSolver<String> g1 = new ConflictsSolver<>();
	    g1.addEdge("a1", "b1");
	    g1.addEdge("a1", "d1");
	    g1.addEdge("a2", "b2");
	    g1.addEdge("a2", "c1");
	    g1.addEdge("a2", "d1");
	    g1.addEdge("a2", "d2");
	    g1.addEdge("b1", "c1");
	    g1.addEdge("b1", "d1");
	    g1.addEdge("b2", "c2");
	    g1.addEdge("b2", "d2");
	    g1.addEdge("c1", "d1");
	    g1.addEdge("c2", "d2");
	}
	
	private class Variable implements Comparable<Variable> {
		private int index;
		private ArrayList<Integer> conflicts = new ArrayList<>();
		
		Variable() {
			index = ++numberVars;
		}
		
		public void addConfl(int v) {
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