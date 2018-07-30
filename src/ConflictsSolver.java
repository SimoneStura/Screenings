import java.util.ArrayList;
import java.util.List;

public class ConflictsSolver<E> {
	private static int numberVars = 0;
	
	private ArrayList<E> vars = new ArrayList<>();
	private ArrayList<Edge> edges = new ArrayList<>();

	public ConflictsSolver() {}
	
	public void add(E e) {
		int index = nodes.indexOf(e);
		if(index >= 0)
			obscurated.set(index, false);
		else {
			nodes.add(e);
			obscurated.add(false);
		}
	}
	
	public void addEdge(E e1, E e2) {
		if(!nodes.contains(e1)) {
			nodes.add(e1);
			obscurated.add(false);
		}
		if(!nodes.contains(e2)) {
			nodes.add(e2);
			obscurated.add(false);
		}
		int i1 = nodes.indexOf(e1),
				i2 = nodes.indexOf(e2);
		Edge toAdd = new Edge(i1, i2);
		if(edges.contains(toAdd)) return;
		edges.add(toAdd);
	}
	
	public boolean hasEdge(E e1, E e2) {
		int i1 = nodes.indexOf(e1),
				i2 = nodes.indexOf(e2);
		if(i1 < 0 || i2 < 0) return false;
		Edge e = new Edge(i1,i2);
		for(Edge ed : edges)
			if(ed.equals(e))
				return true;
		return false;
	}

	public boolean contains(Object arg0) {
		return nodes.contains(arg0);
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public boolean remove(Object arg0) {
		int index = nodes.indexOf(arg0);
		if(index >= 0)
			obscurated.set(index, true);
		return index >= 0;
	}
	
	/*
	 * Marca un elemento del grafo. Tutti gli altri elementi che hanno un arco in comune
	 * con l'elemento scelto saranno oscurati. Tali elementi saranno inclusi nel valore di ritorno
	 */
	public List<E> choose(E chosen) {
		List<E> confl = new ArrayList<>();
		int v = nodes.indexOf(chosen);
		for(Edge e : edges) {
			int adj = e.adjacent(v);
			if(adj >= 0) {
				obscurated.set(adj, true);
				confl.add(nodes.get(adj));
			}
		}
		return confl;
	}

	public static void main(String[] args) {
		Graph<String> g1 = new Graph<>();
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
	    System.out.println("Cycle: " + g1.cyclic());
	    System.out.println("Removing d1 and d2");
	    g1.remove("d1");
	    g1.remove("d2");
	    System.out.println("Cycle: " + g1.cyclic());
	    g1.choose("a1");
	    g1.remove("a2");
	    System.out.println(g1.notObscurated());
	}
	
	private class Variable {
		private E value;
		private int index;
		private int conflicts = 0;
		
		Variable(E value) {
			this.value = value;
			index = ++numberVars;
		}
		
		public boolean equals(Object o) {
			if(o instanceof ConflictsSolver.Variable) {
				Variable e = (Variable) o;
				return this.index == e.index;
			}
			return false;
		}

		public int compareTo(Variable other) {
			if(this.conflicts == other.conflicts)
				return this.index - other.index;
			return this.conflicts - other.conflicts;
		}
	}
}