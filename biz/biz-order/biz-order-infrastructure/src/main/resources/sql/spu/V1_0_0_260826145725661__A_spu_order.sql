ALTER TABLE
  `spu_order` MODIFY COLUMN `order_type` int NULL COMMENT '订单类型[0渠道商选品下单,1c端铺货下单,2运营商礼包下单]',
  MODIFY COLUMN `spu_channel_type` int NULL COMMENT '渠道类型[0供应商商品,1商户自营商品,2外部供应链商品]',
  MODIFY COLUMN `order_state` int NULL COMMENT '订单状态[0新订单,1C端待付款,2渠道商待付款,3运营商待付款,4派发中,6待发货,8待收货,10已收货,12已完成,14售后中,99已关闭]',
  MODIFY COLUMN `settle_send_state` int NULL COMMENT '运费结算发送状态[1是,0否]',
  MODIFY COLUMN `refund` int NULL COMMENT '是否有售后[1是,0否]';
