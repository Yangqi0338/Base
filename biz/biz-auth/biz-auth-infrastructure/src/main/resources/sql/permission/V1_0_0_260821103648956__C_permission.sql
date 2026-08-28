CREATE TABLE `permission` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `pid` bigint NULL COMMENT '父权限ID(0 表示根)',
  `type` varchar(255) NULL COMMENT '权限类型',
  `code` varchar(255) NULL COMMENT '权限编码',
  `name` varchar(255) NULL COMMENT '权限名称',
  `route` varchar(255) NULL COMMENT '前端路由(MENU 为前端 path, FUNC 为后端完整 URL)',
  `icon` varchar(255) NULL COMMENT '菜单图标',
  `sort` int NULL COMMENT '排序',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_permission_name`(`name`) COMMENT '权限名称',
  INDEX `auto_idx_permission_pid`(`pid`) COMMENT '父权限ID',
  INDEX `auto_idx_permission_sort`(`sort`) COMMENT '排序',
  UNIQUE INDEX `auto_idx_permission_code`(`code`) COMMENT '权限编码'
) COMMENT = '权限持久化对象

<p>Function(功能点) + Menu(菜单) 合并为单表 permission, 以 type 区分, pid 构树</p>';
