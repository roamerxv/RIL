CREATE TABLE `ril`.`role` (
  `id`          VARCHAR(36)  NOT NULL,
  `name`        VARCHAR(32)  NOT NULL
  COMMENT '角色名字',
  `description` VARCHAR(256) NULL
  COMMENT '角色描述',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_bin
  COMMENT = '用户角色表';
