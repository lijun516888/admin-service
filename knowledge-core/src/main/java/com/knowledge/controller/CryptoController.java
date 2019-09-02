package com.knowledge.controller;

import com.acooly.openapi.tool.codec.Hex;
import com.gexin.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

@RestController
public class CryptoController {

    @RequestMapping(value = "/sts/services/stsInterfaceTest")
    public String stsInterfaceTest(HttpServletRequest request) {
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
                JSONObject transResponse = new JSONObject();
                JSONObject requestMessage = JSONObject.parseObject(data);
                JSONObject requestHeaderMessage = requestMessage.getJSONObject("transHeader");
                String requestTime = requestHeaderMessage.getString("requestTime");
                String requestId = requestHeaderMessage.getString("requestId");
                String transType = requestHeaderMessage.getString("transType");
                Map<String, String> transHeader = Maps.newHashMap();
                transHeader.put("requestId", requestId);
                transHeader.put("requestTime", requestTime);
                transHeader.put("transType", transType);
                transHeader.put("transResultCode", "0");
                transHeader.put("transResultDesc", "签名不正确");
                transResponse.put("transHeader", transHeader);
                return transResponse.toString();
            } else {
                return routeRequest(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String routeRequest(String requestMessage) {
        String methodCode = JSONObject.parseObject(requestMessage).getJSONObject("transHeader").getString("transType");
        return routeRequest(requestMessage, methodCode);
    }

    public String routeRequest(String requestMessage, String methodCode) {
        String resultMessage = null;
        switch (methodCode) {
            case "1006001":
                resultMessage = processPremiumTrial(requestMessage);
                break;
            case "2006002" :
                resultMessage = processUnderwriting(requestMessage);
                break;
            case "2006003":
                resultMessage = processSigning(requestMessage);
                break;
        }
        return resultMessage;
    }

    public String processPremiumTrial(String requestMessage) {
        SecureRandom random = new SecureRandom();
        JSONObject transRequest = JSONObject.parseObject(requestMessage).getJSONObject("transRequest");
        String requestTime = transRequest.getString("requestTime");
        String requestId = transRequest.getString("requestId");
        String transType = transRequest.getString("transType");
        JSONObject resp = new JSONObject();
        Map<String, String> transHeader = Maps.newHashMap();
        transHeader.put("requestId", requestId);
        transHeader.put("requestTime", requestTime);
        transHeader.put("transType", transType);
        transHeader.put("transResultCode", "1");
        transHeader.put("transResultDesc", "");
        JSONObject transResponse = new JSONObject();
        JSONArray coverages = new JSONArray();
        JSONObject policy = new JSONObject();
        JSONArray jsonArray = JSONObject.parseObject(requestMessage).getJSONObject("transRequest")
                .getJSONObject("policy").getJSONArray("coverages");
        for(Object obj : jsonArray) {
            JSONObject coverage = new JSONObject();
            JSONObject o = JSONObject.parseObject(obj.toString());
            coverage.put("productCode", o.getString("productCode"));
            coverage.put("sumAssured", o.getString("sumAssured")); // 保额
            coverage.put("premium", random.nextInt(100)); // 保费
            coverage.put("coverageInsuredName", "孙二");
            coverages.add(coverage);
        }
        policy.put("coverages", coverages);
        transResponse.put("policy", policy);
        resp.put("transHeader", transHeader);
        resp.put("transResponse", transResponse);
        return resp.toJSONString();
    }

    public String processUnderwriting(String requestMessage) {
        SecureRandom random = new SecureRandom();
        JSONObject transRequest = JSONObject.parseObject(requestMessage).getJSONObject("transRequest");
        String requestTime = transRequest.getString("requestTime");
        String requestId = transRequest.getString("requestId");
        String transType = transRequest.getString("transType");
        JSONObject resp = new JSONObject();
        Map<String, String> transHeader = Maps.newHashMap();
        transHeader.put("requestId", requestId);
        transHeader.put("requestTime", requestTime);
        transHeader.put("transType", transType);
        transHeader.put("transResultCode", "1");
        transHeader.put("transResultDesc", "");
        JSONObject transResponse = new JSONObject();
        JSONObject policy = new JSONObject();
        policy.put("proposalNumber", Strings.padStart(random.nextInt(100000000) + "", 10, '0')); // 投保单号
        transResponse.put("policy", policy);
        resp.put("transHeader", transHeader);
        resp.put("transResponse", transResponse);
        return resp.toJSONString();
    }

    public String processSigning(String requestMessage) {
        SecureRandom random = new SecureRandom();
        JSONObject transRequest = JSONObject.parseObject(requestMessage).getJSONObject("transRequest");
        String requestTime = transRequest.getString("requestTime");
        String requestId = transRequest.getString("requestId");
        String transType = transRequest.getString("transType");
        JSONObject resp = new JSONObject();
        Map<String, String> transHeader = Maps.newHashMap();
        transHeader.put("requestId", requestId);
        transHeader.put("requestTime", requestTime);
        transHeader.put("transType", transType);
        transHeader.put("transResultCode", "1");
        transHeader.put("transResultDesc", "");
        JSONObject transResponse = new JSONObject();
        JSONObject policy = new JSONObject();
        policy.put("proposalNumber", Strings.padStart(random.nextInt(100000000) + "", 10, '0')); // 投保单号
        policy.put("policyNumber", Strings.padStart(random.nextInt(100000) + "", 15, '0')); // 保单号
        transResponse.put("policy", policy);
        resp.put("transHeader", transHeader);
        resp.put("transResponse", transResponse);
        return resp.toJSONString();
    }

    public static final int ONCE_READ_BYTE_LENGTH = 512;

    public static void main(String[] args) {
        InputStream in = null;
        ByteArrayOutputStream os = null;
        try {
            in = new FileInputStream("");
            os = new ByteArrayOutputStream();
            byte[] bufBytes = new byte[ONCE_READ_BYTE_LENGTH];
            int a;
            while((a = in.read(bufBytes, 0, ONCE_READ_BYTE_LENGTH)) != -1) {
                os.write(bufBytes, 0, a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(in);
            closeStream(os);
        }
    }

    public static void closeStream(Closeable closeable) {
        try {
            if(closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
