ALTER TABLE
  `order` MODIFY COLUMN `order_type` int NULL COMMENT '订单类型[0渠道商选品下单,1c端铺货下单,2运营商礼包下单]',
  MODIFY COLUMN `order_state` int NULL COMMENT '订单状态[0新订单,1C端待付款,2渠道商待付款,3运营商待付款,4派发中,6待发货,8待收货,10已收货,12已完成,14售后中,99已关闭]',
  MODIFY COLUMN `member_pay_state` int NULL COMMENT 'C端支付状态[1是,0否]',
  MODIFY COLUMN `channel_pay_state` int NULL COMMENT '渠道商支付状态[1是,0否]',
  MODIFY COLUMN `pay_type` int NULL COMMENT '支付方式[0直接,1微信,2支付宝,3采购金,4兑换码]',
  MODIFY COLUMN `refund` int NULL COMMENT '是否有售后[1是,0否]';
