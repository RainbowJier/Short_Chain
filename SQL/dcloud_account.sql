/******************************************/
/*    dcloud_account */
/******************************************/

/*------------账号数据库创建-----------------*/
CREATE DATABASE IF NOT EXISTS `dcloud_account`;
/*------------账号数据库创建-----------------*/

/*------------account-----------------*/
CREATE TABLE `account`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `account_no`   bigint                                                 DEFAULT NULL COMMENT 'account number',
    `head_img`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'avatar',
    `phone`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'phone number',
    `pwd`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'password',
    `secret`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  DEFAULT NULL COMMENT 'To process sensitive personal information.',
    `mail`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'email',
    `username`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'username',
    `auth`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  DEFAULT NULL COMMENT 'Certification Level: DEFAULT, REALNAME, ENTERPRISE, the numer of visits is different.',
    `gmt_create`   datetime                                               DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified` datetime                                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`) USING BTREE,
    UNIQUE KEY `uk_account` (`account_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;
/*------------account-----------------*/

/*------------traffic-----------------*/
CREATE TABLE `traffic`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `day_limit`    int                                                   DEFAULT NULL COMMENT 'Daily limit for short links',
    `day_used`     int                                                   DEFAULT NULL COMMENT 'Number of short links used for the day',
    `total_limit`  int                                                   DEFAULT NULL COMMENT 'Total limit, used for live codes',
    `account_no`   bigint                                                DEFAULT NULL COMMENT 'Account number',
    `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Order number',
    `level`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Product level: FIRST(Bronze), SECOND(Gold), THIRD(Diamond)',
    `expired_date` date                                                  DEFAULT NULL COMMENT 'Expiration date',
    `plugin_type`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Plugin type',
    `product_id`   bigint                                                DEFAULT NULL COMMENT 'Product primary key',
    `gmt_create`   datetime                                              DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified` datetime                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_trade_no` (`out_trade_no`, `account_no`) USING BTREE,
    KEY `idx_account_no` (`account_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;
/*------------traffic-----------------*/

/*------------traffic-----------------*/
CREATE TABLE `traffic_0`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `day_limit`    int                                                   DEFAULT NULL COMMENT 'Daily limit for short links',
    `day_used`     int                                                   DEFAULT NULL COMMENT 'Number of short links used for the day',
    `total_limit`  int                                                   DEFAULT NULL COMMENT 'Total limit, used for live codes',
    `account_no`   bigint                                                DEFAULT NULL COMMENT 'Account number',
    `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Order number',
    `level`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Product level: FIRST(Bronze), SECOND(Gold), THIRD(Diamond)',
    `expired_date` date                                                  DEFAULT NULL COMMENT 'Expiration date',
    `plugin_type`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Plugin type',
    `product_id`   bigint                                                DEFAULT NULL COMMENT 'Product primary key',
    `gmt_create`   datetime                                              DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified` datetime                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_trade_no` (`out_trade_no`, `account_no`) USING BTREE,
    KEY `idx_account_no` (`account_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;
/*------------traffic-----------------*/

/*------------traffic-----------------*/
CREATE TABLE `traffic_1`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `day_limit`    int                                                   DEFAULT NULL COMMENT 'Daily limit for short links',
    `day_used`     int                                                   DEFAULT NULL COMMENT 'Number of short links used for the day',
    `total_limit`  int                                                   DEFAULT NULL COMMENT 'Total limit, used for live codes',
    `account_no`   bigint                                                DEFAULT NULL COMMENT 'Account number',
    `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Order number',
    `level`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Product level: FIRST(Bronze), SECOND(Gold), THIRD(Diamond)',
    `expired_date` date                                                  DEFAULT NULL COMMENT 'Expiration date',
    `plugin_type`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Plugin type',
    `product_id`   bigint                                                DEFAULT NULL COMMENT 'Product primary key',
    `gmt_create`   datetime                                              DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified` datetime                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_trade_no` (`out_trade_no`, `account_no`) USING BTREE,
    KEY `idx_account_no` (`account_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;
/*------------traffic-----------------*/


/*------------traffic_task-----------------*/
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