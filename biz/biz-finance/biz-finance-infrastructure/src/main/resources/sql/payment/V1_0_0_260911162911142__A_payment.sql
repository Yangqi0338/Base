ALTER TABLE
  `payment` MODIFY COLUMN `consume_type` int NULL COMMENT '消费类型[1礼包,2渠道商充值,3商品,5供应商运营账户充值,6分红,7商品席位,8数智门店,9课程]',
  MODIFY COLUMN `pay_state` int NULL COMMENT '支付状态[0待支付,1支付成功,2支付失败]';
