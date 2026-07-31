package com.newzkl.platform.base.biz.order.domain.service.flow.model;

import java.util.List;
import java.util.Map;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/159:31
 */
public abstract class Engine {

    public Map<String, Node> nodeMap;
    public Context context;

    public void execute(String nodeName){
        List<NextLine> nextLineList = nodeMap.get(nodeName).getNext();
        for (NextLine nextLine : nextLineList) {

        }
    }
}
