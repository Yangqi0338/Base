-- slug 44-fund-loss-fixes / 追加: 移除 account_purse.tripartite_amount(三方余额)
-- 全仓零消费方: Base 无 getter/setter 调用, 8 个前端仓零命中该 JSON 字段, 无业务逻辑写入
-- 三方(汇付)余额应实时查三方接口, 本地冗余列只会与三方账实不符
ALTER TABLE
  `account_purse` DROP COLUMN `tripartite_amount`;
