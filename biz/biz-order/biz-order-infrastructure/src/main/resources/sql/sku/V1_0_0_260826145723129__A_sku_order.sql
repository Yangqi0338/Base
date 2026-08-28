ALTER TABLE
  `sku_order` MODIFY COLUMN `order_state` int NULL COMMENT '订单状态[0新订单,1C端待付款,2渠道商待付款,3运营商待付款,4派发中,6待发货,8待收货,10已收货,12已完成,14售后中,99已关闭]',
  MODIFY COLUMN `settle_send_state` int NULL COMMENT '结算发送状态[1是,0否]';
