package com.eixox.xml.rss;

import com.eixox.xml.Xml;
import com.eixox.xml.XmlType;

/**
 * It specifies a web service that supports the rssCloud interface which can be
 * implemented in HTTP-POST, XML-RPC or SOAP 1.1.
 * 
 * Its purpose is to allow processes to register with a cloud to be notified of
 * updates to the channel, implementing a lightweight publish-subscribe protocol
 * for RSS feeds.
 * 
 * More: http://cyber.harvard.edu/rss/soapMeetsRss.html#rsscloudInterface
 * 
 * @author Rodrigo Portela
 *
 */
@Xml(name = "cloud")
public class RssCloud {

	@Xml(type = XmlType.ATTRIBUTE)
	public String domain;

	@Xml(type = XmlType.ATTRIBUTE)
	public int port;

	@Xml(type = XmlType.ATTRIBUTE)
	public String path;

	@Xml(type = XmlType.ATTRIBUTE)
	public String registerProcedure;

	@Xml(type = XmlType.ATTRIBUTE)
	public String protocol;
}
