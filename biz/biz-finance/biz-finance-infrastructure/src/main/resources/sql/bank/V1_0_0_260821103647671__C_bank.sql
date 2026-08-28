CREATE TABLE `bank` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `bank_code` varchar(255) NULL COMMENT '银行编码',
  `bank_name` varchar(255) NULL COMMENT '银行名称',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_bank_bank_code`(`bank_code`) COMMENT '银行编码'
) COMMENT = '银行 #purse';
