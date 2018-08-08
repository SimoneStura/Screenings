import java.util.Map;
import java.util.HashMap;

public class Cinema implements Comparable<Cinema>{
	
	private String name;
	private Map<Cinema, Integer> distance = new HashMap<>();

	public Cinema(String name) {
		setName(name);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDistance(Cinema c, int dist) {
		distance.put(c, dist);
	}
	
	public String getName() {
		return name;
	}
	
	public int getDistance(Cinema c) {
		Integer d = distance.get(c);
		if(d == null) return 0;
		return d.intValue();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Cinema)
			return name.equals(((Cinema)obj).name);
		return false;
	}
	
	public int compareTo(Cinema c) {
		return name.compareTo(c.name);
	}
}
