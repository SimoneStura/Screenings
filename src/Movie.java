import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Movie implements Comparable<Movie>, Serializable {
	private static final long serialVersionUID = 5781125133996852924L;
	
	private StringProperty title;
	private IntegerProperty year;
	private IntegerProperty runtime;
	private StringProperty directedBy;
	private StringProperty section;
	private IntegerProperty numScreens;
	
	public Movie(String title, int year, int runtime) {
		init();
		setTitle(title);
		setYear(year);
		this.runtime.set(runtime);
	}
	
	private void init() {
		title = new SimpleStringProperty();
		year = new SimpleIntegerProperty();
		runtime = new SimpleIntegerProperty();
		directedBy = new SimpleStringProperty();
		section = new SimpleStringProperty();
		numScreens = new SimpleIntegerProperty();
	}
	
	public void setTitle(String title) {
		this.title.set(title);
	}
	
	public void setYear(int year) {
		this.year.set(year);
	}
	
	public void setDirectedBy(String directors) {
		directedBy.set(directors);
	}
	
	public void setSection(String section) {
		this.section.set(section);
	}
	
	public void setNumScreens(int numScreens) {
		this.numScreens.set(numScreens);
	}
	
	public String getTitle() {
		return title.get();
	}
	
	public int getYear() {
		return year.get();
	}
	
	public int getRuntime() {
		return runtime.get();
	}
	
	public String getDirectedBy() {
		return directedBy.get();
	}
	
	public String getSection() {
		return section.get();
	}
	
	public int getNumScreens() {
		return numScreens.get();
	}
	
	public StringProperty titleProperty() {
		return title;
	}
	
	public IntegerProperty yearProperty() {
		return year;
	}
	
	public IntegerProperty runtimeProperty() {
		return runtime;
	}
	
	public StringProperty directedByProperty() {
		return directedBy;
	}
	
	public StringProperty sectionProperty() {
		return section;
	}
	
	public IntegerProperty numScreensProperty() {
		return numScreens;
	}
    
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeUTF(title.get());
        s.writeInt(year.get());
        s.writeInt(runtime.get());
        s.writeUTF(directedBy.get());
        s.writeUTF(section.get());
        s.writeInt(numScreens.get());
    }
    
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    	init();
    	title.set(s.readUTF());
    	year.set(s.readInt());
    	runtime.set(s.readInt());
    	directedBy.set(s.readUTF());
    	section.set(s.readUTF());
    	numScreens.set(s.readInt());
    }
	
	public String toString() {
		return title.get() + " (" + year.get() + ")";
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Movie)) return false;
		Movie m = (Movie) o;
		return this.title.get() == m.title.get() && this.year.get() == m.year.get();
	}
	
	public int compareTo(Movie m) {
		if(this.title.get() == m.title.get()) return year.get() - m.year.get();
		return this.title.get().compareTo(m.title.get());
	}
	
	public static void main(String[] args) {
		Movie m1 = new Movie("La La Land", 2016, 128);
		Movie m2 = new Movie("La La Land", 2016, 190);
		Movie m3 = new Movie("La La Land", 2028, 128);
		
		System.out.println(m1);
		System.out.println(m2);
		System.out.println(m3);

		System.out.println("m1 equals m2 => " + m1.equals(m2));
		System.out.println("m1 compareTo m2 => " + m1.compareTo(m2));
		System.out.println("m1 equals m3 => " + m1.equals(m3));
		System.out.println("m1 compareTo m3 => " + m1.compareTo(m3));
	}
}
