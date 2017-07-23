package com.eixox.xml;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * An xml adapter that can read and write arrays as child elements of a xml
 * node.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class XmlArrayAdapter<T> implements XmlAdapter<T[]> {

	/**
	 * The name of the child element nodes.
	 */
	public final String xmlName;
	/**
	 * The adapter to use on child element nodes.
	 */
	public final XmlAdapter<T> componentAdapter;

	/**
	 * Creates a new instance of the xml array adapter.
	 * 
	 * @param xmlName
	 * @param componentAdapter
	 */
	public XmlArrayAdapter(String xmlName, XmlAdapter<T> componentAdapter) {
		this.xmlName = xmlName;
		this.componentAdapter = componentAdapter;
	}

	/**
	 * Parses child elements with a specific xml name as an array.
	 */
	@SuppressWarnings("unchecked")
	public T[] parse(Node node) {
		ArrayList<Node> children = new ArrayList<Node>();
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
			if (child.getNodeType() == Node.ELEMENT_NODE && xmlName.equalsIgnoreCase(child.getNodeName()))
				children.add(child);

		T[] array = (T[]) Array.newInstance(componentAdapter.getDataType(), children.size());
		for (int i = 0; i < array.length; i++) {
			array[i] = this.componentAdapter.parse(children.get(i));
		}
		return array;
	}

	/**
	 * Formats the input array value as xml elements of a parent node.
	 */
	@SuppressWarnings("unchecked")
	public Node format(Object value, Node parent) {
		if (value != null) {
			T[] arr = (T[]) value;
			Document document = parent.getOwnerDocument();
			for (int i = 0; i < arr.length; i++) {
				Element element = document.createElement(this.xmlName);
				this.componentAdapter.format(arr[i], element);
				parent.appendChild(element);
			}
		}
		return parent;
	}

	/**
	 * Gets the data type of this xml adapter.
	 */
	@SuppressWarnings("unchecked")
	public Class<T[]> getDataType() {
		return (Class<T[]>) Array.newInstance(componentAdapter.getDataType(), 0).getClass();
	}

}
