package com.king.bio.web.controller.api.bio;

import com.king.bio.common.model.BioFeedback;
import com.king.bio.util.ModelUtils;
import com.king.bio.web.controller.base.BaseController;

public class APIFeedbackController extends BaseController {
    /**
     * 反馈
     */
    public void save() {
        BioFeedback model = getModel(BioFeedback.class, "bean");
        ModelUtils.fillForSave(model);
        model.save();
        renderDWZSuccessJson("反馈成功！");
    }
}
