package com.knowledge.crypto;

import com.acooly.openapi.tool.codec.Hex;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

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

    @Test
    public void testSignature() throws Exception {
        // 将明文用散列算法生成消息摘要
        InputStream inputStream = CryptoTest.class.getResourceAsStream("data.txt");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int b;
        while((b = inputStream.read()) >= 0) {
            byteArrayOutputStream.write(b);
        }
        byte[] content = byteArrayOutputStream.toByteArray();
        // 生成摘要信息 hash算法 MD5,SHA-1
        // 1、MD5
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] digest = messageDigest.digest(content);
        System.out.println(String.format("消息摘要：%s", Hex.encodeHexString(digest)));
        // 使用摘要信息生成签名串
        // 签名算法ECDSA SHA1withECDSA EC
        // 生成加密用的公钥和私钥
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC"); // 使用ECC算法
        keyPairGenerator.initialize(128);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        System.out.println(String.format("私钥：%s", Hex.encodeHexString(privateKey.getEncoded())));
        System.out.println(String.format("公钥：%s", Hex.encodeHexString(publicKey.getEncoded())));
        String privateKeyHex = Hex.encodeHexString(privateKey.getEncoded());
        String publicKeyHex = Hex.encodeHexString(publicKey.getEncoded());
        // 使用私钥生成签名
        // 使用PKCS8密钥编码格式规范生成加密密钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Hex.decodeHex(privateKeyHex.toCharArray()));
        // 获取EC算法的密钥生产工厂
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PrivateKey privateKey1 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance("SHA1withECDSA");
        signature.initSign(privateKey1);
        signature.update(digest);
        byte[] sign = signature.sign();
        System.out.println(String.format("数字签名：%s", Hex.encodeHexString(sign)));

        // 使用公钥验证签名
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Hex.decodeHex(publicKeyHex.toCharArray()));
        PublicKey publicKey1 = keyFactory.generatePublic(x509EncodedKeySpec);
        signature.initVerify(publicKey1);
        signature.update(digest);
        boolean verify = signature.verify(sign);
        System.out.println(String.format("验签结果：%s", verify));
    }

}
