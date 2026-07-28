package com.newzkl.platform.base.biz.order.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.order.application.service.OrderStateRecordService;
import com.newzkl.platform.base.biz.order.domain.service.OrderStateRecordDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderStateRecord;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateRecordVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单状态记录 应用层服务实现类
 *
 * @author sijiwang
 * @since 2026-01-30
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderStateRecordServiceImpl implements OrderStateRecordService {

    private final OrderStateRecordDomain orderStateRecordDomainService;

    /**
     * 新增订单状态记录（支持分布式事务）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderStateRecordVO create(OrderStateRecordCreateReq req) {
        try {
            log.info("开始新增订单状态记录，订单ID：{}，变更后状态：{}", req.getOrderId(), req.getAfterOrderState());

            // 1. Req转换为领域Entity
            OrderStateRecord entity = new OrderStateRecord();
            TransferUtils.transfer(req, entity);

            // 2. 调用领域服务
            OrderStateRecord resultEntity = orderStateRecordDomainService.create(entity);

            // 3. Entity转换为VO返回
            OrderStateRecordVO resultVO = new OrderStateRecordVO();
            TransferUtils.transfer(resultEntity, resultVO);

            log.info("新增订单状态记录成功，记录ID：{}", resultVO.getId());
            return resultVO;
        } catch (Exception e) {
            log.error("新增订单状态记录失败，原因：{}", e.getMessage(), e);
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "新增订单状态记录失败：" + e.getMessage());
        }
    }

    /**
     * 修改订单状态记录（支持分布式事务）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderStateRecordVO update(OrderStateRecordUpdateReq req) {
        try {
            log.info("开始修改订单状态记录，记录ID：{}", req.getId());

            // 1. 先查询原实体（保证修改的是存在的记录）
            List<OrderStateRecord> existList = orderStateRecordDomainService.listByOrderNo(req.getOrderNo());
            OrderStateRecord oldEntity = existList.stream()
                    .filter(e -> e.getId().equals(req.getId()))
                    .findFirst()
                    .orElse(null);
            if (oldEntity == null) {
                throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "订单状态记录不存在，ID：" + req.getId());
            }

            // 2. Req转换为领域Entity（仅覆盖需要修改的字段）
            OrderStateRecord entity = new OrderStateRecord();
            TransferUtils.transfer(req, entity);
            // 保留创建时间等系统字段
            entity.setCreateTime(oldEntity.getCreateTime());

            // 3. 调用领域服务
            OrderStateRecord resultEntity = orderStateRecordDomainService.update(entity);

            // 4. Entity转换为VO返回
            OrderStateRecordVO resultVO = new OrderStateRecordVO();
            TransferUtils.transfer(resultEntity, resultVO);

            log.info("修改订单状态记录成功，记录ID：{}", resultVO.getId());
            return resultVO;
        } catch (Exception e) {
            log.error("修改订单状态记录失败，记录ID：{}，原因：{}", req.getId(), e.getMessage(), e);
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "修改订单状态记录失败：" + e.getMessage());
        }
    }

    /**
     * 按ID查询订单状态记录
     */
    @Override
    public OrderStateRecordVO getById(Long id) {
        try {
            log.info("查询订单状态记录，记录ID：{}", id);

            // 先通过订单ID查询所有记录，再过滤ID（适配领域服务无单独getById的场景）
            List<OrderStateRecord> entityList = orderStateRecordDomainService.listByOrderNo(null);
            OrderStateRecord entity = entityList.stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            
            if (entity == null) {
                return null;
            }

            OrderStateRecordVO vo = new OrderStateRecordVO();
            TransferUtils.transfer(entity, vo);
            return vo;
        } catch (Exception e) {
            log.error("查询订单状态记录失败，记录ID：{}，原因：{}", id, e.getMessage(), e);
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "查询订单状态记录失败：" + e.getMessage());
        }
    }

    /**
     * 按订单ID查询所有状态记录
     */
    @Override
    public List<OrderStateRecordVO> listByOrderNo(String orderNo) {
        try {
            log.info("查询订单状态记录列表，订单ID：{}", orderNo);

            List<OrderStateRecord> entityList = orderStateRecordDomainService.listByOrderNo(orderNo);
            // Entity列表转换为VO列表
            return entityList.stream().map(entity -> {
                OrderStateRecordVO vo = new OrderStateRecordVO();
                TransferUtils.transfer(entity, vo);
                return vo;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("查询订单状态记录列表失败，订单ID：{}，原因：{}", orderNo, e.getMessage(), e);
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "查询订单状态记录列表失败：" + e.getMessage());
        }
    }

    /**
     * 按订单ID和变更后状态查询记录
     */
    @Override
    public Optional<OrderStateRecordVO> findByOrderNoAndAfterOrderState(String orderNo, Integer afterOrderState) {
        try {
            log.info("查询订单状态记录，订单ID：{}，变更后状态：{}", orderNo, afterOrderState);

            Optional<OrderStateRecord> entityOpt = orderStateRecordDomainService.findByOrderIdAndAfterOrderState(orderNo, afterOrderState);
            // Entity转换为VO
            return entityOpt.map(entity -> {
                OrderStateRecordVO vo = new OrderStateRecordVO();
                TransferUtils.transfer(entity, vo);
                return vo;
            });
        } catch (Exception e) {
            log.error("查询订单状态记录失败，订单ID：{}，变更后状态：{}，原因：{}", orderNo, afterOrderState, e.getMessage(), e);
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "查询订单状态记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据操作时间范围查询记录
     */
    @Override
    public List<OrderStateRecordVO> findByOperateTimeBetween(LocalDateTime startTime, LocalDateTime endTime, int limit) {
        try {
            log.info("查询订单状态记录，操作时间范围：{} - {}，限制条数：{}", startTime, endTime, limit);

            List<OrderStateRecord> entityList = orderStateRecordDomainService.findByOperateTimeBetween(startTime, endTime, limit);
            // Entity列表转换为VO列表
            return entityList.stream().map(entity -> {
                OrderStateRecordVO vo = new OrderStateRecordVO();
                TransferUtils.transfer(entity, vo);
                return vo;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("查询订单状态记录失败，操作时间范围：{} - {}，原因：{}", startTime, endTime, e.getMessage(), e);
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "查询订单状态记录失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询订单状态记录
     */
    @Override
    public Page<OrderStateRecordVO> pageQuery(OrderStateRecordPageReq req) {
        try {
            log.info("分页查询订单状态记录，页码：{}，页大小：{}，订单ID：{}，变更后状态：{}",
                    req.getCurrent(), req.getSize(), req.getOrderNo(), req.getAfterOrderState());

            Page<OrderStateRecord> entityPage = orderStateRecordDomainService.findPage(req);

            // 3. 转换为VO分页结果
            Page<OrderStateRecordVO> voPage = new Page<>();
            TransferUtils.transfer(entityPage, voPage);

            // 4. 转换列表数据
            List<OrderStateRecordVO> voList = entityPage.getRecords().stream().map(entity -> {
                OrderStateRecordVO vo = new OrderStateRecordVO();
                TransferUtils.transfer(entity, vo);
                return vo;
            }).collect(Collectors.toList());
            voPage.setRecords(voList);

            return voPage;
        } catch (Exception e) {
            log.error("分页查询订单状态记录失败，原因：{}", e.getMessage(), e);
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "分页查询订单状态记录失败：" + e.getMessage());
        }
    }
}