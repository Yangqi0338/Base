package com.newzkl.platform.base.biz.account.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantQuery;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantReq;
import com.newzkl.platform.base.biz.account.model.merchant.res.MerchantRes;
import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;

import java.util.List;

/**
 * 商户领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.service.IMerchantDomain}。
 * 旧 {@code IRoleService.useStoreCdk} 仅是对本领域同名方法的薄转发, 中台化后不再保留应用层转发,
 * 由 {@link MerchantDomain#useStoreCdk} 直接承担。</p>
 *
 * @author KC
 */
public interface MerchantDomain {

    /**
     * 商户修改
     *
     * @param id  商户 ID
     * @param req 商户入参
     * @return 影响行数
     */
    int edit(Long id, MerchantReq req);

    /**
     * 商户删除
     *
     * @param idList ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 商户详情
     *
     * @param id 商户 ID
     * @return 商户出参, 无则 null
     */
    MerchantRes detail(Long id);

    /**
     * 当前登录商户详情
     *
     * @return 商户出参, 无则 null
     */
    MerchantRes currentMerchant();

    /**
     * 商户分页
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<MerchantRes> pageList(MerchantQuery query);

    /**
     * 商户自助注册
     *
     * <p>保留旧语义: 主键取 {@code accountId}, 名称为空时回落为 username, 数字门店权限初始为 0。</p>
     *
     * @param req 注册入参
     */
    void customSave(MerchantCustomSaveReq req);

    /**
     * 使用门店开通码
     *
     * <p>保留旧语义: 按开通码值定位开通码, 已使用则抛业务异常; 否则置为已使用并记录使用者与时间,
     * 随后把当前登录商户的数字门店权限置为 1。</p>
     *
     * @param cdkValue 开通码值
     */
    void useStoreCdk(String cdkValue);

    /**
     * 查询商户的微信公众号配置
     *
     * @param merchantId 商户 ID
     * @return 微信公众号配置, 无则 null
     */
    WxMpConfigVO wxMpConfig(Long merchantId);

    /**
     * 配置当前登录商户的微信公众号参数 (仅更新该列)
     *
     * @param wxMpConfigVO 微信公众号配置
     */
    void wxMpConfigSet(WxMpConfigVO wxMpConfigVO);
}
