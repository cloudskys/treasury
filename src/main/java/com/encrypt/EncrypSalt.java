package com.encrypt;

import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
/**
 * MD5基于盐的加解密
 * @author Administrator
 *
 */
public class EncrypSalt {
	/**
	 * 生成含有随机盐的密码
	 */
	public static String generate(String password) {
		int i1 = new Random().nextInt(99999999);// 生成第一个随机数
		System.out.println("生成第一个随机数i1=" + i1);
		int i2 = new Random().nextInt(99999999);// 生成第二个随机数
		System.out.println("生成第二个随机数i2=" + i2);
		String salt = String.valueOf(i1) + String.valueOf(i2);
		System.out.println("盐=" + salt);// 生成盐
		if (salt.length() < 16) {
			for (int i = 0; i < 16 - salt.length(); i++) {
				salt += "0";
			}
		}
		password = md5Hex(password + salt);
		System.out.println("加盐后的password=" + password);
		char[] cs1 = password.toCharArray();
		char[] cs2 = salt.toCharArray();
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = cs1[i / 3 * 2];
			cs[i + 1] = cs2[i / 3];
			cs[i + 2] = cs1[i / 3 * 2 + 1];
		}
		return new String(cs);
	}

	/**
	 * 校验密码是否正确
	 */
	public static boolean verify(String password, String md5) {
		char[] cs = md5.toCharArray();
		char[] cs1 = new char[32];
		char[] cs2 = new char[16];
		for (int i = 0; i < 48; i += 3) {
			cs1[i / 3 * 2] = cs[i];
			cs1[i / 3 * 2 + 1] = cs[i + 2];
			cs2[i / 3] = cs[i + 1];
		}
		String salt = new String(cs2);
		System.out.println("验证salt=" + salt);
		return md5Hex(password + salt).equals(new String(cs1));
	}

	/**
	 * 获取十六进制字符串形式的MD5摘要
	 */
	public static String md5Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");// 获取md5的一个加密
			byte[] bs = md5.digest(src.getBytes());
			return new String(new Hex().encode(bs));
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		String password = generate("admin");// 秘钥
		System.out.println("MD5>>>:"+password);
		System.out.println(verify("admin", password));
	}
}
