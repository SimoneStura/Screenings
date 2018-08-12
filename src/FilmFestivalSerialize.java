
import java.io.Serializable;

public class FilmFestivalSerialize implements Serializable {
	private static final long serialVersionUID = 8858739803531137363L;
	private String name;
	private int minimumToWait;
	
	private Screening shows[];
	private Movie movies[];
	private Cinema cinemas[];
	
	public FilmFestivalSerialize(FilmFestival ff) {
		name = ff.getName();
		minimumToWait = ff.getMinimumToWait();
		shows = new Screening[ff.getShows().size()];
		movies = new Movie[ff.getMovies().size()];
		cinemas = new Cinema[ff.getCinemas().size()];
		ff.getShows().toArray(shows);
		ff.getMovies().toArray(movies);
		ff.getCinemas().toArray(cinemas);
	}
	
	public FilmFestival observe() {
		FilmFestival observed = new FilmFestival(name, minimumToWait);
		for(Screening s : shows)
			observed.addScreen(s);
		for(Movie m : movies)
			observed.addMovie(m);
		for(Cinema c : cinemas)
			observed.addCinema(c);
		return observed;
	}
}
