package com.wizarpos.holdlib.utils;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;

import android.text.TextUtils;

/**
 * 公钥加解密参考
 *
 * 
 */
public class RSAUtil
{

	/**
	 * 算法名称
	 * */
	private static final String ALGORITHOM = "RSA";
	/** */
	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** */
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;
	/**
	 * 默认的安全服务提供者
	 * */
//	private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();

	public static  RSAPublicKey publicKey;
	static{
		BigInteger modulus = new BigInteger(
				"9c3c56f623eb8bc4e587e8ede7157704c5d067602a508dc441ca797a74b092647c819d7237855d9c0ebf542251c619391342679f702b03621f51cf52daa52c61295a8e2e29d8bb067b6f2b1e734d65f78e8e04cf435311b9c456ab89f7b5e81b53c34a687bb6a608c7c7d3bbcec537531387362e6d799baa0f13f777012df923",16);
		BigInteger publicExponent = new BigInteger("10001",16);
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);

		KeyFactory keyFactory=null;
		try {
			keyFactory = KeyFactory.getInstance(ALGORITHOM);
			System.out.println("provide:"+keyFactory.getProvider());

			publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception
	{

		BigInteger modulus = new BigInteger(
				"94d2d4bd317da870b4623d7cd6e94c2740a4ed51d816d8c4569bef0970a6cd14a0d2b586eaae0a31533bb5a959a036659168dbb49f4840e87c54602420d5453ba4e33e8236c1f0dd08fa362183659a11dc6e439306ac7e4f425feaa7aa5cd72214cbb8f1b5e19b8c2db78cf79a826430962ce21e61ea2a4a7c4c6cf11adfbfeb",
				16);
		BigInteger publicExponent = new BigInteger("10001", 16);
		// 根据给定的系数和专用指数构造一个RSA专用的公钥对象
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus,
				publicExponent);

		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHOM
				);
		RSAPublicKey publicKey = (RSAPublicKey) keyFactory
				.generatePublic(publicKeySpec);
		// 公钥加密
		String a = "{ \"mid\":\"100105100000173\",  \"pay_channel\":\"A\", \"terminal_no\":\"WP14521000000010\", \"goods_info\":\"1,2,3\", \"goods_detail\":[{\"a\":\"b\"}], \"out_trade_no\":\"10201002120020022\",  \"spbill_create_ip\":\"12.32.2.3\", \"total_fee\":1, \"auth_code\":\"121\", \"version\":\"1.2\",\"sign\":\"111\", \"return_url\":\"htt://\"}";
		String es = encryptString(publicKey, a);
		System.out.println(es);
		// 公钥解密

		String ds = decryptStringByPublicKey(
				publicKey,
				"5b686e2550270edc98476f7f507161a6d64b87b94c1624667a2d893b95b98e1960d053377a551a0835d4f663dce5d1a4f26d8135f5c225e7cf8361fbdf6a3837a0747edab29c50020ba284b50025f1dad7e9f51b302355967228da65d46d904aabf4593cb2d187f2a1b55cc56725a88fa612184b4d1eb663b39e6275eb06d4c446da9d65a31364c077b3a309fbbeb347972589d14a996439a6b5a5d2193316eaa3f883b40622bb77c483460fb74d9663f61d466aa752c875b06d07b07ef97c2c82caf3087bb2b0c227284758b35ad59e7b174840d24d83a7749f1757682fbd9ca4032d1a59ec0092f8b8951226bd0e903452437f5ee26568156ac24c19c4e82943e238ba7f226d0b846bcd2e14a4cb164b7b86d0f69a965c991ecba36ad432d9bbc0005f5f7523722fb35cd2c3924e23d9cf247e6d9d93be3696c04e42fac7c9ea3750c37cbe0e40bd77a3e64125cdd6bf75803c42a93b5b355bf6282a131046a3d8ccccb88a81bd9a7c9e5c8ef2d6e5902ad0f02f9fbee09d9c9f4073671c4c0e1a5a2f0e1a2529b62505dfbbef64c0236070fd6d7b10e5a3796b2f1afe65c4fac58ed366fd0b85cee0fe0460603412bb107b3a148ca49bf3a1aed142cceccf582f8bd4e350db2ff99d8481ed363db09be5a3c2cf66b4a883322019cd0444e37abe53b8b8b4f6608fb05e3c541afb687183b305766664b315daa16d33d8b1c6");

		System.out.println(ds);

		
	}

	/*****************************************
	 * Description：使用给定的公钥加密给定的字符串<br/>
	 * 
	 * @param publicKey
	 *            给定的公钥。
	 * @param plaintext
	 *            字符串。
	 * @return 给定字符串的密文。
	 *******************************************/
	public static String encryptString(PublicKey publicKey, String plaintext)
	{
		if (publicKey == null || plaintext == null)
		{
			return null;
		}
		try
		{
			byte[] data = plaintext.getBytes("utf-8");

			byte[] en_data = encrypt(publicKey, data);
			return new String(Hex.encodeHex(en_data));
		} catch (Exception ex)
		{
		}
		return null;
	}

	/*****************************************
	 * Description：使用指定的公钥加密数据<br/>
	 * 
	 * @param publicKey
	 *            给定的公钥。
	 * @param data
	 *            要加密的数据。
	 * @return 加密后的数据。
	 *******************************************/
	public static byte[] encrypt(PublicKey publicKey, byte[] data)
			throws Exception
	{
		Cipher ci = Cipher.getInstance(ALGORITHOM);
		ci.init(Cipher.ENCRYPT_MODE, publicKey);

		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0)
		{
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK)
			{
				cache = ci.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else
			{
				cache = ci.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();

		return encryptedData;
	}

	/**
	 * 指定的公钥解密
	 * 
	 * @param publicKey
	 * @param encrypttext
	 * @return
	 */
	public static String decryptStringByPublicKey(PublicKey publicKey,
			String encrypttext)
	{
		if (publicKey == null || TextUtils.isEmpty(encrypttext))
		{
			return null;
		}
		try
		{
			byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
			byte[] data = decryptByPublicKey(publicKey, en_data);
			return new String(data);
		} catch (Exception ex)
		{
		}
		return null;
	}

	public static byte[] decryptByPublicKey(PublicKey publicKey, byte[] data)
			throws Exception
	{
		Cipher ci = Cipher.getInstance(ALGORITHOM);
		ci.init(Cipher.DECRYPT_MODE, publicKey);

		int inputLen = data.length;

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0)
		{
			if (inputLen - offSet > MAX_DECRYPT_BLOCK)
			{
				cache = ci.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
			} else
			{
				cache = ci.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();

		return decryptedData;
	}
}
