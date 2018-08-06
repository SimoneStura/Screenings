import java.io.Serializable;

public class Conflict<E> implements Serializable {
	private E e1;
	private E e2;
	
	public Conflict(E e1, E e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	public E getE1() {
		return e1;
	}
	
	public E getE2() {
		return e2;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Conflict<?>)
			return (((Conflict<?>) obj).e1.equals(e1) && ((Conflict<?>) obj).e2.equals(e2)) ||
					(((Conflict<?>) obj).e1.equals(e2) && ((Conflict<?>) obj).e2.equals(e1));
		return false;
	}
	
	public String toString() {
		return "(" + e1 + " -- " + e2 + ")";
	}
	
	public boolean contains(E e) {
		return e1.equals(e) || e2.equals(e);
	}
}
