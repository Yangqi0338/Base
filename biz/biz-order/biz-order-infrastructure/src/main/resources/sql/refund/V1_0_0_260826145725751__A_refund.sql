ALTER TABLE
  `refund` MODIFY COLUMN `create_role` int NULL COMMENT '申请人角色[1平台管理员,2平台员工,1000会员,1001供应商,1002渠道商,1003服务商]',
  MODIFY COLUMN `order_type` int NULL COMMENT '订单类型[0渠道商选品下单,1c端铺货下单,2运营商礼包下单]',
  MODIFY COLUMN `spu_channel_type` int NULL COMMENT '渠道类型[0供应商商品,1商户自营商品,2外部供应链商品]',
  MODIFY COLUMN `from_order_state` int NULL COMMENT '来源订单状态[0新订单,1C端待付款,2渠道商待付款,3运营商待付款,4派发中,6待发货,8待收货,10已收货,12已完成,14售后中,99已关闭]';
