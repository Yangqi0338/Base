package com.newzkl.platform.base.biz.user.domain.service.impl;

import com.newzkl.platform.base.biz.user.domain.adapt.repository.ShipAddressRepository;
import com.newzkl.platform.base.biz.user.domain.service.ShipAddressDomain;
import com.newzkl.platform.base.biz.user.model.relation.dto.ShipAddressDTO;
import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressAddReq;
import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressPageReq;
import com.newzkl.platform.base.biz.user.model.relation.req.ShipAddressUpdateReq;
import com.newzkl.platform.base.biz.user.model.relation.vo.ShipAddressRpcVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.ShipAddressVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 收货地址领域服务实现
 *
 * <p>依赖仓储完成数据访问，专注于业务规则校验和核心逻辑处理。</p>
 *
 * @author sijiwang
 */
@Slf4j
@Service("userShipAddressDomainImpl")
@RequiredArgsConstructor
public class ShipAddressDomainImpl implements ShipAddressDomain {

    private final ShipAddressRepository shipAddressRepository;

    /**
     * 校验编码和名称的非空一致性
     *
     * @param code      编码字段
     * @param name      名称字段
     * @param fieldName 字段名称（如“省份”）
     */
    private void validateCodeAndNameConsistency(String code, String name, String fieldName) {
        boolean codeEmpty = !StringUtils.hasText(code);
        boolean nameEmpty = !StringUtils.hasText(name);
        if (codeEmpty ^ nameEmpty) {
            log.error("{}编码和名称不能一个为空、一个非空，编码：{}，名称：{}", fieldName, code, name);
            throw new PlatformException(BaseErrorCode.PARAM, fieldName + "编码和名称必须同时为空或同时非空");
        }
    }

