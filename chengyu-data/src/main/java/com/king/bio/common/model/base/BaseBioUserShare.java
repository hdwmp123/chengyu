package com.king.bio.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseBioUserShare<M extends BaseBioUserShare<M>> extends Model<M> implements IBean {

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

	public M setOpengid(java.lang.String opengid) {
		set("opengid", opengid);
		return (M)this;
	}

	public java.lang.String getOpengid() {
		return get("opengid");
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