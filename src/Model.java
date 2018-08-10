import java.io.File;
import java.time.LocalDateTime;
import java.io.*;
import java.util.*;

public class Model extends Observable {
	private FilmFestival ff;
	private File saveFile;
	
	public void newFilmFestival(String name, int minimumBtwMovies) {
		ff = new FilmFestival(name, minimumBtwMovies);
		ff.addScreen(new Screening(new Movie("La La Land", 2016, 128), LocalDateTime.of(2015,10,21,17,0)));
	}
	
	public void loadFilmFestival(File file) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(file)));
			FilmFestivalSerialize ffs = (FilmFestivalSerialize) ois.readObject();
			ff = ffs.observe();
            saveFile = file;
            ois.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public boolean saveFilmFestival() {
		if(saveFile == null) return false;
		return saveFilmFestival(saveFile);
	}
	
	public boolean saveFilmFestival(File file) {
		if(ff == null) return false;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream(file)));
			FilmFestivalSerialize ffs = new FilmFestivalSerialize(ff);
            oos.writeObject(ffs);
            oos.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return true;
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
