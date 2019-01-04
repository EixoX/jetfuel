package com.eixox.xml.rss;

import java.util.Date;
import java.util.List;

import com.eixox.adapters.UseAdapter;
import com.eixox.xml.Xml;

/**
 * A channel may contain any number of <item>s. An item may represent a "story"
 * -- much like a story in a newspaper or magazine; if so its description is a
 * synopsis of the story, and the link points to the full story. An item may
 * also be complete in itself, if so, the description contains the text
 * (entity-encoded HTML is allowed; see examples), and the link and title may be
 * omitted. All elements of an item are optional, however at least one of title
 * or description must be present.
 * 
 * @author Rodrigo Portela
 *
 */
@Xml(
		name = "item")
public class RssItem {

	/**
	 * The title of the item. Ex: Venice Film Festival Tries to Quit Sinking
	 */
	@Xml
	public String title;

	/**
	 * The URL of the item. Ex: http://nytimes.com/2004/12/07FEST.html
	 */
	@Xml
	public String link;

	/**
	 * The item synopsis. Ex: Some of the most heated chatter at the Venice Film
	 * Festival this week was about the way that the arrival of the stars at the
	 * Palazzo del Cinema was being staged.
	 */
	@Xml
	public String description;

	/**
	 * Email address of the author of the item. Ex: oprah\@oxygen.net
	 */
	@Xml
	public String author;

	/**
	 * Includes the item in one or more categories.
	 */
	@Xml(
			name = "category")
	public List<String> categories;

	/**
	 * URL of a page for comments relating to the item. Ex:
	 * http://www.myblog.org/cgi-local/mt/mt-comments.cgi?entry_id=290
	 */
	@Xml
	public String comments;

	/**
	 * String that uniquely identifies the item. Ex:
	 * http://inessential.com/2002/09/01.php#a2
	 */
	@Xml
	public String guid;

	/**
	 * Indicates when the item was published. Ex: Sun, 19 May 2002 15:21:36 GMT
	 */
	@Xml
	@UseAdapter(
			format = "EEE, dd MMM yyyy HH:mm:ss Z",
			value = com.eixox.adapters.DateAdapter.class)
	public Date pubDate;

	/**
	 * The RSS channel that the item came from.
	 */
	@Xml
	public String source;

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
	 */
	@Xml
	public RssEnclosure enclosure;
}
