package com.eixox.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlElementAdapter<T> implements XmlAdapter<T> {

	public final String xmlName;
	public final XmlAdapter<T> xmlAdapter;

	public XmlElementAdapter(String xmlName, XmlAdapter<T> xmlAdapter) {
		this.xmlName = xmlName;
		this.xmlAdapter = xmlAdapter;
	}

	public T parse(Node node) {
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
			if (child.getNodeType() == Node.ELEMENT_NODE && xmlName.equalsIgnoreCase(child.getNodeName()))
				return this.xmlAdapter.parse(child);

		return null;
	}

	public Node format(Object value, Node parent) {

		if (this.xmlAdapter instanceof XmlAspect<?>) {
			return ((XmlAspect<?>) this.xmlAdapter).format(
					this.xmlName,
					value,
					parent);
		} else {
			Document document = parent.getOwnerDocument();
			Element element = document.createElement(this.xmlName);
			this.xmlAdapter.format(value, element);
			parent.appendChild(element);
			return element;
		}
	}

	public Class<T> getDataType() {
		return this.xmlAdapter.getDataType();
	}

}
