CREATE TABLE `role` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `code` varchar(255) NULL COMMENT '角色编码',
  `name` varchar(255) NULL COMMENT '角色名称',
  `description` varchar(255) NULL COMMENT '角色描述',
  `sort` int NULL COMMENT '排序',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_role_name`(`name`) COMMENT '角色名称',
  INDEX `auto_idx_role_sort`(`sort`) COMMENT '排序',
  UNIQUE INDEX `auto_idx_role_code`(`code`) COMMENT '角色编码'
) COMMENT = '角色持久化对象

<p>RBAC 权限分组, code 唯一, 通过 permission_relation 绑定账号与权限</p>';
