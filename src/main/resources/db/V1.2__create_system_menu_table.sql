CREATE TABLE `ril`.`system_menu`  (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `name` varchar(128) NOT NULL COMMENT '菜单名称',
  `parent_id` varchar(36) NOT NULL COMMENT '父级菜单 id，如果是0  ，代表是顶级菜单',
  `class` varchar(256) NOT NULL COMMENT '菜单项的 class',
  `label_class` varchar(256) NOT NULL COMMENT '菜单项前面的图标的 class（用 class 来显示图片）',
  `url` varchar(256) NULL COMMENT '菜单项对应的 url',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统菜单表';
