package com.eixox.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.eixox.data.common.TableRow;

public class SortNode {

	public Column column;
	public SortDirection direction;
	public SortNode next;

	public SortNode(Column column, SortDirection direction) {
		this.column = column;
		this.direction = direction;
	}

	public final Comparator<Object[]> createObjectArrayComparator() {
		return new Comparator<Object[]>() {
			final int ordinal = column.getColumnIndex();

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public int compare(Object[] left, Object[] right) {
				int ascSort = 0;
				Object a = left[ordinal];
				Object b = right[ordinal];
				if (a == null)
					ascSort = (b == null)
							? 0
							: 1;
				else if (b == null)
					ascSort = -1;
				else
					ascSort = ((Comparable) a).compareTo(b);
				return direction == SortDirection.ASC
						? ascSort
						: -ascSort;
			}
		};
	}

	public final Comparator<TableRow> createTableRowComparator() {
		return new Comparator<TableRow>() {
			final int ordinal = column.getColumnIndex();

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public int compare(TableRow left, TableRow right) {
				int ascSort = 0;
				Object a = left.getValue(ordinal);
				Object b = right.getValue(ordinal);
				if (a == null)
					ascSort = (b == null)
							? 0
							: 1;
				else if (b == null)
					ascSort = -1;
				else
					ascSort = ((Comparable) a).compareTo(b);
				return direction == SortDirection.ASC
						? ascSort
						: -ascSort;
			}
		};
	}

	public final <T> Comparator<T> createEntityComparator() {
		return new Comparator<T>() {

			final DataAspectField field = (DataAspectField) column;

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public int compare(T left, T right) {
				int ascSort = 0;
				Object a = field.getValue(left);
				Object b = field.getValue(right);
				if (a == null)
					ascSort = (b == null)
							? 0
							: 1;
				else if (b == null)
					ascSort = -1;
				else
					ascSort = ((Comparable) a).compareTo(b);
				return direction == SortDirection.ASC
						? ascSort
						: -ascSort;
			}
		};
	}

	public final void sort(List<Object[]> rows) {
		Collections.sort(rows, createObjectArrayComparator());
	}

	public final void sortRows(List<TableRow> rows) {
		Collections.sort(rows, createTableRowComparator());
	}

	public final <T> void sortEntities(List<T> entities) {
		Collections.sort(entities, createEntityComparator());
	}

}
