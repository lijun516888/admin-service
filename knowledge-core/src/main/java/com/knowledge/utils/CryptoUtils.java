package com.knowledge.utils;

import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Log4j
public class CryptoUtils {

    private static final String CRYPTO_DEFAULT_ENCODING = "UTF-8";
    private static final String CRYPOT_ALGORITHM = "AES";
    private static final String CRYPOT_ALGORITHM_MODEL = "AES/CBC/PKCS5Padding";

    /**
     * 加密
     * @param key
     * @param content
     * @return
     */
    public static String encrypt(String key, String content) {
        try {
            return CryptoUtils.encrypt(Hex.decodeHex(key), content);
        } catch (DecoderException e) {
            log.error("加密错误：{}", e);
        }
        return null;
    }

    /**
     * 加密
     * @param key
     * @param iv
     * @param content
     * @return
     */
    public static String encrypt(String key, String iv, String content) {
        try {
            return CryptoUtils.encrypt(Hex.decodeHex(key), Hex.decodeHex(iv), content);
        } catch (DecoderException e) {
            log.error("加密错误：{}", e);
        }
        return null;
    }

    /**
     * 加密
     * @param key
     * @param content
     * @return
     */
    public static String encrypt(byte[] key, String content) {
        SecretKeySpec keySpec = new SecretKeySpec(key, CRYPOT_ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(CRYPOT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] result = cipher.doFinal(content.getBytes(CRYPTO_DEFAULT_ENCODING));
            return Hex.encodeHexString(result);
        } catch (Exception e) {
            log.error("加密错误：{}", e);
        }
        return null;
    }

    /**
     * 加密
     * @param key
     * @param iv
     * @param content
     * @return
     */
    public static String encrypt(byte[] key, byte[] iv, String content) {
        SecretKeySpec keySpec = new SecretKeySpec(key, CRYPOT_ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(CRYPOT_ALGORITHM_MODEL);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            byte[] result = cipher.doFinal(content.getBytes(CRYPTO_DEFAULT_ENCODING));
            return Hex.encodeHexString(result);
        } catch (Exception e) {
            log.error("加密错误：{}", e);
        }
        return null;
    }

    /**
     * 解密
     * @param key
     * @param content
     * @return
     */
    public static String decrypt(String key, String content) {
        try {
            return CryptoUtils.decrypt(Hex.decodeHex(key),  content);
        } catch (Exception e) {
            log.error("解密错误：{}", e);
        }
        return null;
    }

    /**
     * 解密
     * @param key
     * @param iv
     * @param content
     * @return
     */
    public static String decrypt(String key, String iv, String content) {
        try {
            return CryptoUtils.decrypt(Hex.decodeHex(key), Hex.decodeHex(iv), content);
        } catch (Exception e) {
            log.error("解密错误：{}", e);
        }
        return null;
    }

    /**
     * 解密
     * @param key
     * @param content
     * @return
     */
    public static String decrypt(byte[] key, String content) {
        SecretKeySpec keySpec = new SecretKeySpec(key, CRYPOT_ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(CRYPOT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] result = cipher.doFinal(Hex.decodeHex(content));
            return new String(result, CRYPTO_DEFAULT_ENCODING);
        } catch (Exception e) {
            log.error("解密错误：{}", e);
        }
        return null;
    }

    /**
     * 解密
     * @param key
     * @param content
     * @return
     */
    public static String decrypt(byte[] key, byte[] iv, String content) {
        SecretKeySpec keySpec = new SecretKeySpec(key, CRYPOT_ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(CRYPOT_ALGORITHM_MODEL);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            byte[] result = cipher.doFinal(Hex.decodeHex(content));
            return new String(result, CRYPTO_DEFAULT_ENCODING);
        } catch (Exception e) {
            log.error("解密错误：{}", e);
        }
        return null;
    }

    /**
     * 根据指定明文生成指定密钥
     * @param key
     * @return
     */
    public static String genarateteKey(String key) {
        try {
            SecureRandom random = new SecureRandom(key.getBytes(CRYPTO_DEFAULT_ENCODING));
            return Hex.encodeHexString(genarateRandomKey(random)).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            log.error("随机密钥生成错误，不支持的编码：{}", e);
        }
        return null;
    }

    /**
     * 生成随机密钥
     * @return
     */
    public static byte[] genarateRandomKey() {
        SecureRandom random = new SecureRandom();
        return genarateRandomKey(random);
    }

    /**
     * 获取随机向量
     * @return
     */
    public static String genarateRandomIv() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return Hex.encodeHexString(iv).toUpperCase();
    }

    /**
     * 获取随机密钥
     * @return
     */
    private static byte[] genarateRandomKey(SecureRandom random) {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(CRYPOT_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.error("获取随时密钥失败：{}", e);
        }
        keyGenerator.init(128, random);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

}
