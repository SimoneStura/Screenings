
public interface placedOverTime<E> extends Comparable<E> {
	/*
	 * Se i due elementi si sovrappongono il metodo ritorna 0
	 */
	public int gap(E e);
}
