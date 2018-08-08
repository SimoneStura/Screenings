import java.util.Calendar;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Screening implements PlacedOverTime<Screening>, Serializable{
	private static final long serialVersionUID = 4637016489716615000L;
	
	private Movie m;
	private Date startTime;
	private Date endTime;
	private Cinema cinema;
	private int sala; //TRADURRE
	private int minutesExtra;
	private String notes;
	private boolean hidden = false;
	
	private static SimpleDateFormat df = new SimpleDateFormat("EEE dd/MM HH:mm");
	private static SimpleDateFormat dfHour = new SimpleDateFormat("HH:mm");
	
	public Screening(Movie m, Date startTime) {
		this.m = m;
		setStartTime(startTime);
	}
	
	public void setMovie(Movie m) {
		this.m = m;
		setStartTime(this.startTime);
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		endTime = new Date(startTime.getTime() + 60000 * m.getRuntime());
	}
	
	public void setCinema(Cinema cinema, int sala) {
		this.cinema = cinema;
		this.sala = sala;
	}
	
	public void setMinutesExtra(int minutesExtra) {
		this.minutesExtra = minutesExtra;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Movie getM() {
		return m;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public Cinema getCinema() {
		return cinema;
	}
	
	public int getSala() {
		return sala;
	}
	
	public int getMinutesExtra() {
		return minutesExtra;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public int gap(Screening s) {
		int distance1 = (int) (s.startTime.getTime() - this.endTime.getTime()) / 60000;
		if(distance1 > 0) return distance1;
		int distance2 = (int) (this.startTime.getTime() - s.endTime.getTime()) / 60000;
		if(distance2 <= 0) return 0;
		return Math.negateExact(distance2);
	}
	
	public String toString() {
		return df.format(startTime) + " - " + dfHour.format(endTime);
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Screening)) return false;
		Screening s = (Screening) o;
		return this.m.equals(s.m) && this.startTime.equals(s.startTime) 
				&& this.cinema.equals(s.cinema) && this.sala == s.sala;
	}
	
	public int compareTo(Screening s) {
		int cmp = 0;
		if(startTime.compareTo(s.startTime) == 0) {
			if(endTime.compareTo(s.endTime) == 0) {
				if(m.compareTo(s.m) == 0) {
					if(cinema == null)
						cmp = 0;
					else
						cmp = cinema.compareTo(s.cinema);
				}
				else
					cmp = m.compareTo(s.m);
			} else
				cmp = endTime.compareTo(s.endTime);
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
		return isConflict(s1, s2, 0);
	}
	
	public static boolean isConflict(Screening s1, Screening s2, int minutesBtw) {
		long distance = s2.startTime.getTime() - s1.endTime.getTime();
		if(distance/60000 > minutesBtw) return false;
		distance = s1.startTime.getTime() - s2.endTime.getTime();
		if(distance/60000 > minutesBtw) return false;
		return true;
	}
	
	public static void main(String[] args) {
		Movie m = new Movie("La La Land", 2016, 128);
		Calendar cal = Calendar.getInstance();
		cal.set(2018,6,5,21,45);
		Screening s1 = new Screening(m, cal.getTime());
		cal.set(2018,6,5,22,45);
		Screening s2 = new Screening(m, cal.getTime());
		cal.set(2018,6,5,19,30);
		Screening s3 = new Screening(m, cal.getTime());
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
