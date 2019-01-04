package com.eixox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A generic helpful web client that can connect and maintain cookies to
 * navigate;
 * 
 * @author Rodrigo Portela
 *
 */
public class HttpClient {

	private static final String CONTENT_TYPE_HEADER = "Content-Type";

	private final LinkedHashMap<String, String> form = new LinkedHashMap<>();
	private final LinkedHashMap<String, String> cookies = new LinkedHashMap<>();
	private final LinkedHashMap<String, String> requestHeaders = new LinkedHashMap<>();
	private final LinkedHashMap<String, String> responseHeaders = new LinkedHashMap<>();

	private Date startTime;
	private Date endTime;
	private URL url;
	private String method = "GET";
	private byte[] requestBody;
	private int responseCode;
	private String responseMessage;
	private byte[] responseBytes;
	private String responseText;

	public HttpClient() {
		this.requestHeaders.put("User-Agent", "Port's java http client 2.0");
		this.requestHeaders.put("Pragma", "no-cache");
		this.requestHeaders.put("Cache-Control", "no-cache");
		this.requestHeaders.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
	}

	public final HttpClient setUrl(String address) {
		try {
			this.url = new URL(address);
			return this;
		} catch (MalformedURLException e) {
			throw new JetfuelException(e);
		}
	}

	public final URL getUrl() {
		return this.url;
	}

	public final HttpClient setMethod(String method) {
		this.method = method;
		this.requestHeaders.remove(CONTENT_TYPE_HEADER);
		return this;
	}

	public final HttpClient setMethod(String method, String contentType) {
		this.method = method;
		this.requestHeaders.put(CONTENT_TYPE_HEADER, contentType);
		return this;
	}

	public final HttpClient prepareFormPost() {
		this.requestBody = this.getFormUrlEncoded().getBytes();
		this.method = "POST";
		this.requestHeaders.put(CONTENT_TYPE_HEADER, "application/x-www-form-urlencoded");
		return this;
	}

	public final String getMethod() {
		return this.method;
	}

	public final HttpClient clearForm() {
		this.form.clear();
		return this;
	}

	public final HttpClient setFormField(String name, String value) {
		this.form.put(name, value);
		return this;
	}

	public final String getFormField(String name) {
		return this.form.get(name);
	}

	public final Map<String, String> getForm() {
		return this.form;
	}

	public final Map<String, String> getCookies() {
		return this.cookies;
	}

	public final HttpClient setCookie(String name, String value) {
		this.cookies.put(name, value);
		return this;
	}

	public final String getCookie(String name) {
		return this.cookies.get(name);
	}

	public final String getFormUrlEncoded(String encoding) {
		try {
			StringBuilder builder = new StringBuilder();
			String key;
			String value;
			for (Entry<String, String> e : form.entrySet()) {
				key = e.getKey();
				value = e.getValue();
				if (builder.length() > 0)
					builder.append('&');
				builder.append(URLEncoder.encode(key, encoding));
				builder.append('=');
				builder.append(value == null || value.isEmpty()
						? ""
						: URLEncoder.encode(value, encoding));
			}
			return builder.toString();
		} catch (Exception e) {
			throw new JetfuelException(e);
		}
	}

	public final String getFormUrlEncoded() {
		return this.getFormUrlEncoded("utf-8");
	}

	public final HttpClient setRequestBody(String contentType, byte[] data) {
		this.requestHeaders.put(CONTENT_TYPE_HEADER, contentType);
		this.requestBody = data;
		return this;
	}

