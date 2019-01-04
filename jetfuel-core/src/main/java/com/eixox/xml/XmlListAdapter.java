package com.eixox.xml;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.eixox.JetfuelException;

/**
 * An xml adapter that can read and write objects as xml child elements.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class XmlListAdapter<T> implements XmlAdapter<List<T>> {

	/**
	 * The list type used to instantiate new lists.
	 */
	public final Class<? extends List<T>> listType;

	/**
	 * The xml adapter to use on child element nodes.
	 */
	public final XmlAdapter<T> xmlAdapter;

	/**
	 * The xml name to look for on child elements.
	 */
	public final String xmlName;

	/**
	 * Creates a new xml list adapter.
	 * 
	 * @param xmlName
	 * @param xmlAdapter
	 * @param listType
	 */
	public XmlListAdapter(String xmlName, XmlAdapter<T> xmlAdapter, Class<? extends List<T>> listType) {

		if (xmlAdapter == null)
			throw new JetfuelException("The list xml adapter cannot be null");
		else if (listType == null)
			throw new JetfuelException("The list type cannot be null.");

		this.xmlName = xmlName;
		this.xmlAdapter = xmlAdapter;
		this.listType = listType;
	}

	/**
	 * Parses the child XML element nodes as a list of objects.
	 */
	public List<T> parse(Node node) {
		try {
			List<T> list = listType.getConstructor().newInstance();
			for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
				if (child.getNodeType() == Node.ELEMENT_NODE && xmlName.equalsIgnoreCase(child.getNodeName())) {
					T item = xmlAdapter.parse(child);
					list.add(item);
				}
			return list;

		} catch (Exception e) {
			throw new JetfuelException(e);
		}
	}

	/**
	 * Formats a list as child elements of a parent xml node.
	 */
	@SuppressWarnings("unchecked")
	public Node format(Object value, Node parent) {
		if (value != null) {
			List<T> list = (List<T>) value;
			Document document = parent.getOwnerDocument();
			for (T item : list) {
				Element element = document.createElement(xmlName);
				this.xmlAdapter.format(item, element);
				parent.appendChild(element);
			}
		}
		return parent;
	}

	/**
	 * Gets the data type of this xml adapter;
	 */
	@SuppressWarnings("unchecked")
	public Class<List<T>> getDataType() {
		return (Class<List<T>>) this.listType;
	}

}
