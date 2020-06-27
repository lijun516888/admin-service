package com.knowledge.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MobileNoMask {

	public static boolean isEncode(String no) {
		return no.matches(".*[a-zA-z].*");
	}

	/**
	 * 对电话号码进行加密
	 * 17006691639 -> 170BPA61639
	 * 170BPA61639 -> 170BPA61639
	 * null -> ""
	 */
    public static String encode(String no) {
		if (StringUtils.isBlank(no)) {
			return "";
		}
		no = StringUtils.trim(no);
		if (isEncode(no)){
			return no;
		}
		return encode(no, true);
	}

	/**
	 * 对电话号码进行解密
	 * 170BPA61639 -> 17006691639
	 * 17006691639 -> 17006691639
	 * null -> ""
	 */
	public static String decode(String no) {
		if (StringUtils.isBlank(no)) {
			return "";
		}
		if (isEncode(no)) {
			return encode(no, false);
		}
    	return no;
	}

	private final static String key = "832_587jgcismbidDdpcBlwRitclyinQstvycqOVuSatrseQCRIevjwzfMHDNjyqjndYFvgzrqmP026%-4:554";
	private final static char[] ALPHABET = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz".toCharArray();
	private final static int[] INDEX = new int[128];
	static {
		for(int i = 0;i < INDEX.length; i++) {
			INDEX[i] = -1;
		}
		for(int i = 0;i < ALPHABET.length; i++) {
			INDEX[ALPHABET[i]] = i;
			if(ALPHABET[i] == '0') {
				INDEX['O'] = INDEX['o'] = 0;
			}
			else if(ALPHABET[i] == '1') {
				INDEX['l'] = INDEX['I'] = 1;
			}
		}
	}

	private static String encode(byte[] bytes) {
		if(bytes.length != 3) {
			return null;
		}
		int v = (((bytes[0]&0xff) << 16) | ((bytes[1]&0xff) << 8) | (bytes[2] & 0xff)) & 0x7FFFFF;
		StringBuilder sb = new StringBuilder();
		sb.append(ALPHABET[v % 48 + 10]);
		v /= 48;
		sb.append(ALPHABET[v % 58]);
		v /= 58;
		sb.append(ALPHABET[v % 58]);
		sb.append(ALPHABET[v / 58]);
		return sb.toString();
	}

	private static byte[] decode(char[] v) {
		if(v.length != 4) {
			return null;
		}
		int x = INDEX[v[3]];
		if(x < 0) {
			return null;
		}
		int r = x;
		r *= 58;
		x = INDEX[v[2]];
		if(x < 0) {
			return null;
		}
		r += x;
		r *= 58;
		x = INDEX[v[1]];
		if(x < 0) {
			return null;
		}
		r += x;
		r *= 48;
		x = INDEX[v[0]] - 10;
		if(x < 0) {
			return null;
		}
		r += x;
		byte[] bytes = new byte[3];
		bytes[0] = (byte)((r >> 16) & 0xff);
		bytes[1] = (byte)((r >> 8) & 0xff);
		bytes[2] = (byte)(r & 0xff);
		return bytes;
	}

	private static String encode(String no, boolean encode) {
		if(no.length() <= 8) {
			return no;
		}
		String part1 = no.substring(0, no.length() - 8);
		String part2 = no.substring(no.length() - 8, no.length() - 4);
		String part3 = no.substring(no.length() - 4);
		byte[] bytes;
		int r;
		if(encode) {
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				log.error("", e);
			}
			md.update(no.getBytes());
			md.update(key.getBytes());
			byte[] hash = md.digest();
			r = ((hash[0] & 0x1) << 8) | (hash[1] & 0xff);
			bytes = new byte[3];
		} else {
			bytes = decode(part2.toCharArray());
			if(bytes == null) {
				return null;
			}
			r = ((bytes[0] & 0xff) << 2) | ((bytes[1] & 0xff) >> 6);
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			log.error("", e);
		}
		md.update(part1.getBytes());
		md.update(key.getBytes());
		md.update(part3.getBytes());
		md.update((byte)(r&0xff));
		md.update((byte)(r>>8));
		byte[] hash = md.digest();
		if(encode) {
			int p2 = Integer.parseInt(part2);
			bytes[0] = (byte)((r >> 2) & 0xff);
			bytes[1] = (byte)(((r << 6) & 0xc0) | (((p2 >> 8) ^ hash[0]) & 0x3f));
			bytes[2] = (byte)((p2 ^ hash[1]) & 0xff);
			return part1 + encode(bytes) + part3;
		} else {
			int p2 = (((bytes[1] ^ hash[0]) << 8) & 0x3fff) | ((bytes[2] ^ hash[1]) & 0xff);
			return String.format("%s%04d%s", part1, p2, part3);
		}
	}

	public static void main(String[] args) {
		String no = "17006691639";
		// String no = "13106694113";
		String encode = encode(no);
		log.info(no);
		log.info(encode);
		log.info(decode(encode));
	}

}
