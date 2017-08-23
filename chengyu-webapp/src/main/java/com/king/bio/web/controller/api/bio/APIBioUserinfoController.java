package com.king.bio.web.controller.api.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.aop.Before;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import com.king.bio.common.model.BioUserImg;
import com.king.bio.common.model.BioUserInfo;
import com.king.bio.common.model.BioUserShare;
import com.king.bio.common.model.MappingKit;
import com.king.bio.util.ModelUtils;
import com.king.bio.web.controller.api.bio.validator.APIBioUserinfoValidator;
import com.king.bio.web.controller.base.BaseController;

//@Before(APIBioUserInfoInterceptor.class)
public class APIBioUserinfoController extends BaseController {
    public static Gson gson = new GsonBuilder().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();

    /**
     * 根据openid检测是否已经存在
     */
    @Before(APIBioUserinfoValidator.class)
    public void checkExistByOpenId() {
        Map<String, Object> result = new HashMap<String, Object>();
        String openid = getPara("openid");
        if (StringUtils.isEmpty(openid)) {
            result.put("exist", false);
            renderJson(result);
            return;
        }
        BioUserInfo bean = BioUserInfo.dao.findFirst("select * from "
                + MappingKit.TABLE_bio_user_info + " where openid = ?", openid);
        result.put("exist", bean != null);
        if (bean != null) {
            result.put("bean", bean);
        }
        renderJson(result);
    }

    /**
     * 保存
     */
    public void save() {
        BioUserInfo model = getModel(BioUserInfo.class, "bean");
        Long id = model.getLong("id");
        if (id != null && id > 0) {
            this.update();
            return;
        }
        ModelUtils.fillForSave(model);
        System.out.println("BEAN#" + gson.toJson(model));
        model.save();
        renderDWZSuccessJson("创建用户成功！");
    }

    /**
     * 编辑
     */
    public void edit() {
        renderJson(BioUserInfo.dao.findById(getPara("id")));
    }

    /**
     * 更新
     */
    public void update() {
        BioUserInfo model = getModel(BioUserInfo.class, "bean");
        Long id = model.getLong("id");
        if (id == null || id == 0) {
            this.save();
            return;
        }
        ModelUtils.fillForUpdate(model);
        model.update();
        renderDWZSuccessJson("修改用户成功！");
    }

    /**
     * 上传图片
     */
    public void uploadImage() {
        String path = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        UploadFile file = getFile("imgFile", PathKit.getWebRootPath()
                + File.separator + "upload");
        File source = file.getFile();
        String fileName = file.getFileName();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String prefix;
        if (".png".equals(extension) || ".jpg".equals(extension)
                || ".gif".equals(extension)) {
            prefix = "img";
            fileName = generateWord() + extension;
        } else {
            prefix = "file";
        }
        JSONObject json = new JSONObject();
        try {
            FileInputStream fis = new FileInputStream(source);
            File targetDir = new File(PathKit.getWebRootPath() + "/upload/"
                    + prefix + "/" + path);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            File target = new File(targetDir, fileName);
            if (!target.exists()) {
                target.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(target);
            byte[] bts = new byte[300];
            while (fis.read(bts, 0, 300) != -1) {
                fos.write(bts, 0, 300);
            }
            fos.close();
            fis.close();
            //
            String url = "upload/" + prefix + "/" + path + "/" + fileName;
            String openid = getPara("openid");
            String type = getPara("type");
            BioUserImg img = new BioUserImg();
            img.setOpenid(openid);
            img.setUrl(url);
            img.setType(type);
            boolean save = img.save();
            //
            json.put("url", url);
            json.put("save", save);
            source.delete();
        } catch (FileNotFoundException e) {
            renderDWZErrorJson("上传出现错误，请稍后再上传");
            return;
        } catch (IOException e) {
            renderDWZErrorJson("文件写入服务器出现错误，请稍后再上传");
            return;
        }
        renderDWZSuccessJson(json.toJSONString());
    }

    private String generateWord() {
        String[] beforeShuffle = new String[] { "2", "3", "4", "5", "6", "7",
                "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z" };
        List<String> list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(5, 9);
        return result;
    }

    /**
     * 保存分享信息
     */
    public void saveShareInfo() {
        BioUserShare model = getModel(BioUserShare.class, "bean");
        Long id = model.getLong("id");
        if (id != null && id > 0) {
            this.update();
            return;
        }
        ModelUtils.fillForSave(model);
        System.out.println("BEAN#" + gson.toJson(model));
        model.save();
        renderDWZSuccessJson("保存分享信息成功！");
    }
}
