
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for business_log
-- ----------------------------
DROP TABLE IF EXISTS `business_log`;
CREATE TABLE `business_log` (
  `id` varchar(40) CHARACTER SET utf8 NOT NULL,
  `operator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `clazz` varchar(255) CHARACTER SET utf8 NOT NULL,
  `method` varchar(255) CHARACTER SET utf8 NOT NULL,
  `method_description` varchar(255) CHARACTER SET utf8 NOT NULL,
  `success` tinyint(4) NOT NULL COMMENT '方法是否成功运行',
  `exception_string` text COLLATE utf8_bin COMMENT '方法运行出错，抛出的exception堆栈转换成的string',
  `args` text CHARACTER SET utf8 NOT NULL,
  `time_consuming` bigint(20) NOT NULL DEFAULT '0' COMMENT '方法调用耗时（毫秒）',
  `remote_ip` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `client_os` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `client_browser` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `browser_version` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `client_device_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='业务方法调用日志';


-- ----------------------------
-- Table structure for event
-- ----------------------------
DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `name` varchar(256) COLLATE utf8_bin NOT NULL,
  `start` datetime NOT NULL COMMENT '开始时间',
  `end` datetime NOT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='事件信息表';

-- ----------------------------
-- Table structure for ril_permission
-- ----------------------------
DROP TABLE IF EXISTS `ril_permission`;
CREATE TABLE `ril_permission` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `available` bit(1) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `parent_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `resource_type` enum('menu','button') CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `method` enum('ALL','GET','POST','PUT','DELETE') CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'ALL',
  `clazz` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '菜单项的 class',
  `order_num` int(11) NOT NULL DEFAULT '0' COMMENT '菜单项的排序序号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_permission` (`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='权限表';

-- ----------------------------
-- Records of ril_permission
-- ----------------------------
BEGIN;
INSERT INTO `ril_permission` VALUES ('100', '2017-12-09 06:06:07', '2017-12-11 13:37:55', b'0', '系统日志1', '0', NULL, 'sys:log', NULL, '/system_logs_index', 'GET', 'flaticon-line-graph', 0);
COMMIT;

-- ----------------------------
-- Table structure for ril_role
-- ----------------------------
DROP TABLE IF EXISTS `ril_role`;
CREATE TABLE `ril_role` (
  `id` varchar(36) NOT NULL,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `available` bit(1) DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_role` (`role`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

-- ----------------------------
-- Records of ril_role
-- ----------------------------
BEGIN;
INSERT INTO `ril_role` VALUES ('1', '2017-12-07 15:20:33', NULL, b'1', '管理员', 'ROLE_ADMIN');
INSERT INTO `ril_role` VALUES ('2', '2017-12-07 15:20:33', NULL, b'1', '普通会员', 'ROLE_USER');
COMMIT;

-- ----------------------------
-- Table structure for ril_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `ril_role_permission`;
CREATE TABLE `ril_role_permission` (
  `permission_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `role_id` varchar(36)  NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色权限关系表';

-- ----------------------------
-- Table structure for ril_user
-- ----------------------------
DROP TABLE IF EXISTS `ril_user`;
CREATE TABLE `ril_user` (
  `id` varchar(36)NOT NULL ,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `avatar` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `reset_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_username` (`username`),
  UNIQUE KEY `UK_email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

-- ----------------------------
-- Records of ril_user
-- ----------------------------
BEGIN;
INSERT INTO `ril_user` VALUES ('1', '2017-12-07 13:35:53', '2017-12-09 08:50:45', 'admin@laic1.com', '管理员', '$2a$10$0GVOrjGC.VNjujZRK007/OirSOvwKIOEq44EbK2T0vKokKXXvY7vq', '1334b81229a2ba20adf2302f630b1019', NULL, 1, 'admin');
INSERT INTO `ril_user` VALUES ('2', '2017-12-07 15:36:09', NULL, 'duduba@laic.com', 'duduba', '$2a$10$K9B/stvGf1aoxgGlZ/FoRehGQrsxTZz4Mu9139vKoCmDzs05gM5A6', 'eca421c1c702aace8f7cd137238f1814', NULL, 1, 'duduba');
COMMIT;

-- ----------------------------
-- Table structure for ril_user_role
-- ----------------------------
DROP TABLE IF EXISTS `ril_user_role`;
CREATE TABLE `ril_user_role` (
  `uid` varchar(36) NOT NULL,
  `role_id` varchar(36) NOT NULL,
  KEY `FKfofx4d6eppk26gqtt6k1o4h35` (`role_id`),
  KEY `FKhl4xngfgo8rlybirvkvg8n1vl` (`uid`),
  CONSTRAINT `FKfofx4d6eppk26gqtt6k1o4h35` FOREIGN KEY (`role_id`) REFERENCES `ril_role` (`id`),
  CONSTRAINT `FKhl4xngfgo8rlybirvkvg8n1vl` FOREIGN KEY (`uid`) REFERENCES `ril_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户角色关系表';

-- ----------------------------
-- Records of ril_user_role
-- ----------------------------
BEGIN;
INSERT INTO `ril_user_role` VALUES ('1', '1');
INSERT INTO `ril_user_role` VALUES ('2', '2');
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '角色名字',
  `description` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES ('1', '领导', '领导的描述');
INSERT INTO `role` VALUES ('6efeb563-ff36-4a54-9fdd-39d21107138a', '带队老师', '带队老师');
COMMIT;


-- ----------------------------
-- Table structure for system_configure
-- ----------------------------
DROP TABLE IF EXISTS `system_configure`;
CREATE TABLE `system_configure` (
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '参数名字',
  `value` text COLLATE utf8_bin NOT NULL COMMENT '参数值',
  `description` text COLLATE utf8_bin NOT NULL COMMENT '描述',
  `sort_no` int(11) NOT NULL COMMENT '序号',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统参数表';

-- ----------------------------
-- Records of system_configure
-- ----------------------------
BEGIN;
INSERT INTO `system_configure` VALUES ('banner_message', '多谢你如此精彩耀眼，做我平淡岁月里星辰', '用户的 banner 信息', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
