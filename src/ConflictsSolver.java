import java.util.*;

/**
 * Una classe che risolve Problemi di massimizzazione con n variabili binarie e k vincoli 
 * della forma (Xi + Xj <= 1). Per la risoluzione è utilizzato un algoritmo di Branch & Bound.
 * È inoltre possibile raggruppare le variabili in gruppi. Cosí facendo, le soluzioni conterranno
 * al massimo una variabile per ciascun gruppo.
 * 
 * @author Simone Stura
 *
 * @param <E> gli elementi rappresentati dalle variabili.
 */

public class ConflictsSolver<E> {
	private static int idxVars = 0;
	private static int idxGroups = 0;

	private Map<E,Variable> mapEV = new HashMap<>();
	private List<List<Variable>> varsGroup = new ArrayList<>();
	private List<Variable> vars = new ArrayList<>();
	private List<Conflict<Variable>> confl = new ArrayList<>();
	private boolean sortNecessary = true;

	public ConflictsSolver() {}
	
	private Variable add(E e) {
		Variable v = mapEV.get(e);
		if(v == null) {
			v = new Variable(e);
			mapEV.put(e,v);
			vars.add(v);
		}
		return v;
	}
	
	public void addElement(E element) {
		sortNecessary = true;
		add(element);
	}
	
	public void addGroup(List<E> groupEl) {
		sortNecessary = true;
		int group = -1;
		for(E e : groupEl) {
			if(mapEV.containsKey(e)) {
				Variable v = mapEV.get(e);
				for(int i = 0; i < varsGroup.size(); i++) {
					if(varsGroup.get(i).contains(v)) {
						group = i;
						break;
					}
				}
			}
		}
		if(group < 0)	
			group = idxGroups++;
		List<Variable> g = new ArrayList<>();
		for(E e : groupEl) {
			Variable v = add(e);
			v.group = group;
			g.add(v);
		}
		if(group >= varsGroup.size())
			varsGroup.add(g);
		else
			varsGroup.set(group, g);
	}
	
	public void addConflict(E e1, E e2) {
		sortNecessary = true;
		Variable v1 = add(e1);
		Variable v2 = add(e2);
		Conflict<Variable> c = new Conflict<>(v1, v2);
		if(confl.contains(c)) return;
		confl.add(c);
		v1.numConfl++;
		v2.numConfl++;
	}
	
	private void sortConflicts() {
		Collections.sort(vars);
		for(Conflict<Variable> c : confl) {
			if(c.getE1().compareTo(c.getE2()) <= 0)
				c.getE1().conflicts.add(c.getE2());
			else
				c.getE2().conflicts.add(c.getE1());
		}
		sortNecessary = false;
	}
	
	public int bestResult() {
		if(sortNecessary)
			sortConflicts();
		return bestResult(vars, 0, -1);
	}
	
	private int bestResult(List<Variable> toChoose, int current, int best) {
		return 0;
	}
	
	public List<E> bestSolution() {
		if(sortNecessary)
			sortConflicts();
		return bestSolution(vars, new ArrayList<>(), new ArrayList<>());
	}
	
	private List<E> bestSolution(List<Variable> toChoose, List<Variable> chosen, List<E> currentBest) {
		List<Variable> newToChoose = branch(toChoose);
		if(newToChoose == null) {
			if(chosen.size() > currentBest.size()) {
				List<E> newBest = new ArrayList<>();
				for(Variable vbl : chosen)
					newBest.add(vbl.value);
				return newBest;
			} else
				return currentBest;
		}
		Variable v = newToChoose.get(0);
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
	
	private List<Variable> branch(List<Variable> toChoose) {
		int indexStart = -1, indexEnd = -1;
		for(int i = 0; i < toChoose.size(); i++) {
			Variable tmp = toChoose.get(i);
			if(!tmp.obscured && !tmp.chosen) {
				if(indexStart < 0)
					indexStart = i;
				indexEnd = i+1;
			}
		}
		if(indexStart < 0) return null;
		return toChoose.subList(indexStart, indexEnd);
	}
	
	private int bound(List<Variable> toChoose) {
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
	    List<String> a = new ArrayList<>();
	    a.add("a1");
	    List<String> b = new ArrayList<>();
	    b.add("b1"); b.add("b2");
	    List<String> c = new ArrayList<>();
	    c.add("c1"); c.add("c2");
	    List<String> d = new ArrayList<>();
	    d.add("d1"); d.add("d2");
	    g1.addGroup(a);
	    a.add("a2");
	    g1.addGroup(a);
	    g1.addGroup(b);
	    g1.addGroup(c);
	    g1.addGroup(d);
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
			index = ++idxVars;
		}
		
		void obscure(boolean b) {
			obscured = b;
		}
		
		void choose(boolean b) {
			chosen = b;
			obscured = false;
			for(Variable v : conflicts)
				v.obscure(b);
			for(Variable v : varsGroup.get(group))
				if(this.compareTo(v) < 0) {
					v.chosen = b;
					v.obscured = b;
				}
		}
		
		public String toString() {
			return value.toString();
		}
		
		public boolean equals(Object o) {
			if(o instanceof ConflictsSolver<?>.Variable)
				return this.index == ((ConflictsSolver<?>.Variable) o).index;
			return false;
		}

		public int compareTo(Variable other) {
			if(numConfl == other.numConfl)
				return this.index - other.index;
			return numConfl - other.numConfl;
		}
	}
}