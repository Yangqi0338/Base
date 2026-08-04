package com.newzkl.platform.base.biz.account.domain.service.impl;

import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsInfo;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsQuery;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsSaveReq;
import com.newzkl.platform.base.biz.account.domain.repository.LevelRepository;
import com.newzkl.platform.base.biz.account.model.assembler.LevelAssembler;
import com.newzkl.platform.base.biz.account.model.level.req.LevelQuery;
import com.newzkl.platform.base.biz.account.model.level.req.LevelReq;
import com.newzkl.platform.base.biz.account.model.level.res.LevelRes;
import com.newzkl.platform.base.biz.account.model.level.vo.ConditionVO;
import com.newzkl.platform.base.biz.account.model.level.vo.LevelVO;
import com.newzkl.platform.base.biz.account.model.level.vo.PackCondition;
import com.newzkl.platform.base.biz.account.model.level.vo.PermissionVO;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code LevelDomainImpl} 行为测试
 *
 * <p>纯 JUnit 5 + Mockito, 不启动 Spring 容器, 不连数据库。</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("等级领域服务")
class LevelDomainImplTest {

    private static final Long LEVEL_ID = 70001L;
    private static final Long PACK_GOODS_ID = 80002L;

    @Mock
    private LevelRepository levelRepository;

    @Mock
    private PackGoodsApi packGoodsApi;

    private LevelDomainImpl levelDomain;

    /**
     * 以真实转换语义替代 MapStruct 生成实现, 避免测试依赖注解处理器产物
     */
    private final LevelAssembler assembler = new LevelAssembler() {
        @Override
        public LevelVO req2VO(LevelReq entity) {
            return TransferUtils.transfer(entity, LevelVO::new);
        }

        @Override
        public List<LevelVO> req2VO(List<LevelReq> entity) {
            return TransferUtils.transfers(entity, LevelVO::new);
        }

        @Override
        public LevelRes vo2Res(LevelVO it) {
            return TransferUtils.transfer(it, LevelRes::new);
        }
    };

    @BeforeEach
    void setUp() {
        levelDomain = new LevelDomainImpl(levelRepository, assembler, packGoodsApi);
    }

    /**
     * 构造校验可通过的等级入参
     *
     * @return 等级入参
     */
    private LevelReq validReq() {
        PackCondition pack = new PackCondition();
        pack.setAmount(Money.of(199));
        pack.setPackName("入门礼包");
        pack.setPackImg("pack.png");

        ConditionVO condition = new ConditionVO();
        condition.setPack(pack);

        PermissionVO.DirectConfig directConfig = new PermissionVO.DirectConfig();
        directConfig.setDirectPackType(0);
        directConfig.setDirectPack(List.of(0.1D));
        PermissionVO permission = new PermissionVO();
        permission.setDirectConfig(directConfig);

        LevelReq req = new LevelReq();
        req.setType(1002);
        req.setName("一级");
        req.setCondition(condition);
        req.setPermission(permission);
        return req;
    }

    @Test
    @DisplayName("保存: 等级值恒为 1, 礼包端口返回值回填至 packGoodsId")
    void saveShouldForceLevelValueAndFillPackGoodsId() {
        when(packGoodsApi.save(any())).thenReturn(PACK_GOODS_ID);
        when(levelRepository.save(any())).thenReturn(LEVEL_ID);

        Long id = levelDomain.save(validReq());

        assertEquals(LEVEL_ID, id);
        ArgumentCaptor<LevelVO> captor = ArgumentCaptor.forClass(LevelVO.class);
        verify(levelRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getValue(), "等级值应写死为 1");
        assertEquals(PACK_GOODS_ID, captor.getValue().getCondition().getPack().getPackGoodsId(),
                "礼包商品 ID 应由端口返回值回填");

        ArgumentCaptor<PackGoodsSaveReq> packCaptor = ArgumentCaptor.forClass(PackGoodsSaveReq.class);
        verify(packGoodsApi).save(packCaptor.capture());
        assertEquals(1002, packCaptor.getValue().getType());
        assertEquals(1, packCaptor.getValue().getLevel());
        assertEquals(199, packCaptor.getValue().getAmount());
        assertEquals("入门礼包", packCaptor.getValue().getName());
        assertEquals("pack.png", packCaptor.getValue().getImg());
    }

