package com.king.bio.web.controller.base;

import java.io.File;

import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.StrKit;

public class HtmlController extends Controller {
    public void index() {

        String html = getAttrForStr("html");
        if (StrKit.notBlank(html) && new File(JFinal.me().getServletContext().getRealPath(html)).exists()) {
            render(html);
        } else {
            // 不存在404
            redirect("/static/html/404.html");
        }
    }
}
