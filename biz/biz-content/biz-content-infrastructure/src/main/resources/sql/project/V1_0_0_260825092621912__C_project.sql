CREATE TABLE `project` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(255) NULL COMMENT '名称',
  `desc` varchar(255) NULL COMMENT '简介({@code desc} 为 SQL 保留字, 列名需反引号包裹。)',
  `province` int NULL COMMENT '省编码',
  `city` int NULL COMMENT '市编码',
  `area` int NULL COMMENT '区编码',
  `basic_amount` bigint NULL COMMENT '合作金额(Money 类型, 落库 BIGINT 分)',
  `flags` varchar(255) NULL COMMENT '标签(逗号分隔串)',
  `detail` text NULL COMMENT '详情',
  `interest_num` int NULL COMMENT '意向人数',
  `interest_person` varchar(255) NULL COMMENT '意向人',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '项目数据对象';
