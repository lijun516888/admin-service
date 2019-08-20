package com.knowledge.crypto;

import com.acooly.openapi.tool.codec.Hex;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class CryptoTest {

    @Test
    public void aesEncrypt() throws Exception {
        String content = "123456你说话";
        String key = "16fe91d509df360bfa9608587aca640e";
        String iv = "579271e569c1ffd5a21dce242aaa44f8";
        // 随时数生成对象
        SecureRandom secureRandom = new SecureRandom(Hex.decodeHex(key.toCharArray()));
        // 根据算法获取秘钥生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 初始化秘钥生成器
        keyGenerator.init(secureRandom);
        // 产生安全秘钥对象
        SecretKey secretKey = keyGenerator.generateKey();

        SecretKeySpec keySpec = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "AES");
        // 获取加密对象
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(Hex.decodeHex(iv.toCharArray())));
        byte[] bytes = cipher.doFinal(content.getBytes());
        System.out.println(Hex.encodeHexString(bytes));

        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(Hex.decodeHex(iv.toCharArray())));
        bytes = cipher.doFinal(bytes);
        System.out.println(new String(bytes, Charset.forName("UTF-8")));

        // System.out.println(Hex.encodeHexString(secretKey.getEncoded()));

        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] digest1 = digest.digest(content.getBytes());
        System.out.println(Hex.encodeHexString(digest1));


    }

    @Test
    public void test() {
        byte[] iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        System.out.println(Hex.encodeHexString(iv));

        byte[] key = new byte[16];
        secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        System.out.println(Hex.encodeHexString(key));
    }

}
