package com.eixox.security;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.PasswordProtection;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.eixox.JetfuelException;

public class X509CertificateWithKey {

	public PrivateKey privateKey;
	public X509Certificate certificate;

	public void loadPfx(String fileName, String password)
			throws IOException,
			NoSuchAlgorithmException,
			CertificateException,
			KeyStoreException,
			UnrecoverableEntryException {
		FileInputStream fis = new FileInputStream(fileName);
		try {
			loadPfx(fis, password);
		} finally {
			fis.close();
		}
	}

	public void loadPfx(byte[] bytes, String password)
			throws NoSuchAlgorithmException,
			CertificateException,
			KeyStoreException,
			UnrecoverableEntryException,
			IOException {
		loadPfx(new ByteArrayInputStream(bytes), password);
	}

	public void loadPfx(InputStream is, String password)
			throws NoSuchAlgorithmException,
			CertificateException,
			IOException,
			KeyStoreException,
			UnrecoverableEntryException {

		char[] pwd = password.toCharArray();
		KeyStore keyStore = KeyStore.getInstance("pkcs12");
		keyStore.load(is, pwd);
		PasswordProtection passwordProtection = new KeyStore.PasswordProtection(pwd);

		for (Enumeration<String> aliases = keyStore.aliases(); aliases.hasMoreElements();) {
			String alias = aliases.nextElement();
			KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, passwordProtection);
			Certificate cert = entry.getCertificate();
			if (cert.getType().equals("X.509")) {
				this.certificate = (X509Certificate) cert;
				this.privateKey = entry.getPrivateKey();
				return;
			}
		}
		throw new JetfuelException("Certificate of type X.509 was not found.");

	}

	public final SSLSocketFactory createSocketFactoryForBlindlyTrustedServer()
			throws NoSuchAlgorithmException,
			KeyManagementException {
		SSLContext sc = SSLContext.getInstance("TLSv1.2");
		sc.init(
				new KeyManager[] {
						new DullX509KeyManager(certificate, privateKey)
				},
				new TrustManager[] {
						new DullX509TrustManager()
				},
				new SecureRandom());
		return sc.getSocketFactory();
	}

}
