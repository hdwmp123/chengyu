package com.king.bio.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("bio_favorites", "id", BioFavorites.class);
		arp.addMapping("bio_feedback", "id", BioFeedback.class);
		arp.addMapping("bio_user_img", "id", BioUserImg.class);
		arp.addMapping("bio_user_info", "id", BioUserInfo.class);
		arp.addMapping("bio_user_share", "id", BioUserShare.class);
		arp.addMapping("sys_role", "id", SysRole.class);
		arp.addMapping("sys_role_moudle", "id", SysRoleMoudle.class);
		arp.addMapping("sys_role_user", "id", SysRoleUser.class);
		arp.addMapping("sys_user_info", "id", SysUserInfo.class);
	}
}
