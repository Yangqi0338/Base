package com.newzkl.platform.base.common.ddd.action.config;

import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 全局异常处理器 QDox 字段业务名提取测试
 *
 * <p>验证开发期磁盘读源码路径: 取本测试类字段 javadoc 首行, 拼默认校验文案。</p>
 *
 * @author KC
 */
class GlobalExceptionHandlerTest {

    /**
     * 首行截断: 多行注释仅取首个业务语义片段
     */
    @Test
    void firstLineTruncatesAtStop() throws Exception {
        String head = invokeFirstLine("所属课程ID, 关联课程表主键");
        assertEquals("所属课程ID", head);
    }

    /**
     * 空注释返回空串
     */
    @Test
    void firstLineBlankReturnsEmpty() throws Exception {
        assertEquals("", invokeFirstLine("  "));
    }

    /**
     * QDox 磁盘读: 取主源码类 {@code BaseReq.id} 的 javadoc(验证跨模块 src/main/java 扫描)
     */
    @Test
    void qdoxReadsFieldCommentFromDisk() throws Exception {
        JavaClass javaClass = CommonUtil.findJavaClass(com.newzkl.platform.base.common.ddd.model.req.BaseReq.class);
        JavaField field = CommonUtil.findJavaField(javaClass, "id");
        assertEquals("id", field.getComment().trim());
    }

    /**
     * 反射调用私有 firstLine
     *
     * @param comment 注释体
     * @return 首行
     */
    private String invokeFirstLine(String comment) throws Exception {
        Method m = GlobalExceptionHandler.class.getDeclaredMethod("firstLine", String.class);
        m.setAccessible(true);
        return (String) m.invoke(new GlobalExceptionHandler(), comment);
    }
}
