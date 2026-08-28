ALTER TABLE
  `permission_relation` DROP INDEX `auto_idx_idx_target`,
  DROP INDEX `auto_idx_idx_source`,
  DROP INDEX `auto_idx_idx_key`,
ADD
  UNIQUE INDEX `auto_idx_key`(
    `client`,
    `type`,
    `source_id`,
    `target_id`,
    `del_flag`
  ),
ADD
  INDEX `auto_idx_source`(`client`, `type`, `source_id`),
ADD
  INDEX `auto_idx_target`(`client`, `type`, `target_id`);
