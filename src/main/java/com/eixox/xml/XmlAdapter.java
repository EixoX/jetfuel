package com.eixox.xml;

import org.w3c.dom.Node;

/**
 * A class that can read from and write to xml node elements; An extension to an
 * adapter;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public interface XmlAdapter<T> {

	public T parse(Node node);

	public Node format(Object memberValue, Node parent);

	public Class<T> getDataType();

}
