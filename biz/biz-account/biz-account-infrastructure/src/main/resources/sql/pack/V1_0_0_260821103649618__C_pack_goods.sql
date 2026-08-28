CREATE TABLE `pack_goods` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `type` bigint NULL COMMENT '类型(角色 ID)',
  `level` int NULL COMMENT '礼包等级',
  `amount` bigint NULL COMMENT '礼包金额',
  `name` varchar(255) NULL COMMENT '礼包名称',
  `img` varchar(255) NULL COMMENT '礼包图片',
  `desc` varchar(255) NULL COMMENT '礼包简介',
  `state` int NULL COMMENT '状态',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '入会礼包商品(pack_goods)持久化对象';
