CREATE TABLE `course` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `course_num` varchar(255) NULL COMMENT '课程编码',
  `title` varchar(255) NULL COMMENT '课程标题',
  `intro` varchar(255) NULL COMMENT '课程简介',
  `lecturer_id` bigint NULL COMMENT '讲师ID(关联讲师表主键)',
  `category_id` bigint NULL COMMENT '课程分类ID(关联课程分类表主键)',
  `original_price` bigint NULL COMMENT '原价',
  `sell_price` bigint NULL COMMENT '售价',
  `virtual_purchase_count` int NULL COMMENT '虚拟购买次数',
  `purchase_count` int NULL COMMENT '实际购买数量',
  `cover_image` varchar(255) NULL COMMENT '封面图URL',
  `carousel_images` varchar(255) NULL COMMENT '轮播图URL(多个用逗号分隔)',
  `video_url` varchar(255) NULL COMMENT '视频介绍URL',
  `details` varchar(255) NULL COMMENT '课程详情富文本',
  `chapter_count` int NULL COMMENT '章节总数(冗余字段)',
  `total_duration_centisecond` bigint NULL COMMENT '课程总时长(单位百分秒, 冗余字段)',
  `total_duration_desc` varchar(255) NULL COMMENT '课程总时长描述(冗余字段)',
  `is_enabled` int NULL COMMENT '是否启用',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '课程数据对象

<p>价格列单位为分, 入参单位为元, 换算在仓储实现完成。</p>';
