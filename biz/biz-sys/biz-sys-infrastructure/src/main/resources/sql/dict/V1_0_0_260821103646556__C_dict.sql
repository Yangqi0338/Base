CREATE TABLE `dict` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `code` bigint NULL COMMENT '字典业务键

<p>稳定业务键, 对应 DictEnum.Key 写死码值, 与物理主键 id 解耦</p>',
  `value` json NULL COMMENT '字典值',
  `desc` varchar(255) NULL COMMENT 'desc',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `auto_idx_dict_code`(`code`) COMMENT '字典业务键

<p>稳定业务键, 对应 DictEnum.Key 写死码值, 与物理主键 id 解耦</p>'
) COMMENT = '字典数据对象';
