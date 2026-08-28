ALTER TABLE
  `dict` DROP INDEX `auto_idx_dict_code`,
ADD
  UNIQUE INDEX `auto_idx_key`(`code`, `del_flag`);
