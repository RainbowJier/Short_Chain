/******************************************/
/*   数据库全名 = dcloud_account */
/******************************************/

/*------------流量包任务表-----------------*/
CREATE TABLE `traffic_task`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `account_no`   bigint                                                DEFAULT NULL,
    `traffic_id`   bigint                                                DEFAULT NULL,
    `use_times`    int                                                   DEFAULT NULL,
    `lock_state`   varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Lock state: LOCK, FINISH, CANCEL',
    `biz_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Unique identifier',
    `gmt_create`   datetime                                              DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified` datetime                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_msg_id` (`biz_id`) USING BTREE,
    KEY `idx_release` (`account_no`, `id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;
/*------------流量包任务表 end-----------------*/