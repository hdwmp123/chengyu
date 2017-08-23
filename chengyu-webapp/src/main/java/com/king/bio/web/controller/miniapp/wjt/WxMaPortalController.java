package com.king.bio.web.controller.miniapp.wjt;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Kv;
import com.jfinal.wxaapp.jfinal.WxaMsgController;
import com.jfinal.wxaapp.msg.bean.WxaImageMsg;
import com.jfinal.wxaapp.msg.bean.WxaTextMsg;
import com.jfinal.wxaapp.msg.bean.WxaUserEnterSessionMsg;
import com.king.bio.web.controller.miniapp.wjt.config.WxMaConfiguration;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
// @RestController
// @RequestMapping("/wechat/portal")
public class WxMaPortalController extends WxaMsgController {
    final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private WxMaService wxService = WxMaConfiguration.wxMaService();
    private WxMaMessageRouter router = WxMaConfiguration.router();

    @Override
    public void index() {
        String signature = getPara("signature");
        String timestamp = getPara("timestamp");
        String nonce = getPara("nonce");
        if (!this.wxService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            String msg = "非法请求";
            logger.error(msg);
            Kv data = Kv.by("errcode", 500).set("errmsg", msg);
            renderJson(data);
            return;
        }
        String echostr = getPara("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            String echoStrOut = String.copyValueOf(echostr.toCharArray());
            renderText(echoStrOut);
            return;
        }
        String requestBody = HttpKit.readData(getRequest());
        String msg_signature = getPara("msg_signature");
        String encrypt_type = getPara("encrypt_type");
        final boolean isJson = Objects.equals(this.wxService.getWxMaConfig()
                .getMsgDataFormat(), WxMaConstants.MsgDataFormat.JSON);
        if (StringUtils.isBlank(encrypt_type)) {
            // 明文传输的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromJson(requestBody);
            } else {// xml
                inMessage = WxMaMessage.fromXml(requestBody);
            }
            this.route(inMessage);
            return;
        }

        if ("aes".equals(encrypt_type)) {
            // 是aes加密的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromEncryptedJson(requestBody,
                        this.wxService.getWxMaConfig());
            } else {// xml
                inMessage = WxMaMessage.fromEncryptedXml(requestBody,
                        this.wxService.getWxMaConfig(), timestamp, nonce,
                        msg_signature);
            }
            this.route(inMessage);
            return;
        }
        String msg = "不可识别的加密类型：" + encrypt_type;
        logger.error(msg);
        Kv data = Kv.by("errcode", 500).set("errmsg", msg);
        renderJson(data);
    }
    
    /**
     * 
     * @param message
     */
    private void route(WxMaMessage message) {
        try {
            this.router.route(message);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }
    
    @Override
    protected void processTextMsg(WxaTextMsg textMsg) {
        logger.info(gson.toJson(textMsg));
        renderJson("{}");
    }

    @Override
    protected void processImageMsg(WxaImageMsg imageMsg) {
        logger.info(gson.toJson(imageMsg));
        renderJson("{}");
    }

    @Override
    protected void processUserEnterSessionMsg(
            WxaUserEnterSessionMsg userEnterSessionMsg) {
        logger.info(gson.toJson(userEnterSessionMsg));
        renderJson("{}");
    }

}
