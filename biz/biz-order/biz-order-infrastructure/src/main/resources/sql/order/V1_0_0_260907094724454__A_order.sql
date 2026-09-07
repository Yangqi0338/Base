ALTER TABLE
  `order` MODIFY COLUMN `member_pay_state` int NULL COMMENT 'C端支付状态[1是|启用,0否|禁用]',
  MODIFY COLUMN `channel_pay_state` int NULL COMMENT '渠道商支付状态[1是|启用,0否|禁用]',
  MODIFY COLUMN `refund` int NULL COMMENT '是否有售后[1是|启用,0否|禁用]',
ADD
  COLUMN `order_ext` json NULL COMMENT '订单拓展信息(承接原 spu_order.spu_order_ext, 含取消/关闭原因与门店会员快照)' AFTER `refund`;
