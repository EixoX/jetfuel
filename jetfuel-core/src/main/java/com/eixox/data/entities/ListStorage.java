package com.eixox.data.entities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.eixox.data.ColumnSchema;
import com.eixox.data.DataAspect;
import com.eixox.data.DataAspectField;
import com.eixox.data.DataDelete;
import com.eixox.data.DataSelect;
import com.eixox.data.DataStorage;
import com.eixox.data.DataUpdate;

public class ListStorage<T> extends DataStorage<T> {

	public final DataAspect<T, DataAspectField> aspect;
	public final List<T> list;

	public ListStorage(Class<T> claz, List<T> innerList) {
		this.aspect = new DataAspect<T, DataAspectField>(claz, null) {
			@Override
			protected DataAspectField decorate(Field field) throws Exception {
				int modifiers = field.getModifiers();
				return Modifier.isFinal(modifiers) ||
						Modifier.isStatic(modifiers) ||
						Modifier.isTransient(modifiers) ||
						!Modifier.isPublic(modifiers)
								? null
								: new DataAspectField(aspect, field);
			}
		};
		this.list = innerList;
	}

	public ListStorage(Class<T> claz) {
		this(claz, new ArrayList<T>());
	}

	@Override
	public ColumnSchema<?> getSchema() {
		return this.aspect;
	}

	@Override
	public void insert(T item) {
		this.list.add(item);
	}

	@Override
	public DataSelect<T> select() {
		return new ListSelect<T>(this);
	}

	@Override
	public DataDelete delete() {
		return new ListDelete(this);
	}

	@Override
	public DataUpdate update() {
		return new ListUpdate<T>(this);
	}

}
