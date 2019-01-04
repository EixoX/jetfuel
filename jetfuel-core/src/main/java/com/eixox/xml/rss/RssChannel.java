package com.eixox.xml.rss;

import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.eixox.JetfuelException;
import com.eixox.adapters.UseAdapter;
import com.eixox.restrictions.Required;
import com.eixox.xml.Xml;
import com.eixox.xml.XmlAspect;

/**
 * First we document the required and optional sub-elements of <channel>;
 * 
 * @author Rodrigo Portela
 *
 */
@Xml(
		name = "channel")
public class RssChannel {

	/**
	 * Taken from top level RSS element;
	 */
	public String version;

	/**
	 * The name of the channel. It's how people refer to your service. If you have
	 * an HTML website that contains the same information as your RSS file, the
	 * title of your channel should be the same as the title of your website.
	 */
	@Xml
	@Required
	public String title;

	/**
	 * The URL to the HTML website corresponding to the channel.
	 */
	@Xml
	@Required
	public String link;

	/**
	 * Phrase or sentence describing the channel.
	 */
	@Xml
	@Required
	public String description;

	/**
	 * The language the channel is written in. This allows aggregators to group all
	 * Italian language sites, for example, on a single page. A list of allowable
	 * values for this element, as provided by Netscape. You may also use values
	 * defined by the W3C.
	 */
	@Xml
	public String language;

	/**
	 * Copyright notice for content in the channel.
	 */
	@Xml
	public String copyright;

	/**
	 * Email address for person responsible for editorial content.
	 */
	@Xml
	public String managingEditor;

	/**
	 * Email address for person responsible for technical issues relating to
	 * channel.
	 */
	@Xml
	public String webMaster;

	/**
	 * The publication date for the content in the channel. For example, the New
	 * York Times publishes on a daily basis, the publication date flips once every
	 * 24 hours. That's when the pubDate of the channel changes. All date-times in
	 * RSS conform to the Date and Time Specification of RFC 822, with the exception
	 * that the year may be expressed with two characters or four characters (four
	 * preferred).
	 */
	@Xml
	@UseAdapter(
			format = "EEE, dd MMM yyyy HH:mm:ss Z",
			value = com.eixox.adapters.DateAdapter.class)
	public Date pubDate;

	/**
	 * The last time the content of the channel changed.
	 */
	@Xml
	@UseAdapter(
			format = "EEE, dd MMM yyyy HH:mm:ss Z",
			value = com.eixox.adapters.DateAdapter.class)
	public Date lastBuildDate;

	/**
	 * Specify one or more categories that the channel belongs to. Follows the same
	 * rules as the <item>-level category element.
	 */
	@Xml(
			name = "category")
	public List<String> categories;

	/**
	 * A string indicating the program used to generate the channel.
	 */
	@Xml
	public String generator;

	/**
	 * A URL that points to the documentation for the format used in the RSS file.
	 * It's probably a pointer to this page. It's for people who might stumble
	 * across an RSS file on a Web server 25 years from now and wonder what it is.
	 */
	@Xml
	public String docs;

	/**
	 * Allows processes to register with a cloud to be notified of updates to the
	 * channel, implementing a lightweight publish-subscribe protocol for RSS feeds.
	 */
	@Xml
	public RssCloud cloud;

	/**
	 * ttl stands for time to live. It's a number of minutes that indicates how long
	 * a channel can be cached before refreshing from the source.
	 */
	@Xml
	public int ttl;

	/**
	 * Specifies a GIF, JPEG or PNG image that can be displayed with the channel.
	 */
	@Xml
	public RssImage image;

	/**
	 * The PICS rating for the channel.
	 */
	@Xml
	public String rating;

	/**
	 * The items published on the RSS channel.
	 */
	@Xml(
			name = "item")
	public List<RssItem> items;

	/**
	 * Parses an XML document to find the channel element and it's children;
	 * 
	 * @param document
	 */
	public boolean parse(Document document) {
		NodeList rssNodes = document.getElementsByTagName("rss");
		if (rssNodes.getLength() < 1)
			return false;

		Element rssElement = (Element) rssNodes.item(0);
		this.version = rssElement.getAttribute("version");

		NodeList rssChannels = rssElement.getElementsByTagName("channel");
		if (rssChannels.getLength() < 1)
			return false;

		XML_ASPECT.parse(rssChannels.item(0), this);
		return true;

	}

	/**
	 * Downloads and parses the xml document on the uri.
	 * 
	 * @param uri
	 * @return
	 */
	public boolean parse(String uri) {
		try {
			DocumentBuilder builder = FACTORY.newDocumentBuilder();
			Document document = builder.parse(uri);
			return parse(document);
		} catch (Exception e) {
			throw new JetfuelException(e);
		}
	}

	/**
	 * Holds a pointer to an instance of the XML aspect.
	 */
	public static final XmlAspect<RssChannel> XML_ASPECT = XmlAspect.getInstance(RssChannel.class);

	private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();
}
