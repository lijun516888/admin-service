package com.knowledge.example;

import org.junit.Test;

import java.security.MessageDigest;

public class MaskTest {

    @Test
    public void t1() throws Exception {
        String key = "423efefefewrwerwevdr34r43tt";
        String phone = "13132354113";
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(phone.getBytes());
        digest.update(key.getBytes());
        byte[] hash = digest.digest();

        byte hash1 = hash[0];
        byte hash2 = hash[1];

        System.out.println(hash);


        byte d = -127;
        int a = 0xff;
        int b = -10;
        int c = a & b;
        System.out.println(d);
    }

}
