package com.eixox.xml;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eixox.reflection.Aspect;

/**
 * A generic XML aspect that reads and writes objects to xml elements.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class XmlAspect<T> extends Aspect<T, XmlAspectMember> implements XmlAdapter<T> {

	/**
	 * Gets the xml name of the expected child xml node;
	 */
	public final String xmlName;

	/**
	 * Creates a new xml aspect instance;
	 * 
	 * @param dataType
	 */
	private XmlAspect(Class<T> dataType) {
		super(dataType);
		Xml annotation = dataType.getAnnotation(Xml.class);
		this.xmlName = annotation == null || annotation.name().isEmpty()
				? dataType.getSimpleName()
				: annotation.name();
	}

	/**
	 * Decorates the child xml aspect members;
	 */
	@Override
	protected XmlAspectMember decorate(Field field) {
		return null;
	}

	private void decorateChildren() {
		Field[] fields = this.dataType.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Xml annotation = field.getAnnotation(Xml.class);
			if (annotation != null) {
				this.members.add(new XmlAspectMember(field, annotation));
			}
		}
	}

	/**
	 * Holds all static instances of the xml aspects;
	 */
	private static final HashMap<Class<?>, XmlAspect<?>> INSTANCES = new HashMap<Class<?>, XmlAspect<?>>();

	/**
	 * Gets a default static instance of an xml aspect. Will create one if none
	 * exists.
	 * 
	 * @param claz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized final <T> XmlAspect<T> getInstance(Class<T> claz) {
		XmlAspect<T> aspect = (XmlAspect<T>) INSTANCES.get(claz);
		if (aspect == null) {
			aspect = new XmlAspect<T>(claz);
			INSTANCES.put(claz, aspect);
			aspect.decorateChildren();
		}
		return aspect;
	}

	/**
	 * Gets the xml aspect of the first actual argument of a paramemeterized type;
	 * 
	 * @param parameterizedType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized final <T> XmlAspect<T> getInstance(Type parameterizedType) {
		ParameterizedType pt = (ParameterizedType) parameterizedType;
		Type[] ata = pt.getActualTypeArguments();
		Class<T> claz = (Class<T>) ata[0];
		return getInstance(claz);
	}

	/**
	 * Regardless of the xml name of this instance, will parse all child attributes
	 * from the given node;
	 */
	public void parse(Node node, T target) {
		for (XmlAspectMember member : this) {
			Object value = member.xmlAdapter.parse(node);
			member.setValue(target, value);
		}
	}

	/**
	 * Regardless of the xml name of this instance, will parse all child attributes
	 * from the given node;
	 */
	public T parse(Node node) {
		T item = newInstance();
		parse(node, item);
		return item;
	}

	/**
	 * Looks up a specific path of child nodes and parses the found node using this
	 * xml aspect.|
	 * 
	 * @param node
	 * @param path
	 * @return
	 */
	public T parse(Node node, String... path) {
		for (int i = 0; i < path.length; i++) {
			boolean found = false;
			for (Node child = node.getFirstChild(); child != null && !found; child = child.getNextSibling()) {
				String nodeName = child.getNodeName();
				if (path[i].equalsIgnoreCase(nodeName)) {
					node = child;
					found = true;
				}
			}
			if (!found)
				return null;
		}
		return parse(node);
	}

	/**
	 * Parses an xml document as an object of this instance;
	 * 
	 * @param document
	 * @return
	 */
	public T parse(Document document) {
		NodeList elementsByTagName = document.getElementsByTagName(this.xmlName);
		return elementsByTagName.getLength() > 0
				? parse(elementsByTagName.item(0))
				: null;
	}

	/**
	 * Adds all child members as properties to a specific parent xml node.
	 */
	public Node format(Object value, Node parent) {
		return format(this.xmlName, value, parent);
	}

	/**
	 * Adds all child members as properties to a specific parent xml node.
	 */
	public Node format(String xmlname, Object value, Node parent) {

		Document document = parent.getOwnerDocument();
		Element element = document.createElement(xmlname);
		parent.appendChild(element);

		for (XmlAspectMember member : this) {
			Object memberValue = member.getValue(value);
			member.xmlAdapter.format(memberValue, element);
		}
		return element;
	}

	/**
	 * Gets the data type of the xml adapter.
	 */
	public Class<T> getDataType() {
		return this.dataType;
	}

}