    @Test
    @DisplayName("保存: 升级条件缺失时抛参数异常且不落库")
    void saveShouldRejectWhenConditionMissing() {
        LevelReq req = validReq();
        req.setCondition(null);

        PlatformException ex = assertThrows(PlatformException.class, () -> levelDomain.save(req));
        assertEquals(BaseErrorCode.PARAM.getCode().toString(), ex.getCode());
        verify(levelRepository, never()).save(any());
    }

    @Test
    @DisplayName("保存: 礼包条件缺失时抛参数异常")
    void saveShouldRejectWhenPackMissing() {
        LevelReq req = validReq();
        req.getCondition().setPack(null);

        PlatformException ex = assertThrows(PlatformException.class, () -> levelDomain.save(req));
        assertEquals(BaseErrorCode.PARAM.getCode().toString(), ex.getCode());
        verify(levelRepository, never()).save(any());
    }

    @Test
    @DisplayName("保存: 直推礼包配置无效时抛参数异常")
    void saveShouldRejectWhenDirectPackInactive() {
        LevelReq req = validReq();
        req.getPermission().getDirectConfig().setDirectPackType(1);
        req.getPermission().getDirectConfig().setDirectPack(List.of(0.1D));

        PlatformException ex = assertThrows(PlatformException.class, () -> levelDomain.save(req));
        assertEquals(BaseErrorCode.PARAM.getCode().toString(), ex.getCode());
        verify(levelRepository, never()).save(any());
    }

    @Test
    @DisplayName("更新: 缺少 ID 时抛记录不存在异常")
    void updateShouldRejectWhenIdAbsent() {
        LevelReq req = validReq();

        PlatformException ex = assertThrows(PlatformException.class, () -> levelDomain.update(req));
        assertEquals(BaseErrorCode.NODATA.getCode().toString(), ex.getCode());
        verify(levelRepository, never()).save(any());
    }

    @Test
    @DisplayName("更新: 带 ID 时复用保存逻辑")
    void updateShouldDelegateToSave() {
        when(packGoodsApi.save(any())).thenReturn(PACK_GOODS_ID);
        when(levelRepository.save(any())).thenReturn(LEVEL_ID);
        LevelReq req = validReq();
        req.setId(LEVEL_ID);

        assertEquals(LEVEL_ID, levelDomain.update(req));
        verify(levelRepository).save(any());
    }

    @Test
    @DisplayName("列表: 无数据返回空集合而非 null")
    void listShouldReturnEmptyCollection() {
        when(levelRepository.list(any())).thenReturn(new ArrayList<>());

        List<LevelRes> resList = levelDomain.list(new LevelQuery());

        assertTrue(resList.isEmpty());
    }

    @Test
    @DisplayName("单条查询: 限定 pageSize 为 1 并取首条")
    void findByQueryShouldLimitToOne() {
        LevelVO vo = new LevelVO();
        vo.setId(LEVEL_ID);
        when(levelRepository.list(any())).thenReturn(List.of(vo));
        LevelQuery query = new LevelQuery();

        LevelRes res = levelDomain.findByQuery(query);

        assertEquals(LEVEL_ID, res.getId());
        assertEquals(1, query.getPageSize());
    }

    @Test
    @DisplayName("等级礼包: 按角色与一级等级查询出站端口")
    void levelPackShouldQueryByRoleAndFirstLevel() {
        when(packGoodsApi.findByQuery(any())).thenReturn(null);

        assertNull(levelDomain.levelPack(1002));

        ArgumentCaptor<PackGoodsQuery> captor = ArgumentCaptor.forClass(PackGoodsQuery.class);
        verify(packGoodsApi).findByQuery(captor.capture());
        assertEquals(1002, captor.getValue().getType());
        assertEquals(1, captor.getValue().getLevel());
    }

    @Test
    @DisplayName("等级礼包: 端口返回礼包信息时原样透出")
    void levelPackShouldPassThroughPortResult() {
        PackGoodsInfo info = new PackGoodsInfo();
        info.setId(PACK_GOODS_ID);
        when(packGoodsApi.findByQuery(any())).thenReturn(info);

        assertEquals(PACK_GOODS_ID, levelDomain.levelPack(1002).getId());
    }
}
