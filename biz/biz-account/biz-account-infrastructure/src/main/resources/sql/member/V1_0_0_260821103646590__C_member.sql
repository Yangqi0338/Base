CREATE TABLE `member` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(255) NULL COMMENT '用户名称',
  `background_img` varchar(255) NULL COMMENT '背景图',
  `gender` int NULL COMMENT '性别',
  `birthday` date NULL COMMENT '生日',
  `residence` varchar(255) NULL COMMENT '常住地(省份, 城市, 区县)',
  `wx_id` varchar(255) NULL COMMENT '微信ID',
  `open_id` varchar(255) NULL COMMENT 'openId',
  `channel_id` bigint NULL COMMENT '渠道商ID',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = 'c端客户';
