package com.eixox.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.eixox.adapters.Adapter;

/**
 * An adapter that can read and write to xml attribute nodes.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class XmlAttributeAdapter<T> implements XmlAdapter<T> {

	/**
	 * The name of the attribute;
	 */
	public final String xmlName;

	/**
	 * The component adapter to parse and format strings.
	 */
	public final Adapter<T> componentAdapter;

	/**
	 * Creates a new xml attribute adapter instance with the provided
	 * parameters.
	 * 
	 * @param xmlName
	 * @param componentAdapter
	 */
	public XmlAttributeAdapter(String xmlName, Adapter<T> componentAdapter) {
		this.xmlName = xmlName;
		this.componentAdapter = componentAdapter;
	}

	/**
	 * Parses an attribute node with the name specified on this instance.
	 */
	public T parse(Node node) {

		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null)
			return null;

		Node att = attributes.getNamedItem(xmlName);
		return att == null
				? null
				: componentAdapter.parse(att.getNodeValue());
	}

	/**
	 * Formats a value as an attribute of a parent node.
	 */
	public Node format(Object value, Node parent) {

		String f = componentAdapter.formatObject(value);
		((Element) parent).setAttribute(xmlName, f);
		return parent;
	}

	/**
	 * Gets the data type of this xml adapter.
	 */
	public Class<T> getDataType() {
		return this.componentAdapter.dataType;
	}

}
