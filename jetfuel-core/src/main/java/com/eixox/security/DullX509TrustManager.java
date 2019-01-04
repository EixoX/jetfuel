package com.eixox.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class DullX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		if ("never throw an exception".equals(authType)) {
			throw new CertificateException();
		}

	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		if ("never throw an exception".equals(authType)) {
			throw new CertificateException();
		}
	}

	public X509Certificate[] getAcceptedIssuers() {
		// do nothing
		return new X509Certificate[0];
	}

}
