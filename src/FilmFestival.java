import java.util.*;

public class FilmFestival {
	
	private TreeSet<Screening> shows = new TreeSet<>((Screening s1, Screening s2) -> s1.compareTo(s2));
	private ArrayList<Movie> movies = new ArrayList<>();
	private HashMap<Movie,ArrayList<Screening>> screens = new HashMap<>();
	private List<Conflict<Screening>> confl = new ArrayList<>();
	
	public FilmFestival() {}
	
	public boolean addScreen(Screening s) {
		boolean ctrl = shows.add(s);
		if(ctrl) {
			Movie m = s.getM();
			if(!(movies.contains(m))) {
				movies.add(m);
				screens.put(m, new ArrayList<>(5));
			}
			screens.get(m).add(s);
			for(Screening screen : shows)
				if(!(screen.getM().equals(m)) && Screening.isConflict(s, screen))
					confl.add(new Conflict<>(screen, s));
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
	
	public boolean hasComplessConflict(Movie m) {
		return false;
	}
	
	private void searchConflicts(ConflictsSolver<Screening> conSol, Movie m, 
			List<Screening> conflS, List<Movie> conflM) {
		conflM.add(m);
		List<Screening> screenGroup = screens.get(m);
		conflS.addAll(screenGroup);
		conSol.addGroup(screenGroup);
		for(Conflict<Screening> c : confl)
			for(Screening s1 : screenGroup) {
				Screening s2 = null;
				if(c.getE1().equals(s1))
					s2 = c.getE2();
				else if(c.getE2().equals(s1))
					s2 = c.getE1();
				else
					continue;
				if(conflS.contains(s2))
					conSol.addConflict(s1, s2);
				else
					searchConflicts(conSol, s2.getM(), conflS, conflM);
				break;
			}
	}
	
	private void printSolutions(List<List<Screening>> solutions) {
		for(int i = 0; i < solutions.size(); i++) {
			List<Screening> ls = solutions.get(i);
			Collections.sort(ls);
			System.out.println("SOLUZIONE " + (i+1));
			for(Screening s : ls)
				System.out.println("\t" + s.getM() + "  -  " + s);
		}
	}
	
	public List<Movie> solveConflicts(Movie m) {
		List<Screening> conflictingScreen = new ArrayList<>();
		List<Movie> conflictingMovie = new ArrayList<>();
		ConflictsSolver<Screening> conSol = new ConflictsSolver<>();
		searchConflicts(conSol, m, conflictingScreen, conflictingMovie);
		if(conSol.bestResult() == conflictingMovie.size())
			return conflictingMovie;
		printSolutions(conSol.allBestSolutions());
		
		return null;
	}
	
	public static void main(String[] args) {
		FilmFestival tff = new FilmFestival();
		Movie m = new Movie("God Bless The Child", 2015, 92);
		Calendar cal = Calendar.getInstance();
		cal.set(2015,10,21,17,0);
		Screening scr = new Screening(m, cal.getTime());
		tff.addScreen(scr);
		cal.set(2015,10,22,11, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		//cal.set(2015,10,23,19, 45);
		//tff.addScreen(new Screening(m, cal.getTime()));
		
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
		//cal.set(2015,10,24,19, 45);
		//tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("La Patota", 2015, 103);
		cal.set(2015,10,22,22, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,23,9, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		//cal.set(2015,10,24,9, 45);
		//tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("Lo Scambio", 2015, 93);
		cal.set(2015,10,23,17, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		//cal.set(2015,10,24,19, 45);
		//tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("Sopladora De Hojas", 2015, 96);
		cal.set(2015,10,23,20, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		//cal.set(2015,10,24,9, 45);
		//tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("High-Rise", 2015, 112);
		//cal.set(2015,10,23,22, 30);
		//tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,24,14, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("Me & Earl & The Dying Girl", 2015, 100);
		cal.set(2015,10,22,22, 15);
		tff.addScreen(new Screening(m, cal.getTime()));
		//cal.set(2015,10,23,14, 15);
		//tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("February", 2015, 93);
		cal.set(2015,10,22,9, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		//cal.set(2015,10,23,22, 15);
		//tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("Mia Madre Fa L'Attrice", 2015, 78);
		cal.set(2015,10,21,19, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,22,9, 45);
		tff.addScreen(new Screening(m, cal.getTime()));
		//cal.set(2015,10,23,11, 45);
		//tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("The Girl In The Photographs", 2015, 95);
		cal.set(2015,10,21,22, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,22,9, 0);
		Screening ch2 = new Screening(m, cal.getTime());
		tff.addScreen(ch2);
		//cal.set(2015,10,24,22, 15);
		//tff.addScreen(new Screening(m, cal.getTime()));
		
		m = new Movie("The Quiet Earth", 2015, 91);
		cal.set(2015,10,21,14, 45);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,22,9, 0);
		tff.addScreen(new Screening(m, cal.getTime()));
		//cal.set(2015,10,23,17, 15);
		//Screening ch1 = new Screening(m, cal.getTime());
		//tff.addScreen(ch1);
		
		m = new Movie("Nusty Baby", 2015, 100);
		cal.set(2015,10,21,22, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		cal.set(2015,10,22,14, 30);
		tff.addScreen(new Screening(m, cal.getTime()));
		//cal.set(2015,10,23,9, 0);
		//tff.addScreen(new Screening(m, cal.getTime()));
		
		List<Movie> movies = tff.solveConflicts(m);
		//for(Movie mov : movies)
			//System.out.println(mov);
	}
}
