ALTER TABLE
  `article`
ADD
  COLUMN `issuer` json NULL COMMENT '发布人信息' AFTER `is_visible`;
