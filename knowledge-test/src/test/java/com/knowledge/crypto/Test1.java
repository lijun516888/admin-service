package com.knowledge.crypto;

import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class Test1 {

    private static byte[] publicKey = {48, 89, 48, 19, 6, 7, 42, -122, 72, -50, 61, 2, 1, 6, 8, 42, -122, 72, -50, 61, 3, 1, 7, 3, 66, 0, 4, -113, 104, -12, 102, -127, 36, -16, -90, 34, -115, -90, 56, 75, -40, 54, 84, -4, -48, 104, -108, 115, -113, 29, 125, -72, -68, -126, -43, 80, -54, -111, 66, -42, 124, 9, 10, 84, 73, -13, 73, 43, -103, 10, 95, -103, -107, 94, 120, -127, 17, -80, 79, 82, 56, 15, 118, 29, -75, 85, 85, -61, -57, 111, 33, };
    private static byte[] privateKey = {48, 65, 2, 1, 0, 48, 19, 6, 7, 42, -122, 72, -50, 61, 2, 1, 6, 8, 42, -122, 72, -50, 61, 3, 1, 7, 4, 39, 48, 37, 2, 1, 1, 4, 32, -3, -42, 55, -99, 87, -15, 57, 116, 101, 84, 27, -34, 11, -99, -88, -42, -25, 98, 71, 100, -14, -68, -79, 14, -42, 121, 101, -39, 18, -91, 50, 15, };
    private static byte[] encKey = {-49, 48, 120, 88, 60, 77, 121, 64, 14, -64, -20, -12, -2, -30, 29, 55, };
    private String data = "123";

    @Test
    public void pairKeyGen() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        System.out.println("密钥为：");
        for(byte b : secretKey.getEncoded()) {
            System.out.print(String.format("%d, ", b));
        }
        KeyPairGenerator key = KeyPairGenerator.getInstance("EC");
        KeyPair keyPair = key.generateKeyPair();
        PublicKey aPublic = keyPair.getPublic();
        System.out.println("公钥为：");
        for(byte b : aPublic.getEncoded()) {
            System.out.print(String.format("%d, ", b));
        }
        PrivateKey aPrivate = keyPair.getPrivate();
        System.out.println("私钥为：");
        for(byte b : aPrivate.getEncoded()) {
            System.out.printf("%d, ", b);
        }
    }

    @Test
    public void encoder() throws Exception {
        Signature signature = Signature.getInstance("SHA1withECDSA");
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Test1.privateKey));
        signature.initSign(privateKey);
        byte[] sign = signature.sign();

        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        byte[] rand = kg.generateKey().getEncoded();

        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encKey, "AES"));

        SecretKeySpec newKey = new SecretKeySpec(cipher.doFinal(rand), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, new IvParameterSpec(new byte[16]));
        cipher.update(data.getBytes());

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        b.write(rand);
        b.write((byte) (sign.length & 0xff));
        b.write(cipher.doFinal());
        b.write(sign);
        b.flush();

        System.out.printf(new BASE64Encoder().encode(b.toByteArray()).replace("\n", "").replace("\r", ""));
    }

    @Test
    public void decoder() throws Exception {
        byte[] bytes = new BASE64Decoder().decodeBuffer("2CmAvsQfnjQgPFZKIrgDekjCN4OUe6BdJGjKAoo1SathMEYCIQDVpcHjo/XIMKO2bwlIOj/J7CrJAHyGA2UJwcAUbGbeMQIhAJ13SuwkyQn16xVvcW+nf3F/ZKOABPDOHWZ9iJFo59oq");

        byte[] rand = Arrays.copyOfRange(bytes, 0, 16);
        byte[] signLength = Arrays.copyOfRange(bytes, 16, 17);
        byte[] encoded = Arrays.copyOfRange(bytes, 17, bytes.length - signLength[0]);
        byte[] sign = Arrays.copyOfRange(bytes, bytes.length - signLength[0], bytes.length);

        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(privateKey, "AES"));
        cipher.doFinal(rand);

        System.out.println("rand:" + new BASE64Encoder().encode(rand).replace("\r","").replace("\n", ""));
        System.out.println("signLength:" + new BASE64Encoder().encode(signLength).replace("\r","").replace("\n", ""));
        System.out.println("encoded:" + new BASE64Encoder().encode(encoded).replace("\r","").replace("\n", ""));
        System.out.println("sign:" + new BASE64Encoder().encode(sign).replace("\r","").replace("\n", ""));
    }

    @Test
    public void signature() throws Exception {
        Signature signature = Signature.getInstance("SHA1withECDSA");
        KeyFactory kf = KeyFactory.getInstance("EC");
        PrivateKey signKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        signature.initSign(signKey);
        signature.update(data.getBytes());
        byte[] sign = signature.sign();
        System.out.print(new BASE64Encoder().encode(sign).replace("\r", "").replace("\n", ""));
    }

    @Test
    public void verify() throws Exception {
        String sign = "MEYCIQCd/jm15q2ohqg1BH+ywy2TndRycBmlEY3btXdHmTwwMgIhAJUKqZvnypA3bkJR8lELt67714EEy8Iq1LiaTKsrB2Ue";
        Signature signature = Signature.getInstance("SHA1withECDSA");
        KeyFactory kf = KeyFactory.getInstance("EC");
        PublicKey key = kf.generatePublic(new X509EncodedKeySpec(publicKey));
        signature.initVerify(key);
        signature.update(data.getBytes());
        boolean verify = signature.verify(new BASE64Decoder().decodeBuffer(sign));
        System.out.printf(String.valueOf(verify));
    }

}
