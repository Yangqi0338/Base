ALTER TABLE
  `promise_flow`
ADD
  COLUMN `audit_state` int NULL COMMENT '审核状态 (查询)[0待用户提交,1待审核,2通过,3未通过,4终止]' AFTER `certificate_url`,
ADD
  COLUMN `audit_refuse_reason` varchar(255) NULL COMMENT '审核拒绝原因' AFTER `audit_state`;
