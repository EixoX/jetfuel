package com.eixox.security;

import java.security.Key;
import java.security.cert.X509Certificate;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;

public class X509CertificateKeySelector extends KeySelector {

	@Override
	public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method, XMLCryptoContext context)
			throws KeySelectorException {

		for (XMLStructure o : keyInfo.getContent()) {
			if (o instanceof X509Data) {
				for (Object o2 : ((X509Data) o).getContent()) {
					if (o2 instanceof X509Certificate) {
						return new KeySelectorResult() {
							private final X509Certificate cert = (X509Certificate) o2;

							public Key getKey() {
								return cert.getPublicKey();
							}
						};
					}
				}
			}
		}

		return null;
	}

}
