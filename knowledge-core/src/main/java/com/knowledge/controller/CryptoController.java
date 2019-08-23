package com.knowledge.controller;

import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.openapi.tool.codec.Hex;
import com.gexin.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Map;

@RestController
public class CryptoController {

    @RequestMapping(value = "/sts/services/stsInterfaceTest")
    public JsonEntityResult<String> stsInterfaceTest(HttpServletRequest request) {
        JsonEntityResult<String> result = new JsonEntityResult<>();
        String publicKeyHex =
                "3036301006072a8648ce3d020106052b8104001c0322000405f936a4b78982e2aa340cf970d32bfe4f2f760bd22474aa2c08a8ce3b2aade8";
        try {
            InputStream is = request.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            String content = CharStreams.toString(reader);
            JSONObject jsonObject = JSONObject.parseObject(content);
            String sign = jsonObject.getString("sign");
            String data = jsonObject.getString("data");

            // 摘要
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(data.getBytes("UTF-8"));
            byte[] digest = md.digest();

            // 验签
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Hex.decodeHex(publicKeyHex.toCharArray()));
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("SHA1withECDSA");
            signature.initVerify(publicKey);
            signature.update(digest);
            boolean verify = signature.verify(Hex.decodeHex(sign.toCharArray()));
            if(!verify) {
                result.setSuccess(false);
                result.setMessage("签名不正确");
            }


            JSONObject resp = new JSONObject();
            Map<String, String> transHeader = Maps.newHashMap();
            transHeader.put("requestId", "0000000000000000000000000000000002025257");
            transHeader.put("requestTime", "2019-07-11 22:43:14");
            transHeader.put("transType", "1006001");
            transHeader.put("transResultCode", "1");
            Map<String, String> transResponse = Maps.newHashMap();

            List<Map<String, String>> coverages = Lists.newArrayList();

            resp.put("transHeader", transHeader);
            resp.put("transResponse", transResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
