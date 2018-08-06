import java.io.File;
import java.io.*;
import java.util.*;

public class Model extends Observable {
	private FilmFestival ff;
	private File saveFile;
	
	public void newFilmFestival(String name) {
		ff = new FilmFestival(name);
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
	
	public String getFFName() {
		return ff.getName();
	}
	
	public File getSaveFile() {
		return saveFile;
	}
	
	public Collection<Screening> getShows() {
		return ff.getShows();
	}
}
