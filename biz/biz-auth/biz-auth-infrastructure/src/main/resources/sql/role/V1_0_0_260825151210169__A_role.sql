ALTER TABLE
  `role` COMMENT = '角色持久化对象

<p>RBAC 权限分组, (client, code) 端内唯一, 通过 permission_relation 绑定账号与权限。
端隔离: 各端角色独立, code 仅在同端内唯一</p>';
