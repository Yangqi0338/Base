ALTER TABLE
  `local_message` MODIFY COLUMN `send_state` int NOT NULL COMMENT '发送状态[0待发送,1发送中,2发送成功,3发送失败]',
  MODIFY COLUMN `consume_state` int NOT NULL COMMENT '消费状态(0-待消费, 1-消费成功, 2-消费失败, 3-异常)[0待消费,1消费成功,2消费失败,3异常:需人工处理]',
  MODIFY COLUMN `can_consume` int NOT NULL COMMENT '可消费标识(0-不可消费, 1-可消费)[1是,0否]';
