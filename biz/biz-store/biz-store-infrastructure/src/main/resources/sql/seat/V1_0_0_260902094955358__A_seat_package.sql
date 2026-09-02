ALTER TABLE
  `seat_package` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `seat_package_code` varchar(255) NULL COMMENT 'seatPackageCode',
  MODIFY COLUMN `seat_package_name` varchar(255) NULL COMMENT 'seatPackageName',
  MODIFY COLUMN `seat_num` int NULL COMMENT 'seatNum',
  MODIFY COLUMN `package_price` bigint NULL COMMENT 'packagePrice',
  MODIFY COLUMN `package_describe` varchar(255) NULL COMMENT 'packageDescribe',
  MODIFY COLUMN `state` int NULL COMMENT 'state[1是,0否]',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'SeatPackageDO表';
