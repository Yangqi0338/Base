-- ① 全表留底(表小, 且下面三条语句改动面覆盖全表)
CREATE TABLE market_bind_bak_260902 AS SELECT * FROM market_bind;

-- ② 物理删 F-1 插进来的垃圾空行
--    F-1: MarketRepositoryImpl:94/:99 误用 marketBindDAO + MarketBindDO 写 market 表数据,
--    TransferUtils 按同名字段拷贝, MarketDTO 与 MarketBindDO 零同名交集 ⇒ 6 个自有列全 NULL
DELETE FROM market_bind
WHERE market_id IS NULL AND user_id IS NULL AND bind_type IS NULL;

-- ③ F-2 存量: ordinal 陷阱把渠道商写成 1001(SUPPLIER), new-scm 遗留数据用旧口径 3
UPDATE market_bind SET bind_type = 1002 WHERE bind_type IN (3, 1001);

-- ④ F-6 存量: 新绑定行 state 落 NULL ⇒ 补成 1(CommonEnum.YesOrNo.YES)
UPDATE market_bind SET state = 1 WHERE state IS NULL;
