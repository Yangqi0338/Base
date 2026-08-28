ALTER TABLE
  `refund_operation_record` MODIFY COLUMN `operator_role_code` int NULL COMMENT '操作方角色编码[1平台管理员,2平台员工,1000会员,1001供应商,1002渠道商,1003服务商]',
  MODIFY COLUMN `operator_client` varchar(8) NULL COMMENT '操作方客户端类型[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端]';
