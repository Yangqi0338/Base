ALTER TABLE
  `payment` MODIFY COLUMN `payee_info` json NULL COMMENT '收款方信息(嵌套 Money 由 MoneyCentJsonSerializer 按分存取)';
