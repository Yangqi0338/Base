ALTER TABLE
  `audit_data_work_table` MODIFY COLUMN `operate_type` int NULL COMMENT '操作类型[1修改,2新增,3删除]',
  MODIFY COLUMN `operate_target` int NULL COMMENT '操作目标[1SPU基础信息,2销售属性,3参数属性,4SKU基础信息,5SPU状态]',
  MODIFY COLUMN `state` int NULL COMMENT '审批状态[0待用户提交,1待审核,2通过,3未通过,4终止]';
