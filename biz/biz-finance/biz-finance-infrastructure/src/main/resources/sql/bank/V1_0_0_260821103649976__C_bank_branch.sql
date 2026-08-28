CREATE TABLE `bank_branch` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `bank_code` varchar(255) NULL COMMENT '银行编码',
  `branch_code` varchar(255) NULL COMMENT '支行编码',
  `branch_name` varchar(255) NULL COMMENT '支行名称',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_bank_branch_bank_code`(`bank_code`) COMMENT '银行编码',
  INDEX `auto_idx_bank_branch_branch_code`(`branch_code`) COMMENT '支行编码'
) COMMENT = '银行支行';
