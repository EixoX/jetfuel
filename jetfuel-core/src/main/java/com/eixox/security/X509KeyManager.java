package com.eixox.security;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class X509KeyManager implements javax.net.ssl.X509KeyManager {

	private X509Certificate certificate;
	private PrivateKey privateKey;

	public X509KeyManager(X509Certificate certificate, PrivateKey privateKey) {
		this.certificate = certificate;
		this.privateKey = privateKey;
	}

	public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
		return certificate.getIssuerDN().getName();
	}

	public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
		return null;
	}

	public X509Certificate[] getCertificateChain(String arg0) {
		return new X509Certificate[] { certificate };
	}

	public String[] getClientAliases(String arg0, Principal[] arg1) {
		return new String[] { certificate.getIssuerDN().getName() };
	}

	public PrivateKey getPrivateKey(String arg0) {
		return privateKey;
	}

	public String[] getServerAliases(String arg0, Principal[] arg1) {
		return null;
	}

}
