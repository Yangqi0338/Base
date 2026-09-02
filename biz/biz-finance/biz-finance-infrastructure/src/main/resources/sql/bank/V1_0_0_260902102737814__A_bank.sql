ALTER TABLE
  `bank` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `bank_code` varchar(255) NULL COMMENT '银行编码',
  MODIFY COLUMN `bank_name` varchar(255) NULL COMMENT '银行名称',
  COMMENT = '银行 #purse';
