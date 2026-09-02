ALTER TABLE
  `bank` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `bank_code` varchar(255) NULL COMMENT 'bankCode',
  MODIFY COLUMN `bank_name` varchar(255) NULL COMMENT 'bankName',
  COMMENT = 'BankDO表';
