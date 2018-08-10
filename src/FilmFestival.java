import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import javafx.collections.*;

public class FilmFestival implements Serializable {
	private static final long serialVersionUID = 8858739803531137363L;

	private String name;
	private LocalDate firstDay = null;
	private LocalDate lastDay = null;
	private int minimumToWait;
	
	private SortedSet<Screening> shows = new TreeSet<>();
	private ObservableList<Movie> movies = FXCollections.observableArrayList();
	private Map<Movie,ObservableList<Screening>> screens = new HashMap<>();
	private List<Conflict<Screening>> confl = new ArrayList<>();
	
	public FilmFestival(String name, int minimumToWait) {
		setName(name);
		setMinimumToWait(minimumToWait);
	}
	
	public void setName(String name) {
		this.name = name;
		
	}
	
	public void setMinimumToWait(int minimumToWait) {
		this.minimumToWait = minimumToWait;
	}
	
	public String getName() {
		return name;
	}
	
	public LocalDate getFirstDay() {
		return firstDay;
	}
	
	public LocalDate getLastDay() {
		return lastDay;
	}
	
	public int getMinimumToWait() {
		return minimumToWait;
	}
	
	public boolean addMovie(Movie m) {
		if(movies.contains(m)) return false;
		movies.add(m);
		screens.put(m, FXCollections.observableArrayList());
		return true;
	}
	
	public boolean addScreen(Screening s) {
		if(!shows.add(s)) return false;
		updateDays(s.getStartTime().toLocalDate());
		Movie m = s.getM();
		addMovie(m);
		screens.get(m).add(s);
		for(Screening screen : shows) {
			if(screen.gap(s) < 0) break;
			if(!(screen.getM().equals(m)) && Screening.isConflict(s, screen))
				confl.add(new Conflict<>(screen, s));
		}
		return true;
	}
	
	private void updateDays(LocalDate newDate) {
		if(firstDay == null || firstDay.compareTo(newDate) > 0)
			firstDay = newDate;
		if(lastDay == null || newDate.compareTo(lastDay) > 0)
			lastDay = newDate;
	}
	
	public SortedSet<Screening> getShows() {
		return shows;
	}
	
	public ObservableList<Movie> getMovies() {
		return movies;
	}
	
	public ObservableList<Screening> getScreens(Movie m) {
		return screens.get(m);
	}
	
	public List<Movie> solveConflicts() {
		SortedSet<Movie> visited = new TreeSet<>();
		List<Movie> chosen = new ArrayList<>();
		for(Movie m : movies) {
			if(visited.contains(m))
				continue;
			List<Movie> nextChosen = solveConflicts(m, visited);
			if(nextChosen == null)
				continue;
			chosen.addAll(nextChosen);
		}
		return chosen;
	}
	
	private List<Movie> solveConflicts(Movie m, SortedSet<Movie> visited) {
		List<Movie> conflMovie = new ArrayList<>();
		ConflictsSolver<Screening> conSol = new ConflictsSolver<>();
		searchConflicts(conSol, m, conflMovie, visited);
		if(conSol.bestResult() == conSol.getNumGroups()) {
			for(Screening s : conSol.bestSolution())
				System.out.println(s + "  |  " + s.getM());
			System.out.println();
			return conflMovie;
		}
		System.out.println("CONFLITTO NON RISOLVIBILE\n");
		List<List<Screening>> solutions = conSol.allGoodSolutions();
		SortedSet<Movie> exclSet = new TreeSet<>();
		List<List<Movie>> excluded = findExcluded(solutions, conflMovie, exclSet);
		List<Movie> toExclude = chooseBetween(excluded.get(0).size() ,exclSet);
		conflMovie.removeAll(toExclude);
		return conflMovie;
	}
	
