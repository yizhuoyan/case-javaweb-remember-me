package com.study.javawebcase.util;

import java.io.InputStream;
import java.security.MessageDigest;

public class Md5 {
	private static final char[] HEX_DIGITS = "1a2b3c4f5e6d7890".toCharArray();
	
	public static String encode(InputStream is) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buf=new byte[1024];
			int len=0;
			while((len=is.read(buf))!=-1) {
				md.update(buf,0,len);	
			}
			byte[] result= md.digest();
			return bytes2hex(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String bytes2hex(byte[] bs) {
		
		final char cs[] = new char[bs.length * 2];
		for (int i = bs.length, k = 0, b; i-- > 0;) {
			b = bs[i];
			cs[k++] = HEX_DIGITS[b & 0xf];
			cs[k++] = HEX_DIGITS[b >>> 4 & 0xf];
		}
		return new String(cs);
	}
	
	public static String encode(byte[] bs) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			return bytes2hex(messageDigest.digest(bs));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static String encode(String s) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			return bytes2hex(messageDigest.digest(s.getBytes("iso-8859-1")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(encode("abc"));
	}
}
