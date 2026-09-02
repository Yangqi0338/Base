ALTER TABLE
  `market_bind` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `market_id` bigint NULL COMMENT 'marketId',
  MODIFY COLUMN `bind_type` int NULL COMMENT 'bindType[1平台管理员,2平台员工,1000会员,1001供应商,1002渠道商,1003服务商,1004脉脉通渠道商]',
  MODIFY COLUMN `user_id` bigint NULL COMMENT 'userId',
  MODIFY COLUMN `user_name` varchar(255) NULL COMMENT 'userName',
  MODIFY COLUMN `state` int NULL COMMENT 'state[1是,0否]',
  MODIFY COLUMN `debind_time` datetime NULL COMMENT 'debindTime',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'MarketBindDO表';
