ALTER TABLE
  `supplier` MODIFY COLUMN `promise_pay_config` int NULL COMMENT '保证金缴纳配置(源列 promise_pay_config)[0即时缴纳,1延迟缴纳]';
