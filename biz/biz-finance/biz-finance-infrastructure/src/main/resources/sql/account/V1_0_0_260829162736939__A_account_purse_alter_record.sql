ALTER TABLE
  `account_purse_alter_record` MODIFY COLUMN `account_type` int NULL COMMENT '客户类型[0会员,1供应商,2渠道商]',
  MODIFY COLUMN `purse_type` int NULL COMMENT '账户类型[0总账户,11商品分润账户,13商品货款结余账户,2采购金账户,3保证金账户,4营销账户,5商品位]',
  MODIFY COLUMN `alter_type` int NULL COMMENT '变动类型[1转出,2订单支付,3采购金充值,4售后退款,5渠道商订单结算,6分润,10供应商订单结算补充保证金,11供应商运营账户充值,12商品位购买,13平台赠送商品位,14商品审核失败或终止审核返还商品位,15商品提交平台扣除商品位,16渠道商下游同步,17订单流水分红,18保证金充值,19商品上架扣除商品位,20商品下架返还商品位,21转出审核拒绝,22渠道商订单结算]';
