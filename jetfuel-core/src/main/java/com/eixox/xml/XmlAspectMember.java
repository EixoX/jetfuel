package com.eixox.xml;

import java.lang.reflect.Field;
import java.util.List;

import com.eixox.JetfuelException;
import com.eixox.adapters.ArrayAdapter;
import com.eixox.adapters.ListAdapter;
import com.eixox.reflection.AspectField;

/**
 * A representation of xml aspect member.
 * 
 * @author Rodrigo Portela
 *
 */
public class XmlAspectMember extends AspectField {

	/**
	 * The xml adapter that will be used to read and write to xml nodes.
	 */
	public final XmlAdapter<?> xmlAdapter;

	/**
	 * Creates a new instance of the xml aspect member.
	 * 
	 * @param field
	 * @param annotation
	 */
	public XmlAspectMember(Field field, Xml annotation) {
		super(field);
		this.xmlAdapter = createXmlAdapter(
				annotation.name().isEmpty()
						? field.getName()
						: annotation.name(),
				annotation);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private XmlAdapter<?> createXmlAdapter(String xmlName, Xml annotation) {
		switch (annotation.type()) {
		case ATTRIBUTE:
			// All attributes are parseable directly.
			return new XmlAttributeAdapter(xmlName, this.adapter);
		case CDATA:
			// An array of cdata simple elements?
			if (this.adapter instanceof ArrayAdapter<?>) {
				ArrayAdapter<?> arrayAdapter = (ArrayAdapter<?>) this.adapter;
				return new XmlArrayAdapter(
						xmlName,
						new XmlCdataAdapter(arrayAdapter.componentAdapter));
			}
			// A list of cdata simple elements?
			else if (this.adapter instanceof ListAdapter<?>) {
				ListAdapter<?> listAdapter = (ListAdapter<?>) this.adapter;
				return new XmlListAdapter(
						xmlName,
						new XmlCdataAdapter(listAdapter.componentAdapter),
						listAdapter.dataType);
			}
			// A direct parser of cdata elements.
			else
				return new XmlCdataAdapter(this.adapter);
		case ELEMENT:
			// Simple elements?
			if (this.adapter != null) {

				// An array of simple elements.
				if (this.adapter instanceof ArrayAdapter<?>) {
					ArrayAdapter<?> arrayAdapter = (ArrayAdapter<?>) this.adapter;
					return new XmlArrayAdapter(
							xmlName,
							new XmlTextAdapter(arrayAdapter.componentAdapter));
				}

				// A list of simple elements.
				else if (this.adapter instanceof ListAdapter<?>) {
					ListAdapter<?> listAdapter = (ListAdapter<?>) this.adapter;
					return listAdapter.componentAdapter != null
							? new XmlListAdapter(
									xmlName,
									new XmlTextAdapter(listAdapter.componentAdapter),
									listAdapter.dataType)
							: new XmlListAdapter(
									xmlName,
									XmlAspect.getInstance(field.getGenericType()),
									listAdapter.dataType);
				}

				// A simple element
				else
					return new XmlElementAdapter(
							xmlName,
							new XmlTextAdapter(this.adapter));
			}
			// Complex elements, no standard adapter was present.
			else {
				Class<?> dataType = this.getDataType();

				// An array of complex elements.
				if (dataType.isArray())
					return new XmlArrayAdapter(
							xmlName,
							XmlAspect.getInstance(dataType.getComponentType()));

				// A list of complex elements.
				else if (List.class.isAssignableFrom(dataType))
					return new XmlListAdapter(
							xmlName,
							XmlAspect.getInstance(field.getGenericType()),
							dataType);

				// A simple element
				else
					return new XmlElementAdapter(
							xmlName,
							XmlAspect.getInstance(dataType));
			}
		case TEXT:
			// An array of simple text elements?
			if (this.adapter instanceof ArrayAdapter<?>) {
				ArrayAdapter<?> arrayAdapter = (ArrayAdapter<?>) this.adapter;
				return new XmlArrayAdapter(
						xmlName,
						new XmlTextAdapter(arrayAdapter.componentAdapter));
			}
			// A list of simple text elements?
			else if (this.adapter instanceof ListAdapter<?>) {
				ListAdapter<?> listAdapter = (ListAdapter<?>) this.adapter;
				return new XmlListAdapter(
						xmlName,
						new XmlTextAdapter(listAdapter.componentAdapter),
						listAdapter.dataType);
			}
			// A direct parser of text elements.
			else
				return new XmlTextAdapter(this.adapter);
		default:
			throw new JetfuelException("Can't create xml adapters for xml type " +
					annotation.type() +
					" on " +
					field +
					" in " +
					field.getDeclaringClass());
		}
	}

}
