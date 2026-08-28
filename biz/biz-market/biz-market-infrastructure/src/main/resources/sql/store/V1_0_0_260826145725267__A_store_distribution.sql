ALTER TABLE
  `store_distribution` MODIFY COLUMN `goods_state` int NULL COMMENT '商品状态[0上架,1下架,-1平台下架,2待上架,3平台门店下架]';
