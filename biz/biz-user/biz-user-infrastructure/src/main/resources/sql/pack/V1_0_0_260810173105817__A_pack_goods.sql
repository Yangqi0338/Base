ALTER TABLE
  `pack_goods` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `type` bigint NULL COMMENT '类型(角色 ID)',
  MODIFY COLUMN `amount` bigint NULL COMMENT '礼包金额',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '礼包名称',
  MODIFY COLUMN `img` varchar(255) NULL COMMENT '礼包图片',
  MODIFY COLUMN `desc` varchar(255) NULL COMMENT '礼包简介',
  MODIFY COLUMN `state` int NULL COMMENT '状态',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `state`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '入会礼包商品(pack_goods)持久化对象';
