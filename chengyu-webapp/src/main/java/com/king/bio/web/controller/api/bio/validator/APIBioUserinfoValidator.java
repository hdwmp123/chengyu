package com.king.bio.web.controller.api.bio.validator;

import com.jfinal.core.Controller;
import com.king.bio.web.controller.base.BaseValidator;

public class APIBioUserinfoValidator extends BaseValidator {

    @Override
    protected void validate(Controller c) {
        //validateRequiredString("bean.openid","openid","openid参数缺失");
        //validateRequiredString("bean.openid","openid","openid参数缺失");
    }

    @Override
    protected void handleError(Controller c) {
    }

}
