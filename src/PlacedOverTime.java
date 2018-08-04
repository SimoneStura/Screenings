import java.util.Date;

public interface PlacedOverTime<E> extends Comparable<E> {
	
	public Date getStartTime();

	public Date getEndTime();

	/*
	 * Se i due elementi si sovrappongono il metodo ritorna 0
	 */
	public int gap(E e);
}
