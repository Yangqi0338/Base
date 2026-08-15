package com.newzkl.platform.base.biz.auth.domain.adapt.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.model.oauth.dto.AccountLoginLogDTO;
import com.newzkl.platform.base.biz.auth.model.oauth.query.AccountLoginLogQuery;


/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:44
 */
public interface AccountLoginRepository {

    void accountLoginLogSave(AccountLoginLogDTO accountLoginLog);

    Page<AccountLoginLogDTO> selectPage(AccountLoginLogQuery accountLoginLogQuery);
}
