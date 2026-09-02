ALTER TABLE
  `market_bind` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `market_id` bigint NULL COMMENT '市场id',
  MODIFY COLUMN `bind_type` int NULL COMMENT '绑定类型[1平台管理员,2平台员工,1000会员,1001供应商,1002渠道商,1003服务商,1004脉脉通渠道商]',
  MODIFY COLUMN `user_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `user_name` varchar(255) NULL COMMENT '客户名称',
  MODIFY COLUMN `state` int NULL COMMENT '状态[1是,0否]',
  MODIFY COLUMN `debind_time` datetime NULL COMMENT '解除绑定时间',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))';
