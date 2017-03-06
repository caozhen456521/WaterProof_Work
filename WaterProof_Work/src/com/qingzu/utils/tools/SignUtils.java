package com.qingzu.utils.tools;

import org.apache.http.NameValuePair;
import java.security.MessageDigest;
import java.util.List;

public class SignUtils {

	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";

	// public static String sign(String content, String privateKey) {
	// try {
	// PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
	// Base64.decode(privateKey));
	// KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
	// PrivateKey priKey = keyf.generatePrivate(priPKCS8);
	//
	// java.security.Signature signature = java.security.Signature
	// .getInstance(SIGN_ALGORITHMS);
	//
	// signature.initSign(priKey);
	// signature.update(content.getBytes(DEFAULT_CHARSET));
	//
	// byte[] signed = signature.sign();
	//
	// return Base64.encode(signed);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return null;
	// }
	//
	public static String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append("CH6NmrxE6x9E7xO9pACbASExFovBlCWQ");

		String appSign = getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return appSign;
	}

	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

}
