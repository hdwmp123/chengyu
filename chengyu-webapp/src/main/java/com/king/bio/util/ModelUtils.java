package com.king.bio.util;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.plugin.activerecord.Model;

public class ModelUtils {
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public static void fillForSave(Model m) {
        m.set("create_date", new Date());
        m.set("modify_date", new Date());
        System.out.println("BEAN.fillForSave # "+gson.toJson(m));
    }

    public static void fillForUpdate(Model m) {
        m.set("modify_date", new Date());
        System.out.println("BEAN.fillForUpdate # "+gson.toJson(m));
    }
}
