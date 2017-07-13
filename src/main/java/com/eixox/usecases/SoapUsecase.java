package com.eixox.usecases;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.eixox.adapters.StringAdapter;
import com.eixox.xml.XmlAspect;

/**
 * Performs a webservice call;
 * 
 * @author Rodrigo Portela
 *
 * @param <TParams>
 * @param <TResult>
 */
public abstract class SoapUsecase<TParams, TResult> extends UsecaseImplementation<TParams, TResult> {

	public static final DocumentBuilderFactory BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
	public static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();

	/**
	 * Sets debug mode on to print shit on the output;
	 */
	public boolean is_debugging;

	/**
	 * Gets the url of the webservice call;
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	protected abstract URL getUrl() throws MalformedURLException;

	/**
	 * Creates the soap element wrapper and appends it to the document; Override
	 * this method to implement specific soap versions; Defaults to
	 * soapenv:Envelop;
	 * 
	 * @param document
	 * @return
	 */
	protected Element createEnvelopeElement(Document document) {
		final Element envelope = document.createElement("soap:Envelope");
		envelope.setAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		document.appendChild(envelope);
		return envelope;
	}

	/**
	 * Appends the header element of the soap webservice call;
	 * 
	 * @param document
	 * @param header
	 * @return
	 */
	protected Element createHeaderElement(Element envelope) {
		final Element header = envelope.getOwnerDocument().createElement("soap:Header");
		envelope.appendChild(header);
		return header;
	}

	/**
	 * Appends the body element of the soap webservice call;
	 * 
	 * @param envelope
	 * @return
	 */
	protected Element createBodyElement(Element envelope) {
		final Element header = envelope.getOwnerDocument().createElement("soap:Body");
		envelope.appendChild(header);
		return header;
	}

	/**
	 * Override this method to perform custom processing of the response soap
	 * header;
	 * 
	 * @param soapHeader
	 * @param execution
	 */
	protected void processResponseHeader(Element soapHeader, UsecaseExecution<TParams, TResult> execution) throws Exception {

	}

	/**
	 * Override this method to perform custom parsing of the response body;
	 * 
	 * @param soapBody
	 * @param execution
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	protected void processResponseBody(Element soapBody, UsecaseExecution<TParams, TResult> execution) throws Exception {
		XmlAspect<TResult> xmlAspect = XmlAspect.getInstance(getResultClass());

		NodeList resultList = soapBody.getElementsByTagName(xmlAspect.xmlName);
		if (resultList != null && resultList.getLength() > 0) {
			execution.result = xmlAspect.parse(resultList.item(0));
			execution.result_type = UsecaseResultType.SUCCESS;
		}
	}

	/**
	 * Executes the main flow of the soap usecase;
	 */
	@Override
	protected void mainFlow(UsecaseExecution<TParams, TResult> execution) throws Exception {

		URL url = getUrl();

		DocumentBuilder documentBuilder = BUILDER_FACTORY.newDocumentBuilder();

		// Builds the entire request xml;
		Document xmlrequest = documentBuilder.newDocument();
		Element xmlenvelope = createEnvelopeElement(xmlrequest);
		Element xmlheader = createHeaderElement(xmlenvelope);

		Object soapHeader = execution.headers == null
				? null
				: execution.headers.get("soap");
		if (soapHeader != null)
			XmlAspect.getInstance(soapHeader.getClass()).format(soapHeader, xmlheader);

		Element xmlbody = createBodyElement(xmlenvelope);
		XmlAspect.getInstance(getParamsClass()).format(execution.params, xmlbody);

		// debugging
		if (is_debugging)
			System.out.println(new StringAdapter().convertXml(xmlrequest));

		// Sends the document to output;
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/xml");

		TRANSFORMER_FACTORY.newTransformer().transform(
				new DOMSource(xmlrequest),
				new StreamResult(connection.getOutputStream()));

		try {

			String contentType = connection.getContentType();

			String charSet = (contentType != null && contentType.contains("charset="))
					? contentType.substring(contentType.indexOf('=') + 1)
					: "UTF-8";

			InputStream inputStream = connection.getInputStream();
			InputStreamReader isReader = new InputStreamReader(inputStream, charSet);

			// Reads the response xml
			Document xmlresponse = documentBuilder.parse(new InputSource(isReader));

			// Debugging
			if (is_debugging)
				System.out.println(new StringAdapter().convertXml(xmlresponse));

			isReader.close();
			inputStream.close();
			connection.disconnect();

			NodeList headerList = xmlresponse.getElementsByTagName("soap:Header");
			if (headerList != null && headerList.getLength() == 1)
				processResponseHeader((Element) headerList.item(0), execution);

			NodeList bodyList = xmlresponse.getElementsByTagName("soap:Body");
			if (bodyList == null || bodyList.getLength() != 1) {
				execution.result_type = UsecaseResultType.EXECUTION_FAILED;
				execution.exception = new Exception("SOAP Body not found on response");
				return;
			}

			// Processes the response xml;
			processResponseBody((Element) bodyList.item(0), execution);

		} catch (IOException ioe) {
			System.out.println(new StringAdapter().convertStream(connection.getErrorStream()));
			execution.exception = ioe;
			execution.result_type = UsecaseResultType.EXECUTION_FAILED;
		}
	}

}
