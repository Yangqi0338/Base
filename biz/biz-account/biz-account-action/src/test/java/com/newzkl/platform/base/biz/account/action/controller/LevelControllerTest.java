package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsInfo;
import com.newzkl.platform.base.biz.account.domain.service.LevelDomain;
import com.newzkl.platform.base.biz.account.model.level.req.LevelQuery;
import com.newzkl.platform.base.biz.account.model.level.req.LevelReq;
import com.newzkl.platform.base.biz.account.model.level.res.LevelRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link LevelController} 入参与出参契约测试。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("等级控制器")
class LevelControllerTest {

    @Mock
    private LevelDomain levelDomain;

    @InjectMocks
    private LevelController levelController;

    @Test
    @DisplayName("等级礼包: 透传角色 ID, 端口未接线时返回 null")
    void levelPackShouldPassRoleId() {
        when(levelDomain.levelPack(1002)).thenReturn(null);

        PlatformResult<PackGoodsInfo> result = levelController.levelPack(1002);

        assertNull(result.getData());
        verify(levelDomain).levelPack(1002);
    }

    @Test
    @DisplayName("保存: 委托领域层且返回空数据结果")
    void saveShouldDelegate() {
        LevelReq req = new LevelReq();

        PlatformResult<Void> result = levelController.save(req);

        assertNull(result.getData());
        verify(levelDomain).save(req);
    }

    @Test
    @DisplayName("列表: 保持旧契约返回列表, 无数据为空集合")
    void levelListShouldReturnList() {
        when(levelDomain.pageList(any())).thenReturn(new ArrayList<>());

        PlatformResult<List<LevelRes>> result = levelController.levelList(new LevelQuery());

        assertTrue(result.getData().isEmpty());
    }

    @Test
    @DisplayName("列表: 透传领域层结果")
    void levelListShouldPassThroughRecords() {
        LevelRes res = new LevelRes();
        res.setId(70001L);
        when(levelDomain.pageList(any())).thenReturn(List.of(res));

        assertEquals(70001L, levelController.levelList(new LevelQuery()).getData().get(0).getId());
    }
}
