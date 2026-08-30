-- slug 44-fund-loss-fixes / task 01: AccountPurse 字段更名 earnings -> amount, total_earnings -> total_amount
-- 用 CHANGE COLUMN 原地更名: 保留存量数据(不可用 ADD 新列, 否则全量供应商余额归零)
-- 注意: 本脚本必须先于 autotable 反射建列执行; 若 autotable 已建出空的 amount / total_amount, 需先手工 DROP 空列再执行本脚本
ALTER TABLE
  `account_purse` CHANGE COLUMN `earnings` `amount` bigint NULL COMMENT '账户余额',
  CHANGE COLUMN `total_earnings` `total_amount` bigint NULL COMMENT '累计入账总额';
