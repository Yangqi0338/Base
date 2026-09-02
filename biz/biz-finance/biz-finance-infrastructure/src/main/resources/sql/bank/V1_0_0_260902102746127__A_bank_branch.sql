ALTER TABLE
  `bank_branch` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `bank_code` varchar(255) NULL COMMENT '银行编码',
  MODIFY COLUMN `branch_code` varchar(255) NULL COMMENT '支行编码',
  MODIFY COLUMN `branch_name` varchar(255) NULL COMMENT '支行名称',
  COMMENT = '银行支行';
