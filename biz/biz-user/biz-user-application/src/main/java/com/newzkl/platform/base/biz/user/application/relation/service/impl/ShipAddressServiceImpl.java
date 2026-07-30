package com.newzkl.platform.base.biz.user.application.relation.service.impl;

import com.newzkl.platform.base.biz.user.application.relation.service.ShipAddressService;
import com.newzkl.platform.base.biz.user.domain.service.ShipAddressDomain;
import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressAddReq;
import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressPageReq;
import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressUpdateReq;
import com.newzkl.platform.base.biz.user.model.relation.vo.ShipAddressVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址应用服务实现
 *
 * <p>核心职责：1. 上下文参数填充 2. 跨领域服务编排 3. 日志记录。</p>
 *
 * @author sijiwang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShipAddressServiceImpl implements ShipAddressService {

    private final ShipAddressDomain shipAddressDomain;

    @Override
    public ShipAddressVO add(ShipAddressAddReq req) {
        log.info("应用层新增收货地址，请求参数：{}", req);

        Long accountId = SecurityUtils.getAccountId();
        if (accountId == null) {
            log.error("新增收货地址失败，未获取到登录账号ID");
            throw new RuntimeException("未登录或登录状态失效");
        }
        req.setAccountId(accountId);

        ShipAddressVO vo = shipAddressDomain.add(req);
        log.info("应用层新增收货地址成功，VO：{}", vo);
        return vo;
    }

    @Override
    public ShipAddressVO update(ShipAddressUpdateReq req) {
        log.info("应用层更新收货地址，请求参数：{}", req);

        ShipAddressVO vo = shipAddressDomain.update(req);
        log.info("应用层更新收货地址成功，VO：{}", vo);
        return vo;
    }

    @Override
    public ShipAddressVO getById(Long id) {
        log.info("应用层查询收货地址详情，地址ID：{}", id);

        ShipAddressVO vo = shipAddressDomain.getById(id);
        log.info("应用层查询收货地址成功，VO：{}", vo);
        return vo;
    }

    @Override
    public ShipAddressVO getDefaultByAccountId(Long accountId) {
        Long targetAccountId = accountId != null ? accountId : SecurityUtils.getAccountId();
        log.info("应用层查询默认收货地址，账号ID：{}", targetAccountId);

        ShipAddressVO vo = shipAddressDomain.getDefaultByAccountId(targetAccountId);
        log.info("应用层查询默认收货地址完成，VO：{}", vo);
        return vo;
    }

    @Override
    public List<ShipAddressVO> listByAccountId(Long accountId) {
        Long targetAccountId = accountId != null ? accountId : SecurityUtils.getAccountId();
        log.info("应用层查询账号下所有收货地址，账号ID：{}", targetAccountId);

        List<ShipAddressVO> voList = shipAddressDomain.listByAccountId(targetAccountId);
        log.info("应用层查询收货地址列表完成，数量：{}", voList.size());
        return voList;
    }

    @Override
    public List<ShipAddressVO> pageQuery(ShipAddressPageReq req) {
        log.info("应用层分页查询收货地址，请求参数：{}", req);

        if (req.getAccountId() == null) {
            Long accountId = SecurityUtils.getAccountId();
            if (accountId == null) {
                log.error("分页查询收货地址失败，未获取到登录账号ID");
                throw new RuntimeException("未登录或登录状态失效");
            }
            req.setAccountId(accountId);
        }

        // TODO[page-meta] 领域层降级为 List，total 元数据不再上抛。
        List<ShipAddressVO> voList = shipAddressDomain.pageQuery(req);
        log.info("应用层分页查询收货地址成功，数量：{}", voList.size());
        return voList;
    }

    @Override
    public boolean delete(Long id, String operator) {
        log.info("应用层删除收货地址，地址ID：{}，操作人：{}", id, operator);

        boolean deleteResult = shipAddressDomain.delete(id, operator);
        log.info("应用层删除收货地址结果：{}，地址ID：{}", deleteResult, id);
        return deleteResult;
    }

    @Override
    public boolean setDefault(Long id, String operator) {
        log.info("应用层设置默认收货地址，地址ID：{}，操作人：{}", id, operator);

        boolean setResult = shipAddressDomain.setDefault(id, operator);
        log.info("应用层设置默认地址结果：{}，地址ID：{}", setResult, id);
        return setResult;
    }
}
