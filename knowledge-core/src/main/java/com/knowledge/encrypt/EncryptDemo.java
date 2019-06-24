package com.knowledge.encrypt;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;

public class EncryptDemo {

    public static void main(String[] args) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            System.out.println(keyGenerator.generateKey());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
