package com.eixox;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * A generic helpful web client.
 * 
 * @author Rodrigo Portela
 *
 */
public class HttpClient implements Closeable, AutoCloseable {

	public final LinkedHashMap<String, String> headers = new LinkedHashMap<String, String>();
	public URL url;
	public HttpURLConnection connection;
	public InputStream responseStream;
	public String method = "GET";
	public byte[] requestBody;

	public final HttpClient setUrl(String address) {
		try {
			this.url = new URL(address);
			return this;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public final HttpClient setMethod(String method) {
		this.method = method;
		return this;
	}

	public final HttpClient setBody(Map<String, ?> data) {
		try {
			StringBuilder builder = new StringBuilder();
			for (Entry<String, ?> e : data.entrySet()) {
				if (builder.length() > 0)
					builder.append('&');
				builder.append(URLEncoder.encode(e.getKey(), "utf-8"));
				builder.append('=');
				builder.append(URLEncoder.encode(String.valueOf(e.getValue()), "utf-8"));
			}
			this.requestBody = builder.toString().getBytes("utf-8");
			this.headers.put("Content-Type", "application/x-www-form-urlencoded");
			return this;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public final HttpClient setBody(byte[] data) {
		this.requestBody = data;
		return this;
	}

	public final HttpClient setBody(String body) {
		try {
			this.requestBody = body == null ? null : body.getBytes("utf-8");
			return this;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public HttpClient connect() throws IOException {
		this.close();
		this.connection = (HttpURLConnection) this.url.openConnection();
		this.connection.setRequestMethod(this.method);
		this.connection.setInstanceFollowRedirects(true);
		this.connection.setUseCaches(false);

		for (Entry<String, String> entry : headers.entrySet())
			this.connection.setRequestProperty(entry.getKey(), entry.getValue());

		if (this.requestBody != null && this.requestBody.length > 0) {
			this.connection.setRequestProperty("Content-Length", Integer.toString(requestBody.length));

			this.connection.setDoOutput(true);
			OutputStream outputStream = this.connection.getOutputStream();
			outputStream.write(requestBody);
			outputStream.flush();
			outputStream.close();
		}
		this.responseStream = connection.getInputStream();
		return this;
	}

	public String downloadString() {
		try {
			StringWriter writer = new StringWriter();
			for (int i = this.responseStream.read(); i >= 0; i = this.responseStream.read())
				writer.write(i);

			String ret = writer.toString();

			writer.close();
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] downloadBytes() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			for (int i = this.responseStream.read(); i >= 0; i = this.responseStream.read())
				bos.write(i);
			byte[] bytes = bos.toByteArray();
			bos.close();
			return bytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Document downloadXml() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(this.responseStream);
	}

	public void close() throws IOException {
		if (this.responseStream != null) {
			this.responseStream.close();
			this.responseStream = null;
		}
	}

}
