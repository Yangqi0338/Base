ALTER TABLE
  `bank` DROP COLUMN `create_time`,
  DROP COLUMN `update_time`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `bank_code` varchar(255) NULL COMMENT '银行编码',
  MODIFY COLUMN `bank_name` varchar(255) NULL COMMENT '银行名称',
ADD
  INDEX `auto_idx_bank_bank_code`(`bank_code`) COMMENT '银行编码',
  COMMENT = '银行 #purse';
