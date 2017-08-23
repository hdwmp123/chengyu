package com.king.bio.web.controller.api.bio.service;

import com.king.bio.common.model.BioFavorites;
import com.king.bio.common.model.MappingKit;

public class APIBioFavoritesService {
    public static final BioFavorites dao = new BioFavorites().dao();

    public boolean checkExists(BioFavorites bean) {
        BioFavorites findFirst = dao.findFirst("select * from " + MappingKit.TABLE_bio_favorites
                + " where openid=? and fa_openid=? and is_delete=?",
                bean.getOpenid(), bean.getFaOpenid(), 0);
        
        return findFirst!=null;
    }
}
