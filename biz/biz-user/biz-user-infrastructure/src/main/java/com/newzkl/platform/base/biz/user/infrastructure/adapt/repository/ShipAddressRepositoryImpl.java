package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.ShipAddressRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.UserShipAddressDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.ShipAddressDO;
import com.newzkl.platform.base.biz.user.model.relation.dto.ShipAddressDTO;
import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressPageReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 收货地址仓储实现
 *
 * <p>迁移说明：新 common BaseDO 使用 delFlag(@TableLogic)，源 isDeleted/deletedTime
 * 字段不存在，逻辑删除交由 MP removeById 处理；pageQuery 降级为 List(TODO[page-meta])。</p>
 *
 * @author sijiwang
 */
@Slf4j
@Repository("userShipAddressRepositoryImpl")
public class ShipAddressRepositoryImpl extends ServiceImpl<UserShipAddressDAO, ShipAddressDO> implements ShipAddressRepository {

    @Override
    public ShipAddressDTO save(ShipAddressDTO dto) {
        Assert.notNull(dto, "保存收货地址失败：地址DTO不能为空");
        Assert.notNull(dto.getAccountId(), "保存收货地址失败：账号ID不能为空");
        Assert.hasText(dto.getShipName(), "保存收货地址失败：收货人姓名不能为空");
        Assert.hasText(dto.getShipPhone(), "保存收货地址失败：收货人手机号不能为空");

        ShipAddressDO entity = TransferUtils.transfer(dto, ShipAddressDO::new);
        boolean saveResult = this.save(entity);
        if (!saveResult) {
            log.error("保存收货地址失败，账号ID：{}，DO：{}", dto.getAccountId(), entity);
            throw new RuntimeException(String.format("保存收货地址失败，账号ID：%s", dto.getAccountId()));
        }
        return TransferUtils.transfer(entity, ShipAddressDTO::new);
    }

    @Override
    public boolean updateById(ShipAddressDTO dto) {
        Assert.notNull(dto, "更新收货地址失败：地址DTO不能为空");
        Assert.notNull(dto.getId(), "更新收货地址失败：地址ID不能为空");
        Assert.notNull(dto.getAccountId(), "更新收货地址失败：账号ID不能为空");

        ShipAddressDO entity = TransferUtils.transfer(dto, ShipAddressDO::new);
        boolean updateResult = this.updateById(entity);
        log.info("更新收货地址结果：{}，地址ID：{}，账号ID：{}", updateResult, dto.getId(), dto.getAccountId());
        return updateResult;
    }

    @Override
    public Optional<ShipAddressDTO> findById(Long id) {
        return Optional.ofNullable(id)
                .map(this::getById)
                .map(entity -> TransferUtils.transfer(entity, ShipAddressDTO::new));
    }

    @Override
    public Optional<ShipAddressDTO> findDefaultByAccountId(Long accountId) {
        Assert.notNull(accountId, "查询默认收货地址失败：账号ID不能为空");

        LambdaQueryWrapper<ShipAddressDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShipAddressDO::getAccountId, accountId)
                .eq(ShipAddressDO::getIsDefault, CommonEnum.YesOrNo.YES);

        return Optional.ofNullable(this.getOne(wrapper))
                .map(entity -> TransferUtils.transfer(entity, ShipAddressDTO::new));
    }

    @Override
    public List<ShipAddressDTO> findByAccountId(Long accountId) {
        Assert.notNull(accountId, "查询收货地址失败：账号ID不能为空");

        LambdaQueryWrapper<ShipAddressDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShipAddressDO::getAccountId, accountId)
                .orderByDesc(ShipAddressDO::getIsDefault)
                .orderByDesc(ShipAddressDO::getUpdateTime);

        List<ShipAddressDO> entityList = this.list(wrapper);
        return Optional.ofNullable(entityList)
                .map(list -> TransferUtils.transfers(list, ShipAddressDTO::new))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<ShipAddressDTO> pageQuery(ShipAddressPageReq req) {
        Assert.notNull(req, "分页查询收货地址失败：分页请求参数不能为空");
        Assert.notNull(req.getAccountId(), "分页查询收货地址失败：账号ID不能为空（数据隔离）");

        // TODO[page-meta] 领域层降级为 List，total/pages 元数据不再上抛。
        Page<ShipAddressDO> page = new Page<>(req.getPageNo(), req.getPageSize());
        LambdaQueryWrapper<ShipAddressDO> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(ShipAddressDO::getAccountId, req.getAccountId());
        Optional.ofNullable(req.getShipName())
                .filter(str -> !str.trim().isEmpty())
                .ifPresent(name -> wrapper.like(ShipAddressDO::getShipName, name));
        Optional.ofNullable(req.getShipPhone())
                .filter(str -> !str.trim().isEmpty())
                .ifPresent(phone -> wrapper.like(ShipAddressDO::getShipPhone, phone));
        Optional.ofNullable(req.getIsDefault())
                .ifPresent(isDefault -> wrapper.eq(ShipAddressDO::getIsDefault, CommonEnum.YesOrNo.getByCode(isDefault)));
        Optional.ofNullable(req.getRoleType())
                .ifPresent(roleType -> wrapper.eq(ShipAddressDO::getRoleType, roleType));

        wrapper.orderByDesc(ShipAddressDO::getIsDefault)
                .orderByDesc(ShipAddressDO::getUpdateTime);

        Page<ShipAddressDO> resultPage = this.page(page, wrapper);
        return TransferUtils.transfers(resultPage.getRecords(), ShipAddressDTO::new);
    }

    @Override
    public boolean logicDeleteById(Long id, String operator) {
        Assert.notNull(id, "删除收货地址失败：地址ID不能为空");
        Assert.hasText(operator, "删除收货地址失败：操作人不能为空");

        // 逻辑删除交由 MP @TableLogic(delFlag) 处理。
        boolean deleteResult = this.removeById(id);
        log.info("逻辑删除收货地址结果：{}，地址ID：{}，操作人：{}", deleteResult, id, operator);
        return deleteResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefault(Long id, Long accountId, String operator) {
        Assert.notNull(id, "设置默认地址失败：地址ID不能为空");
        Assert.notNull(accountId, "设置默认地址失败：账号ID不能为空");
        Assert.hasText(operator, "设置默认地址失败：操作人不能为空");

        log.info("开始设置默认地址，地址ID：{}，账号ID：{}，操作人：{}", id, accountId, operator);

        LambdaQueryWrapper<ShipAddressDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShipAddressDO::getAccountId, accountId)
                .eq(ShipAddressDO::getIsDefault, CommonEnum.YesOrNo.YES)
                .ne(ShipAddressDO::getId, id);

        ShipAddressDO cancelDefault = new ShipAddressDO();
        cancelDefault.setIsDefault(CommonEnum.YesOrNo.NO);
        cancelDefault.setUpdateTime(LocalDateTime.now());
        boolean cancelResult = this.update(cancelDefault, wrapper);
        log.info("取消其他默认地址结果：{}，账号ID：{}", cancelResult, accountId);

        ShipAddressDO setDefault = new ShipAddressDO();
        setDefault.setId(id);
        setDefault.setIsDefault(CommonEnum.YesOrNo.YES);
        setDefault.setUpdateTime(LocalDateTime.now());
        boolean setResult = this.updateById(setDefault);
        log.info("设置当前地址为默认结果：{}，地址ID：{}", setResult, id);

        return setResult;
    }
}
