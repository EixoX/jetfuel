package com.eixox.adapters;

import java.nio.charset.StandardCharsets;

/**
 * A byte array Base64 adapter;
 * 
 * @author Rodrigo Portela
 *
 */
public class Base64Adapter extends ByteArrayAdapter {

	/**
	 * 3-byte to 4-byte conversion
	 */
	private void convert3to4byte(byte[] source, byte[] dest, byte[] data) {
		for (int sidx = 0, didx = 0; sidx < source.length; sidx += 3, didx += 4) {
			dest[didx] = (byte) ((data[sidx] >>> 2) & 077);
			dest[didx + 1] = (byte) ((data[sidx + 1] >>> 4) & 017 |
					(data[sidx] << 4) & 077);
			dest[didx + 2] = (byte) ((data[sidx + 2] >>> 6) & 003 |
					(data[sidx + 1] << 2) & 077);
			dest[didx + 3] = (byte) (data[sidx + 2] & 077);
		}
	}

	/**
	 * 3-byte to 4-byte conversion
	 */
	private void convert4to3byte(byte[] dest, byte[] data) {
		// 4-byte to 3-byte conversion
		int sidx;
		int didx;
		for (sidx = 0, didx = 0; didx < dest.length - 2; sidx += 4, didx += 3) {
			dest[didx] = (byte) (((data[sidx] << 2) & 255) |
					((data[sidx + 1] >>> 4) & 3));
			dest[didx + 1] = (byte) (((data[sidx + 1] << 4) & 255) |
					((data[sidx + 2] >>> 2) & 017));
			dest[didx + 2] = (byte) (((data[sidx + 2] << 6) & 255) |
					(data[sidx + 3] & 077));
		}
		if (didx < dest.length) {
			dest[didx] = (byte) (((data[sidx] << 2) & 255) |
					((data[sidx + 1] >>> 4) & 3));
		}
		if (++didx < dest.length) {
			dest[didx] = (byte) (((data[sidx + 1] << 4) & 255) |
					((data[sidx + 2] >>> 2) & 017));
		}
	}

	/**
	 * 0-63 to ASCII printable conversion
	 */
	private void convertAsciiPrintable(byte[] dest) {
		for (int idx = 0; idx < dest.length; idx++) {
			if (dest[idx] < 26)
				dest[idx] = (byte) (dest[idx] + 'A');
			else if (dest[idx] < 52)
				dest[idx] = (byte) (dest[idx] + 'a' - 26);
			else if (dest[idx] < 62)
				dest[idx] = (byte) (dest[idx] + '0' - 52);
			else if (dest[idx] < 63)
				dest[idx] = (byte) '+';
			else
				dest[idx] = (byte) '/';
		}
	}

	private void convertFromAscii(byte[] data) {

		// ASCII printable to 0-63 conversion
		for (int idx = 0; idx < data.length; idx++) {
			if (data[idx] == '=')
				data[idx] = 0;
			else if (data[idx] == '/')
				data[idx] = 63;
			else if (data[idx] == '+')
				data[idx] = 62;
			else if (data[idx] >= '0' && data[idx] <= '9')
				data[idx] = (byte) (data[idx] - ('0' - 52));
			else if (data[idx] >= 'a' && data[idx] <= 'z')
				data[idx] = (byte) (data[idx] - ('a' - 26));
			else if (data[idx] >= 'A' && data[idx] <= 'Z')
				data[idx] = (byte) (data[idx] - 'A');
		}
	}

	/**
	 * Formats the input byte array into a base64 string.
	 */
	@Override
	public String format(byte[] source) {
		if (source == null || source.length == 0)
			return null;
		else {
			byte[] data = new byte[source.length + 2];

			System.arraycopy(source, 0, data, 0, source.length);
			byte[] dest = new byte[(data.length / 3) * 4];

			convert3to4byte(source, dest, data);
			convertAsciiPrintable(dest);
			for (int idx = dest.length - 1; idx > (source.length * 4) / 3; idx--) {
				dest[idx] = (byte) '=';
			}
			return new String(dest);
		}
	}

	/**
	 * Parses the input base64 string into a byte array.
	 */
	@Override
	public byte[] parse(String source) {
		if (source == null || source.isEmpty()) {
			return new byte[0];
		} else {
			byte[] data = source.getBytes(StandardCharsets.US_ASCII);
			int tail = data.length;
			while (data[tail - 1] == '=')
				tail--;
			byte[] dest = new byte[tail - data.length / 4];
			convertFromAscii(data);
			convert4to3byte(dest, data);

			return dest;
		}
	}

}
