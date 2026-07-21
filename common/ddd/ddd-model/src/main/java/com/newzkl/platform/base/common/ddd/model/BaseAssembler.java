package com.newzkl.platform.base.common.ddd.model;

import java.util.List;

/**
 * @author 孔祥基
 * @date 2023/4/13 11:49:17
 * 自定义增强
 */
public interface BaseAssembler<Req, VO> {

    /*
     * 实体类转VO
     * */
    VO req2VO(Req entity);

    List<VO> req2VO(List<Req> entity);

}
