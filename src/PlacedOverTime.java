import java.time.LocalDateTime;

public interface PlacedOverTime<E> extends Comparable<E> {
	
	public LocalDateTime getStartTime();

	public LocalDateTime getEndTime();

	/*
	 * Se i due elementi si sovrappongono il metodo ritorna 0
	 */
	public int gap(E e);
}
