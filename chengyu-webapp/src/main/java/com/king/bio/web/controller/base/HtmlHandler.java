package com.king.bio.web.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class HtmlHandler extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        if (target.toLowerCase().endsWith(".html")) {
            request.setAttribute("html", target);
            target = "/jhtml";
        }
        next.handle(target, request, response, isHandled);
    }

}
