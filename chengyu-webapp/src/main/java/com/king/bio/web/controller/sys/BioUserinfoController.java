package com.king.bio.web.controller.sys;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.king.bio.common.model.BioUserInfo;
import com.king.bio.util.ModelUtils;
import com.king.bio.web.controller.base.BaseController;

public class BioUserinfoController extends BaseController {
    public void index() {
        StringBuffer whee = new StringBuffer();
        List<Object> param = new ArrayList<Object>();
        String ig_code = getPara("ig_code");
        if (StrKit.notBlank(ig_code)) {
            whee.append(" and ig_code like ?");
            param.add("%" + ig_code + "%");
        }
        String ig_camp = getPara("ig_camp");
        if (StrKit.notBlank(ig_camp)) {
            whee.append(" and ig_camp = ?");
            param.add(ig_camp);
        }
        whee.append(" order by create_date desc ");
        Page<BioUserInfo> pager = BioUserInfo.dao.paginate(
                getParaToInt("pager.pageNumber", 1),
                getParaToInt("pager.pageSize", 20), "select * ",
                " from bio_user_info where 1=1 " + whee.toString(),
                param.toArray());

        setAttr("pager", pager);
        setAttr("ig_code", ig_code);
        setAttr("ig_camp", ig_camp);
        render("list.html");
    }

    public void add() {
        render("input.html");
    }

    public void checkIsNotExist() {
        renderJson(checkIsNotExist("bio_user_info", "username",
                getPara("userInfo.username")));
    }

    public void save() {
        BioUserInfo model = getModel(BioUserInfo.class, "userInfo");
        ModelUtils.fillForSave(model);
        model.save();
        renderDWZSuccessJson("创建用户成功！");
    }

    public void edit() {
        setAttr("userInfo", BioUserInfo.dao.findById(getPara("id")));
        render("../input.html");
    }

    public void update() {
        String id = getPara("id");
        BioUserInfo model = getModel(BioUserInfo.class, "userInfo");
        model.set("id", id);
        ModelUtils.fillForUpdate(model);
        model.update();
        renderDWZSuccessJson("修改用户成功！");
    }

    public void delete() {
        String[] ids = getParaValues("ids");
        StringBuffer noDeleteBuffer = new StringBuffer();
        int noDeleteCount = 0;
        int deleteCount = 0;
        for (String id : ids) {
            BioUserInfo bioUserInfo = BioUserInfo.dao.findById(id);
            bioUserInfo.delete();
            deleteCount++;
        }
        renderDWZSuccessJson("操作成功，删除成功" + deleteCount + "个，失败" + noDeleteCount
                + "个，删除失败的用户名：" + noDeleteBuffer.toString());
    }
}
