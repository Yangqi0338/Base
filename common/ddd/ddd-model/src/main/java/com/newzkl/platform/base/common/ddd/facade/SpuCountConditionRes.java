package com.newzkl.platform.base.common.ddd.facade;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

@Data
@NoArgsConstructor
public class SpuCountConditionRes extends ArrayList<Map<String, Object>> implements Serializable {

}
