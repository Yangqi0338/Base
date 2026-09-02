ALTER TABLE
  `bank_branch` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `bank_code` varchar(255) NULL COMMENT 'bankCode',
  MODIFY COLUMN `branch_code` varchar(255) NULL COMMENT 'branchCode',
  MODIFY COLUMN `branch_name` varchar(255) NULL COMMENT 'branchName',
  COMMENT = 'BankBranchDO表';
