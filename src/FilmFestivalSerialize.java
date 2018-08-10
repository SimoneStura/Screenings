import java.io.Serializable;


public class FilmFestivalSerialize implements Serializable {
	private static final long serialVersionUID = 8858739803531137363L;
	private String name;
	private int minimumToWait;
	
	private Screening shows[];
	
	public FilmFestivalSerialize(FilmFestival ff) {
		name = ff.getName();
		minimumToWait = ff.getMinimumToWait();
		shows = new Screening[ff.getShows().size()];
		ff.getShows().toArray(shows);
	}
	
	public FilmFestival observe() {
		FilmFestival observed = new FilmFestival(name, minimumToWait);
		for(Screening s : shows)
			observed.addScreen(s);
		return observed;
	}
}
