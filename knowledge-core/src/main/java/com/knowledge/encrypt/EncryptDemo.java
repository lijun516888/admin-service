package com.knowledge.encrypt;

import com.knowledge.utils.CryptoUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncryptDemo {

    private static String ENCRYPT_ALGORI = "AES";

    public static void main(String[] args) {

        try {
            String encode = URLEncoder.encode("123456789abc爸爸妈妈@#$%<>~'()（）、、\\\\//【】%", "UTF-8");
            System.out.println(encode);
            String decode = URLDecoder.decode(encode, "UTF-8");
            System.out.println(decode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String content = "123456789abc爸爸妈妈@#$%<>~'()（）、、\\\\//【】%";
        String key = CryptoUtils.genarateteKey("111");
        key = "832EB84CB764129D05D498ED9CA7E5CE";
        System.out.println(key);

        String enStr = CryptoUtils.encrypt(key, content);
        System.out.println(enStr);

        String deStr = CryptoUtils.decrypt(key, enStr);
        System.out.println(deStr);

        System.out.println(DigestUtils.md5Hex(content));

    }

}
