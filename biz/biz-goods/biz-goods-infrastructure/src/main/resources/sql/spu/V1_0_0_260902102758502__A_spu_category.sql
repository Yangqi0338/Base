ALTER TABLE
  `spu_category` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `pid` bigint NULL COMMENT '父ID',
  MODIFY COLUMN `pid_list` varchar(255) NULL COMMENT '祖先路径含自身ID，逗号分隔逗号结尾(根节点为 "id,"；子节点为 "父pidList+自身id+,"；用于树路径查询)',
  MODIFY COLUMN `level` int NULL COMMENT '层级深度，根节点为1',
  MODIFY COLUMN `brand_id_list` varchar(255) NULL COMMENT '已绑定的品牌 ID 集合, 逗号分隔无尾逗号 (如 "11,12,13")

<p>迁移自 new-scm category 表的 brand_id_list 列 (Base 建表时曾删, 本轮 bindBrand 端点补迁时恢复)。
整存整取, 无 like 查询, 故不加尾逗号, 与 industry.category_id_list 同格式 ({@code BizUtil#addIdString})</p>',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '专属人',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '名称(查询)',
  MODIFY COLUMN `desc` varchar(255) NULL COMMENT '描述',
  MODIFY COLUMN `img` varchar(255) NULL COMMENT '图片',
  MODIFY COLUMN `idx` int NULL COMMENT '排序',
  MODIFY COLUMN `is_enabled` int NULL COMMENT '是否启用[1是,0否]',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '商品分类';
