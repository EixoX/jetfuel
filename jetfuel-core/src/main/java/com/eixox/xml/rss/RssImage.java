package com.eixox.xml.rss;

import com.eixox.xml.Xml;

/**
 * <image> is an optional sub-element of <channel>, which contains three
 * required and three optional sub-elements.
 * 
 * @author Rodrigo Portela
 *
 */
@Xml(name = "image")
public class RssImage {

	/**
	 * The URL of a GIF, JPEG or PNG image that represents the channel.
	 */
	@Xml
	public String url;

	/**
	 * Describes the image, it's used in the ALT attribute of the HTML <img> tag
	 * when the channel is rendered in HTML.
	 */
	@Xml
	public String title;

	/**
	 * The URL of the site, when the channel is rendered, the image is a link to
	 * the site. (Note, in practice the image <title> and <link> should have the
	 * same value as the channel's <title> and <link>.
	 */
	@Xml
	public String link;

	/**
	 * Optional elements include <width> and <height>, numbers, indicating the
	 * width and height of the image in pixels. <description> contains text that
	 * is included in the TITLE attribute of the link formed around the image in
	 * the HTML rendering.
	 * 
	 * Maximum value for width is 144, default value is 88.
	 * 
	 */
	@Xml
	public int width;

	/**
	 * Optional elements include <width> and <height>, numbers, indicating the
	 * width and height of the image in pixels. <description> contains text that
	 * is included in the TITLE attribute of the link formed around the image in
	 * the HTML rendering.
	 * 
	 * Maximum value for height is 400, default value is 31.
	 * 
	 */
	@Xml
	public int height;
}
