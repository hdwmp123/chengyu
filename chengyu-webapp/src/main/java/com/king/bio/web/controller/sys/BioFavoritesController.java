package com.king.bio.web.controller.sys;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.king.bio.common.model.BioUserInfo;
import com.king.bio.web.controller.base.BaseController;

public class BioFavoritesController extends BaseController {
    public void index() {
        StringBuffer whee = new StringBuffer();
        List<Object> param = new ArrayList<Object>();
        String ig_code = getPara("ig_code");
        if (StrKit.notBlank(ig_code)) {
            whee.append(" and bui_a.ig_code like ?");
            param.add("%" + ig_code + "%");
            setAttr("ig_code", ig_code);
        }
        
        String ig_code2 = getPara("ig_code2");
        if (StrKit.notBlank(ig_code2)) {
            whee.append(" and bui_b.ig_code like ?");
            param.add("%" + ig_code2 + "%");
            setAttr("ig_code2", ig_code2);
        }
        whee.append(" order by bf.create_date desc ");
        Page<Record> pager = Db.paginate(
                getParaToInt("pager.pageNumber", 1),
                getParaToInt("pager.pageSize", 20),
                "select "
                + "bui_a.openid a_openid,"
                + "bui_a.nick_name a_nick_name,"
                + "bui_a.avatar_url a_avatar_url,"
                + "bui_a.ig_code a_ig_code,"
                + "bui_a.ig_camp a_ig_camp,"
                
                + "bui_b.openid b_openid,"
                + "bui_b.nick_name b_nick_name,"
                + "bui_b.avatar_url b_avatar_url,"
                + "bui_b.ig_code b_ig_code,"
                + "bui_b.ig_camp b_ig_camp,"
                
                + "bf.id as bf_id ",
                " from "
                + "bio_favorites bf,"
                + "bio_user_info bui_a,"
                + "bio_user_info bui_b"
                + " where 1=1 "
                + "and bf.is_delete=0 "
                + "and bf.openid=bui_a.openid "
                + "and bf.fa_openid=bui_b.openid "
                + whee.toString(), param.toArray());

        setAttr("pager", pager);
        render("list.html");
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
