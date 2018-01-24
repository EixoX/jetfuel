package com.eixox.usecases;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LocalFile implements BlobSource {

	public final File file;

	public LocalFile(String fileName) {
		this.file = new File(fileName);
	}

	@Override
	public String getContentType() {
		return "application/octet-stream";
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(file);
	}

	@Override
	public long getSize() {
		return this.file.getTotalSpace();
	}

	@Override
	public String getName() {
		return this.file.getName();
	}

}
