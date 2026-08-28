ALTER TABLE
  `refund` MODIFY COLUMN `create_role` int NULL COMMENT '申请人角色[1平台管理员,2平台员工,1000会员,1001供应商,1002渠道商,1003服务商,1004脉脉通渠道商]';
