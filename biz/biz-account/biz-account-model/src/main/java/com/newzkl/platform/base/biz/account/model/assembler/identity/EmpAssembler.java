package com.newzkl.platform.base.biz.account.model.assembler.identity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.biz.account.model.res.EmpOutRes;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;

/**
 * 员工
 *
 * <p>{@code jobIdList} 在 {@code EmpVO} 侧是落库列形态 (逗号拼接的字符串),
 * 在 {@code EmpOutRes} 侧是出参形态 ({@code List<String>}), 故显式给出两向转换方法,
 * 由 MapStruct 自动挑用</p>
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface EmpAssembler extends BaseAssembler<EmpOutRes, EmpVO> {

    EmpOutRes vo2OutRes(EmpVO identityVO);

    EmpRes vo2Res(EmpVO empVO);

    void account2Res(AccountVO accountVO, @MappingTarget EmpRes res);

    /**
     * 逗号拼接列拆成列表
     *
     * @param value 逗号拼接的岗位 ID 串, 允许末尾带逗号
     * @return 岗位 ID 列表, 入参为空时返回空列表
     */
    default List<String> splitIdList(String value) {
        if (StrUtil.isBlank(value)) {
            return Collections.emptyList();
        }
        return StrUtil.split(value, ',', true, true);
    }

    /**
     * 列表拼回逗号列
     *
     * <p>按 {@code *_id_list} 列约定补末位逗号, 便于 SQL 侧 {@code like CONCAT(#{id}, ',%')} 右模糊</p>
     *
     * @param value 岗位 ID 列表
     * @return 逗号拼接串, 入参为空时返回 {@code null}
     */
    default String joinIdList(List<String> value) {
        if (CollUtil.isEmpty(value)) {
            return null;
        }
        return CollUtil.join(value, ",") + ",";
    }
}
