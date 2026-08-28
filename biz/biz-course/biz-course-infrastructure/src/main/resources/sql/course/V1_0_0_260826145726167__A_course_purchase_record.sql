ALTER TABLE
  `course_purchase_record` MODIFY COLUMN `pay_type` int NULL COMMENT '支付方式(1-微信支付, 2-支付宝支付)[0直接,1微信,2支付宝,3采购金,4兑换码]';
