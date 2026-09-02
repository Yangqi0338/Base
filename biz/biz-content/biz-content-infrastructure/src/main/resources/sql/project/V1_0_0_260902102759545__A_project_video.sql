ALTER TABLE
  `project_video` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `project_id` bigint NULL COMMENT '项目 id',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '名称',
  MODIFY COLUMN `url` varchar(255) NULL COMMENT '视频地址',
  MODIFY COLUMN `index` int NULL COMMENT '顺序({@code index} 为 SQL 保留字, 列名需反引号包裹。)',
  MODIFY COLUMN `extra` varchar(255) NULL COMMENT '视频额外信息',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '项目视频数据对象';
