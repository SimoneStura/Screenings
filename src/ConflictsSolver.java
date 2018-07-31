import java.util.*;

public class ConflictsSolver<E> {
	private static int numVars = 0;
	private static int numGroups = 0;

	private HashMap<E,Variable> mapEV = new HashMap<>();
	private ArrayList<List<Variable>> varsGroup = new ArrayList<>();
	private ArrayList<Variable> vars = new ArrayList<>();
	private ArrayList<Conflict> confl = new ArrayList<>();

	public ConflictsSolver() {}
	
	public void add(E e) {
		if(mapEV.containsKey(e)) return;
		Variable v = new Variable(e);
		mapEV.put(e,v);
		vars.add(v);
	}
	
	public void setGroup(E groupEl[]) {
		int group = numGroups++;
		ArrayList<Variable> g = new ArrayList<>();
		for(E el : groupEl) {
			add(el);
			Variable v = mapEV.get(el);
			v.group = group;
			g.add(v);
		}
		varsGroup.add(g);
	}
	
	public void addConflict(E e1, E e2) {
		add(e1);
		add(e2);
		Variable v1 = mapEV.get(e1);
		Variable v2 = mapEV.get(e2);
		Conflict c = new Conflict(v1, v2);
		if(confl.contains(c)) return;
		confl.add(c);
		v1.numConfl++;
		v2.numConfl++;
	}
	
	public List<E> bestSolution() {
		Collections.sort(vars);
		for(Conflict c : confl) {
			if(c.v1.compareTo(c.v2) <= 0)
				c.v1.conflicts.add(c.v2);
			else
				c.v2.conflicts.add(c.v1);
		}
		return bestSolution(vars, new ArrayList<>(), new ArrayList<>());
	}
	
	public List<E> bestSolution(E forced) {
		Collections.sort(vars);
		Variable v = mapEV.get(forced);
		List<Variable> chosen = new ArrayList<>();
		v.chosen = true;
		chosen.add(v);
		for(Conflict c : confl) {
			if(c.v1.compareTo(c.v2) <= 0)
				c.v1.conflicts.add(c.v2);
			else
				c.v2.conflicts.add(c.v1);
			if(c.v1.equals(v))
				c.v2.obscure(true);
			else if(c.v2.equals(v))
				c.v1.obscure(true);
		}
		return bestSolution(vars, chosen, new ArrayList<>());
	}
	
	private List<E> bestSolution(List<Variable> toChoose, List<Variable> chosen, List<E> currentBest) {
		Variable v = null;
		int indexStart = -1, indexEnd = -1;
		for(int i = 0; i < toChoose.size(); i++) {
			Variable tmp = toChoose.get(i);
			if(!tmp.obscured && !tmp.chosen) {
				if(indexStart < 0) {
					v = tmp;
					indexStart = i+1;
				}
				indexEnd = i+1;
			}
		}
		if(v == null) {
			if(chosen.size() > currentBest.size()) {
				List<E> newBest = new ArrayList<>();
				for(Variable vbl : chosen)
					newBest.add(vbl.value);
				return newBest;
			} else
				return currentBest;
		}
		List<Variable> newToChoose = toChoose.subList(indexStart, indexEnd);
		v.choose(true);
		if(chosen.size() + 1 + bound(newToChoose) > currentBest.size()) {
			chosen.add(v);
			currentBest = bestSolution(newToChoose, chosen, currentBest);
			chosen.remove(v);
		}
		v.choose(false);
		v.obscure(true);
		if(chosen.size() + bound(newToChoose) > currentBest.size())
			currentBest = bestSolution(newToChoose, chosen, currentBest);
		v.obscure(false);
		return currentBest;
	}
	
	private int bound(List<Variable> toChoose) {
		int i = 0;
		Collection<Integer> groupsDone = new TreeSet<>();
		for(Variable v : toChoose)
			if(!v.chosen && (v.obscured || groupsDone.contains(v.group)))
				continue;
			else
				groupsDone.add(v.group);
		return groupsDone.size();
	}

	public static void main(String[] args) {
		ConflictsSolver<String> g1 = new ConflictsSolver<>();
	    String a[] = {"a1","a2"};
	    String b[] = {"b1","b2"};
	    String c[] = {"c1","c2"};
	    String d[] = {"d1","d2"};
	    g1.setGroup(a);
	    g1.setGroup(b);
	    g1.setGroup(c);
	    g1.setGroup(d);
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
	    
	    System.out.println(g1.bestSolution());
	    System.out.println("forzo d1");
	    System.out.println(g1.bestSolution("d1"));
	    System.out.println("forzo d2");
	    System.out.println(g1.bestSolution("d2"));
	}
	
	private class Conflict {
		Variable v1;
		Variable v2;
		
		Conflict(Variable v1,Variable v2) {
			this.v1 = v1;
			this.v2 = v2;
		}
		
		public String toString() {
			return "(" + v1.value + " -- " + v2.value + ")";
		}
		
		boolean contains(Variable v) {
			return v1.equals(v) || v2.equals(v);
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
		E value;
		int group = 0;
		int index;
		int numConfl = 0;
		boolean chosen = false, obscured = false;
		TreeSet<Variable> conflicts = new TreeSet<>();
		
		Variable(E v) {
			value = v;
			index = ++numVars;
		}
		
		void obscure(boolean b) {
			obscured = b;
			chosen = false;
		}
		
		void choose(boolean b) {
			chosen = b;
			obscured = false;
			for(Variable v : conflicts)
				v.obscure(b);
			for(Variable v : varsGroup.get(group))
				if(this.compareTo(v) < 0) v.obscure(b);
		}
		
		public String toString() {
			return value.toString();
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
			if(numConfl == other.numConfl)
				return this.index - other.index;
			return numConfl - other.numConfl;
		}
	}
}