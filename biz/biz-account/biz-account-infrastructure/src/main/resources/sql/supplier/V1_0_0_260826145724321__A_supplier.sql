ALTER TABLE
  `supplier` MODIFY COLUMN `audit_state` int NULL COMMENT '审批状态[0待用户提交,1待审核,2通过,3未通过,4终止]',
  MODIFY COLUMN `promise_pay_state` int NULL COMMENT '是否缴纳保证金[1是,0否]',
  MODIFY COLUMN `promise_pay_audit_state` int NULL COMMENT '保证金审批状态[0待用户提交,1待审核,2通过,3未通过,4终止]';
