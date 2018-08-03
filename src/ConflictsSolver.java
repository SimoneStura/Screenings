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

public class ConflictsSolver<E extends placedOverTime<E>> {
	private int idxVars = 0;
	private int idxGroups = 0;
	private Map<E,Variable> mapEV = new HashMap<>();
	private List<List<Variable>> varsGroup = new ArrayList<>();
	private List<Variable> vars = new ArrayList<>();
	private List<Conflict<Variable>> confl = new ArrayList<>();
	private boolean sortNecessary = true;

	public ConflictsSolver() {}
	
	public int getNumElements() {
		return vars.size();
	}
	
	public int getNumGroups() {
		return varsGroup.size();
	}
	
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
		if(sortNecessary)
			sortConflicts((Variable v1, Variable v2) -> {return v1.compareTo(v2);});
		sortNecessary = false;
	}
	
	private void sortConflicts(Comparator<Variable> comp) {
		Collections.sort(vars, comp);
		for(Conflict<Variable> c : confl) {
			if(comp.compare(c.getE1(),c.getE2()) <= 0)
				c.getE1().conflicts.add(c.getE2());
			else
				c.getE2().conflicts.add(c.getE1());
		}
	}
	
	public int bestResult() {
		sortConflicts();
		return bestResult(vars, 0, -1);
	}
	
	private int bestResult(List<Variable> toChoose, int current, int best) {
		List<Variable> newToChoose = branch(toChoose);
		if(newToChoose == null) {
			if(current > best)
				return current;
			else
				return best;
		}
		Variable v = newToChoose.get(0);
		v.choose(true);
		if(bound(newToChoose, current + 1) > best)
			best = bestResult(newToChoose, current + 1, best);
		v.choose(false);
		if(best == varsGroup.size()) return best;
		v.obscure(true);
		if(bound(newToChoose, current) > best)
			best = bestResult(newToChoose, current, best);
		v.obscure(false);
		return best;
	}
	
	public List<List<E>> allGoodSolutions() {
		sortConflicts();
		return allGoodSolutions(vars, new ArrayList<>(), new ArrayList<>(), -1);
	}
	
	private List<List<E>> allGoodSolutions(List<Variable> toChoose, List<Variable> chosen, 
				List<List<E>> best, int bestRes) {
		List<Variable> newToChoose = branch(toChoose);
		if(newToChoose == null) {
			List<E> currentBest = new ArrayList<>();
			for(Variable vbl : chosen)
				currentBest.add(vbl.value);
			if(best.size() == 0 || currentBest.size() == bestRes)
				best.add(currentBest);
			else if(currentBest.size() > bestRes) {
				List<List<E>> newBest = new ArrayList<>();
				newBest.add(currentBest);
				best = newBest;
			}
			return best;
		}
		Variable v = newToChoose.get(0);
		v.choose(true);
		if(bound(newToChoose, chosen.size() + 1) >= bestRes) {
			chosen.add(v);
			best = allGoodSolutions(newToChoose, chosen, best, bestRes);
			bestRes = best.get(0).size();
			chosen.remove(v);
		}
		v.choose(false);
		v.obscure(true);
		if(bound(newToChoose, chosen.size()) >= bestRes)
			best = allGoodSolutions(newToChoose, chosen, best, bestRes);
		v.obscure(false);
		return best;
	}
	
	public SortedSet<E> bestSolution() {
		sortConflicts((Variable v1, Variable v2) -> {return v1.value.compareTo(v2.value);});
		return bestSolution(vars, new ArrayList<>(), new ArrayList<>());
	}
	
	private SortedSet<E> bestSolution(List<Variable> toChoose, List<Variable> chosen, 
				List<E> best) {
		return null;
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
	
	private int bound(List<Variable> toChoose, int currentValue) {
		Collection<Integer> groupsDone = new TreeSet<>();
		for(Variable v : toChoose)
			if(!v.chosen && (v.obscured || groupsDone.contains(v.group)))
				continue;
			else
				groupsDone.add(v.group);
		return groupsDone.size() + currentValue;
	}

	public static void main(String[] args) {
		ConflictsSolver<StringTest> cs = new ConflictsSolver<>();
		List<StringTest> test = new ArrayList<>();
		StringTest a1 = new StringTest("a1",1,3);
		StringTest a2 = new StringTest("a2",5,7);
		StringTest b1 = new StringTest("b1",2,4);
		StringTest b2 = new StringTest("b2",6,8);
		StringTest c1 = new StringTest("c1",4,5);
		StringTest c2 = new StringTest("c2",8,10);
		StringTest d1 = new StringTest("d1",2,5);
		StringTest d2 = new StringTest("d2",6,9);
	    List<StringTest> a = new ArrayList<>();
	    a.add(a1); a.add(a2);
	    List<StringTest> b = new ArrayList<>();
	    b.add(b1); b.add(b2);
	    List<StringTest> c = new ArrayList<>();
	    c.add(c1); c.add(c2);
	    List<StringTest> d = new ArrayList<>();
	    d.add(d1); d.add(d2);
	    cs.addGroup(a); test.addAll(a);
	    cs.addGroup(b); test.addAll(b);
	    cs.addGroup(c); test.addAll(c);
	    cs.addGroup(d); test.addAll(d);
	    
	    for(StringTest st1 : test)
	    	for(StringTest st2 : test.subList(test.indexOf(st1) + 1, test.size()))
	    		if(StringTest.isConflict(st1, st2))
	    			cs.addConflict(st1, st2);
	    
	    System.out.println(cs.bestResult());
	    System.out.println(cs.allGoodSolutions());
	}
	
	private class Variable implements Comparable<Variable> {
		E value;
		int group = 0;
		int index;
		int numConfl = 0;
		boolean chosen = false, obscured = false, previous = false;
		TreeSet<Variable> conflicts = new TreeSet<>();
		
		Variable(E v) {
			value = v;
			index = ++idxVars;
		}
		
		void obscure(boolean b) {
			if(b) {
				previous = obscured;
				obscured = true;
			} else {
				obscured = previous;
				previous = false;
			}
		}
		
		void choose(boolean b) {
			chosen = b;
			obscured = false;
			previous = false;
			for(Variable v : conflicts)
				v.obscure(b);
			for(Variable v : varsGroup.get(group))
				if(this.compareTo(v) < 0) {
						v.chosen = b;
						v.obscure(b);
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