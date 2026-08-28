ALTER TABLE
  `promise_flow` MODIFY COLUMN `identity` int NULL COMMENT '角色ID[1平台管理员,2平台员工,1000会员,1001供应商,1002渠道商,1003服务商]',
  MODIFY COLUMN `pay_type` int NULL COMMENT '支付方式[0直接,1微信,2支付宝,3采购金,4兑换码]';
