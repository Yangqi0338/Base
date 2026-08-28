ALTER TABLE
  `permission_relation` COMMENT = '权限关系持久化对象

<p>account-role / role-permission / account-permission 三方多态关联单表。
端隔离: 每条关系归属一个 client, 唯一键含 client, 保证跨端同 id 关系不冲突, 账号只与同端角色/权限绑定</p>';
