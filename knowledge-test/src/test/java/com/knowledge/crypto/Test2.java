package com.knowledge.crypto;

import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Test2 {

    private static byte[] key = {105, -37, -7, 48, 115, 76, 2, -8, -123, -37, -112, -90, -75, -41, -18, -83,};

    private static byte[] privateKey = {48, 65, 2, 1, 0, 48, 19, 6, 7, 42, -122, 72, -50, 61, 2, 1, 6, 8, 42, -122, 72, -50, 61, 3, 1, 7, 4, 39, 48, 37, 2, 1, 1, 4, 32, -94, 78, 126, -107, -69, 13, 81, -116, 10, -70, -43, -111, -8, -20, 52, 83, -57, 9, -52, -17, 16, 41, 40, -16, 15, 68, 21, -52, -58, -127, 104, -97,};

    private static byte[] publicKey = {48, 89, 48, 19, 6, 7, 42, -122, 72, -50, 61, 2, 1, 6, 8, 42, -122, 72, -50, 61, 3, 1, 7, 3, 66, 0, 4, 3, 97, -32, 53, -62, 119, 101, 93, 52, 95, 116, 101, -62, 1, -6, 39, -8, -72, -95, -82, 45, -103, 127, -83, -43, -80, -42, 51, 29, -50, -13, -23, -65, 75, -45, -8, -45, 11, -44, 71, 111, 33, -81, -52, -108, -44, 73, 63, -95, 42, -5, -118, 112, 11, -61, 4, -93, -15, -52, -78, -120, 101, 103, 48,};

    private static String data = "123456";

    @Test
    public void t1() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        byte[] encKey = kg.generateKey().getEncoded();
        for(byte b : encKey) {
            System.out.print(String.format("%d, ", b));
        }
        System.out.print("\n");
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
        KeyPair keyPair = keyPairGen.generateKeyPair();
        PublicKey aPublic = keyPair.getPublic();
        for(byte b : aPublic.getEncoded()) {
            System.out.print(String.format("%d, ", b));
        }
        System.out.print("\n");
        PrivateKey aPrivate = keyPair.getPrivate();
        for(byte b : aPrivate.getEncoded()) {
            System.out.print(String.format("%d, ", b));
        }
    }

    @Test
    public void t2() throws Exception {
        Signature signature = Signature.getInstance("SHA1withECDSA");
        KeyFactory kf = KeyFactory.getInstance("EC");
        signature.initSign(kf.generatePrivate(new PKCS8EncodedKeySpec(Test2.privateKey)));
        signature.update(data.getBytes());
        byte[] sign = signature.sign();
        System.out.printf(new BASE64Encoder().encode(sign).replace("\n", "").replace("\r", ""));
    }

    @Test
    public void t3() throws Exception {
        String d = "MEUCIQDSAREKf4QgGCEVRMBhMnXsqkr5OJOl/ddF0tT64/xL/gIgDogNBsOPiTak98InFsN75feY6epCTNmgTNViHX9RfNQ=";
        Signature signature = Signature.getInstance("SHA1withECDSA");
        KeyFactory kf = KeyFactory.getInstance("EC");
        signature.initVerify(kf.generatePublic(new X509EncodedKeySpec(publicKey)));
        signature.update(data.getBytes());
        boolean verify = signature.verify(new BASE64Decoder().decodeBuffer(d));
        System.out.printf(String.valueOf(verify));
    }

    @Test
    public void t4() {

    }

}
