/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云-youyou 数据库
 Source Server Type    : MySQL
 Source Server Version : 50634
 Source Host           : rds8a08c30s41zzv710xo.mysql.rds.aliyuncs.com:3306
 Source Schema         : youyou

 Target Server Type    : MySQL
 Target Server Version : 50634
 File Encoding         : 65001

 Date: 18/08/2017 13:00:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for business_log
-- ----------------------------
CREATE TABLE `business_log` (
  `id`                 VARCHAR(40)
                       CHARACTER SET utf8      NOT NULL,
  `operator`           VARCHAR(255)
                       COLLATE utf8_bin                  DEFAULT NULL,
  `clazz`              VARCHAR(255)
                       CHARACTER SET utf8      NOT NULL,
  `method`             VARCHAR(255)
                       CHARACTER SET utf8      NOT NULL,
  `method_description` VARCHAR(255)
                       CHARACTER SET utf8      NOT NULL,
  `success`            TINYINT(4)              NOT NULL
  COMMENT '方法是否成功运行',
  `exception_string`   TEXT COLLATE utf8_bin COMMENT '方法运行出错，抛出的exception堆栈转换成的string',
  `args`               TEXT CHARACTER SET utf8 NOT NULL,
  `time_consuming`     BIGINT                  NOT NULL  DEFAULT 0
  COMMENT '方法调用耗时（毫秒）',
  `remote_ip`          VARCHAR(32)
                       COLLATE utf8_bin                  DEFAULT NULL,
  `client_os`          VARCHAR(255)
                       COLLATE utf8_bin                  DEFAULT NULL,
  `client_browser`     VARCHAR(255)
                       COLLATE utf8_bin                  DEFAULT NULL,
  `browser_version`    VARCHAR(255)
                       COLLATE utf8_bin                  DEFAULT NULL,
  `client_device_type` VARCHAR(255)
                       COLLATE utf8_bin                  DEFAULT NULL,
  `created_at`         TIMESTAMP               NOT NULL  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin
  ROW_FORMAT = DYNAMIC
  COMMENT ='业务方法调用日志';

# 系统参数表
CREATE TABLE `system_configure` (
  `name`        VARCHAR(32)
                COLLATE utf8_bin      NOT NULL
  COMMENT '参数名字',
  `value`       TEXT COLLATE utf8_bin NOT NULL
  COMMENT '参数值',
  `description` TEXT COLLATE utf8_bin NOT NULL
  COMMENT '描述',
  `sort_no`     INT                   NOT NULL
  COMMENT '序号',
  PRIMARY KEY (`name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin
  COMMENT ='系统参数表';

INSERT INTO `system_configure` VALUES ('banner_message', '多谢你如此精彩耀眼，做我平淡岁月里星辰', '用户的 banner 信息', 1);

# 用户信息表
CREATE TABLE `user` (
  `name`   VARCHAR(36)
           COLLATE utf8_bin NOT NULL
  COMMENT '用户名',
  `passwd` VARCHAR(36)
           COLLATE utf8_bin NOT NULL
  COMMENT '密码',
  PRIMARY KEY (`name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `user` VALUES ('admin', '1');

# 事件表
CREATE TABLE `event` (
  `id`    VARCHAR(36)  NOT NULL,
  `name`  VARCHAR(256) NOT NULL,
  `start` DATETIME(0)  NOT NULL
  COMMENT '开始时间',
  `end`   DATETIME(0)  NOT NULL
  COMMENT '结束时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_bin
  COMMENT = '事件信息表';
