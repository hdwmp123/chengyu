package com.king.bio.web.route;

import com.jfinal.config.Routes;
import com.king.bio.web.controller.api.bio.APIBioFavoritesController;
import com.king.bio.web.controller.api.bio.APIBioUserinfoController;
import com.king.bio.web.controller.api.bio.APIFeedbackController;
import com.king.bio.web.controller.base.HtmlController;
import com.king.bio.web.controller.miniapp.jfi.WxaUserController;
import com.king.bio.web.controller.miniapp.wjt.WxMaPortalController;
import com.king.bio.web.controller.miniapp.wjt.WxMaUserController;
import com.king.bio.web.controller.sys.BioFavoritesController;
import com.king.bio.web.controller.sys.BioUserinfoController;
import com.king.bio.web.controller.sys.IndexController;
import com.king.bio.web.controller.sys.SysUserinfoController;

public class RouteConfigBio {

    public static void config(Routes me) {
        me.add("/", IndexController.class);
        me.add("/jhtml", HtmlController.class);
        
        me.add("/sys/user", SysUserinfoController.class);
        
        me.add("/bio/user", BioUserinfoController.class);
        me.add("/bio/favorites", BioFavoritesController.class);
        
        me.add("/bio_api/user", APIBioUserinfoController.class);
        me.add("/bio_api/favorites", APIBioFavoritesController.class);
        me.add("/bio_api/feedback", APIFeedbackController.class);

        me.add("/applet_api/api", WxaUserController.class);
        
        me.add("/wechat/portal", WxMaPortalController.class);
        me.add("/wechat/user", WxMaUserController.class);
    }
}
