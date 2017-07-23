package com.eixox.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.eixox.security.X509CertificateKeySelector;
import com.eixox.security.X509CertificateWithKey;

/**
 * An XML Signature handler that can sign with x509 certificates xml documents.
 * 
 * @author Rodrigo Portela
 *
 */

public class XmlSignatureHandler {

	private final DocumentBuilderFactory builderFactory;
	private final TransformerFactory transformerFactory;
	private final XMLSignatureFactory signatureFactory;
	private final DigestMethod digestMethod;
	private final List<Transform> transformList;
	private final CanonicalizationMethod canonicalizationMethod;
	private final SignatureMethod signatureMethod;
	private final KeyInfoFactory keyInfoFactory;
	public final X509CertificateWithKey certificateWithKey = new X509CertificateWithKey();
	public Document document;
	public String referenceUri = "";

	public XmlSignatureHandler() throws NoSuchAlgorithmException,
			InvalidAlgorithmParameterException {
		this.builderFactory = DocumentBuilderFactory.newInstance();
		this.builderFactory.setNamespaceAware(true);
		this.transformerFactory = TransformerFactory.newInstance();
		this.signatureFactory = XMLSignatureFactory.getInstance("DOM");
		this.digestMethod = signatureFactory.newDigestMethod(DigestMethod.SHA1, null);
		this.transformList = new ArrayList<Transform>(2);

		this.transformList.add(
				signatureFactory.newTransform(
						Transform.ENVELOPED,
						(TransformParameterSpec) null));

		this.transformList.add(
				signatureFactory.newTransform(
						"http://www.w3.org/TR/2001/REC-xml-c14n-20010315",
						(TransformParameterSpec) null));

		this.canonicalizationMethod = this.signatureFactory.newCanonicalizationMethod(
				CanonicalizationMethod.INCLUSIVE,
				(C14NMethodParameterSpec) null);

		this.signatureMethod = this.signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
		this.keyInfoFactory = this.signatureFactory.getKeyInfoFactory();

	}

	public synchronized void sign()
			throws MarshalException,
			XMLSignatureException,
			KeyException {

		if (this.document == null)
			throw new RuntimeException("Can't sign a NULL document");

		Reference reference = this.signatureFactory.newReference(
				referenceUri,
				this.digestMethod,
				this.transformList,
				null,
				null);

		SignedInfo signedInfo = this.signatureFactory.newSignedInfo(
				this.canonicalizationMethod,
				this.signatureMethod,
				Collections.singletonList(reference));

		// Create the KeyInfo containing the X509Data.
		X509Data xd = this.keyInfoFactory.newX509Data(
				Collections.singletonList(this.certificateWithKey.certificate));

		KeyInfo keyInfo = this.keyInfoFactory.newKeyInfo(Collections.singletonList(xd));

		XMLSignature signature = this.signatureFactory.newXMLSignature(
				signedInfo,
				keyInfo);

		DOMSignContext signingContext = new DOMSignContext(
				this.certificateWithKey.privateKey,
				document.getDocumentElement());

		signature.sign(signingContext);
	}

	public synchronized void write(OutputStream os)
			throws TransformerException {
		Transformer trans = this.transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(os);
		trans.transform(domSource, streamResult);
	}

	public synchronized boolean validate()
			throws MarshalException,
			XMLSignatureException {

		// Find Signature element.
		NodeList list = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
		if (list.getLength() == 0) {
			throw new RuntimeException("Cannot find Signature element");
		}

		// Create a DOMValidateContext and specify a KeySelector
		// and document context.
		DOMValidateContext validateContext = new DOMValidateContext(new X509CertificateKeySelector(), list.item(0));

		// Unmarshal the XMLSignature.
		XMLSignature signature = this.signatureFactory.unmarshalXMLSignature(validateContext);

		// Validate the XMLSignature.
		if (signature.validate(validateContext)) {
			return true;
		} else {
			Iterator<?> i = signature.getSignedInfo().getReferences().iterator();
			for (int j = 0; i.hasNext(); j++) {
				System.out.print("ref[" + j + "] -> ");
				Reference ref = (Reference) i.next();
				System.out.print(ref.getURI());
				System.out.print(", ");
				System.out.print(ref.getDigestMethod().toString());
				System.out.print(", ");
				System.out.print(ref.getId());
				boolean refValid = ref.validate(validateContext);
				System.out.print(", validity status: " + refValid + "\r\n");
			}
			return false;
		}
	}

	public void loadDocument(InputStream is)
			throws SAXException,
			IOException,
			ParserConfigurationException {
		this.document = this.builderFactory.newDocumentBuilder().parse(is);
	}

	public void loadDocument(String uri)
			throws SAXException,
			IOException,
			ParserConfigurationException {
		this.document = this.builderFactory.newDocumentBuilder().parse(uri);
	}

	public void loadDocument(File file)
			throws SAXException,
			IOException,
			ParserConfigurationException {
		this.document = this.builderFactory.newDocumentBuilder().parse(file);
	}
}
