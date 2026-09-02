ALTER TABLE
  `permission` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `client` varchar(11) NULL COMMENT '所属端[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端,mmt_channel脉脉通渠道商端]',
  MODIFY COLUMN `pid` bigint NULL COMMENT '父权限ID(0 表示根)',
  MODIFY COLUMN `type` varchar(4) NULL COMMENT '权限类型[MENU菜单,FUNC功能]',
  MODIFY COLUMN `code` varchar(255) NULL COMMENT '权限编码',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '权限名称',
  MODIFY COLUMN `route` varchar(255) NULL COMMENT '前端路由(MENU 为前端 path, FUNC 为后端完整 URL)',
  MODIFY COLUMN `icon` varchar(255) NULL COMMENT '菜单图标',
  MODIFY COLUMN `sort` int NULL COMMENT '排序',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '权限持久化对象

<p>Function(功能点) + Menu(菜单) 合并为单表 permission, 以 type 区分, pid 构树。
端隔离: (client, code) 端内唯一, 各端权限独立</p>';
