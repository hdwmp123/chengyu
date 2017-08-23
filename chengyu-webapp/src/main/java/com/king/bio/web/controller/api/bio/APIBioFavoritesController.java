package com.king.bio.web.controller.api.bio;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.log.Log4jLog;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.king.bio.common.model.BioFavorites;
import com.king.bio.util.ModelUtils;
import com.king.bio.web.controller.api.bio.service.APIBioFavoritesService;
import com.king.bio.web.controller.base.BaseController;

public class APIBioFavoritesController extends BaseController {
    final static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();
    final static Log log = Log4jLog.getLog(APIBioFavoritesController.class);
    final static APIBioFavoritesService SERVICE = new APIBioFavoritesService();

    /**
     * 收藏者列表
     */
    public void index() {
        StringBuffer whee = new StringBuffer();
        List<Object> param = new ArrayList<Object>();
        String openid = getPara("openid");
        if (StrKit.notBlank(openid)) {
            param.add(openid);
            param.add(openid);
            param.add(openid);
            param.add(openid);
        } else {
            renderDWZErrorJson("openid不能为空");
            return;
        }
        String ig_code = getPara("ig_code");
        if (StrKit.notBlank(ig_code)) {
            whee.append(" and temp.ig_code like ? ");
            param.add("%" + ig_code + "%");
        }
        whee.append(" order by temp.flag desc ");
        Page<Record> pager = Db
                .paginate(
                        getParaToInt("pager.pageNumber", 1),
                        getParaToInt("pager.pageSize", 20),
                        "select * ",
                        " from (SELECT "+
                        "   bf.fa_openid, "+
                        "   bui.avatar_url, "+
                        "   bui.ig_code, "+
                        "   bui.ig_camp, "+
                        "   bf.id AS bf_id, "+
                        "   ( "+
                        "       SELECT "+
                        "           count(1) "+
                        "       FROM "+
                        "           bio_favorites bff "+
                        "       WHERE "+
                        "           bff.fa_openid = ? "+
                        "       AND bff.openid = bf.fa_openid "+
                        "       AND bff.is_delete = 0 "+
                        "   ) flag "+
                        "FROM "+
                        "   bio_favorites bf, "+
                        "   bio_user_info bui "+
                        "WHERE "+
                        "   1 = 1 "+
                        "AND bf.is_delete = 0 "+
                        "AND bf.fa_openid = bui.openid "+
                        "AND bf.openid = ? "+
                        "UNION ALL "+
                        "   SELECT "+
                        "       bf.openid fa_openid, "+
                        "       bui.avatar_url, "+
                        "       bui.ig_code, "+
                        "       bui.ig_camp, "+
                        "       bf.id AS bf_id, "+
                        "       -1 "+
                        "   FROM "+
                        "       bio_favorites bf, "+
                        "       bio_user_info bui "+
                        "   WHERE "+
                        "       1 = 1 "+
                        "   AND NOT EXISTS ( "+
                        "       SELECT "+
                        "           1 "+
                        "       FROM "+
                        "           bio_favorites bff "+
                        "       WHERE "+
                        "           bff.openid = ? "+
                        "       AND bff.fa_openid = bf.openid "+
                        "       AND bff.is_delete = 0 "+
                        "   ) "+
                        "   AND bf.is_delete = 0 "+
                        "   AND bf.openid = bui.openid "+
                        "   AND bf.fa_openid = ? ) temp " + whee.toString(), param.toArray());
        setAttr("pager", pager);
        renderJson(pager);
    }

    /**
     * 收藏
     */
    public void add() {
        BioFavorites model = getModel(BioFavorites.class, "bean");
        boolean checkExists = SERVICE.checkExists(model);
        if (checkExists) {
            renderDWZErrorJson("已经收藏该用户!");
            return;
        }
        ModelUtils.fillForSave(model);
        model.setIsDelete(0);
        model.setIsNew(0);
        model.save();
        renderDWZSuccessJson("收藏用户成功!");
    }

    /**
     * 取消收藏
     */
    public void remove() {
        Long id = getParaToLong("id");
        if (id == null || id == 0) {
            renderDWZErrorJson("ID 不能为空!");
            return;
        }
        BioFavorites model = new BioFavorites();
        model.setId(id);
        model.setIsDelete(1);
        ModelUtils.fillForUpdate(model);
        model.update();
        renderDWZSuccessJson("取消收藏成功!");
    }
}
