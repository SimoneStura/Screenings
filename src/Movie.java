import java.io.Serializable;

import javafx.beans.property.*;

public class Movie implements Comparable<Movie>, Serializable {
	private static final long serialVersionUID = 5781125133996852924L;
	
	private StringProperty title;
	private IntegerProperty year;
	private IntegerProperty runtime;
	private StringProperty directedBy;
	private StringProperty section;
	
	public Movie(String title, int year, int runtime) {
		setTitle(title);
		setYear(year);
		this.runtime = new SimpleIntegerProperty(runtime);
	}
	
	public void setTitle(String title) {
		this.title = new SimpleStringProperty(title);
	}
	
	public void setYear(int year) {
		this.year = new SimpleIntegerProperty(year);
	}
	
	public void setDirectedBy(String directors) {
		directedBy = new SimpleStringProperty(directors);
	}
	
	public void setSection(String section) {
		this.section = new SimpleStringProperty(section);
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
	
	public StringProperty getTitleProperty() {
		return title;
	}
	
	public IntegerProperty getYearProperty() {
		return year;
	}
	
	public IntegerProperty getRuntimeProperty() {
		return runtime;
	}
	
	public StringProperty getDirectedByProperty() {
		return directedBy;
	}
	
	public StringProperty getSectionProperty() {
		return section;
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
