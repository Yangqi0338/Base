package com.newzkl.platform.base.biz.account.domain.auth.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.AccountLoginLogQuery;
import com.newzkl.platform.base.biz.account.model.res.AccountLoginLog;
import com.newzkl.platform.base.biz.account.model.vo.tencent.TencentImConfig;
import com.newzkl.platform.base.biz.account.model.auth.vo.AccountLoginLogVO;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:44
 */
public interface AccountLoginRepository {

    void accountLoginLogSave(AccountLoginLog accountLoginLog);

    Page<AccountLoginLogVO> selectPage(AccountLoginLogQuery accountLoginLogQuery);

    TencentImConfig getTencentImConfig();
}
