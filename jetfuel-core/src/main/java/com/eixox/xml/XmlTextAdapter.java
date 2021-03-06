package com.eixox.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.eixox.JetfuelException;
import com.eixox.adapters.Adapter;

/**
 * An Xml adapter that can read from and write to xml text nodes;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class XmlTextAdapter<T> implements XmlAdapter<T> {

	/**
	 * The component adapter to parse and format text content;
	 */
	public final Adapter<T> componentAdapter;

	/**
	 * Creates a new simple xml text adapter;
	 * 
	 * @param componentAdapter
	 */
	public XmlTextAdapter(Adapter<T> componentAdapter) {
		if (componentAdapter == null)
			throw new JetfuelException("The component adapter cannot be null");
		this.componentAdapter = componentAdapter;

	}

	/**
	 * Parses the text content of the given node as the data type of this component
	 * adapter;
	 */
	public T parse(Node node) {

		StringBuilder builder = new StringBuilder();
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
			if (child.getNodeType() == Node.TEXT_NODE)
				builder.append(child.getNodeValue());

		return this.componentAdapter.parse(builder.toString());
	}

	/**
	 * Appends the formatted text of the value as a text node to it's parent;
	 */
	public Node format(Object value, Node parent) {
		String txt = this.componentAdapter.formatObject(value);
		if (txt != null && !txt.isEmpty()) {
			Document document = parent.getOwnerDocument();
			Text text = document.createTextNode(txt);
			parent.appendChild(text);
			return text;
		} else {
			return null;
		}
	}

	/**
	 * Gets the data type of this XML adapter.
	 */
	public Class<T> getDataType() {
		return this.componentAdapter.dataType;
	}

}