	public HttpClient connect() throws IOException {
		this.startTime = new Date();
		this.endTime = null;
		HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
		connection.setRequestMethod(this.method);
		connection.setInstanceFollowRedirects(true);
		connection.setUseCaches(false);

		for (Entry<String, String> entry : this.requestHeaders.entrySet())
			connection.setRequestProperty(entry.getKey(), entry.getValue());

		if (!cookies.isEmpty()) {
			final ArrayList<String> cks = new ArrayList<>(cookies.size());
			for (Entry<String, String> entry : cookies.entrySet()) {
				cks.add(entry.getKey() + "=" + entry.getValue());
			}
			connection.setRequestProperty("Cookie", cks.size() > 1
					? String.join(";", cks)
					: cks.get(0));
		}

		if (this.requestBody != null && this.requestBody.length > 0) {
			connection.setRequestProperty("Content-Length", Integer.toString(requestBody.length));
			connection.setDoOutput(true);
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(requestBody);
			outputStream.flush();
			outputStream.close();
		}
		this.responseText = null;
		this.responseCode = connection.getResponseCode();
		this.responseMessage = connection.getResponseMessage();
		this.responseHeaders.clear();
		for (Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
			String key = entry.getKey();
			List<String> val = entry.getValue();

			this.responseHeaders.put(key, val == null || val.isEmpty()
					? null
					: String.join(";", val));

			if ("Set-Cookie".equalsIgnoreCase(key)) {
				for (String cookie : val) {
					int idx = cookie.indexOf(';');
					cookie = cookie.substring(0, idx);
					idx = cookie.indexOf('=');
					this.cookies.put(cookie.substring(0, idx), cookie.substring(idx + 1));
				}
			}

		}

		try (InputStream inputStream = connection.getInputStream()) {
			byte[] buffer = new byte[4096];
			ByteArrayOutputStream bos = new ByteArrayOutputStream(buffer.length);
			for (int i = inputStream.read(buffer); i >= 0; i = inputStream.read(buffer)) {
				bos.write(buffer, 0, i);
			}
			this.responseBytes = bos.toByteArray();
			bos.close();
		}

		this.endTime = new Date();
		return this;
	}

	public final int getResponseCode() {
		return this.responseCode;
	}

	public final String getResponseMessage() {
		return this.responseMessage;
	}

	public final byte[] getResponseBytes() {
		return this.responseBytes;
	}

	public final String getResponseText(String charset) throws UnsupportedEncodingException {
		return new String(this.responseBytes, charset);
	}

	public final String getResponseText() {
		if (this.responseText == null) {
			this.responseText = new String(this.responseBytes);
		}
		return this.responseText;
	}

	public final HttpClient parseHtmlInputTags() {
		String html = this.getResponseText();
		int ipos = html.indexOf("<input ");
		while (ipos > 0) {
			int epos = html.indexOf('>', ipos + 1);
			if (epos > ipos) {
				String inputHtml = html.substring(ipos, epos);
				String name = getHtmlAttribute("name", inputHtml);
				String value = getHtmlAttribute("value", inputHtml);
				if (name != null) {
					this.form.put(name, value);
				}
			} else {
				epos = ipos;
			}
			ipos = html.indexOf("<input ", epos + 1);
		}
		return this;
	}

	public final Date getStartTime() {
		return this.startTime;
	}

	public final Date getEndTime() {
		return this.endTime;
	}

	private String getHtmlAttribute(String attName, String htmlText) {
		int apos;
		int eqpos;
		int fpos;

		apos = htmlText.indexOf(attName);
		if (apos < 0)
			return null;
		eqpos = htmlText.indexOf('=', apos + 1);
		if (eqpos < 0)
			return null;

		char stringWrapper = htmlText.charAt(eqpos + 1);
		switch (stringWrapper) {
		case '\'':
			fpos = htmlText.indexOf('\'', eqpos + 2);
			return (fpos > eqpos)
					? htmlText.substring(eqpos + 2, fpos)
					: null;
		case '"':
			fpos = htmlText.indexOf('"', eqpos + 2);
			return (fpos > eqpos)
					? htmlText.substring(eqpos + 2, fpos)
					: null;
		default:
			fpos = htmlText.indexOf(' ', eqpos + 2);
			return (fpos > eqpos)
					? htmlText.substring(eqpos + 2, fpos)
					: null;
		}
	}

}
