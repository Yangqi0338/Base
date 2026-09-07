ALTER TABLE
  `local_message` MODIFY COLUMN `can_consume` int NOT NULL COMMENT '可消费标识(0-不可消费, 1-可消费)[1是|启用,0否|禁用]';
