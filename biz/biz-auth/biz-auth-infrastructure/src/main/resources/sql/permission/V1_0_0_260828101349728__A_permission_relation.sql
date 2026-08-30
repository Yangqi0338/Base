ALTER TABLE
  `permission_relation` DROP COLUMN `source_id`,
  DROP COLUMN `target_id`,
  MODIFY COLUMN `source` varchar(255) NULL COMMENT '源对象标识(account 侧存账号 id 字符串, role 侧存角色 code)',
ADD
  COLUMN `target` varchar(255) NULL COMMENT '目标对象标识(role 侧存角色 code, permission 侧存权限 id 字符串)' AFTER `source`,
ADD
  COLUMN `origin` varchar(12) NULL COMMENT '关系来源[ROLE_DERIVED角色派生,DIRECT直接授权]' AFTER `target`,
  DROP INDEX `auto_idx_key`,
  DROP INDEX `auto_idx_source`,
  DROP INDEX `auto_idx_target`,
ADD
  UNIQUE INDEX `auto_idx_key`(`client`, `type`, `source`, `target`, `del_flag`),
ADD
  INDEX `auto_idx_source`(`client`, `type`, `source`),
ADD
  INDEX `auto_idx_target`(`client`, `type`, `target`);