    /**
     * 拼接省市区街道完整地址
     *
     * @param province 省份名称
     * @param city     城市名称
     * @param area     区县名称
     * @param street   街道名称（可选）
     * @return 拼接后的地址字符串
     */
    private String buildFullRegion(String province, String city, String area, String street) {
        List<String> regionParts = new ArrayList<>();
        if (StringUtils.hasText(province)) {
            regionParts.add(province);
        }
        if (StringUtils.hasText(city)) {
            regionParts.add(city);
        }
        if (StringUtils.hasText(area)) {
            regionParts.add(area);
        }
        if (StringUtils.hasText(street)) {
            regionParts.add(street);
        }
        return String.join(",", regionParts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShipAddressVO add(ShipAddressAddReq req) {
        log.info("开始新增收货地址，账号ID：{}，收货人：{}", req.getAccountId(), req.getShipName());

        validateCodeAndNameConsistency(req.getShipProvinceCode(), req.getShipProvinceName(), "省份");
        validateCodeAndNameConsistency(req.getShipCityCode(), req.getShipCityName(), "城市");
        validateCodeAndNameConsistency(req.getShipAreaCode(), req.getShipAreaName(), "区县");
        validateCodeAndNameConsistency(req.getShipStreetCode(), req.getShipStreetName(), "街道");

        ShipAddressDTO dto = TransferUtils.transfer(req, ShipAddressDTO::new, (source, target) -> {
            target.setCreateTime(LocalDateTime.now());
            target.setUpdateTime(LocalDateTime.now());
            target.setIsDeleted(0);
            target.setShipFullRegion(buildFullRegion(
                    source.getShipProvinceName(),
                    source.getShipCityName(),
                    source.getShipAreaName(),
                    source.getShipStreetName()
            ));
        });

        ShipAddressDTO savedDto = shipAddressRepository.save(dto);
        log.info("收货地址保存成功，ID：{}", savedDto.getId());

        if (savedDto.getIsDefault() != null && savedDto.getIsDefault() == 1) {
            boolean setDefaultResult = shipAddressRepository.setDefault(
                    savedDto.getId(), savedDto.getAccountId(), req.getOperator()
            );
            log.info("设置默认地址结果：{}，地址ID：{}", setDefaultResult, savedDto.getId());
        }

        ShipAddressVO vo = TransferUtils.transfer(savedDto, ShipAddressVO::new);
        log.info("新增收货地址完成，VO：{}", vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShipAddressVO update(ShipAddressUpdateReq req) {
        log.info("开始更新收货地址，地址ID：{}", req.getId());

        Optional<ShipAddressDTO> dtoOpt = shipAddressRepository.findById(req.getId());
        if (dtoOpt.isEmpty()) {
            log.error("更新收货地址失败，地址ID：{} 不存在", req.getId());
            throw new PlatformException(BaseErrorCode.NODATA, "地址不存在");
        }
        ShipAddressDTO oldDto = dtoOpt.get();

        if (StringUtils.hasText(req.getShipProvinceCode()) || StringUtils.hasText(req.getShipProvinceName())) {
            validateCodeAndNameConsistency(req.getShipProvinceCode(), req.getShipProvinceName(), "省份");
        }
        if (StringUtils.hasText(req.getShipCityCode()) || StringUtils.hasText(req.getShipCityName())) {
            validateCodeAndNameConsistency(req.getShipCityCode(), req.getShipCityName(), "城市");
        }
        if (StringUtils.hasText(req.getShipAreaCode()) || StringUtils.hasText(req.getShipAreaName())) {
            validateCodeAndNameConsistency(req.getShipAreaCode(), req.getShipAreaName(), "区县");
        }
        if (StringUtils.hasText(req.getShipStreetCode()) || StringUtils.hasText(req.getShipStreetName())) {
            validateCodeAndNameConsistency(req.getShipStreetCode(), req.getShipStreetName(), "街道");
        }

        ShipAddressDTO dto = TransferUtils.transfer(req, ShipAddressDTO::new, (source, target) -> {
            target.setUpdateTime(LocalDateTime.now());
            target.setAccountId(oldDto.getAccountId());
            target.setShipName(StringUtils.hasText(source.getShipName()) ? source.getShipName() : oldDto.getShipName());
            target.setShipPhone(StringUtils.hasText(source.getShipPhone()) ? source.getShipPhone() : oldDto.getShipPhone());
            target.setShipProvinceCode(StringUtils.hasText(source.getShipProvinceCode()) ? source.getShipProvinceCode() : oldDto.getShipProvinceCode());
            target.setShipProvinceName(StringUtils.hasText(source.getShipProvinceName()) ? source.getShipProvinceName() : oldDto.getShipProvinceName());
            target.setShipCityCode(StringUtils.hasText(source.getShipCityCode()) ? source.getShipCityCode() : oldDto.getShipCityCode());
            target.setShipCityName(StringUtils.hasText(source.getShipCityName()) ? source.getShipCityName() : oldDto.getShipCityName());
            target.setShipAreaCode(StringUtils.hasText(source.getShipAreaCode()) ? source.getShipAreaCode() : oldDto.getShipAreaCode());
            target.setShipAreaName(StringUtils.hasText(source.getShipAreaName()) ? source.getShipAreaName() : oldDto.getShipAreaName());
            target.setShipStreetCode(StringUtils.hasText(source.getShipStreetCode()) ? source.getShipStreetCode() : oldDto.getShipStreetCode());
            target.setShipStreetName(StringUtils.hasText(source.getShipStreetName()) ? source.getShipStreetName() : oldDto.getShipStreetName());
            target.setShipDetailAddress(StringUtils.hasText(source.getShipDetailAddress()) ? source.getShipDetailAddress() : oldDto.getShipDetailAddress());
            target.setShipZipCode(StringUtils.hasText(source.getShipZipCode()) ? source.getShipZipCode() : oldDto.getShipZipCode());
            target.setIsDefault(source.getIsDefault() != null ? source.getIsDefault() : oldDto.getIsDefault());
            target.setRoleType(source.getRoleType() != null ? source.getRoleType() : oldDto.getRoleType());
            target.setRole(source.getRole() != null ? source.getRole() : oldDto.getRole());
            target.setIsDeleted(oldDto.getIsDeleted());
            target.setCreateTime(oldDto.getCreateTime());
            target.setShipFullRegion(buildFullRegion(
                    target.getShipProvinceName(),
                    target.getShipCityName(),
                    target.getShipAreaName(),
                    target.getShipStreetName()
            ));
        });

        boolean updateResult = shipAddressRepository.updateById(dto);
        if (!updateResult) {
            log.error("更新收货地址失败，地址ID：{}", req.getId());
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "更新地址失败");
        }
        log.info("收货地址更新成功，地址ID：{}", req.getId());

        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            boolean setDefaultResult = shipAddressRepository.setDefault(
                    dto.getId(), oldDto.getAccountId(), req.getOperator()
            );
            log.info("更新默认地址结果：{}，地址ID：{}", setDefaultResult, dto.getId());
        }

        ShipAddressDTO updatedDto = shipAddressRepository.findById(req.getId())
                .orElseThrow(() -> new PlatformException(BaseErrorCode.NODATA, "地址更新后查询失败"));
        ShipAddressVO vo = TransferUtils.transfer(updatedDto, ShipAddressVO::new);
        log.info("更新收货地址完成，VO：{}", vo);
        return vo;
    }

    @Override
    public ShipAddressVO getById(Long id) {
        log.info("查询收货地址详情，地址ID：{}", id);

        ShipAddressDTO dto = shipAddressRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("查询收货地址失败，地址ID：{} 不存在", id);
                    return new PlatformException(BaseErrorCode.NODATA, "地址不存在");
                });

        ShipAddressVO vo = TransferUtils.transfer(dto, ShipAddressVO::new);
        log.info("查询收货地址成功，VO：{}", vo);
        return vo;
    }

