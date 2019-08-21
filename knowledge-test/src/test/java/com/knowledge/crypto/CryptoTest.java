package com.knowledge.crypto;

import com.acooly.openapi.tool.codec.Hex;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

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
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
        byte[] bytes = cipher.doFinal(content.getBytes());
        System.out.println(Hex.encodeHexString(bytes));

        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
        bytes = cipher.doFinal(bytes);
        System.out.println(new String(bytes, Charset.forName("UTF-8")));
        // System.out.println(Hex.encodeHexString(secretKey.getEncoded()));
    }

    @Test
    public void pairKeyTest() throws Exception {
        String content = "@#$%^&123qwr好";

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey aPublic = keyPair.getPublic();
        PrivateKey aPrivate = keyPair.getPrivate();
        System.out.println(Hex.encodeHexString(aPublic.getEncoded()));
        System.out.println(Hex.encodeHexString(aPrivate.getEncoded()));

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(aPrivate.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initSign(privateKey);
        signature.update(content.getBytes());
        byte[] sign = signature.sign();
        System.out.println(Hex.encodeHexString(sign));

    }

    @Test
    public void eccAlgorithm() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(112);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        ECPublicKey aPublic = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey aPrivate = (ECPrivateKey) keyPair.getPrivate();
        System.out.println(Hex.encodeHexString(aPublic.getEncoded()));
        System.out.println(Hex.encodeHexString(aPrivate.getEncoded()));
    }

    @Test
    public void generateMessageDigest() throws Exception {
        String content = "@#$%^&123qwr好";
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] digestByte = digest.digest(content.getBytes());
        System.out.println(Hex.encodeHexString(digestByte));
    }

    @Test
    public void generateKey() {
        byte[] keyByte = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(keyByte);
        String key = Hex.encodeHexString(keyByte);
        System.out.println(key);
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
