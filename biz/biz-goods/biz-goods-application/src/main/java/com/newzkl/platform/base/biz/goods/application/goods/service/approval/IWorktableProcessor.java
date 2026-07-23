package com.newzkl.platform.base.biz.goods.application.goods.service.approval;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.AuditDataWorkTableRepository;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataWorkTableVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.model.check.AddCommand;
import com.newzkl.platform.base.common.ddd.model.check.CheckCommand;
import com.newzkl.platform.base.common.ddd.model.check.DeleteCommand;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 工单执行
 * @date 2023/8/79:16
 */
@Service
public abstract class IWorktableProcessor<A extends AddCommand, U extends UpdateCommand, D extends DeleteCommand, C extends CheckCommand> {

    @Autowired
    private GoodsQueryService goodsQueryService;
    @Autowired
    private AuditDataWorkTableRepository auditDataWorkTableRepository;
    /**
     * 支持
     * @return
     */
    public abstract Integer support();

    /**
     * 提交工单
     * @param operateType
     * @param addCommand
     * @param updateCommand
     * @param deleteCommand
     */
    public void submitWorkTable(List<Long> spuIdList, Integer operateType, A addCommand, U updateCommand, D deleteCommand){
        if(ObjectUtil.isEmpty(spuIdList)) {
            throw new ScmException(BaseErrorCode.PARAM, "spuId缺少");
        }
        for (Long id : spuIdList) {
            SpuVO spuVO = goodsQueryService.spuVO(id);
            AuditDataWorkTableVO auditDataWorkTableVO = new AuditDataWorkTableVO();
            auditDataWorkTableVO.setSpuId(id);
            auditDataWorkTableVO.setSpuName(spuVO.getName());
            auditDataWorkTableVO.setOperateTarget(support());
            auditDataWorkTableVO.setAccountId(SecurityUtils.getAccountId());
            auditDataWorkTableVO.setOperateType(operateType);
            auditDataWorkTableVO.setSpuInfoJson(JSONObject.toJSONString(spuVO));
            if(addCommand != null){
                auditDataWorkTableVO.setSpuEditInfoJson(JSONObject.toJSONString(addCommand));
            } else if(updateCommand != null){
                auditDataWorkTableVO.setSpuEditInfoJson(JSONObject.toJSONString(updateCommand));
            } else if(deleteCommand != null){
                auditDataWorkTableVO.setSpuEditInfoJson(JSONObject.toJSONString(deleteCommand));
            }
            auditDataWorkTableRepository.auditDataWorkTableSave(auditDataWorkTableRepository.voToAuditDataWorkTable(auditDataWorkTableVO));
        }
    }
    /**
     * 新增
     * @param addCommand
     */
    public abstract void add(String addCommand, String spuAdminEditInfoJson);
    /**
     * 修改
     * @param updateCommand
     */
    public abstract void update(String updateCommand, String spuAdminEditInfoJson);
    /**
     * 删除
     * @param deleteCommand
     */
    public abstract void delete(String deleteCommand, String spuAdminEditInfoJson);
    /**
     * 检查
     * @param checkCommand
     */
    public abstract void check(String checkCommand);
}
