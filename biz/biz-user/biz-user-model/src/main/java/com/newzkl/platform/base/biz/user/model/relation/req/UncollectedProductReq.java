package com.newzkl.platform.base.biz.user.model.relation.req;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

import java.util.List;

/**
 * 取消收藏请求
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.collection.model.req.UncollectedProductReq}。
 * 两种用法二选一: 直接给收藏记录ID列表, 或给店铺+商品由服务端反查。</p>
 *
 * @author KC
 */
@Data
public class UncollectedProductReq {

    /**
     * 收藏记录ID列表
     */
    private List<Long> idList;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 单个ID写入（兼容旧契约的单值传参）
     *
     * @param id 收藏记录ID
     */
    public void setId(Long id) {
        this.idList = QuerySupport.doWrapperList(this.idList, id);
    }

    /**
     * 校验关键Key非空
     *
     * @return 是否合法
     */
    @AssertTrue(message = "关键Key不能为空")
    public boolean isValidKey() {
        return CollUtil.isNotEmpty(idList) || (storeId != null && goodsId != null);
    }
}
