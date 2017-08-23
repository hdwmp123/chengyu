alter table bio_user_info add COLUMN bio_card_front varchar(255) COMMENT 'BioCard正面' after avatar_url;
alter table bio_user_info add COLUMN bio_card_back varchar(255) COMMENT 'BioCard背面' after bio_card_front;
alter table bio_user_info add COLUMN bio_card_wxa_qrcode varchar(255) COMMENT 'BioCard小程序二维码' after bio_card_front;

CREATE TABLE `bio_user_share` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(32) DEFAULT NULL COMMENT 'openid',
  `opengid` varchar(32) DEFAULT NULL COMMENT '微信群ID',
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `bio_user_img` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(32) DEFAULT NULL COMMENT 'openid',
  `url` varchar(255) DEFAULT NULL COMMENT '头像',
  `type` varchar(20) DEFAULT NULL COMMENT 'Ingress 阵营',
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;


