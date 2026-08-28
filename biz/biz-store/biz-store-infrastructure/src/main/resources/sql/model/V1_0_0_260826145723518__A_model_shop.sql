ALTER TABLE
  `model_shop` MODIFY COLUMN `audit_state` int NULL COMMENT '审核状态[0待用户提交,1待审核,2通过,3未通过,4终止]',
  MODIFY COLUMN `state` int NULL COMMENT '状态:0正常，1已禁用[1是,0否]';
