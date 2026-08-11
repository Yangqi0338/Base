ALTER TABLE
  `bank_branch` DROP COLUMN `create_time`,
  DROP COLUMN `update_time`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `bank_code` varchar(255) NULL COMMENT '银行编码',
  MODIFY COLUMN `branch_code` varchar(255) NULL COMMENT '支行编码',
  MODIFY COLUMN `branch_name` varchar(255) NULL COMMENT '支行名称',
ADD
  INDEX `auto_idx_bank_branch_bank_code`(`bank_code`) COMMENT '银行编码',
ADD
  INDEX `auto_idx_bank_branch_branch_code`(`branch_code`) COMMENT '支行编码';
