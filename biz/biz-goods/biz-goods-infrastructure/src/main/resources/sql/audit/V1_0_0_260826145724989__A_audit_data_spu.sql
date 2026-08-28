ALTER TABLE
  `audit_data_spu` MODIFY COLUMN `state` int NULL COMMENT '审批状态[0待用户提交,1待审核,2通过,3未通过,4终止]';
