ALTER TABLE
  `account_purse` DROP COLUMN `earnings`,
  DROP COLUMN `total_earnings`,
ADD
  COLUMN `amount` bigint NULL COMMENT '账户余额' AFTER `account_type`,
ADD
  COLUMN `total_amount` int NULL COMMENT '累计入账总额' AFTER `amount`;
