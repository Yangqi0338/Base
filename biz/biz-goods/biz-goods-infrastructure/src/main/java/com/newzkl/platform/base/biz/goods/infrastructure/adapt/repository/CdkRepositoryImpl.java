package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.virtualSpu.repository.CdkRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.GoodsCdkDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.CdkDO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.virtualSpu.CdkDTO;
import com.newzkl.platform.base.biz.goods.model.goods.query.virtualSpu.CdkQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.virtualSpu.CdkVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 虚拟商品兑换码仓储实现
 *
 * <p>面向 {@code cdk} 表的商品域视角 (虚拟 SPU 兑换码), 与账号域
 * {@code biz-account} 的同名仓储 (角色/门店开通码) 是同表不同用例, 互不复用
 * 对齐旧 {@code scm-user} 的 {@code CdkDAO.xml}: {@code insertBatch} /
 * {@code existValue} / {@code cdkEditForToCdk} 自定义 SQL 改由 MyBatis-Plus
 * 通用方法与 wrapper 表达, 本仓不写 mapper xml。</p>
 *
 * <p>{@link CdkDTO#getBelowRole} 为 {@code RoleEnum.CompanyRole} 枚举而 {@code CdkDO}
 * 侧为 {@code Long} 编码, 属性拷贝时显式排除该字段并手工转 {@code code}, 避免
 * hutool 类型转换失败。</p>
 *
 * <p>无 gap 方法: 接口 6 个方法均已落地。</p>
 *
 * @author KC
 */
@Repository("goodsCdkRepositoryImpl")
@RequiredArgsConstructor
public class CdkRepositoryImpl implements CdkRepository {

    /**
     * belowRole 在 DTO/DO 两侧类型不同, 拷贝时跳过, 由 {@link CdkRepositoryImpl#toDO} / {@link CdkRepositoryImpl#toDTO} 手工映射
     */
    private static final CopyOptions SKIP_BELOW_ROLE = CopyOptions.create().setIgnoreProperties("belowRole");

    private final GoodsCdkDAO cdkDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cdkSaveBatch(List<CdkDTO> cdkDTOList) {
        if (CollUtil.isEmpty(cdkDTOList)) {
            return;
        }
        // 兑换码 ID 由领域层雪花生成后透传, 不走 preInsert 清 id
        cdkDAO.insert(cdkDTOList.stream().map(this::toDO).collect(Collectors.toList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cdkEdit(CdkDTO cdkDTO) {
        return cdkDAO.updateById(toDO(cdkDTO));
    }

    @Override
    public CdkDTO cdk(Long cdkId) {
        return toDTO(cdkDAO.selectById(cdkId));
    }

    @Override
    public Set<String> existValue(Integer systemType, Set<String> valueList) {
        if (CollUtil.isEmpty(valueList)) {
            return new HashSet<>();
        }
        List<CdkDO> cdkList = cdkDAO.selectList(new LambdaQueryWrapper<CdkDO>()
                .select(CdkDO::getValue)
                .eq(CdkDO::getSystemType, systemType)
                .in(CdkDO::getValue, valueList));
        return cdkList.stream().map(CdkDO::getValue).collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cdkEditForToCdk(CdkDTO cdkDTO, List<Long> cdkIdList) {
        if (CollUtil.isEmpty(cdkIdList)) {
            return 0;
        }
        Long belowRole = roleCode(cdkDTO.getBelowRole());
        LambdaUpdateWrapper<CdkDO> wrapper = new LambdaUpdateWrapper<CdkDO>()
                .set(cdkDTO.getToState() != null, CdkDO::getToState, cdkDTO.getToState())
                .set(belowRole != null, CdkDO::getBelowRole, belowRole)
                .set(cdkDTO.getOperatorId() != null, CdkDO::getOperatorId, cdkDTO.getOperatorId())
                .set(cdkDTO.getDealerId() != null, CdkDO::getDealerId, cdkDTO.getDealerId())
                .set(cdkDTO.getChannelId() != null, CdkDO::getChannelId, cdkDTO.getChannelId())
                .set(cdkDTO.getToDealerTime() != null, CdkDO::getToDealerTime, cdkDTO.getToDealerTime())
                .set(cdkDTO.getToChannelTime() != null, CdkDO::getToChannelTime, cdkDTO.getToChannelTime())
                .in(CdkDO::getId, cdkIdList);
        // 保留旧 SQL 的重复分配保护: 目标位已有归属则不覆盖
        wrapper.isNull(cdkDTO.getDealerId() != null, CdkDO::getDealerId)
                .isNull(cdkDTO.getChannelId() != null, CdkDO::getChannelId);
        return cdkDAO.update(null, wrapper);
    }

    @Override
    public Page<CdkVO> cdkPage(CdkQuery cdkQuery) {
        Page<CdkDO> page = cdkDAO.selectPage(RepositorySupport.page(cdkQuery),
                cdkDAO.buildQueryWrapper(cdkQuery));
        return TransferUtils.transferPage(page, CdkVO::new);
    }

    /**
     * DTO 转 DO, belowRole 枚举转编码
     *
     * @param cdkDTO 兑换码 DTO
     * @return 兑换码 DO, 入参为 null 时返回 null
     */
    private CdkDO toDO(CdkDTO cdkDTO) {
        CdkDO cdkDO = TransferUtils.transfer(cdkDTO, CdkDO::new, SKIP_BELOW_ROLE);
        if (cdkDO != null) {
            cdkDO.setBelowRole(roleCode(cdkDTO.getBelowRole()));
        }
        return cdkDO;
    }

    /**
     * DO 转 DTO, belowRole 编码转枚举
     *
     * @param cdkDO 兑换码 DO
     * @return 兑换码 DTO, 入参为 null 时返回 null
     */
    private CdkDTO toDTO(CdkDO cdkDO) {
        CdkDTO cdkDTO = TransferUtils.transfer(cdkDO, CdkDTO::new, SKIP_BELOW_ROLE);
        if (cdkDTO != null) {
            cdkDTO.setBelowRole(RoleEnum.CompanyRole.getByCode(cdkDO.getBelowRole()));
        }
        return cdkDTO;
    }

    /**
     * 取角色编码
     *
     * @param role 公司角色枚举
     * @return 角色编码, 入参为 null 时返回 null
     */
    private Long roleCode(RoleEnum.CompanyRole role) {
        return role == null ? null : role.getCode();
    }
}
