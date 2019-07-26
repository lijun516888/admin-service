package com.knowledge.wx;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/wx/portal/{appid}")
public class WxPortalController {

    private final WxMpService wxService;

    private final WxMpMessageRouter messageRouter;

    @ResponseBody
    @RequestMapping(value = "/service")
    public String service(@PathVariable String appid, HttpServletRequest request) {
        if(!"POST".equalsIgnoreCase(request.getMethod())) {
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
                    timestamp, nonce, echostr);
            return this.get(appid, signature, timestamp, nonce, echostr);
        } else {
            String requestBody = "";
            try {
                BufferedReader bufferedReader = request.getReader();
                String line;
                StringBuilder sb = new StringBuilder();
                while((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                requestBody = sb.toString();
            } catch (IOException e) {
                log.error("读取讲求参数错误：{}", e);
            }
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String openid = request.getParameter("openid");
            String encType = request.getParameter("encrypt_type");
            String msgSignature = request.getParameter("msg_signature");
            log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                            + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                    openid, signature, encType, msgSignature, timestamp, nonce, requestBody);
            return this.post(appid, requestBody, signature, timestamp, nonce, openid, encType, msgSignature);
        }
    }

    // @GetMapping(produces = "text/plain;charset=utf-8")
    public String get(String appid, String signature, String timestamp, String nonce, String echostr) {

        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    // @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(String appid, String requestBody, String signature, String timestamp, String nonce, String
            openid, String encType, String msgSignature) {

        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }
        log.debug("\n组装回复信息：{}", out);
        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }
        return null;
    }

}
