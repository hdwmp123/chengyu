package com.king.bio.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseBioUserInfo<M extends BaseBioUserInfo<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public M setOpenid(java.lang.String openid) {
		set("openid", openid);
		return (M)this;
	}

	public java.lang.String getOpenid() {
		return get("openid");
	}

	public M setNickName(java.lang.String nickName) {
		set("nick_name", nickName);
		return (M)this;
	}

	public java.lang.String getNickName() {
		return get("nick_name");
	}

	public M setGender(java.lang.String gender) {
		set("gender", gender);
		return (M)this;
	}

	public java.lang.String getGender() {
		return get("gender");
	}

	public M setLanguage(java.lang.String language) {
		set("language", language);
		return (M)this;
	}

	public java.lang.String getLanguage() {
		return get("language");
	}

	public M setCountry(java.lang.String country) {
		set("country", country);
		return (M)this;
	}

	public java.lang.String getCountry() {
		return get("country");
	}

	public M setProvince(java.lang.String province) {
		set("province", province);
		return (M)this;
	}

	public java.lang.String getProvince() {
		return get("province");
	}

	public M setCity(java.lang.String city) {
		set("city", city);
		return (M)this;
	}

	public java.lang.String getCity() {
		return get("city");
	}

	public M setAvatarUrl(java.lang.String avatarUrl) {
		set("avatar_url", avatarUrl);
		return (M)this;
	}

	public java.lang.String getAvatarUrl() {
		return get("avatar_url");
	}

	public M setBioCardFront(java.lang.String bioCardFront) {
		set("bio_card_front", bioCardFront);
		return (M)this;
	}

	public java.lang.String getBioCardFront() {
		return get("bio_card_front");
	}

	public M setBioCardWxaQrcode(java.lang.String bioCardWxaQrcode) {
		set("bio_card_wxa_qrcode", bioCardWxaQrcode);
		return (M)this;
	}

	public java.lang.String getBioCardWxaQrcode() {
		return get("bio_card_wxa_qrcode");
	}

	public M setBioCardBack(java.lang.String bioCardBack) {
		set("bio_card_back", bioCardBack);
		return (M)this;
	}

	public java.lang.String getBioCardBack() {
		return get("bio_card_back");
	}

	public M setIgCode(java.lang.String igCode) {
		set("ig_code", igCode);
		return (M)this;
	}

	public java.lang.String getIgCode() {
		return get("ig_code");
	}

	public M setIgCamp(java.lang.String igCamp) {
		set("ig_camp", igCamp);
		return (M)this;
	}

	public java.lang.String getIgCamp() {
		return get("ig_camp");
	}

	public M setRemark(java.lang.String remark) {
		set("remark", remark);
		return (M)this;
	}

	public java.lang.String getRemark() {
		return get("remark");
	}

	public M setCreateDate(java.util.Date createDate) {
		set("create_date", createDate);
		return (M)this;
	}

	public java.util.Date getCreateDate() {
		return get("create_date");
	}

	public M setModifyDate(java.util.Date modifyDate) {
		set("modify_date", modifyDate);
		return (M)this;
	}

	public java.util.Date getModifyDate() {
		return get("modify_date");
	}

}
