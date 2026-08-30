ALTER TABLE
  `dict`
ADD
  COLUMN `code` bigint NULL COMMENT '字典业务键

<p>稳定业务键, 对应 DictEnum.Key 写死码值, 与物理主键 id 解耦</p>' AFTER `id`,
ADD
  UNIQUE INDEX `auto_idx_key`(`code`, `del_flag`);
