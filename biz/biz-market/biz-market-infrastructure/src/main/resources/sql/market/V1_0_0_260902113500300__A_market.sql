-- ① 留底: DROP 前的 market_level 全量值 + 重算前的 sub_bind_num(一石二鸟)
CREATE TABLE market_bak_260902 AS SELECT id, market_level, sub_bind_num FROM market;

-- ② F-3 存量: 按 state=1 的绑定行数重算(修前是无条件自增, 计数虚高)
UPDATE market m
SET m.sub_bind_num = (
    SELECT COUNT(*) FROM market_bind mb
    WHERE mb.market_id = m.id AND mb.state = 1 AND mb.del_flag = 0
);

-- ③ DROP market_level + sub_bind_num 列注释正名
ALTER TABLE market
    DROP COLUMN market_level,
    MODIFY COLUMN sub_bind_num int NULL COMMENT '绑定该市场的渠道商数量';
