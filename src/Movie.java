import java.io.Serializable;

public class Movie implements Comparable<Movie>, Serializable {
	private static final long serialVersionUID = 5781125133996852924L;
	
	private String title;
	private int year;
	private int runtime;
	
	public Movie(String title, int year, int runtime) {
		this.title = title;
		this.year = year;
		this.runtime = runtime;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getRuntime() {
		return runtime;
	}
	
	public String toString() {
		return title + " (" + year + ")";
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Movie)) return false;
		Movie m = (Movie) o;
		return this.title == m.title && this.year == m.year;
	}
	
	public int compareTo(Movie m) {
		if(this.title == m.title) return this.year - m.year;
		return this.title.compareTo(m.title);
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
