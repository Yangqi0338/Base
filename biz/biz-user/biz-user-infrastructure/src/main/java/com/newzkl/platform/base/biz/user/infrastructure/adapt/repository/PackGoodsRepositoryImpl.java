package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.PackGoodsRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.PackGoodsDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.PackGoodsDO;
import com.newzkl.platform.base.biz.user.model.pack.query.PackGoodsQuery;
import com.newzkl.platform.base.biz.user.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 入会礼包商品仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.repository.PackGoodsRepositoryImpl}。
 * 旧实现继承 {@code ServiceImpl}，中台改为直接注入 DAO + {@code TransferUtils}。</p>
 *
 * <p>字段映射：前端契约字段 {@code desc}（{@code PackGoodsRes}）↔ DO 字段 {@code intro}
 * （旧列 {@code desc} 为 MySQL 保留字，本仓改名规避）。{@code TransferUtils} 仅自动映射同名字段，
 * {@code desc↔intro} 异名，故本实现显式映射。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class PackGoodsRepositoryImpl implements PackGoodsRepository {

    private final PackGoodsDAO packGoodsDAO;

    @Override
    public PackGoodsRes save(PackGoodsRes res) {
        PackGoodsDO doObj = TransferUtils.transfer(res, PackGoodsDO::new);
        doObj.setDesc(res.getDesc());
        packGoodsDAO.insert(doObj);
        return toRes(doObj);
    }

    @Override
    public PackGoodsRes updateById(PackGoodsRes res) {
        PackGoodsDO doObj = TransferUtils.transfer(res, PackGoodsDO::new);
        doObj.setDesc(res.getDesc());
        packGoodsDAO.updateById(doObj);
        return getById(doObj.getId());
    }

    @Override
    public int deleteBatch(List<Long> idList) {
        return packGoodsDAO.deleteByIds(idList);
    }

    @Override
    public PackGoodsRes getById(Long id) {
        return toRes(packGoodsDAO.selectById(id));
    }

    @Override
    public Page<PackGoodsRes> pageQuery(PackGoodsQuery query) {
        Page<PackGoodsDO> doPage = packGoodsDAO.selectPage(
                RepositorySupport.page(query), packGoodsDAO.getLw(query));
        Page<PackGoodsRes> resPage = TransferUtils.transferPage(doPage, PackGoodsRes.class);
        List<PackGoodsRes> records = resPage.getRecords();
        List<PackGoodsDO> doRecords = doPage.getRecords();
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setDesc(doRecords.get(i).getDesc());
        }
        return resPage;
    }

    /**
     * DO 转 Res 并补齐 {@code desc↔intro} 异名映射
     *
     * @param doObj 持久化对象，可能为 null
     * @return 出参，入参为 null 时返回 null
     */
    private PackGoodsRes toRes(PackGoodsDO doObj) {
        if (doObj == null) {
            return null;
        }
        PackGoodsRes res = TransferUtils.transfer(doObj, PackGoodsRes::new);
        res.setDesc(doObj.getDesc());
        return res;
    }
}
