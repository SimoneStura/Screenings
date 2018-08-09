import java.io.File;
import java.io.*;
import java.util.*;

public class Model extends Observable {
	private FilmFestival ff;
	private File saveFile;
	
	public void newFilmFestival(String name, int minimumBtwMovies) {
		ff = new FilmFestival(name, minimumBtwMovies);
		Calendar cal = Calendar.getInstance();
		cal.set(2015,10,21,17,0);
		ff.addScreen(new Screening(new Movie("La La Land", 2016, 128), cal.getTime()));
	}
	
	public void loadFilmFestival(File file) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            ff = (FilmFestival) ois.readObject();
            saveFile = file;
            ois.close();
		} catch(Exception e) {
			System.err.println(e);
			System.exit(0);
		}
	}
	
	public void saveFilmFestival() {
		if(saveFile == null) return;
		saveFilmFestival(saveFile);
	}
	
	public void saveFilmFestival(File file) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream(file)));
            oos.writeObject(ff);
            oos.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public FilmFestival getFilmFestival() {
		return ff;
	}
	
	public File getSaveFile() {
		return saveFile;
	}
	
	public Collection<Screening> getShows() {
		return ff.getShows();
	}
}
