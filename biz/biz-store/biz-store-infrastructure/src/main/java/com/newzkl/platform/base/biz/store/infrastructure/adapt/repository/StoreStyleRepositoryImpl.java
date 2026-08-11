package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.store.StoreStyleEnum;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreStyle;
import com.newzkl.platform.base.biz.store.model.store.query.StoreStyleQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleResponse;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreStyleRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.StoreStyleDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreStyleDO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 门店样式仓储实现
 */
@Slf4j
@Repository
public class StoreStyleRepositoryImpl extends ServiceImpl<StoreStyleDAO, StoreStyleDO> implements StoreStyleRepository {

    private final StoreStyleDAO storeStyleDAO;

    public StoreStyleRepositoryImpl(StoreStyleDAO storeStyleDAO) {
        this.storeStyleDAO = storeStyleDAO;
    }

    @Override
    public Page<StoreStyleResponse> storeStylePage(StoreStyleQuery req) {
        // 创建分页对象
        Page<StoreStyleDO> queryPage = new Page<>(req.getPageNo(), req.getPageSize());

        LambdaQueryWrapper<StoreStyleDO> wrapper = new LambdaQueryWrapper<StoreStyleDO>()
                .eq(req.getState() != null, StoreStyleDO::getState, req.getState())
                .eq(StoreStyleDO::getCreatorId, SecurityUtils.getAccountId())
                .orderByDesc(StoreStyleDO::getId);

        // 多字段模糊查询
        if (StrUtil.isNotBlank(req.getSearchContent())) {
            wrapper.and(w -> w
                    .eq(StoreStyleDO::getStyleCode, req.getSearchContent())
                    .or()
                    .like(StoreStyleDO::getStyleName, req.getSearchContent())
            );
        }

        // 执行分页查询
        Page<StoreStyleDO> page = this.page(queryPage, wrapper);

        // 转换为响应对象列表 (使用门店数由领域层聚合回填)
        List<StoreStyleResponse> responseList = page.getRecords().stream()
                .map(x -> BeanUtil.toBean(x, StoreStyleResponse.class)).collect(Collectors.toList());

        // 构建新的 Page 对象
        Page<StoreStyleResponse> resultPage = new Page<>();
        resultPage.setRecords(responseList);
        resultPage.setTotal(page.getTotal());
        resultPage.setSize(page.getSize());
        resultPage.setCurrent(page.getCurrent());

        return resultPage;
    }

    @Override
    public void create(StoreStyle storeStyle) {
        this.save(TransferUtils.transfer(storeStyle, StoreStyleDO::new));
    }

    @Override
    public void update(StoreStyle storeStyle) {
        this.update(TransferUtils.transfer(storeStyle, StoreStyleDO::new), new BaseLambdaQueryWrapper<StoreStyleDO>()
                .notEmptyEq(StoreStyleDO::getStyleCode, storeStyle.getStyleCode())
                .notEmptyEq(StoreStyleDO::getId, storeStyle.getId())
        );
    }

    @Override
    public void deleteDefaultStyle() {
        this.update(new LambdaUpdateWrapper<StoreStyleDO>().eq(StoreStyleDO::getType, StoreStyleEnum.Type.DEFAULT.getCode()).set(StoreStyleDO::getType, StoreStyleEnum.Type.OTHERS.getCode()));
    }

    @Override
    public StoreStyle getDefaultStyle() {
        return TransferUtils.transfer(this.getOne(new LambdaQueryWrapper<StoreStyleDO>().eq(StoreStyleDO::getType, StoreStyleEnum.Type.DEFAULT.getCode())), StoreStyle::new);
    }

    @Override
    public StoreStyle copyStyle(String styleCode) {
        StoreStyleDO storeStyleDO;
        if (StrUtil.isEmpty(styleCode)) {
            storeStyleDO = this.getOne(new LambdaQueryWrapper<StoreStyleDO>().eq(StoreStyleDO::getType, StoreStyleEnum.Type.DEFAULT.getCode()));
        } else {
            storeStyleDO = this.getOne(new LambdaQueryWrapper<StoreStyleDO>().eq(StoreStyleDO::getStyleCode, styleCode));
        }

        StoreStyleDO copyStyle = new StoreStyleDO();
        copyStyle.setType(StoreStyleEnum.Type.OTHERS.getCode());
        copyStyle.setStyleName(storeStyleDO.getStyleName());
        copyStyle.setStyleCode(BusinessCodeUtil.generate(BusinessType.STORE_STYLE));
        copyStyle.setPackageDescribe(storeStyleDO.getPackageDescribe());
        copyStyle.setPageType(storeStyleDO.getPageType());
        copyStyle.setSourceCode(storeStyleDO.getStyleCode());
        copyStyle.setSourceName(storeStyleDO.getStyleName());
        copyStyle.setStyleContent(storeStyleDO.getStyleContent());
        copyStyle.setPreviewImage(storeStyleDO.getPreviewImage());
        if (StrUtil.isNotEmpty(styleCode)) {
            // 默认模版不拷贝商品列表
            copyStyle.setGoodsIdListStr(storeStyleDO.getGoodsIdListStr());
        }
        this.save(copyStyle);
        return this.getByStyleCode(copyStyle.getStyleCode());
    }

    @Override
    public StoreStyle getByStyleCode(String styleCode) {
        return TransferUtils.transfer(storeStyleDAO.selectOne(new BaseLambdaQueryWrapper<StoreStyleDO>().eq(StoreStyleDO::getStyleCode, styleCode)), StoreStyle::new);
    }

    @Override
    public StoreStyle getOneselfStyle(String storeStyle) {
        return TransferUtils.transfer(storeStyleDAO.selectOne(new BaseLambdaQueryWrapper<StoreStyleDO>().notNullEq(StoreStyleDO::getStyleCode, storeStyle).eq(StoreStyleDO::getCreatorId, SecurityUtils.getAccountId())), StoreStyle::new);
    }

    @Override
    public void deleteCopyStyle(String styleCode) {
        storeStyleDAO.delete(new BaseLambdaQueryWrapper<StoreStyleDO>().eq(StoreStyleDO::getStyleCode, styleCode).eq(StoreStyleDO::getCreatorId, SecurityUtils.getAccountId()));
    }

    @Override
    public List<StoreStyle> getByStoreStyleList(List<String> storeStyleList) {
        return TransferUtils.transfers(storeStyleDAO.selectList(new BaseLambdaQueryWrapper<StoreStyleDO>().in(StoreStyleDO::getStyleCode, storeStyleList)), StoreStyle::new);
    }
}