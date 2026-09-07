ALTER TABLE
  `supplier` MODIFY COLUMN `promise_pay_state` int NULL COMMENT '是否缴纳保证金[1是|启用,0否|禁用]',
  MODIFY COLUMN `period_set_state` int NULL COMMENT '是否设置账期[1是|启用,0否|禁用]';
