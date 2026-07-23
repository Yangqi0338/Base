package com.newzkl.platform.base.biz.goods.model.assembler;

import com.newzkl.platform.base.biz.goods.model.goods.req.report.ReportReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 报告装配器。
 *
 * <p>迁移调整: 原实现依赖 {@code BaseConvert.list2Str(List<String>)}, 但 {@code ReportReq}
 * 的 {@code categoryIdList/spuIdList} 为 {@code List<Long>}, 类型不匹配, 故改用接口内
 * {@code default} 方法转换; 同时忽略查询继承而来的 {@code createTime}(String[] → LocalDateTime 不可映射)。</p>
 *
 * @author kc
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportAssembler extends BaseAssembler<ReportReq, ReportVO> {

    @Override
    @Mappings({
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "categoryIdList", expression = "java(longListToStr(entity.getCategoryIdList()))"),
            @Mapping(target = "spuIdList", expression = "java(longListToStr(entity.getSpuIdList()))"),
    })
    ReportVO req2VO(ReportReq entity);

    /**
     * 将 {@code List<Long>} 以英文逗号拼接为字符串, 空集合返回 {@code null}。
     *
     * @param list 待拼接的 ID 列表
     * @return 逗号拼接字符串, 入参为空返回 {@code null}
     */
    default String longListToStr(List<Long> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
