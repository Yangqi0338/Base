package com.newzkl.platform.base.biz.account.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantQuery;
import com.newzkl.platform.base.biz.account.model.merchant.vo.MerchantVO;
import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;

import java.util.List;

/**
 * 商户仓储端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.repository.IMerchantRepository}。
 * 旧接口的 {@code merchantEdit(List<EditColumnDTO>, Long)} 列自增能力在旧代码中无调用方, 未迁移。</p>
 *
 * @author KC
 */
public interface MerchantRepository {

    /**
     * 保存商户
     *
     * <p>商户主键与账号 ID 同值, 由领域层显式传入, 仓储不生成主键。</p>
     *
     * @param merchant 商户领域视图 (需带 id)
     * @return 主键 ID
     */
    Long save(MerchantVO merchant);

    /**
     * 按主键更新商户 (仅更新非 null 列)
     *
     * @param merchant 商户领域视图 (需带 id)
     * @return 影响行数
     */
    int edit(MerchantVO merchant);

    /**
     * 按 ID 列表删除商户
     *
     * @param idList ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 商户详情
     *
     * @param id 商户 ID
     * @return 商户领域视图, 无则 null
     */
    MerchantVO detail(Long id);

    /**
     * 商户分页
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<MerchantVO> pageList(MerchantQuery query);

    /**
     * 查询商户的微信公众号配置
     *
     * @param merchantId 商户 ID
     * @return 微信公众号配置, 无则 null
     */
    WxMpConfigVO wxMpConfig(Long merchantId);
}
