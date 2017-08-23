package com.king.bio.web.controller.miniapp.wjt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import me.chanjar.weixin.common.exception.WxErrorException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.agent.repkg.de.schlichtherle.io.FileInputStream;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;

import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.weixin.sdk.utils.JsonUtils;
import com.jfinal.wxaapp.jfinal.WxaController;
import com.king.bio.common.model.BioUserImg;
import com.king.bio.common.model.BioUserInfo;
import com.king.bio.util.ModelUtils;
import com.king.bio.web.controller.miniapp.wjt.config.WxMaConfiguration;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class WxMaUserController extends WxaController{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private WxMaService wxService  = WxMaConfiguration.wxMaService();
    private WxMaQrcodeService wxMaQrcodeService = WxMaConfiguration.wxMaQrcodeService();
    /**
     * 登陆接口
     */
    public void login() {
        String code = getPara("code");
        if (StringUtils.isBlank(code)) {
            Kv data = Kv.by("errcode", 500).set("errmsg", "code is blank");
            renderJson(data);
            return;
        }

        try {
            WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
            this.logger.info(session.getSessionKey());
            this.logger.info(session.getOpenid());
            this.logger.info(session.getExpiresin().toString());
            renderJson(JsonUtils.toJson(session));
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            Kv data = Kv.by("errcode", 500).set("errmsg", "根据jsCode获取用户信息失败");
            renderJson(data);
            return;
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    public void info() {
        String sessionKey = getPara("sessionKey"); 
        String signature = getPara("signature"); 
        String rawData = getPara("rawData"); 
        String encryptedData = getPara("encryptedData"); 
        String iv = getPara("iv");
        // 用户信息校验
        if (!this.wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            Kv data = Kv.by("errcode", 500).set("errmsg", "校验用户失败");
            renderJson(data);
            return;
        }
        // 解密用户信息
        WxMaUserInfo userInfo = this.wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        renderJson(JsonUtils.toJson(userInfo));
    }
    
    /**
     * 生成小程序二维码
     */
    public void createWxaQrcode() {
        String path = getPara("path");
        if (StringUtils.isEmpty(path)) {
            Kv data = Kv.by("errcode", 500).set("errmsg", "path为空");
            renderJson(data);
            return;
        }
        //
        String flag = "openid=";
        String openid = path.substring(path.indexOf(flag) + flag.length());
        //
        Kv result = Kv.ok();
        try {
            File createQrcode = wxMaQrcodeService.createQrcode(path,340);
            //
            String createQrcodeName = createQrcode.getName();
            String extension = createQrcodeName.substring(createQrcodeName.lastIndexOf("."), createQrcodeName.length());
            String fileName = openid + extension;
            File targetDir = new File(PathKit.getWebRootPath() + "/qrcode/");
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            File target = new File(targetDir, fileName);
            if (!target.exists()) {
                target.createNewFile();
            }
            //
            FileInputStream in = new FileInputStream(createQrcode);
            FileOutputStream fos = new FileOutputStream(target);
            byte[] b = new byte[1024];
            int nRead = 0;
            while ((nRead = in.read(b)) != -1) {
                fos.write(b, 0, nRead);
            }
            fos.flush();
            fos.close();
            in.close();
            //
            String url = "qrcode/" + fileName;
            //
            BioUserImg img = new BioUserImg();
            img.setOpenid(openid);
            img.setUrl(url);
            img.setType("qrcode");
            ModelUtils.fillForSave(img);
            boolean save = img.save();
            //
            BioUserInfo user = BioUserInfo.dao.findFirst("select * from bio_user_info where openid=?",openid);
            if (user != null) {
                user.setBioCardWxaQrcode(url);
                ModelUtils.fillForUpdate(user);
                boolean update = user.update();
            }
            //
            result.set("url", url);
            result.set("save", save);
        } catch (FileNotFoundException e) {
            Kv data = Kv.by("errcode", 500).set("errmsg", "创建文件错误，请稍后再再试");
            renderJson(data);
            return;
        } catch (IOException e) {
            Kv data = Kv.by("errcode", 500).set("errmsg", "文件写入服务器出现错误，请稍后再再试");
            renderJson(data);
            return;
        } catch (WxErrorException e) {
            Kv data = Kv.by("errcode", 500).set("errmsg", "生成二维码失败，请稍后再再试");
            renderJson(data);
            return;
        }
        renderJson(result);
    }

}
