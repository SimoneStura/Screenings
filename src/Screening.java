
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.io.Serializable;

public class Screening implements PlacedOverTime<Screening>, Serializable{
	private static final long serialVersionUID = 4637016489716615000L;
	
	private Movie m;
	private LocalDateTime startTime;
	private Cinema cinema;
	private int theater;
	private int minimumToWait = 0;
	private int minutesToWait = 0;
	private String notes;
	private boolean hidden = false;

	private static DateTimeFormatter dateHour = DateTimeFormatter.ofPattern("EEE dd/MM HH:mm");
	private static DateTimeFormatter hour = DateTimeFormatter.ofPattern("HH:mm");
	
	public Screening(Movie m, LocalDateTime startTime) {
		this.m = m;
		setStartTime(startTime);
	}
	
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	
	public void setCinema(Cinema cinema, int theater) {
		this.cinema = cinema;
		this.theater = theater;
	}
	
	public void setMinutesToWait(int minutesToWait) {
		this.minutesToWait = minutesToWait;
	}
	
	public void setMinimumToWait(int minimumToWait) {
		this.minimumToWait = minimumToWait;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Movie getM() {
		return m;
	}
	
	public LocalDateTime getStartTime() {
		return startTime;
	}
	
	public LocalDateTime getEndTime() {
		return startTime.plusMinutes(m.getRuntime());
	}
	
	public Cinema getCinema() {
		return cinema;
	}
	
	public int getTheater() {
		return theater;
	}
	
	public int getMinimumToWait() {
		return minimumToWait;
	}
	
	public int getMinutesToWait() {
		return minutesToWait;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public int extraMinutes() {
		return Math.max(minimumToWait, minutesToWait);
	}
	
	public int cinemaDistance(Screening s) {
		if(s == null || this.cinema == null || s.cinema == null)
			return 0;
		return Math.max(this.cinema.getDistance(s.cinema), s.cinema.getDistance(this.cinema));
	}
	
	public int gap(Screening s) {
		int distance1 = ((int) (ChronoUnit.MINUTES.between(this.getEndTime(), s.startTime)))
				- this.extraMinutes() - this.cinemaDistance(s);
		if(distance1 > 0) return distance1;
		int distance2 = ((int) (ChronoUnit.MINUTES.between(this.startTime, s.getEndTime())))
				+ s.extraMinutes() + s.cinemaDistance(this);
		if(distance2 < 0) return distance2;
		return 0;
	}
	
	public String toString() {
		return startTime.format(dateHour) + " - " + getEndTime().format(hour);
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Screening)) return false;
		Screening s = (Screening) o;
		return this.m.equals(s.m) && this.startTime.equals(s.startTime) 
				&& (this.cinema == null || s.cinema == null 
				|| (this.cinema.equals(s.cinema) && this.theater == s.theater));
	}
	
	public int compareTo(Screening s) {
		int cmp = 0;
		if(startTime.compareTo(s.startTime) == 0) {
			if(getEndTime().compareTo(s.getEndTime()) == 0) {
				if(m.compareTo(s.m) == 0) {
					if(cinema == null)
						cmp = 0;
					else if(cinema.compareTo(s.cinema) == 0)
						cmp = theater - s.theater;
					else
						cmp = cinema.compareTo(s.cinema);
				}
				else
					cmp = m.compareTo(s.m);
			} else
				cmp = getEndTime().compareTo(s.getEndTime());
		} else
			cmp = startTime.compareTo(s.startTime);
		return cmp;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void hide(boolean b) {
		hidden = b;
	}
	
	public static boolean isConflict(Screening s1, Screening s2) {
		return s1.gap(s2) == 0;
	}
	
	public static void main(String[] args) {
		Movie m = new Movie("La La Land", 2016, 128);
		Screening s1 = new Screening(m, LocalDateTime.of(2018,6,5,21,45));
		Screening s2 = new Screening(m, LocalDateTime.of(2018,6,5,21,45));
		Screening s3 = new Screening(m, LocalDateTime.of(2018,6,5,19,30));
		System.out.println(s1 + " " + s1.getM());
		System.out.println(s2 + " " + s2.getM());
		System.out.println(s3 + " " + s3.getM());
		System.out.println("s1 equals s2 => " + s1.equals(s2));
		System.out.println("s1 equals s3 => " + s1.equals(s3));
		System.out.println("s1 compareTo s2 => " + s1.compareTo(s2));
		System.out.println("s1 compareTo s3 => " + s1.compareTo(s3));
		System.out.println("gap between s1 - s2 => " + s1.gap(s2));
		System.out.println("gap between s2 - s1 => " + s2.gap(s1));
		System.out.println("gap between s1 - s3 => " + s1.gap(s3));
		System.out.println("gap between s3 - s1 => " + s3.gap(s1));
	}
}
