-- ============================================================
-- 迁移说明: 将 admin_account 表平台账号数据合并至 account 表
--
-- 映射规则:
--   client           固定 'admin'
--   face             -> head (头像列名变更)
--   arole_id_list    -> role_id_list (若为空则默认写入 PLATFORM 角色码 '1')
--   state            完全反向映射 (admin: 0=正常,1=冻结; account: 1=正常,0=冻结)
--                    即: admin.state=0 -> account.state=1(ENABLE)
--                        admin.state=1 -> account.state=0(DISABLE)
--   sub_user_type    固定 1 (AccountEnum.SubUserType.MAIN)
--   pid              固定 0
--   main_account_id  固定 0
--   pid_list         固定 '' (空串, 根节点无上级链)
--   p_role_list      固定 '' (空串)
--   im_sync_status   固定 0 (未同步)
--
-- 前置条件:
--   1. account 表已存在且包含 client 列
--   2. admin_account 表数据仍在 (本 SQL 不删 admin_account 数据, 由应用发布后确认再清理)
--
-- 执行幂等保障: WHERE NOT EXISTS 防止重复插入
-- ============================================================

INSERT INTO account (
    id,
    client,
    nickname,
    head,
    phone,
    username,
    password,
    state,
    role_id_list,
    sub_user_type,
    pid,
    main_account_id,
    pid_list,
    p_role_list,
    im_sync_status,
    create_time,
    update_time,
    del_flag
)
SELECT
    a.id,
    'admin'                                                         AS client,
    a.nickname,
    a.face                                                          AS head,
    a.phone,
    a.username,
    a.password,
    CASE WHEN a.state = 0 THEN 1
         WHEN a.state = 1 THEN 0
         ELSE 1
    END                                                              AS state,
    CASE WHEN a.arole_id_list IS NULL OR a.arole_id_list = ''
         THEN '1'
         ELSE a.arole_id_list
    END                                                              AS role_id_list,
    1                                                               AS sub_user_type,
    0                                                               AS pid,
    0                                                               AS main_account_id,
    ''                                                              AS pid_list,
    ''                                                              AS p_role_list,
    0                                                               AS im_sync_status,
    a.create_time,
    a.update_time,
    a.del_flag
FROM admin_account a
WHERE NOT EXISTS (
    SELECT 1 FROM account t WHERE t.id = a.id AND t.client = 'admin'
);
