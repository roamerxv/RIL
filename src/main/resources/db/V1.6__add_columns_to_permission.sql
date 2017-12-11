ALTER TABLE `ril`.`ril_permission` 
ADD COLUMN `clazz` varchar(255) NULL COMMENT '菜单项的 class' AFTER `method`,
ADD COLUMN `order_num` int  NOT NULL  DEFAULT  0  COMMENT '菜单项的排序序号' AFTER `clazz`;
