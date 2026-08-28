ALTER TABLE
  `permission_relation`
ADD
  COLUMN `client` varchar(255) NULL COMMENT '所属端' AFTER `id`,
  DROP INDEX `auto_idx_idx_key`,
  DROP INDEX `auto_idx_idx_source`,
  DROP INDEX `auto_idx_idx_target`,
ADD
  UNIQUE INDEX `auto_idx_idx_key`(`client`, `type`, `source_id`, `target_id`),
ADD
  INDEX `auto_idx_idx_source`(`client`, `type`, `source_id`),
ADD
  INDEX `auto_idx_idx_target`(`client`, `type`, `target_id`);
