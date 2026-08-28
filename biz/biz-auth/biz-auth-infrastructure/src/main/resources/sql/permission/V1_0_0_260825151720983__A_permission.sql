ALTER TABLE
  `permission`
ADD
  COLUMN `client` varchar(255) NULL COMMENT '所属端' AFTER `id`,
  DROP INDEX `auto_idx_permission_code`,
ADD
  UNIQUE INDEX `auto_idx_idx_client_code`(`client`, `code`);
