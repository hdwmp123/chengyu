package com.king.bio.web.controller.sys;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.king.bio.common.model.SysUserInfo;
import com.king.bio.util.ChristStringUtil;
import com.king.bio.util.ModelUtils;
import com.king.bio.web.controller.base.BaseController;

public class SysUserinfoController extends BaseController {
    public void index() {
        StringBuffer whee = new StringBuffer();
        List<Object> param = new ArrayList<Object>();
        String tag = getPara("tag");
        if (StrKit.notBlank(tag)) {
            whee.append(" and tag like ?");
            param.add("%" + tag + "%");
        }
        whee.append(" order by create_date desc ");
        Page<SysUserInfo> pager = SysUserInfo.dao.paginate(
                getParaToInt("pager.pageNumber", 1),
                getParaToInt("pager.pageSize", 20), "select * ",
                " from sys_user_info where 1=1 " + whee.toString(),
                param.toArray());

        setAttr("pager", pager);
        render("list.html");
    }

    public void add() {
        render("input.html");
    }

    public void checkIsNotExist() {
        renderJson(checkIsNotExist("sys_user_info", "username",
                getPara("user.username")));
    }

    public void save() {
        SysUserInfo model = getModel(SysUserInfo.class, "user");
        String pwd = model.get("pwd");
        String md5Pwd = ChristStringUtil.md5(pwd);
        model.set("pwd", md5Pwd);
        model.set("is_system", false);
        StringBuffer tag = new StringBuffer();
        tag.append(";").append(model.getStr("username")).append(",");
        String realName = model.get("realname");
        if (ChristStringUtil.isNotEmpty(realName)) {
            tag.append(";").append(realName).append(",");
        }
        model.set("tag", tag.toString());
        ModelUtils.fillForSave(model);
        model.save();
        renderDWZSuccessJson("创建用户成功！");
    }

    public void edit() {
        setAttr("user", SysUserInfo.dao.findById(getPara("id")));
        render("input.html");
    }

    public void update() {
        String id = getPara("id");
        SysUserInfo model = getModel(SysUserInfo.class, "user");
        model.set("id", id);
        StringBuffer tag = new StringBuffer();
        tag.append(";").append(model.getStr("username")).append(",");
        String realName = model.get("realname");
        if (ChristStringUtil.isNotEmpty(realName)) {
            tag.append(";").append(realName).append(",");
        }
        model.set("tag", tag.toString());
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
            SysUserInfo bioUserInfo = SysUserInfo.dao.findById(id);
            if (bioUserInfo.getBoolean("is_system") == true) {
                if (noDeleteBuffer.length() == 0) {
                    noDeleteBuffer.append(bioUserInfo.get("username"));
                } else {
                    noDeleteBuffer.append(",").append(
                            bioUserInfo.get("username"));
                }
                noDeleteCount++;
            } else {
                bioUserInfo.delete();
                deleteCount++;
            }
        }
        renderDWZSuccessJson("操作成功，删除成功" + deleteCount + "个，失败" + noDeleteCount
                + "个，删除失败的用户名：" + noDeleteBuffer.toString());
    }

    public void toUpdatePassword() {
        setAttr("user", SysUserInfo.dao.findById(getPara("id")));
        render("updatePassword.html");
    }

    public void updatePassword() {
        String id = getPara("id");
        String pwd = getPara("user.pwd");
        String md5Pwd = ChristStringUtil.md5(pwd);
        SysUserInfo.dao.findById(id).set("pwd", md5Pwd).update();
        renderDWZSuccessJson("修改密码成功！");
    }

}
