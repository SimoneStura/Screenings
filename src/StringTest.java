import java.util.Calendar;
import java.util.Date;

public class StringTest implements PlacedOverTime<StringTest> {
	private String s;
	private int start;
	private int end;
	
	public StringTest(String s, int start, int end) {
		this.s = s;
		this.start = start;
		this.end = end;
	}
	
	public int compareTo(StringTest st) {
		if(start == st.start)
			if(end == st.end)
				return s.compareTo(st.s);
			else
				return end - st.end;
		return start - st.start;
	}
	
	public Date getStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(2018,6,5,start,0);
		return cal.getTime();
	}

	public Date getEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(2018,6,5,end,0);
		return cal.getTime();
	}
	
	public static boolean isConflict(StringTest s1, StringTest s2) {
		if(s2.start - s1.end > 0) return false;
		if(s1.start - s2.end > 0) return false;
		return true;
	}
	
	public int gap(StringTest st) {
		int distance = st.start - this.end;
		if(distance > 0) return distance;
		distance = this.start - st.end;
		if(distance > 0) return Math.negateExact(distance);
		return 0;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof StringTest)
			return s.equals(((StringTest) obj).s);
		return false;
	}
	
	public String toString() {
		return s;
	}
}
