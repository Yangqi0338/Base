ALTER TABLE
  `permission` COMMENT = '权限持久化对象

<p>Function(功能点) + Menu(菜单) 合并为单表 permission, 以 type 区分, pid 构树。
端隔离: (client, code) 端内唯一, 各端权限独立</p>';
