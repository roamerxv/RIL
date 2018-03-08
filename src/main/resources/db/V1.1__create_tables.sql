/*
 Navicat Premium Data Transfer

 Source Server         : MySQL@Docker ril
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:13306
 Source Schema         : ril

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 08/03/2018 17:16:55
*/

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
INSERT INTO `ril_permission` VALUES ('100', '2017-12-09 06:06:07', '2018-03-08 08:50:57', b'1', '系统日志', '200', NULL, 'sys:log', NULL, '/system_logs_index', 'GET', 'flaticon-line-graph', 0);
INSERT INTO `ril_permission` VALUES ('200', '2018-03-08 05:58:26', NULL, b'1', '系统功能', '0', NULL, '11', NULL, '#', 'GET', '11', 1);
INSERT INTO `ril_permission` VALUES ('201', '2018-03-08 05:59:04', NULL, b'1', '清除缓存', '200', NULL, '11:1', NULL, '/cleanCache', 'GET', '11', 1);
INSERT INTO `ril_permission` VALUES ('300', '2018-03-08 14:24:18', '2018-03-08 09:09:23', b'1', '权限管理', '0', NULL, '2', NULL, '#', 'ALL', '1', 1);
INSERT INTO `ril_permission` VALUES ('301', '2018-03-08 14:24:51', '2018-03-08 09:09:37', b'1', '系统菜单维护', '300', NULL, '2:2', NULL, '/systemMenuMaintain', 'ALL', '1', 1);
INSERT INTO `ril_permission` VALUES ('302', '2018-03-08 08:36:31', '2018-03-08 09:09:29', b'0', '角色管理', '300', NULL, '2:1', NULL, '/roleMaintain', 'GET', 'class', 0);
COMMIT;

-- ----------------------------
-- Table structure for ril_role
-- ----------------------------
DROP TABLE IF EXISTS `ril_role`;
CREATE TABLE `ril_role` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `available` bit(1) DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

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
  `role_id` varchar(36) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`permission_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色权限关系表';

-- ----------------------------
-- Records of ril_role_permission
-- ----------------------------
BEGIN;
INSERT INTO `ril_role_permission` VALUES ('100', '1');
INSERT INTO `ril_role_permission` VALUES ('201', '1');
INSERT INTO `ril_role_permission` VALUES ('301', '1');
INSERT INTO `ril_role_permission` VALUES ('302', '1');
COMMIT;

-- ----------------------------
-- Table structure for ril_system_configure
-- ----------------------------
DROP TABLE IF EXISTS `ril_system_configure`;
CREATE TABLE `ril_system_configure` (
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '参数名字',
  `value` text COLLATE utf8_bin NOT NULL COMMENT '参数值',
  `description` text COLLATE utf8_bin NOT NULL COMMENT '描述',
  `sort_no` int(11) NOT NULL COMMENT '序号',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统参数表';

-- ----------------------------
-- Records of ril_system_configure
-- ----------------------------
BEGIN;
INSERT INTO `ril_system_configure` VALUES ('banner_message', '多谢你如此精彩耀眼，做我平淡岁月里星辰', '用户的 banner 信息', 1);
COMMIT;

-- ----------------------------
-- Table structure for ril_user
-- ----------------------------
DROP TABLE IF EXISTS `ril_user`;
CREATE TABLE `ril_user` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

-- ----------------------------
-- Records of ril_user
-- ----------------------------
BEGIN;
INSERT INTO `ril_user` VALUES ('1', '2017-12-07 13:35:53', '2017-12-09 08:50:45', 'admin@laic1.com', '管理员', '$2a$10$lhIwqjLMi/dJ4vidGP1zXue.tAnz1lBtSghqRX/MTLjLykyskEmN6', '1334b81229a2ba20adf2302f630b1019', NULL, 1, 'admin');
COMMIT;

-- ----------------------------
-- Table structure for ril_user_role
-- ----------------------------
DROP TABLE IF EXISTS `ril_user_role`;
CREATE TABLE `ril_user_role` (
  `uid` varchar(36) COLLATE utf8_bin NOT NULL,
  `role_id` varchar(36) COLLATE utf8_bin NOT NULL,
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
COMMIT;

-- ----------------------------
-- Table structure for schema_version
-- ----------------------------
DROP TABLE IF EXISTS `schema_version`;
CREATE TABLE `schema_version` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(200) COLLATE utf8_bin NOT NULL,
  `type` varchar(20) COLLATE utf8_bin NOT NULL,
  `script` varchar(1000) COLLATE utf8_bin NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) COLLATE utf8_bin NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `schema_version_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
