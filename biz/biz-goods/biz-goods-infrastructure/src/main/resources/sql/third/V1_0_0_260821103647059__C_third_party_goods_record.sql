CREATE TABLE `third_party_goods_record` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `platform_type` varchar(255) NULL COMMENT '平台类型',
  `out_spu_id` varchar(255) NULL COMMENT '外部商品(SPU)ID',
  `interface_name` varchar(255) NULL COMMENT '接口名称',
  `request_json` json NULL COMMENT '请求参数',
  `response_json` json NULL COMMENT '响应结果',
  `request_status` int NULL COMMENT '请求状态',
  `error_message` varchar(255) NULL COMMENT '错误信息',
  `retry_count` int NULL COMMENT '重试次数',
  `next_retry_time` datetime NULL COMMENT '下次重试时间',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '第三方商品同步记录数据对象 (DO)';
