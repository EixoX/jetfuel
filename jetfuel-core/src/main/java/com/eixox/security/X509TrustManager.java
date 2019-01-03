package com.eixox.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class X509TrustManager implements javax.net.ssl.X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// do nothing

	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// do nothing

	}

	public X509Certificate[] getAcceptedIssuers() {
		// do nothing
		return new X509Certificate[0];
	}

}
