package com.newzkl.platform.base.biz.goods.application.goods.service.spu;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SupplierSpuStatisticsVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuDetailVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuStateVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;
import com.newzkl.platform.base.common.ddd.facade.SupplierSpuStatisticsQuery;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/11/317:50
 */
public interface SpuService {
    /**
     * 外部供应链商品同步
     * @param spuDTO
     */
    void outGoodsSync(SpuDTO spuDTO);

    /**
     * 货盘选择商品
     */
    Long palletSelectGoods(SpuVO spuVO);

    SpuVO spuVO(Long spuId, boolean needExtraInfo);
}
