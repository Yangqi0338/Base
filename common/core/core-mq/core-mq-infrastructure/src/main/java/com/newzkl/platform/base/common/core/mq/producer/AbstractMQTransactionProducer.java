package com.newzkl.platform.base.common.core.mq.producer;

import com.newzkl.platform.base.common.core.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * RocketMQ的事务生产者的抽象基类
 */
@Slf4j
public abstract class AbstractMQTransactionProducer extends AbstractMQProducer implements TransactionListener {

//    @Autowired
//    private ILocalMessageService localMessageService;

    public TransactionSendResult sendMessageInTransaction(Message msg, Object arg) throws MQException {
        try {
            TransactionSendResult sendResult = this.getProducer().sendMessageInTransaction(msg, arg);
            if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                log.error("事务消息发送失败，topic : {}, msgObj {}", msg.getTopic(), msg);
                throw new MQException("事务消息发送失败，topic :" + msg.getTopic() + ", status :" + sendResult.getSendStatus());
            }
            log.info("发送事务消息成功，事务id: {}", msg.getTransactionId());
            return sendResult;
        } catch (Exception e) {
            log.error("事务消息发送失败，topic : {}, msgObj {}", msg.getTopic(), msg);
            throw new MQException("事务消息发送失败，topic :" + msg.getTopic() + ",e:" + e.getMessage());
        }
    }

//    @Override
//    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//        boolean exists = localMessageService.exists(null, arg.toString());
//        if (exists) {
//            msg.putUserProperty(LocalMessageEnum.LOCAL_MESSAGE_ID_KEY, arg.toString());
//        }
//        return LocalTransactionState.UNKNOW;
//    }
//
//    @Override
//    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
//        boolean exists = localMessageService.exists(null, msg.getMsgId());
//        return exists ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
//    }
}
