package com.king.bio.web.controller.miniapp.wjt.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaQrcodeServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage.Data;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;

import com.google.common.collect.Lists;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class WxMaConfiguration {
    //配置文件
    private static WxMaProperties properties;
    public static void setProperties(WxMaProperties properties) {
        WxMaConfiguration.properties = properties;
    }
    //模板消息
    private static final WxMaMessageHandler templateMsgHandler = new WxMaMessageHandler() {

        public void handle(WxMaMessage message, Map<String, Object> context,
                WxMaService service, WxSessionManager sessionManager)
                throws WxErrorException {
            WxMaTemplateMessage.Data data = new WxMaTemplateMessage.Data("keyword1", "339208499", "#173177");
            ArrayList<Data> newArrayList = Lists.newArrayList(data);
            WxMaTemplateMessage build = WxMaTemplateMessage.newBuilder()
                    .templateId("此处更换为自己的模板id")
                    .formId("自己替换可用的formid")
                    .data(newArrayList)
                    .toUser(message.getFromUser()).build();
            service.getMsgService().sendTemplateMsg(build);

        }
    };
    //日志消息
    private static final WxMaMessageHandler logHandler = new WxMaMessageHandler() {
        public void handle(WxMaMessage message, Map<String, Object> context,
                WxMaService service, WxSessionManager sessionManager)
                throws WxErrorException {
            System.out.println("收到消息：" + message.toString());
            WxMaKefuMessage build = WxMaKefuMessage.TEXT().content("收到信息为：" + message.toJson()).toUser(message.getFromUser()).build();
            service.getMsgService().sendKefuMsg(build);
        }
    };
    //文本消息
    private static final WxMaMessageHandler textHandler = new WxMaMessageHandler() {
        public void handle(WxMaMessage message, Map<String, Object> context,
                WxMaService service, WxSessionManager sessionManager)
                throws WxErrorException {
            WxMaKefuMessage build = WxMaKefuMessage.TEXT().content("回复文本消息").toUser(message.getFromUser()).build();
            service.getMsgService().sendKefuMsg(build);
        }
    };
    //图片消息
    private static final WxMaMessageHandler picHandler = new WxMaMessageHandler() {

        public void handle(WxMaMessage message, Map<String, Object> context,
                WxMaService service, WxSessionManager sessionManager)
                throws WxErrorException {
            try {
                WxMediaUploadResult uploadResult = service.getMediaService()
                        .uploadMedia("image","png",ClassLoader.getSystemResourceAsStream("tmp.png"));
                WxMaKefuMessage build = WxMaKefuMessage.IMAGE()
                        .mediaId(uploadResult.getMediaId())
                        .toUser(message.getFromUser()).build();
                service.getMsgService().sendKefuMsg(build);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
    };
    //二维码消息
    private static final WxMaMessageHandler qrcodeHandler = new WxMaMessageHandler() {

        public void handle(WxMaMessage message, Map<String, Object> context,
                WxMaService service, WxSessionManager sessionManager)
                throws WxErrorException {
            try {
                final File file = service.getQrcodeService().createQrcode("123", 430);
                WxMediaUploadResult uploadResult = service.getMediaService()
                        .uploadMedia("image", file);
                WxMaKefuMessage build = WxMaKefuMessage.IMAGE()
                        .mediaId(uploadResult.getMediaId())
                        .toUser(message.getFromUser()).build();
                service.getMsgService().sendKefuMsg(build);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
    };

   
    /**
     * 配置信息
     * @return
     */
    private static WxMaConfig config() {
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        config.setAppid(properties.getAppid());
        config.setSecret(properties.getSecret());
        config.setToken(properties.getToken());
        config.setAesKey(properties.getAesKey());
        config.setMsgDataFormat(properties.getMsgDataFormat());
        return config;
    }
    /**
     * 
     * @return WxMaService
     */
    public static WxMaService wxMaService() {
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config());
        return service;
    }
    /**
     * 
     * @return WxMaMessageRouter
     */
    public static WxMaMessageRouter router() {
        final WxMaMessageRouter router = new WxMaMessageRouter(wxMaService());
        router.rule().handler(logHandler).next().rule().async(false)
                .content("模板").handler(templateMsgHandler).end().rule()
                .async(false).content("文本").handler(textHandler).end().rule()
                .async(false).content("图片").handler(picHandler).end().rule()
                .async(false).content("二维码").handler(qrcodeHandler).end();
        return router;
    }
    /**
     * 
     * @return WxMaQrcodeService
     */
    public static WxMaQrcodeService wxMaQrcodeService() {
        WxMaQrcodeService wxMaQrcodeService = new WxMaQrcodeServiceImpl(wxMaService());
        return wxMaQrcodeService;
    }

}
