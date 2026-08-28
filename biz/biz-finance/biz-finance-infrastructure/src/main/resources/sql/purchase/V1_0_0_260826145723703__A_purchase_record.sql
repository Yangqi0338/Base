ALTER TABLE
  `purchase_record` MODIFY COLUMN `pay_type` int NULL COMMENT '支付方式[0直接,1微信,2支付宝,3采购金,4兑换码]',
  MODIFY COLUMN `pay_state` int NULL COMMENT '支付状态[0新订单,1C端待付款,2渠道商待付款,3运营商待付款,4派发中,6待发货,8待收货,10已收货,12已完成,14售后中,99已关闭]';
