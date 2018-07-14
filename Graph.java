import java.util.ArrayList;
import java.util.List;

public class Graph<E> {
	
	private ArrayList<E> nodes = new ArrayList<>();
	private ArrayList<Boolean> obscurated = new ArrayList<>();
	private ArrayList<Edge> edges = new ArrayList<>();

	public Graph() {}
	
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
	
	public boolean isCyclic() {
		boolean visited[] = new boolean[nodes.size()];
		for(int j = 0; j < visited.length; j++) 
			visited[j] = false;
		for(int i = 0; i < visited.length; i++)
			if(!visited[i] && !obscurated.get(i))
				if(isCyclicAux(i, visited, -1))
					return true;
		return false;
	}
	
	private boolean isCyclicAux(int v, boolean visited[], int parent) {
		visited[v] = true;
		
		for(int i = 0; i < edges.size(); i++) {
			Edge ed = edges.get(i);
			int adj = ed.adjacent(v);
			if(adj >= 0) {
				if(visited[adj]) {
					if(adj != parent)
						return true;
				} else if(isCyclicAux(adj, visited, v))
					return true;
			}
		}
		return false;
	}
	
	public List<E> cyclic() {
		boolean visited[] = new boolean[nodes.size()];
		for(int j = 0; j < visited.length; j++) 
			visited[j] = false;
		for(int i = 0; i < visited.length; i++)
			if(!visited[i] && !obscurated.get(i)) {
				ArrayList<E> cycle = new ArrayList<>(nodes.size());
				if(cyclicAux(i, visited, -1, cycle))
					return cycle;
			}
		return null;
	}
	
	private boolean cyclicAux(int v, boolean visited[], int parent, List<E> cycle) {
		visited[v] = true;
		
		for(int i = 0; i < edges.size(); i++) {
			Edge ed = edges.get(i);
			int adj = ed.adjacent(v);
			if(adj >= 0 && !obscurated.get(adj)) {
				if(visited[adj]) {
					if(adj != parent) {
						cycle.add(nodes.get(adj));
						cycle.add(nodes.get(v));
						return true;
					}
				} else if(cyclicAux(adj, visited, v, cycle)) {
					cycle.add(nodes.get(v));
					return true;
				}
			}
		}
		return false;
	}
	
	public List<E> notObscurated() {
		List<E> no = new ArrayList<>();
		for(int i = 0; i < nodes.size(); i++) {
			if(obscurated.get(i)) continue;
			no.add(nodes.get(i));
		}
		return no;
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
	
	private class Edge {
		private int i1;
		private int i2;
		
		Edge(int i1, int i2) {
			this.i1 = i1;
			this.i2 = i2;
		}
		
		public boolean equals(Object o) {
			if(!(o instanceof Graph.Edge)) return false;
			Edge e = (Edge) o;
			return (i1 == e.i1 && i2 == e.i2)
					|| (i1 == e.i2 && i2 == e.i1);
		}
		
		public int adjacent(int v) {
			if(i1 == v) return i2;
			if(i2 == v) return i1;
			return -1;
		}
	}
}
