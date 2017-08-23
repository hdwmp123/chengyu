/*
Navicat MySQL Data Transfer

Source Server         : localhost_root_root
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : bio_card

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-06-13 01:52:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bio_favorites
-- ----------------------------
DROP TABLE IF EXISTS `bio_favorites`;
CREATE TABLE `bio_favorites` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openid` varchar(32) DEFAULT NULL COMMENT '当前用户openid',
  `fa_openid` varchar(32) DEFAULT NULL COMMENT '收藏用户openid',
  `is_new` int(1) DEFAULT '0' COMMENT '0未读1已读',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '0未删除1删除',
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bio_feedback
-- ----------------------------
DROP TABLE IF EXISTS `bio_feedback`;
CREATE TABLE `bio_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openid` varchar(32) DEFAULT NULL COMMENT '当前用户openid',
  `contact` varchar(255) DEFAULT NULL COMMENT '联系方式',
  `remark` varchar(400) DEFAULT NULL COMMENT '消息体',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '0未删除1删除',
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bio_user_info
-- ----------------------------
DROP TABLE IF EXISTS `bio_user_info`;
CREATE TABLE `bio_user_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(32) DEFAULT NULL COMMENT 'openid',
  `nick_name` varchar(100) DEFAULT NULL COMMENT '微信昵称',
  `gender` varchar(100) DEFAULT NULL COMMENT '性别',
  `language` varchar(100) DEFAULT NULL COMMENT '语言',
  `country` varchar(100) DEFAULT NULL COMMENT '国家',
  `province` varchar(100) DEFAULT NULL COMMENT '省(直辖市)',
  `city` varchar(100) DEFAULT NULL COMMENT '城市(区)',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像',
  `ig_code` varchar(100) DEFAULT NULL COMMENT 'Ingress Code',
  `ig_camp` varchar(5) DEFAULT NULL COMMENT 'Ingress 阵营',
  `remark` varchar(255) DEFAULT NULL COMMENT '简介',
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '记录编号',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `modify_date` datetime DEFAULT NULL COMMENT '修改日期',
  `name` varchar(32) DEFAULT NULL COMMENT '名称',
  `description` varchar(128) DEFAULT NULL COMMENT '描述',
  `parent_id` bigint(20) DEFAULT '-1' COMMENT '主订单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Table structure for sys_role_moudle
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_moudle`;
CREATE TABLE `sys_role_moudle` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '记录编号',
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色ID',
  `username` varchar(32) DEFAULT NULL COMMENT '用户userName',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `modify_date` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色用户关联表';

-- ----------------------------
-- Table structure for sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '记录编号',
  `username` varchar(32) DEFAULT NULL COMMENT '用户登录名',
  `realname` varchar(12) DEFAULT NULL COMMENT '真实姓名',
  `pwd` varchar(32) DEFAULT NULL COMMENT '登录密码',
  `mobile` varchar(32) DEFAULT NULL COMMENT '移动电话号码',
  `email` varchar(32) DEFAULT NULL COMMENT '邮件',
  `qq` varchar(32) DEFAULT NULL COMMENT 'QQ',
  `tel` varchar(32) DEFAULT NULL COMMENT '电话',
  `memo` varchar(64) DEFAULT NULL COMMENT '备注',
  `inuse` int(11) DEFAULT NULL COMMENT '是否停用',
  `tag` varchar(255) DEFAULT NULL,
  `is_system` bit(1) DEFAULT NULL COMMENT '是否系统默认',
  `parent_id` bigint(20) DEFAULT '-1' COMMENT '主订单id',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `modify_date` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_sys_userinfo_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户信息';
