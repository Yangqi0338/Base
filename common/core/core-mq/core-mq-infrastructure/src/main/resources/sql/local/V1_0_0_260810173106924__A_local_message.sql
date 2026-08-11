ALTER TABLE
  `local_message` DROP COLUMN `can_send_time`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `topic` varchar(50) NOT NULL COMMENT '消息主题' AFTER `id`,
  MODIFY COLUMN `tag` varchar(255) NOT NULL COMMENT '消息标签' AFTER `topic`,
  MODIFY COLUMN `out_key` varchar(255) NULL COMMENT '外部唯一键(用于延迟消息唤醒)' AFTER `tag`,
  MODIFY COLUMN `send_state` int NOT NULL COMMENT '发送状态',
  MODIFY COLUMN `message_content` json NOT NULL COMMENT '消息体' AFTER `send_state`,
  MODIFY COLUMN `message_class` varchar(255) NOT NULL COMMENT '消息体类全路径' AFTER `message_content`,
  MODIFY COLUMN `message_id` varchar(50) NULL COMMENT 'RocketMQ 消息 ID' AFTER `message_class`,
  MODIFY COLUMN `consume_state` int NOT NULL COMMENT '消费状态(0-待消费, 1-消费成功, 2-消费失败, 3-异常)',
  MODIFY COLUMN `can_consume` int NOT NULL COMMENT '可消费标识(0-不可消费, 1-可消费)' AFTER `consume_state`,
  MODIFY COLUMN `send_count` int NOT NULL COMMENT '发送次数' AFTER `can_consume`,
  MODIFY COLUMN `send_time` datetime NULL COMMENT '发送时间' AFTER `send_count`,
  MODIFY COLUMN `consume_time` datetime NULL COMMENT '消费时间' AFTER `send_time`,
  MODIFY COLUMN `consume_error_msg` text NULL COMMENT '消费异常信息' AFTER `consume_time`,
  MODIFY COLUMN `consume_error_count` int NOT NULL COMMENT '消费异常次数' AFTER `consume_error_msg`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `consume_error_count`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_local_message_topic`(`topic`) COMMENT '消息主题',
ADD
  INDEX `auto_idx_local_message_tag`(`tag`) COMMENT '消息标签',
ADD
  INDEX `auto_idx_local_message_send_state`(`send_state`) COMMENT '发送状态',
ADD
  INDEX `auto_idx_local_message_consume_state`(`consume_state`) COMMENT '消费状态',
ADD
  INDEX `auto_idx_local_message_consume_time`(`consume_time`) COMMENT '消费时间',
  COMMENT = '本地消息实体';
