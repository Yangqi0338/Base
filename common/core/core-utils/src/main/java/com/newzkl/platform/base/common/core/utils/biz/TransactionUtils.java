package com.newzkl.platform.base.common.core.utils.biz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 编程式事务工具类，简化事务操作，支持函数式编程
 *
 * @author fang
 */
@Slf4j
@Component
@Lazy
public class TransactionUtils {

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 执行有返回值的事务操作
     *
     * @param callback 事务回调函数
     * @param <T>      返回值类型
     * @return 业务处理结果
     */
    public <T> T execute(TransactionCallback<T> callback) {
        try {
            return transactionTemplate.execute(callback);
        } catch (Exception e) {
            log.error("事务执行失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 执行无返回值的事务操作
     *
     * @param action 事务操作
     */
    public void executeWithoutResult(Runnable action) {
        execute(status -> {
            try {
                action.run();
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
            return null;
        });
    }

    /**
     * 执行有返回值的事务操作，并处理异常
     *
     * @param callback     事务回调函数
     * @param errorHandler 异常处理器
     * @param <T>          返回值类型
     * @return 业务处理结果
     */
    public <T> T executeWithErrorHandler(TransactionCallback<T> callback, TransactionErrorHandler<T> errorHandler) {
        try {
            return transactionTemplate.execute(callback);
        } catch (Exception e) {
            log.error("事务执行失败，进入异常处理", e);
            return errorHandler.handle(e);
        }
    }

    /**
     * 带超时设置的事务执行（有返回值）
     *
     * @param callback 事务回调函数
     * @param timeout  超时时间（秒）
     * @param <T>      返回值类型
     * @return 业务处理结果
     */
    public <T> T executeWithTimeout(TransactionCallback<T> callback, int timeout) {
        int originalTimeout = transactionTemplate.getTimeout();
        try {
            transactionTemplate.setTimeout(timeout);
            return execute(callback);
        } finally {
            transactionTemplate.setTimeout(originalTimeout);
        }
    }

    /**
     * 事务异常处理器接口
     *
     * @param <T> 返回值类型
     */
    @FunctionalInterface
    public interface TransactionErrorHandler<T> {
        T handle(Exception e);
    }
}
