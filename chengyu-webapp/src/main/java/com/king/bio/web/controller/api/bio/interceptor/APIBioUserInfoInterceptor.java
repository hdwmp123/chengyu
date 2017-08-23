package com.king.bio.web.controller.api.bio.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class APIBioUserInfoInterceptor implements Interceptor {

    public void intercept(Invocation inv) {
        System.out.println("Before invoking " + inv.getActionKey());
        inv.invoke();
        System.out.println("After invoking " + inv.getActionKey());
    }

}
