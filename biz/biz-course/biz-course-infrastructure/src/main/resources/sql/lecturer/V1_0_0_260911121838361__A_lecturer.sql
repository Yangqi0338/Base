ALTER TABLE
  `lecturer`
ADD
  COLUMN `is_top` int NULL COMMENT '是否金牌(0-否, 1-是)[1是|启用,0否|禁用]' AFTER `is_enabled`;
