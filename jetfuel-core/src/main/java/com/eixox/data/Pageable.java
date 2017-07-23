package com.eixox.data;

/*
 * Represents a "page able" programming mode.
 */
public interface Pageable<T> {

	/*
	 * Sets the page size and ordinal. Offset and limit are automatically
	 * calculated.
	 */
	public T page(int pageSize, int pageOrdinal);

	/*
	 * Sets the offset of records.
	 */
	public T setOffset(int offset);

	/*
	 * Sets the record limit.
	 */
	public T setLimit(int limit);
}
