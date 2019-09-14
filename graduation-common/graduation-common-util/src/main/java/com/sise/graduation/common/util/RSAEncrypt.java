package com.sise.graduation.common.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAEncrypt {
	/** 
	 * 随机生成密钥对 
	 * @throws NoSuchAlgorithmException 
	 */  
	public static Pair<String, String> genKeyPair() throws NoSuchAlgorithmException {  
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象  
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
		// 初始化密钥对生成器，密钥大小为96-1024位  
		keyPairGen.initialize(1024,new SecureRandom());  
		// 生成一个密钥对，保存在keyPair中  
		KeyPair keyPair = keyPairGen.generateKeyPair();  
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥  
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥  
		// 得到公钥字符串
		String publicKeyString = Base64.encode(publicKey.getEncoded());
		// 得到私钥字符串  
		String privateKeyString = Base64.encode((privateKey.getEncoded()));
		// 将公钥和私钥保存到Map
		return new Pair<String, String>(publicKeyString, privateKeyString);
	}  
	/** 
	 * RSA公钥加密 
	 *  
	 * @param str 
	 *            加密字符串
	 * @param publicKey 
	 *            公钥 
	 * @return 密文 
	 * @throws Exception 
	 *             加密过程中的异常信息 
	 */  
	public static String encrypt( String str, String publicKey ) throws Exception{
		//base64编码的公钥
		byte[] decoded = Base64.decode(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.encode(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
		return outStr;
	}

	/** 
	 * RSA私钥解密
	 *  
	 * @param str 
	 *            加密字符串
	 * @param privateKey 
	 *            私钥 
	 * @return 铭文
	 * @throws Exception 
	 *             解密过程中的异常信息 
	 */  
	public static String decrypt(String str, String privateKey) throws Exception{
		//64位解码加密后的字符串
		byte[] inputByte = Base64.decode(str);
		//base64编码的私钥
		byte[] decoded = Base64.decode(privateKey);
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));  
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}
	
	public static void main(String[] args) throws Exception {
		//生成公钥和私钥
		//Pair<String, String> keyPair = genKeyPair();
		//加密字符串
		String pu = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQT0cOIL11bsAXDc1FsZXbILiPJGAusvOcU+MiBnBPax9dKLi3s2J1X4Ih6aOpnrEht7Soh/iUPKODFIj2S/otWvRZL2xITDB7PObjm7BxWeVY8c38gtXwRTYfBSPUqqPxsS9wTGIw/QlXJZtyPCl4UUMmMS0+B/tUX9jV28/V+wIDAQAB";
		String pr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJBPRw4gvXVuwBcNzUWxldsguI8kYC6y85xT4yIGcE9rH10ouLezYnVfgiHpo6mesSG3tKiH+JQ8o4MUiPZL+i1a9FkvbEhMMHs85uObsHFZ5VjxzfyC1fBFNh8FI9Sqo/GxL3BMYjD9CVclm3I8KXhRQyYxLT4H+1Rf2NXbz9X7AgMBAAECgYAUmWxwJNZGjA+ZetvhZe8ZP3t4211rbRn/J65VbgvcGlN4zdZISq/0RzThOGOnU8K/KGT+ZaJjLqC8llLSrZfBaMP6ynqBxO1gF3Jhv4tHd4T9/ljOR3pFz9jcPcbv7quO+6MiKjFL80BPAR7eltf4WYYyuLQegvlw6jlNqLLRMQJBAMXMZ58wapVoqsL6ncuORn1d3PAVdYpxmP3pF+SlpJwFvI2nqBkwWIfnwvMS2gRUeCnXbxmBO7CwBM4VoioY4EkCQQC6xbcZYRpl1KzoFOeK3y1wc717ZCvIPeKXNz4q/wfCVpNL6wNasnwV5MtMgabdwiQLHD2KJO3skmGArkVYN8wjAkEAhFRU+u5EUELfA2Txjo5X4GWce06P/Zg0N6LrJkAfWXaJB+gjkP6ELCBEj6SMERuF7NBBdrm7+mA4Rru4axuLaQJAchPjUxgRRJNTDOn/xr2sBlxbJqu3ZjYOWWg/i4l71sKEc0FXKiWMTt8RIWirYBV9aF/WYrNNs0pxTWSkLE7gWwJBAKK80TiEHUQcG30bSbK2S4ImUj8+NEABQeDlh+v1C3SvZvmvrqOCJvEc9UGnJw1vUK8NXhHLOVUGe4JWvOhk1dA=";
		Pair<String, String> keyPair = new Pair<String, String>(pu, pr);
		String message = "123456";
		System.out.println("随机生成的公钥为:" + keyPair.getFirst());
		System.out.println("随机生成的私钥为:" + keyPair.getSecond());
		String messageEn = encrypt(message,keyPair.getFirst());
		System.out.println(message + "\t加密后的字符串为:" + messageEn);
		String messageDe = decrypt(messageEn,keyPair.getSecond());
		System.out.println("还原后的字符串为:" + messageDe);
		
		String messageEn2 = "cH1uqUaK1z/dmIwnYJDpTy7NcXKe5rOnQwUZDsmAokHmhwtaKXP16eBUmlrLrXFoUb63o8lJYv2GroxWuS8dyKT8BS0ymFUn5kRVpyqYuxPxFrR1HG1XVoYOuLqPQQ3h2GIj7TKwktYEIE2jzffGLHNItj9I0bHK/PV8TaJToes=";
		System.out.println(decrypt(messageEn2,keyPair.getSecond()));
	}
}
