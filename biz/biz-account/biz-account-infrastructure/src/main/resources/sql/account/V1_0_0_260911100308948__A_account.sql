ALTER TABLE
  `account`
ADD
  COLUMN `wx_permission` int NULL COMMENT '微信绑定权限(1-已绑定, 0/空-未绑定; 注册/绑定回写时置 YES, 用于快速判断跳过 member/channel 查询)[1是|启用,0否|禁用]' AFTER `tripartite_account_id`;
