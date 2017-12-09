alter table `ril`.`ril_permission` add column `method` enum('ALL','GET','POST','PUT','DELETE') COLLATE utf8_unicode_ci DEFAULT 'ALL';