	private void searchConflicts(ConflictsSolver<Screening> conSol, Movie m, 
			List<Movie> conflMovie, SortedSet<Movie> visited) {
		visited.add(m);
		conflMovie.add(m);
		List<Screening> screenGroup = screens.get(m);
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
				if(visited.contains(s2.getM()))
					conSol.addConflict(s1, s2);
				else
					searchConflicts(conSol, s2.getM(), conflMovie, visited);
				break;
			}
	}
	
	private List<List<Movie>> findExcluded(List<List<Screening>> solutions, List<Movie> conflMovie, 
			SortedSet<Movie> exclSet) {
		List<List<Movie>> excluded = new ArrayList<>();
		for(List<Screening> ls : solutions) {
			List<Movie> excl = new ArrayList<>();
			excl.addAll(conflMovie);
			for(Screening s : ls)
				excl.remove(s.getM());
			excluded.add(excl);
			exclSet.addAll(excl);
		}
		return excluded;
	}
	
	private List<Movie> chooseBetween(int numChoices, SortedSet<Movie> exclSet) {
		Movie excl[] = new Movie[exclSet.size()];
		excl = exclSet.toArray(excl);
		List<Movie> toBeExcluded = new ArrayList<>();
		System.out.println("SCEGLINE " + numChoices + " DA ESCLUDERE\n");
		int idx = 1;
		for(Movie m : exclSet)
			System.out.println((idx++) + " -> " + m);
		Scanner scan = new Scanner(System.in);
		for(int i = 0; i < numChoices; i++) {
			System.out.print("Inserisci il numero del film da escludere: ");
			int idxExcluded = scan.nextInt();
			System.out.println();
			toBeExcluded.add(excl[idxExcluded-1]);
		}
		scan.close();
		return toBeExcluded;
	}
	/*
	private void printSolutions(List<List<Screening>> solutions) {
		for(int i = 0; i < solutions.size(); i++) {
			List<Screening> ls = solutions.get(i);
			Collections.sort(ls);
			System.out.println("SOLUZIONE " + (i+1));
			for(Screening s : ls)
				System.out.println("\t" + s.getM() + "  -  " + s);
		}
	}
	*/
	public static void main(String[] args) {
		FilmFestival tff = new FilmFestival("Torino Film Festival", 10);
		
		Movie m = new Movie("God Bless The Child", 2015, 92);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,21,17,0)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,22,11,30)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,19,45)));
		
		m = new Movie("Sufragette", 2015, 100);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,21,14,30)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,17,15)));
		
		m = new Movie("Idealisten", 2015, 114);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,22,16,30)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,12,0)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,24,19,45)));
		
		m = new Movie("La Patota", 2015, 103);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,22,22,0)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,9,30)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,24,9,45)));
		
		m = new Movie("Lo Scambio", 2015, 93);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,17,30)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,24,19,45)));
		
		m = new Movie("Sopladora De Hojas", 2015, 96);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,20,0)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,24,9,45)));
		
		m = new Movie("High-Rise", 2015, 112);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,22,30)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,24,14,0)));
		
		m = new Movie("Me & Earl & The Dying Girl", 2015, 100);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,22,22,15)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,14,15)));
		
		m = new Movie("February", 2015, 93);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,22,9,0)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,22,15)));
		
		m = new Movie("Mia Madre Fa L'Attrice", 2015, 78);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,21,19,30)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,22,9,45)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,11,45)));
		
		m = new Movie("The Girl In The Photographs", 2015, 95);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,21,22,30)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,22,9,0)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,24,22,15)));
		
		m = new Movie("The Quiet Earth", 2015, 91);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,21,14,45)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,22,9,0)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,17,15)));
		
		m = new Movie("Nusty Baby", 2015, 100);
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,21,22,30)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,22,14,30)));
		tff.addScreen(new Screening(m, LocalDateTime.of(2015,10,23,9,0)));
		
		
		List<Movie> movies = tff.solveConflicts();
		if(movies == null) System.exit(0);
		System.out.println("FILM SCELTI: " + movies.size() + "/" + tff.getMovies().size() + "\n");
		for(Movie mov : movies)
			System.out.println(mov);
	}
}
