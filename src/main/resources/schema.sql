CREATE TABLE IF NOT EXISTS `vps_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` int DEFAULT NULL COMMENT '类型(1服务器 2其它)',
  `brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '提供商',
  `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地区',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'aff地址',
  `price` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '价格',
  `expire_start` date DEFAULT NULL COMMENT '开始时间',
  `expire_end` date DEFAULT NULL COMMENT '到期时间',
  `notify_limit` int DEFAULT NULL COMMENT '通知限制天数',
  `email_notify_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮件通知地址',
  `recipient` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收信人称呼',
  `deleted` int DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='服务器信息';
