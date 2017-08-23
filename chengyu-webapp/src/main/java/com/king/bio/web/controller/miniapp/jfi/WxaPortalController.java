package com.king.bio.web.controller.miniapp.jfi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.log.Log;
import com.jfinal.wxaapp.WxaConfigKit;
import com.jfinal.wxaapp.api.WxaMessageApi;
import com.jfinal.wxaapp.jfinal.WxaMsgController;
import com.jfinal.wxaapp.msg.bean.WxaImageMsg;
import com.jfinal.wxaapp.msg.bean.WxaMsg;
import com.jfinal.wxaapp.msg.bean.WxaTextMsg;
import com.jfinal.wxaapp.msg.bean.WxaUserEnterSessionMsg;
import com.king.bio.web.controller.miniapp.jfi.interceptor.MsgInterceptor;

public class WxaPortalController extends WxaMsgController {
    final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private static final Log log = Log.getLog(WxaMsgController.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // 消息API
    WxaMessageApi wxaMessageApi = Duang.duang(WxaMessageApi.class);
    @Override
    @Before(MsgInterceptor.class)
    public void index() {
        // 开发模式输出微信服务发送过来的 xml、json 消息
        if (WxaConfigKit.isDevMode()) {
            System.out.println("接收消息:");
            System.out.println(getWxaMsgXml());
        }
        WxaMsg wxaMsg = getWxaMsg();

        if (wxaMsg instanceof WxaTextMsg) {
            processTextMsg((WxaTextMsg) wxaMsg);
        } else if (wxaMsg instanceof WxaImageMsg) {
            processImageMsg((WxaImageMsg) wxaMsg);
        } else if (wxaMsg instanceof WxaUserEnterSessionMsg) {
            processUserEnterSessionMsg((WxaUserEnterSessionMsg) wxaMsg);
        } else {
            log.error("未能识别的小程序消息类型。 消息内容为：\n" + getWxaMsgXml());
        }

        // 直接回复success（推荐方式）
        renderText("success");
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
