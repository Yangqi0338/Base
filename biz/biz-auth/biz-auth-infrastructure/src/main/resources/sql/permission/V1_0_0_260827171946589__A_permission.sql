ALTER TABLE
  `permission` DROP INDEX `auto_idx_idx_client_code`,
ADD
  UNIQUE INDEX `auto_idx_idx_client_code`(`client`, `code`, `del_flag`);
