/******************************************/
/*   数据库全名 = dcloud_shop */
/******************************************/

/*------------产品表-----------------*/
CREATE TABLE `product`
(
    `id`           bigint NOT NULL COMMENT '主键 ID',
    `title`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品标题',
    `detail`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '详情',
    `img`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片',
    `level`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品层级：FIRST 青铜、SECOND 黄金、THIRD 钻石',
    `old_amount`   decimal(16, 0)                                         DEFAULT NULL COMMENT '原价',
    `amount`       decimal(16, 0)                                         DEFAULT NULL COMMENT '现价',
    `plugin_type`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '工具类型：short_link、qrcode',
    `day_times`    int                                                    DEFAULT NULL COMMENT '日次数：短链类型',
    `total_times`  int                                                    DEFAULT NULL COMMENT '总次数：活码才有',
    `valid_day`    int                                                    DEFAULT NULL COMMENT '有效天数',
    `gmt_modified` datetime                                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    `gmt_create`   datetime                                               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='产品表';
/*------------产品表-end-----------------*/

/*------------订单表-----------------*/
CREATE TABLE `product_order`
(
    `id`                  bigint NOT NULL,
    `product_id`          bigint         DEFAULT NULL COMMENT '商品id',
    `product_title`       varchar(64)    DEFAULT NULL COMMENT '商品标题',
    `product_amount`      decimal(16, 2) DEFAULT NULL COMMENT '商品单价',
    `product_snapshot`    varchar(2048)  DEFAULT NULL COMMENT '商品快照',
    `buy_num`             int            DEFAULT NULL COMMENT '购买数量',
    `out_trade_no`        varchar(64)    DEFAULT NULL COMMENT '订单唯一标识',
    `state`               varchar(11)    DEFAULT NULL COMMENT 'NEW 未支付订单, PAY 已经支付订单, CANCEL 超时取消订单',
    `create_time`         datetime       DEFAULT NULL COMMENT '订单生成时间',
    `total_amount`        decimal(16, 2) DEFAULT NULL COMMENT '订单总金额',
    `pay_amount`          decimal(16, 2) DEFAULT NULL COMMENT '订单实际支付价格',
    `pay_type`            varchar(64)    DEFAULT NULL COMMENT '支付类型，微信-银行-支付宝',
    `nickname`            varchar(64)    DEFAULT NULL COMMENT '账号昵称',
    `account_no`          bigint         DEFAULT NULL COMMENT '用户ID',
    `del`                 int            DEFAULT '0' COMMENT '0表示未删除，1表示已经删除',
    `gmt_modified`        datetime       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `gmt_create`          datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `bill_type`           varchar(32)    DEFAULT NULL COMMENT '发票类型：0->不开发票；1->电子发票；2->纸质发票',
    `bill_header`         varchar(200)   DEFAULT NULL COMMENT '发票抬头',
    `bill_content`        varchar(200)   DEFAULT NULL COMMENT '发票内容',
    `bill_receiver_phone` varchar(32)    DEFAULT NULL COMMENT '发票收票人电话',
    `bill_receiver_email` varchar(200)   DEFAULT NULL COMMENT '发票收票人邮箱',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_query` (`out_trade_no`, `account_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*------------订单表s-end-----------------*/

/*------------订单分表-0-----------------*/
CREATE TABLE `product_order_0`
(
    `id`                  bigint NOT NULL,
    `product_id`          bigint         DEFAULT NULL COMMENT '商品id',
    `product_title`       varchar(64)    DEFAULT NULL COMMENT '商品标题',
    `product_amount`      decimal(16, 2) DEFAULT NULL COMMENT '商品单价',
    `product_snapshot`    varchar(2048)  DEFAULT NULL COMMENT '商品快照',
    `buy_num`             int            DEFAULT NULL COMMENT '购买数量',
    `out_trade_no`        varchar(64)    DEFAULT NULL COMMENT '订单唯一标识',
    `state`               varchar(11)    DEFAULT NULL COMMENT 'NEW 未支付订单, PAY 已经支付订单, CANCEL 超时取消订单',
    `create_time`         datetime       DEFAULT NULL COMMENT '订单生成时间',
    `total_amount`        decimal(16, 2) DEFAULT NULL COMMENT '订单总金额',
    `pay_amount`          decimal(16, 2) DEFAULT NULL COMMENT '订单实际支付价格',
    `pay_type`            varchar(64)    DEFAULT NULL COMMENT '支付类型，微信-银行-支付宝',
    `nickname`            varchar(64)    DEFAULT NULL COMMENT '账号昵称',
    `account_no`          bigint         DEFAULT NULL COMMENT '用户ID',
    `del`                 int            DEFAULT '0' COMMENT '0表示未删除，1表示已经删除',
    `gmt_modified`        datetime       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `gmt_create`          datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `bill_type`           varchar(32)    DEFAULT NULL COMMENT '发票类型：0->不开发票；1->电子发票；2->纸质发票',
    `bill_header`         varchar(200)   DEFAULT NULL COMMENT '发票抬头',
    `bill_content`        varchar(200)   DEFAULT NULL COMMENT '发票内容',
    `bill_receiver_phone` varchar(32)    DEFAULT NULL COMMENT '发票收票人电话',
    `bill_receiver_email` varchar(200)   DEFAULT NULL COMMENT '发票收票人邮箱',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_query` (`out_trade_no`, `account_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*------------订单分表-0-end-----------------*/

/*------------订单分表-1-----------------*/
CREATE TABLE `product_order_0`
(
    `id`                  bigint NOT NULL,
    `product_id`          bigint         DEFAULT NULL COMMENT '商品id',
    `product_title`       varchar(64)    DEFAULT NULL COMMENT '商品标题',
    `product_amount`      decimal(16, 2) DEFAULT NULL COMMENT '商品单价',
    `product_snapshot`    varchar(2048)  DEFAULT NULL COMMENT '商品快照',
    `buy_num`             int            DEFAULT NULL COMMENT '购买数量',
    `out_trade_no`        varchar(64)    DEFAULT NULL COMMENT '订单唯一标识',
    `state`               varchar(11)    DEFAULT NULL COMMENT 'NEW 未支付订单, PAY 已经支付订单, CANCEL 超时取消订单',
    `create_time`         datetime       DEFAULT NULL COMMENT '订单生成时间',
    `total_amount`        decimal(16, 2) DEFAULT NULL COMMENT '订单总金额',
    `pay_amount`          decimal(16, 2) DEFAULT NULL COMMENT '订单实际支付价格',
    `pay_type`            varchar(64)    DEFAULT NULL COMMENT '支付类型，微信-银行-支付宝',
    `nickname`            varchar(64)    DEFAULT NULL COMMENT '账号昵称',
    `account_no`          bigint         DEFAULT NULL COMMENT '用户ID',
    `del`                 int            DEFAULT '0' COMMENT '0表示未删除，1表示已经删除',
    `gmt_modified`        datetime       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `gmt_create`          datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `bill_type`           varchar(32)    DEFAULT NULL COMMENT '发票类型：0->不开发票；1->电子发票；2->纸质发票',
    `bill_header`         varchar(200)   DEFAULT NULL COMMENT '发票抬头',
    `bill_content`        varchar(200)   DEFAULT NULL COMMENT '发票内容',
    `bill_receiver_phone` varchar(32)    DEFAULT NULL COMMENT '发票收票人电话',
    `bill_receiver_email` varchar(200)   DEFAULT NULL COMMENT '发票收票人邮箱',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_query` (`out_trade_no`, `account_no`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*------------订单分表-1-end-----------------*/