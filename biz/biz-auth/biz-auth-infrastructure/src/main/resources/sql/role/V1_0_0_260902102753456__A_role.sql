ALTER TABLE
  `role` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `client` varchar(11) NULL COMMENT '所属端[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端,mmt_channel脉脉通渠道商端]',
  MODIFY COLUMN `code` varchar(255) NULL COMMENT '角色编码',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '角色名称',
  MODIFY COLUMN `description` varchar(255) NULL COMMENT '角色描述',
  MODIFY COLUMN `sort` int NULL COMMENT '排序',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '角色持久化对象

<p>RBAC 权限分组, (client, code) 端内唯一, 通过 permission_relation 绑定账号与权限。
端隔离: 各端角色独立, code 仅在同端内唯一</p>';
