ALTER TABLE
  `permission_relation` DROP INDEX `auto_idx_idx_key`,
ADD
  UNIQUE INDEX `auto_idx_idx_key`(
    `client`,
    `type`,
    `source_id`,
    `target_id`,
    `del_flag`
  );
