package com.king.bio.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysRoleUser<M extends BaseSysRoleUser<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public M setRoleId(java.lang.String roleId) {
		set("role_id", roleId);
		return (M)this;
	}

	public java.lang.String getRoleId() {
		return get("role_id");
	}

	public M setUsername(java.lang.String username) {
		set("username", username);
		return (M)this;
	}

	public java.lang.String getUsername() {
		return get("username");
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
