package com.newzkl.platform.base.biz.goods.domain.video.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.video.adapt.repository.ShortVideoRepository;
import com.newzkl.platform.base.biz.goods.domain.video.service.ShortVideoDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.video.ShortVideoQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.video.ShortVideoReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.video.ShortVideoVO;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品-短视频领域服务实现。
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class ShortVideoDomainImpl implements ShortVideoDomain {

    private final ShortVideoRepository shortVideoRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ShortVideoReq req) {
        mergeSpuIdList(req);
        return shortVideoRepository.insert(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(ShortVideoReq req) {
        mergeSpuIdList(req);
        shortVideoRepository.edit(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Long id) {
        shortVideoRepository.del(id);
    }

    @Override
    public ShortVideoVO detail(Long id) {
        ShortVideoVO vo = shortVideoRepository.detail(id);
        if (vo == null) {
            throw new ScmException(400, "短视频不存在");
        }
        return vo;
    }

    @Override
    public Page<ShortVideoVO> page(ShortVideoQuery query) {
        return shortVideoRepository.page(query);
    }

    /**
     * 合并单个 spuId 到关联列表并校验非空。
     *
     * <p>迁移修正: new-scm 无条件 {@code spuIdList.add(getSpuId())} 会把 null 塞入列表,
     * 令后续空校验永不生效。此处仅在 spuId 非空时追加。</p>
     *
     * @param req 短视频请求
     */
    private void mergeSpuIdList(ShortVideoReq req) {
        List<Long> spuIdList = CollUtil.newArrayList(CollUtil.emptyIfNull(req.getSpuIdList()));
        if (req.getSpuId() != null && !spuIdList.contains(req.getSpuId())) {
            spuIdList.add(req.getSpuId());
        }
        if (CollUtil.isEmpty(spuIdList)) {
            throw new ScmException(400, "关联的SPU不能为空");
        }
        req.setSpuIdList(spuIdList);
    }
}
