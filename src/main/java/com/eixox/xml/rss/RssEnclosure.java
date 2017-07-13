package com.eixox.xml.rss;

import com.eixox.xml.Xml;
import com.eixox.xml.XmlType;

/**
 * <enclosure> is an optional sub-element of <item>.
 * 
 * It has three required attributes. url says where the enclosure is located,
 * length says how big it is in bytes, and type says what its type is, a
 * standard MIME type.
 * 
 * The url must be an http url.
 * 
 * <enclosure url="http://www.scripting.com/mp3s/weatherReportSuite.mp3" length=
 * "12216320" type="audio/mpeg" />
 * 
 * @author Rodrigo Portela
 *
 */
@Xml(name = "enclosure")
public class RssEnclosure {

	@Xml(type = XmlType.ATTRIBUTE)
	public String url;

	@Xml(type = XmlType.ATTRIBUTE)
	public long length;

	@Xml(type = XmlType.ATTRIBUTE)
	public String type;
}
