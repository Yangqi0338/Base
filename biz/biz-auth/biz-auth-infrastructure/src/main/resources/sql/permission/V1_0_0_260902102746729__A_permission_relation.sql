ALTER TABLE
  `permission_relation` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `client` varchar(11) NULL COMMENT '所属端[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端,mmt_channel脉脉通渠道商端]',
  MODIFY COLUMN `type` varchar(18) NULL COMMENT '关系类型[ACCOUNT_ROLE账号-角色,ROLE_PERMISSION角色-权限,ACCOUNT_PERMISSION账号-权限]',
  MODIFY COLUMN `source` varchar(255) NULL COMMENT '源对象标识(account 侧存账号 id 字符串, role 侧存角色 code)',
  MODIFY COLUMN `target` varchar(255) NULL COMMENT '目标对象标识(role 侧存角色 code, permission 侧存权限 id 字符串)',
  MODIFY COLUMN `origin` varchar(12) NULL COMMENT '关系来源[ROLE_DERIVED角色派生,DIRECT直接授权]',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '权限关系持久化对象

<p>account-role / role-permission / account-permission 三方多态关联单表。
端隔离: 每条关系归属一个 client, 唯一键含 client, 保证跨端同 id 关系不冲突, 账号只与同端角色/权限绑定</p>';
