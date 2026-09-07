-- ① 留底: relation_type=1 的一级市场商品关系行(可逆依据)
CREATE TABLE market_goods_relation_bak_260902 AS
SELECT * FROM market_goods_relation WHERE relation_type = 1;

-- ② 一级市场商品降维为市场商品(ONE_MARKET_GOODS(1) 枚举值已删, 存量 1 是读取炸点)
UPDATE market_goods_relation SET relation_type = 2 WHERE relation_type = 1;

-- ③ 列注释去掉「1：一级市场商品」(方括号部分由 autotable 下次启动自行重刷)
ALTER TABLE market_goods_relation
    MODIFY COLUMN relation_type int NULL COMMENT '关系类型(2：市场商品  3：市场选品商品)[2二市场-商品,3市场选品-商品]';
