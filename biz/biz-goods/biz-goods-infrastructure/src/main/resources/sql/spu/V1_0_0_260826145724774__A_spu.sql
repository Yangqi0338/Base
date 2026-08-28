ALTER TABLE
  `spu` MODIFY COLUMN `deliver_time_type` int NULL COMMENT '发货时效类型[03日内,1大于3日]',
  MODIFY COLUMN `state` int NULL COMMENT '状态[-1初始化,0仓库中,1平台下架,2在售,3供应商下架]',
  MODIFY COLUMN `channel_type` int NULL COMMENT '渠道类型[0供应商商品,1商户自营商品,2外部供应链商品]',
  MODIFY COLUMN `identity` int NULL COMMENT '角色ID[1平台管理员,2平台员工,1000会员,1001供应商,1002渠道商,1003服务商]',
  MODIFY COLUMN `audit_state` int NULL COMMENT '审批状态[0待用户提交,1待审核,2通过,3未通过,4终止]';
