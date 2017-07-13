package com.eixox.data.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.eixox.Visitor;
import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;
import com.eixox.data.DataAspectField;
import com.eixox.data.DataSelect;

public class ListSelect<T> extends DataSelect<T> {

	public final ListStorage<T> storage;

	public ListSelect(ListStorage<T> storage) {
		this.storage = storage;
	}

	public Iterator<T> iterator() {
		Iterator<T> iterator = new ListStorageIterator<T>(storage, filter, offset, limit);
		if (sort != null) {
			LinkedList<T> list = new LinkedList<T>();
			while (iterator.hasNext())
				list.add(iterator.next());
			sort.sortEntities(list);
			iterator = list.iterator();
		}
		return iterator;
	}

	@Override
	public T first() {
		Iterator<T> iterator = iterator();
		return iterator.hasNext()
				? iterator.next()
				: null;
	}

	@Override
	public Map<String, Object> toMap() {
		T first = first();
		if (first == null)
			return null;

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (DataAspectField field : storage.aspect) {
			String name = field.name;
			Object value = field.getValue(first);

			map.put(name, value);
		}

		return map;

	}

	@Override
	public long count() {
		long counter = 0;
		Iterator<T> iterator = new ListStorageIterator<T>(storage, filter, 0, 0);
		while (iterator.hasNext())
			counter++;
		return counter;
	}

	@Override
	public boolean exists() {
		return new ListStorageIterator<T>(storage, filter, 0, 0).hasNext();
	}

	@Override
	public Object firstMember(Column column) {
		T item = first();
		return item == null
				? null
				: column.getValue(item);
	}

	@Override
	public List<Object> getMembers(Column column) {
		List<Object> list = new ArrayList<Object>();
		Iterator<T> iterator = iterator();
		while (iterator.hasNext())
			list.add(column.getValue(iterator.next()));
		return list;
	}

	@Override
	public ColumnSchema<?> getSchema() {
		return storage.getSchema();
	}

	@Override
	public void accept(Visitor<T> visitor) {
		for (Iterator<T> i = iterator(); i.hasNext(); visitor.visit(i.next()))
			;
	}

}
