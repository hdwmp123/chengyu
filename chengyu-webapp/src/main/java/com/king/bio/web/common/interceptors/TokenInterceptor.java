package com.king.bio.web.common.interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.log.Log4jLog;
import com.king.bio.web.controller.base.BaseController;

public class TokenInterceptor implements Interceptor {
    final static Log log = Log4jLog.getLog(TokenInterceptor.class);
    public void intercept(Invocation ai) {
        String controllerKey = ai.getControllerKey();
        if (controllerKey.contains("api")) {
            Controller controller = ai.getController();
            String bioToken = controller.getPara("bioToken");
            String bioTokenProp = PropKit.get("bioToken");
            System.out.println("bioToken#" + bioToken);
            System.out.println("bioTokenProp#" + bioTokenProp);
            if (StrKit.isBlank(bioToken)) {
                ((BaseController)controller).renderDWZErrorJson("请求失败,缺少bioToken参数");
                return;
            }
            if (!bioTokenProp.equals(bioToken)) {
                ((BaseController)controller).renderDWZErrorJson("请求失败,bioToken参数校验错误");
                return;
            }
        }
        ai.invoke();
    }
}
