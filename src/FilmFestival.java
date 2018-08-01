import java.util.*;

public class FilmFestival {
	
	private TreeSet<Screening> shows = new TreeSet<>((Screening s1, Screening s2) -> s1.compareTo(s2));
	private ArrayList<Movie> movies = new ArrayList<>();
	private HashMap<Movie,ArrayList<Screening>> screens = new HashMap<>();
	private ConflictsSolver<Screening> conflicts = new ConflictsSolver<>();
	
	public FilmFestival() {}
	
	public boolean addScreen(Screening s) {
		boolean ctrl = shows.add(s);
		if(ctrl) {
			Movie m = s.getM();
			if(!(movies.contains(m))) {
				movies.add(m);
				screens.put(m, new ArrayList<>(3));
			}
			List<Screening> screenGroup = screens.get(m);
			screenGroup.add(s);
			conflicts.addGroup(screenGroup);
			for(Screening screen : shows)
				if(!(screen.getM().equals(m)) && Screening.isConflict(s, screen))
					conflicts.addConflict(screen, s);
		}
		return ctrl;
	}
	
	public Collection<Screening> getShows() {
		return shows;
	}
	
	public Collection<Movie> getMovies() {
		return movies;
	}
	
	public Map<Movie,ArrayList<Screening>> getScreens() {
		return screens;
	}
	/*
	public List<Movie> conflicting(Movie m) {
		if(m == null) return null;
		ArrayList<Movie> confl = new ArrayList<>();
		ArrayList<Screening> actualScreens = screens.get(m);
		List<List<Screening>> conflScreens = new ArrayList<List<Screening>>(actualScreens.size());
		for(Screening screen : actualScreens)
			conflScreens.add(conflicts(screen).notObscurated());
		
		return confl;
	}
	
	public Graph<Screening> conflicts(Screening screen) {
		Graph<Screening> g = new Graph<>();
		return conflicts(screen, g, null, new ArrayList<>());
	}
	
	private Graph<Screening> conflicts(Screening screen, Graph<Screening> g, 
			Screening father, List<Screening> visited) {
		visited.add(screen);
		for(Screening s : shows) {
			Calendar calS = Calendar.getInstance();
			Calendar calE = Calendar.getInstance();
			calS.setTime(s.getStartTime());
			calE.setTime(screen.getEndTime());
			if(calS.get(Calendar.DAY_OF_MONTH) > calE.get(Calendar.DAY_OF_MONTH) 
					|| calS.get(Calendar.MONTH) > calE.get(Calendar.MONTH)
					|| calS.get(Calendar.YEAR) > calE.get(Calendar.YEAR))
				break;
			if(!(s.getM().equals(screen.getM()) || (father != null && s.getM().equals(father.getM())))
					&& Screening.isConflict(screen, s)) {
				g.addEdge(screen, s);
				if(visited.contains(s)) continue;
				conflicts(s, g, screen, visited);
			}
		}
		return g;
	}
	*/
	public static void main(String[] args) {
		FilmFestival tff = new FilmFestival();
		Movie m = new Movie("God Bless The Child", 2015, 92);
		Calendar cal = Calendar.getInstance();
		cal.set(2015,10,21,17,0);
		Screening scr = new Screening(m, cal.getTime());
		tff.addScreen(scr);
		cal.set(2015,10,22,11, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,23,19, 45);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("Sufragette", 2015, 100);
		cal.set(2015,10,21,14, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,23,17, 15);
		scr = new Screening(m, cal.getTime());
		tff.addScreen(scr);
		
		m = new Movie("Idealisten", 2015, 114);
		cal.set(2015,10,22,16, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,23,12, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,24,19, 45);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("La Patota", 2015, 103);
		cal.set(2015,10,22,22, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,23,9, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,24,9, 45);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("Lo Scambio", 2015, 93);
		cal.set(2015,10,23,17, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,24,19, 45);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("Sopladora De Hojas", 2015, 96);
		cal.set(2015,10,23,20, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,24,9, 45);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("High-Rise", 2015, 112);
		cal.set(2015,10,23,22, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,24,14, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("Me & Earl & The Dying Girl", 2015, 100);
		cal.set(2015,10,22,22, 15);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,23,14, 15);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("February", 2015, 93);
		cal.set(2015,10,22,9, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,23,22, 15);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("Mia Madre Fa L'Attrice", 2015, 78);
		cal.set(2015,10,21,19, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,22,9, 45);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,23,11, 45);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("The Girl In The Photographs", 2015, 95);
		cal.set(2015,10,21,22, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,22,9, 0);
		Screening ch2 = new Screening(m, cal.getTime());
		tff.addScreen(ch2);
		cal.set(2015,10,24,22, 15);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("The Quiet Earth", 2015, 91);
		cal.set(2015,10,21,14, 45);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,22,9, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,23,17, 15);
		Screening ch1 = new Screening(m, cal.getTime());
		tff.addScreen(ch1);
		
		m = new Movie("Nusty Baby", 2015, 100);
		cal.set(2015,10,21,22, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,22,14, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,23,9, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		List<Screening> bestSol = tff.conflicts.bestSolution();
		Collections.sort(bestSol);
		for(Screening s : bestSol) {
			System.out.print(s);
			System.out.println("  -  " + s.getM());
		}
		
//		for(Screening screen : tff.getShows())
//			System.out.println(screen + " " + screen.getM());
		/*
		for(Movie mov : tff.getMovies()) {
			System.out.println(mov);
			for(Screening screen : tff.getScreens().get(mov))
				System.out.println("\t" + screen);
			System.out.println();
		}
		
		//tff.conflicts.choose(ch1);
		//tff.conflicts.choose(ch2);
		List<Screening> list = tff.conflicts.cyclic();
		if(list == null) return;
		for(Screening screen : list)
			System.out.println("\t" + screen + " " + screen.getM());
/*		for(Screening scree : tff.getShows()) {
			System.out.println(scree + " " + scree.getM());
			Graph<Screening> g = tff.conflicts(scree);
			List<Screening> l = g.cyclic();
			if(l != null) {
				System.out.println("CICLO:");
				for(Screening screen : g.notObscurated())
					System.out.println("\t" + screen + " " + screen.getM());
			}
		}
		*/
	}

}
