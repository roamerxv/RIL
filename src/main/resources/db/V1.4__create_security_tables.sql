CREATE TABLE IF NOT EXISTS `ril`.`ril_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `avatar` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reset_token` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT 0,
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_username` (`username`),
  UNIQUE KEY `UK_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT = '用户表';

CREATE TABLE IF NOT EXISTS `ril`.`ril_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `available` bit(1) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_role` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT = '角色表';

CREATE TABLE IF NOT EXISTS `ril`.`ril_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `available` bit(1) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `parent_ids` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `permission` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `resource_type` enum('menu','button') COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_permission` (`permission`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT = '权限表';


CREATE TABLE IF NOT EXISTS `ril`.`ril_role_permission` (
  `permission_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FKajgd9p4wmj2y3dne8cijel0ci` (`role_id`),
  KEY `FK22shhycv7ryca901q56ye0f2s` (`permission_id`),
  CONSTRAINT `FK22shhycv7ryca901q56ye0f2s` FOREIGN KEY (`permission_id`) REFERENCES `ril_permission` (`id`),
  CONSTRAINT `FKajgd9p4wmj2y3dne8cijel0ci` FOREIGN KEY (`role_id`) REFERENCES `ril_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT = '角色权限关系表';

CREATE TABLE IF NOT EXISTS `ril`.`ril_user_role` (
  `uid` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FKfofx4d6eppk26gqtt6k1o4h35` (`role_id`),
  KEY `FKhl4xngfgo8rlybirvkvg8n1vl` (`uid`),
  CONSTRAINT `FKfofx4d6eppk26gqtt6k1o4h35` FOREIGN KEY (`role_id`) REFERENCES `ril_role` (`id`),
  CONSTRAINT `FKhl4xngfgo8rlybirvkvg8n1vl` FOREIGN KEY (`uid`) REFERENCES `ril_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT = '用户角色关系表';