    @Override
    public ShipAddressVO getDefaultByAccountId(Long accountId) {
        log.info("查询默认收货地址，账号ID：{}", accountId);

        Optional<ShipAddressDTO> dtoOpt = shipAddressRepository.findDefaultByAccountId(accountId);
        if (dtoOpt.isEmpty()) {
            log.info("账号ID：{} 无默认收货地址", accountId);
            return null;
        }

        ShipAddressVO vo = TransferUtils.transfer(dtoOpt.get(), ShipAddressVO::new);
        log.info("查询默认收货地址成功，VO：{}", vo);
        return vo;
    }

    @Override
    public List<ShipAddressVO> listByAccountId(Long accountId) {
        log.info("查询账号下所有收货地址，账号ID：{}", accountId);

        List<ShipAddressDTO> dtoList = shipAddressRepository.findByAccountId(accountId);
        log.info("查询到收货地址数量：{}，账号ID：{}", dtoList.size(), accountId);

        return TransferUtils.transfers(dtoList, ShipAddressVO::new);
    }

    @Override
    public List<ShipAddressVO> pageQuery(ShipAddressPageReq req) {
        log.info("分页查询收货地址，参数：{}", req);

        // TODO[page-meta] 仓储降级为 List，total/pages 元数据跨层丢失。
        List<ShipAddressDTO> dtoList = shipAddressRepository.pageQuery(req);
        return TransferUtils.transfers(dtoList, ShipAddressVO::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id, String operator) {
        log.info("逻辑删除收货地址，地址ID：{}，操作人：{}", id, operator);

        Optional<ShipAddressDTO> dtoOpt = shipAddressRepository.findById(id);
        if (dtoOpt.isEmpty()) {
            log.error("删除收货地址失败，地址ID：{} 不存在", id);
            throw new PlatformException(BaseErrorCode.NODATA, "地址不存在");
        }

        boolean deleteResult = shipAddressRepository.logicDeleteById(id, operator);
        log.info("逻辑删除收货地址结果：{}，地址ID：{}", deleteResult, id);
        return deleteResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefault(Long id, String operator) {
        log.info("设置默认收货地址，地址ID：{}，操作人：{}", id, operator);

        Optional<ShipAddressDTO> dtoOpt = shipAddressRepository.findById(id);
        if (dtoOpt.isEmpty()) {
            log.error("设置默认地址失败，地址ID：{} 不存在", id);
            throw new PlatformException(BaseErrorCode.NODATA, "地址不存在");
        }

        boolean setDefaultResult = shipAddressRepository.setDefault(
                id, dtoOpt.get().getAccountId(), operator
        );
        log.info("设置默认收货地址结果：{}，地址ID：{}", setDefaultResult, id);
        return setDefaultResult;
    }

    @Override
    public ShipAddressRpcVO getAddressDetail(Long id, Long accountId) {
        log.info("查询收货地址详情，地址ID：{}，账号ID：{}", id, accountId);

        if (id == null && accountId == null) {
            log.error("查询收货地址失败，账号ID不能为空");
            throw new PlatformException(BaseErrorCode.PARAM, "ID 和 账号ID不能都为空");
        }

        ShipAddressDTO addressDTO = null;

        if (id != null) {
            Optional<ShipAddressDTO> dtoOpt = shipAddressRepository.findById(id);
            if (dtoOpt.isPresent()) {
                addressDTO = dtoOpt.get();
                log.info("根据地址ID查询到地址，ID：{}", id);
            } else {
                log.warn("根据地址ID未查询到地址，ID：{}", id);
            }
        }

        if (addressDTO == null) {
            Optional<ShipAddressDTO> defaultDtoOpt = shipAddressRepository.findDefaultByAccountId(accountId);
            if (defaultDtoOpt.isPresent()) {
                addressDTO = defaultDtoOpt.get();
                log.info("根据账号ID查询到默认地址，账号ID：{}，地址ID：{}", accountId, addressDTO.getId());
            } else {
                log.warn("账号ID：{} 无默认收货地址，尝试查询首个地址", accountId);

                List<ShipAddressDTO> dtoList = shipAddressRepository.findByAccountId(accountId);
                if (!dtoList.isEmpty()) {
                    addressDTO = dtoList.get(0);
                    log.info("账号ID：{} 查询到首个地址，地址ID：{}", accountId, addressDTO.getId());
                } else {
                    log.warn("账号ID：{} 无任何收货地址", accountId);
                    return null;
                }
            }
        }

        ShipAddressRpcVO rpcVO = TransferUtils.transfer(addressDTO, ShipAddressRpcVO::new);
        log.info("查询收货地址详情完成，RpcVO：{}", rpcVO);
        return rpcVO;
    }
}
