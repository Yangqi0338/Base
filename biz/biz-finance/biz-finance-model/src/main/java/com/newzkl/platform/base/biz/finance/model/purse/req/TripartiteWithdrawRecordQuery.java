package com.newzkl.platform.base.biz.finance.model.purse.req;


import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author niu
 * @description:
 * @date 2024/1/5 18:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TripartiteWithdrawRecordQuery extends BusinessPageQuery {

    @AllArgsConstructor
    @Getter
    public enum SortType implements SortField {
        /**
         * 默认
         */
        DEFAULT(0, "create_time"),
        ;

        private final Integer code;
        private final String field;

        @Override
        public String getField(Integer code) {
            return Arrays.stream(SortType.values()).filter(item -> item.getCode().equals(code)).findFirst()
                    .map(SortType::getField).orElse(null);
        }
    }

}
