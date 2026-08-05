package com.newzkl.platform.base.biz.goods.domain.interaction.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.interaction.repository.StoreTargetInteractionStatRepository;
import com.newzkl.platform.base.biz.goods.domain.interaction.service.StoreTargetInteractionStatService;
import com.newzkl.platform.base.biz.goods.model.constant.StoreInteractionStatRedisConstant;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum;
import com.newzkl.platform.base.biz.goods.model.goods.entity.interaction.StoreTargetInteractionStat;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.BatchSyncStatReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.InteractionStatReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatBatchReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatPageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.interaction.StoreTargetInteractionStatPageRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.interaction.CacheKeyParam;
import com.newzkl.platform.base.biz.goods.rpc.model.interaction.StoreTargetInteractionEvent;
import com.newzkl.platform.base.biz.goods.rpc.model.interaction.StoreTargetInteractionSummaryObj;
import com.newzkl.platform.base.common.core.redis.lock.impl.RedissonLockUtil;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 门店对象互动统计领域服务实现
 *
 * <p>缓存存储互动统计最终值, 同步任务用缓存值覆盖数据库, 解决查询延迟问题。互动提交、
 * MQ 消费处理、缓存同步任务共用此服务。</p>
 *
 * @author sijiwang
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StoreTargetInteractionStatServiceImpl implements StoreTargetInteractionStatService {

    private final StoreTargetInteractionStatRepository statRepository;

    /** 缓存默认最终值 JSON。 */
    private static final String DEFAULT_FINAL_VALUE_JSON = "{\"viewCount\":0,\"likeCount\":0,\"shareCount\":0}";

    @Override
    public StoreTargetInteractionStat getStat(String targetTypeCode, Long targetId) {
        String redisStatKey = buildRedisStatKey(targetTypeCode, targetId);
        String finalValueJson = RedisUtil.get(redisStatKey);
        if (finalValueJson != null) {
            FinalValueDTO dto = JSONUtil.toBean(finalValueJson, FinalValueDTO.class);
            StoreTargetInteractionStat cacheStat = new StoreTargetInteractionStat();
            cacheStat.setTargetType(targetTypeCode);
            cacheStat.setTargetId(targetId);
            cacheStat.setViewCount(Math.toIntExact(dto.getViewCount()));
            cacheStat.setLikeCount(Math.toIntExact(dto.getLikeCount()));
            cacheStat.setShareCount(Math.toIntExact(dto.getShareCount()));
            return cacheStat;
        }

        StoreTargetInteractionStat dbStat = statRepository.findByStoreTarget(null, targetTypeCode, targetId);
        if (dbStat == null) {
            dbStat = initDefaultStat(targetTypeCode, targetId, null);
        }

        FinalValueDTO dto = new FinalValueDTO(
                Long.valueOf(dbStat.getViewCount()),
                Long.valueOf(dbStat.getLikeCount()),
                Long.valueOf(dbStat.getShareCount()));
        RedisUtil.set(redisStatKey, JSONUtil.toJsonStr(dto));
        RedisUtil.sAdd(StoreInteractionStatRedisConstant.SYNC_CACHE_KEY_SET, redisStatKey);
        return dbStat;
    }

    @Override
    public void submitInteraction(InteractionStatReq requestVO) {
        // TODO[mq-defer]: 源实现经 goodsCountFacade.remoteProcess 异步发 MQ (生产者未迁);
        // 暂降级为同步直调 remoteProcess, #98 回填 MQ 生产者后恢复异步。
        try {
            StoreTargetInteractionEvent event = convertReqToEvent(requestVO);
            remoteProcess(event);
        } catch (Exception e) {
            log.error("互动请求处理失败, requestVO={}", JSONUtil.toJsonStr(requestVO), e);
        }
    }

    @Override
    public void remoteProcess(StoreTargetInteractionEvent event) {
        String targetTypeCode = event.getTargetType();
        Long targetId = event.getTargetId();
        String actionTypeCode = event.getActionType();
        long increment = event.getIncrement() == null ? 1L : event.getIncrement();

        InteractionEnum.TargetTypeEnum targetType = InteractionEnum.TargetTypeEnum.getByCode(targetTypeCode);
        InteractionEnum.ActionTypeEnum actionType = InteractionEnum.ActionTypeEnum.getByCode(actionTypeCode);
        if (targetType == null || actionType == null) {
            log.error("无效的枚举类型: targetTypeCode={}, actionTypeCode={}", targetTypeCode, actionTypeCode);
            return;
        }

        String redisStatKey = buildRedisStatKey(targetTypeCode, targetId);
        RedisUtil.sAdd(StoreInteractionStatRedisConstant.SYNC_CACHE_KEY_SET, redisStatKey);

        initCacheFromDbIfNotExist(redisStatKey, targetTypeCode, targetId, event.getPublisherId());
        updateRedisFinalValue(redisStatKey, actionType, increment);
    }

    @Override
    public void syncCacheToDb() {
        Set<Object> syncKeySet = RedisUtil.sMembers(StoreInteractionStatRedisConstant.SYNC_CACHE_KEY_SET);
        if (CollUtil.isEmpty(syncKeySet)) {
            return;
        }
        List<String> syncKeyList = syncKeySet.stream().map(String::valueOf).collect(Collectors.toList());
        for (List<String> batch : CollUtil.split(syncKeyList, 1000)) {
            processBatchForCover(batch);
        }
        log.info("互动统计缓存同步数据库完成, 时间: {}", new Date());
    }

    @Override
    public StoreTargetInteractionStat findByStoreTarget(BatchSyncStatReq queryVO) {
        StoreTargetInteractionStat cacheStat = getStat(queryVO.getTargetType(), queryVO.getTargetId());
        if (cacheStat != null) {
            return cacheStat;
        }
        return statRepository.findByStoreTarget(queryVO.getStoreId(), queryVO.getTargetType(), queryVO.getTargetId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByStoreTarget(BatchSyncStatReq queryVO) {
        String redisStatKey = buildRedisStatKey(queryVO.getTargetType(), queryVO.getTargetId());
        RedisUtil.del(redisStatKey);
        RedisUtil.sRem(StoreInteractionStatRedisConstant.SYNC_CACHE_KEY_SET, redisStatKey);
        return statRepository.deleteByStoreTarget(queryVO.getStoreId(), queryVO.getTargetType(), queryVO.getTargetId());
    }

    @Override
    public StoreTargetInteractionStatPageRes pageQuery(StoreTargetInteractionStatPageReq queryVO) {
        validatePageParam(queryVO);
        Page<StoreTargetInteractionStat> resultPage = statRepository.pageQuery(queryVO);
        List<StoreTargetInteractionStat> records = resultPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            records.forEach(this::fillCacheValue);
        }
        return StoreTargetInteractionStatPageRes.build(
                resultPage.getTotal(), records, queryVO.getPageNum(), queryVO.getPageSize());
    }

    @Override
    public List<StoreTargetInteractionStat> batchQuery(StoreTargetInteractionStatBatchReq queryVO) {
        validateBatchParam(queryVO);
        List<StoreTargetInteractionStat> resultList = statRepository.batchQuery(queryVO);
        if (CollUtil.isNotEmpty(resultList)) {
            resultList.forEach(this::fillCacheValue);
        }
        return resultList;
    }
    @Override
    public List<StoreTargetInteractionSummaryObj> summaryByTargetTypeAndIdList(List<String> targetTypeList, List<Long> targetIdList) {
        if (CollUtil.isEmpty(targetTypeList) || CollUtil.isEmpty(targetIdList)) {
            return Collections.emptyList();
        }
        Set<Map.Entry<String, Long>> needQuerySet = targetTypeList.stream()
                .filter(Objects::nonNull)
                .filter(type -> !type.trim().isEmpty())
                .flatMap(type -> targetIdList.stream()
                        .filter(Objects::nonNull)
                        .filter(id -> id > 0)
                        .map(id -> new AbstractMap.SimpleEntry<>(type, id)))
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(needQuerySet)) {
            return Collections.emptyList();
        }

        Map<String, Map.Entry<String, Long>> key2ComboMap = new HashMap<>();
        List<String> cacheKeyList = new ArrayList<>();
        for (Map.Entry<String, Long> combo : needQuerySet) {
            String cacheKey = buildRedisStatKey(combo.getKey(), combo.getValue());
            key2ComboMap.put(cacheKey, combo);
            cacheKeyList.add(cacheKey);
        }

        Map<String, String> cacheResultMap = RedisUtil.get(cacheKeyList);
        Map<String, String> hitMap = new HashMap<>();
        Set<Map.Entry<String, Long>> missComboSet = new HashSet<>();
        for (String cacheKey : cacheKeyList) {
            String cacheValue = cacheResultMap == null ? null : cacheResultMap.get(cacheKey);
            if (!ObjectUtil.isEmpty(cacheValue)) {
                hitMap.put(cacheKey, cacheValue);
            } else {
                missComboSet.add(key2ComboMap.get(cacheKey));
            }
        }

        Map<Map.Entry<String, Long>, StoreTargetInteractionStat> dbResultMap = new HashMap<>();
        if (CollUtil.isNotEmpty(missComboSet)) {
            List<String> missTypeList = missComboSet.stream().map(Map.Entry::getKey).collect(Collectors.toList());
            List<Long> missIdList = missComboSet.stream().map(Map.Entry::getValue).collect(Collectors.toList());
            List<StoreTargetInteractionStat> dbStatList = statRepository.selectByTargetTypeAndIdList(missTypeList, missIdList);
            for (StoreTargetInteractionStat stat : dbStatList) {
                dbResultMap.put(new AbstractMap.SimpleEntry<>(stat.getTargetType(), stat.getTargetId()), stat);
            }

            Map<String, String> batchCacheMap = new HashMap<>();
            for (Map.Entry<String, Long> combo : missComboSet) {
                StoreTargetInteractionStat stat = dbResultMap.get(combo);
                if (stat != null) {
                    FinalValueDTO dto = new FinalValueDTO(
                            Long.valueOf(stat.getViewCount()), Long.valueOf(stat.getLikeCount()), Long.valueOf(stat.getShareCount()));
                    String cacheKey = buildRedisStatKey(combo.getKey(), combo.getValue());
                    batchCacheMap.put(cacheKey, JSONUtil.toJsonStr(dto));
                    RedisUtil.sAdd(StoreInteractionStatRedisConstant.SYNC_CACHE_KEY_SET, cacheKey);
                }
            }
            if (!batchCacheMap.isEmpty()) {
                RedisUtil.set(batchCacheMap);
            }
        }

        return needQuerySet.stream().map(combo -> {
            String targetType = combo.getKey();
            Long targetId = combo.getValue();
            String cacheKey = buildRedisStatKey(targetType, targetId);
            int view = 0;
            int like = 0;
            int share = 0;
            if (hitMap.containsKey(cacheKey)) {
                FinalValueDTO dto = JSONUtil.toBean(hitMap.get(cacheKey), FinalValueDTO.class);
                view = Math.toIntExact(dto.getViewCount());
                like = Math.toIntExact(dto.getLikeCount());
                share = Math.toIntExact(dto.getShareCount());
            } else if (dbResultMap.containsKey(combo)) {
                StoreTargetInteractionStat dbStat = dbResultMap.get(combo);
                view = dbStat.getViewCount();
                like = dbStat.getLikeCount();
                share = dbStat.getShareCount();
            }
            return new StoreTargetInteractionSummaryObj(null, null, targetType, targetId, view, like, share);
        }).collect(Collectors.toList());
    }

    @Override
    public StoreTargetInteractionSummaryObj selectSummaryByPublisherId(Long publisherId) {
        if (publisherId == null || publisherId <= 0) {
            return new StoreTargetInteractionSummaryObj(null, publisherId, null, null, 0, 0, 0);
        }
        List<StoreTargetInteractionStat> baseStatList = statRepository.selectByPublisherId(publisherId);
        if (CollUtil.isEmpty(baseStatList)) {
            return new StoreTargetInteractionSummaryObj(null, publisherId, null, null, 0, 0, 0);
        }
        Set<Map.Entry<String, Long>> comboSet = baseStatList.stream()
                .map(stat -> new AbstractMap.SimpleEntry<>(stat.getTargetType(), stat.getTargetId()))
                .collect(Collectors.toSet());

        Map<String, Map.Entry<String, Long>> key2ComboMap = new HashMap<>();
        List<String> cacheKeyList = new ArrayList<>();
        for (Map.Entry<String, Long> combo : comboSet) {
            String cacheKey = buildRedisStatKey(combo.getKey(), combo.getValue());
            cacheKeyList.add(cacheKey);
            key2ComboMap.put(cacheKey, combo);
        }
        Map<String, String> cacheResultMap = RedisUtil.get(cacheKeyList);

        Map<String, String> cacheKey2ValueMap = new HashMap<>();
        Set<Map.Entry<String, Long>> missComboSet = new HashSet<>();
        for (String cacheKey : cacheKeyList) {
            String cacheValue = cacheResultMap == null ? null : cacheResultMap.get(cacheKey);
            if (!ObjectUtil.isEmpty(cacheValue)) {
                cacheKey2ValueMap.put(cacheKey, cacheValue);
            } else {
                missComboSet.add(key2ComboMap.get(cacheKey));
            }
        }

        Map<Map.Entry<String, Long>, StoreTargetInteractionStat> dbResultMap = new HashMap<>();
        if (CollUtil.isNotEmpty(missComboSet)) {
            List<String> missTypeList = missComboSet.stream().map(Map.Entry::getKey).collect(Collectors.toList());
            List<Long> missIdList = missComboSet.stream().map(Map.Entry::getValue).collect(Collectors.toList());
            List<StoreTargetInteractionStat> dbStatList = statRepository.selectByTargetTypeAndIdList(missTypeList, missIdList);
            for (StoreTargetInteractionStat stat : dbStatList) {
                dbResultMap.put(new AbstractMap.SimpleEntry<>(stat.getTargetType(), stat.getTargetId()), stat);
            }
            Map<String, String> batchCacheMap = new HashMap<>();
            for (Map.Entry<String, Long> combo : missComboSet) {
                StoreTargetInteractionStat stat = dbResultMap.getOrDefault(combo, initDefaultStat(combo.getKey(), combo.getValue(), publisherId));
                FinalValueDTO dto = new FinalValueDTO(
                        Long.valueOf(stat.getViewCount()), Long.valueOf(stat.getLikeCount()), Long.valueOf(stat.getShareCount()));
                String cacheKey = buildRedisStatKey(combo.getKey(), combo.getValue());
                batchCacheMap.put(cacheKey, JSONUtil.toJsonStr(dto));
                RedisUtil.sAdd(StoreInteractionStatRedisConstant.SYNC_CACHE_KEY_SET, cacheKey);
            }
            if (!batchCacheMap.isEmpty()) {
                RedisUtil.set(batchCacheMap);
            }
        }

        int totalView = 0;
        int totalLike = 0;
        int totalShare = 0;
        for (Map.Entry<String, Long> combo : comboSet) {
            String cacheKey = buildRedisStatKey(combo.getKey(), combo.getValue());
            StoreTargetInteractionStat stat;
            if (cacheKey2ValueMap.containsKey(cacheKey)) {
                FinalValueDTO dto = JSONUtil.toBean(cacheKey2ValueMap.get(cacheKey), FinalValueDTO.class);
                stat = new StoreTargetInteractionStat();
                stat.setViewCount(Math.toIntExact(dto.getViewCount()));
                stat.setLikeCount(Math.toIntExact(dto.getLikeCount()));
                stat.setShareCount(Math.toIntExact(dto.getShareCount()));
            } else {
                stat = dbResultMap.getOrDefault(combo, initDefaultStat(combo.getKey(), combo.getValue(), publisherId));
            }
            totalView += ObjectUtil.isEmpty(stat.getViewCount()) ? 0 : stat.getViewCount();
            totalLike += ObjectUtil.isEmpty(stat.getLikeCount()) ? 0 : stat.getLikeCount();
            totalShare += ObjectUtil.isEmpty(stat.getShareCount()) ? 0 : stat.getShareCount();
        }
        return new StoreTargetInteractionSummaryObj(null, publisherId, null, null, totalView, totalLike, totalShare);
    }
    /**
     * 用缓存最新值填充统计记录的计数字段
     *
     * @param stat 统计记录
     */
    private void fillCacheValue(StoreTargetInteractionStat stat) {
        StoreTargetInteractionStat cacheStat = getStat(stat.getTargetType(), stat.getTargetId());
        if (cacheStat != null) {
            stat.setViewCount(cacheStat.getViewCount());
            stat.setLikeCount(cacheStat.getLikeCount());
            stat.setShareCount(cacheStat.getShareCount());
        }
    }

    /**
     * 缓存不存在时从数据库加载值初始化缓存
     *
     * @param redisStatKey   缓存 Key
     * @param targetTypeCode 目标类型编码
     * @param targetId       目标 ID
     * @param publisherId    发布者 ID
     */
    private void initCacheFromDbIfNotExist(String redisStatKey, String targetTypeCode, Long targetId, Long publisherId) {
        if (RedisUtil.exists(redisStatKey)) {
            return;
        }
        StoreTargetInteractionStat dbStat = queryStatFromDb(targetTypeCode, targetId, publisherId);
        FinalValueDTO dto = new FinalValueDTO(
                Long.valueOf(dbStat.getViewCount()), Long.valueOf(dbStat.getLikeCount()), Long.valueOf(dbStat.getShareCount()));
        RedisUtil.set(redisStatKey, JSONUtil.toJsonStr(dto));
    }

    /**
     * 更新 Redis 最终值 (加分布式锁保证原子性)
     *
     * @param redisStatKey 缓存 Key
     * @param actionType   互动类型
     * @param increment    增量
     */
    private void updateRedisFinalValue(String redisStatKey, InteractionEnum.ActionTypeEnum actionType, long increment) {
        String lockKey = buildRedisLockKeyForIncrement(redisStatKey);
        RLock lock = RedissonLockUtil.lock(lockKey, TimeUnit.SECONDS, 5);
        try {
            String finalValueJson = RedisUtil.get(redisStatKey);
            if (finalValueJson == null) {
                finalValueJson = DEFAULT_FINAL_VALUE_JSON;
            }
            FinalValueDTO dto = JSONUtil.toBean(finalValueJson, FinalValueDTO.class);
            switch (actionType) {
                case VIEW -> dto.setViewCount(dto.getViewCount() + increment);
                case LIKE -> dto.setLikeCount(dto.getLikeCount() + increment);
                case SHARE -> dto.setShareCount(dto.getShareCount() + increment);
                default -> throw new IllegalArgumentException("不支持的互动类型: " + actionType.getDesc());
            }
            RedisUtil.set(redisStatKey, JSONUtil.toJsonStr(dto));
        } catch (Exception e) {
            log.error("更新 Redis 最终值缓存失败: redisStatKey={}, actionType={}", redisStatKey, actionType, e);
            throw new RuntimeException("更新最终值缓存失败", e);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                RedissonLockUtil.unlock(lock);
            }
        }
    }

    /**
     * 分批处理缓存 Key, 用缓存最终值覆盖数据库
     *
     * @param batchKeyList 缓存 Key 批次
     */
    private void processBatchForCover(List<String> batchKeyList) {
        List<StoreTargetInteractionStat> syncDomainList = new ArrayList<>();
        List<Object> successKeyList = new ArrayList<>();

        for (String cacheKey : batchKeyList) {
            RLock keyLock = null;
            boolean keyLocked = false;
            try {
                CacheKeyParam keyParam = parseCacheKey(cacheKey);
                if (keyParam == null) {
                    continue;
                }
                String redisLockKey = buildRedisLockKey(keyParam.getTargetTypeCode(), keyParam.getTargetId());
                keyLocked = RedissonLockUtil.tryLock(redisLockKey, TimeUnit.SECONDS, 0, StoreInteractionStatRedisConstant.COMMON_LOCK_EXPIRE);
                if (!keyLocked) {
                    continue;
                }
                keyLock = RedissonLockUtil.lock(redisLockKey);

                String finalValueJson = RedisUtil.get(cacheKey);
                if (finalValueJson == null) {
                    continue;
                }
                FinalValueDTO dto = JSONUtil.toBean(finalValueJson, FinalValueDTO.class);
                StoreTargetInteractionStat dbStat = statRepository.findByStoreTarget(null, keyParam.getTargetTypeCode(), keyParam.getTargetId());
                if (dbStat == null) {
                    continue;
                }
                StoreTargetInteractionStat syncStat = new StoreTargetInteractionStat();
                syncStat.setId(dbStat.getId());
                syncStat.setTargetType(keyParam.getTargetTypeCode());
                syncStat.setTargetId(keyParam.getTargetId());
                syncStat.setPublisherId(dbStat.getPublisherId());
                syncStat.setStoreId(dbStat.getStoreId());
                syncStat.setViewCount(Math.toIntExact(dto.getViewCount()));
                syncStat.setLikeCount(Math.toIntExact(dto.getLikeCount()));
                syncStat.setShareCount(Math.toIntExact(dto.getShareCount()));
                syncStat.setCreatedTime(dbStat.getCreatedTime());
                syncStat.setUpdatedTime(LocalDateTime.now());

                syncDomainList.add(syncStat);
                successKeyList.add(cacheKey);
            } catch (Exception e) {
                log.error("处理缓存 Key 同步异常: {}", cacheKey, e);
            } finally {
                if (keyLocked && keyLock != null && keyLock.isHeldByCurrentThread()) {
                    RedissonLockUtil.unlock(keyLock);
                }
            }
        }

        if (CollUtil.isNotEmpty(syncDomainList)) {
            boolean saveSuccess = statRepository.batchSaveOrUpdate(syncDomainList);
            if (saveSuccess) {
                RedisUtil.sRem(StoreInteractionStatRedisConstant.SYNC_CACHE_KEY_SET, successKeyList.toArray());
            } else {
                log.error("批量覆盖数据库失败, 批次: {}", JSONUtil.toJsonStr(syncDomainList));
            }
        }
    }

    /**
     * 初始化默认统计记录到数据库与缓存
     *
     * @param targetTypeCode 目标类型编码
     * @param targetId       目标 ID
     * @param publisherId    发布者 ID
     * @return 默认统计记录
     */
    private StoreTargetInteractionStat initDefaultStat(String targetTypeCode, Long targetId, Long publisherId) {
        StoreTargetInteractionStat defaultStat = new StoreTargetInteractionStat();
        defaultStat.setTargetType(targetTypeCode);
        defaultStat.setPublisherId(publisherId);
        defaultStat.setTargetId(targetId);
        defaultStat.setViewCount(0);
        defaultStat.setLikeCount(0);
        defaultStat.setShareCount(0);
        defaultStat.setCreatedTime(LocalDateTime.now());
        defaultStat.setUpdatedTime(LocalDateTime.now());

        boolean saveSuccess = statRepository.batchSaveOrUpdate(Collections.singletonList(defaultStat));
        if (!saveSuccess) {
            throw new RuntimeException("初始化统计记录保存数据库失败: " + JSONUtil.toJsonStr(defaultStat));
        }
        String redisStatKey = buildRedisStatKey(targetTypeCode, targetId);
        RedisUtil.set(redisStatKey, DEFAULT_FINAL_VALUE_JSON);
        RedisUtil.sAdd(StoreInteractionStatRedisConstant.SYNC_CACHE_KEY_SET, redisStatKey);
        return defaultStat;
    }

    private void validatePageParam(StoreTargetInteractionStatPageReq queryVO) {
        if (queryVO.getPageNum() < 1) {
            throw new IllegalArgumentException("页码不能小于1");
        }
        if (queryVO.getPageSize() < 1 || queryVO.getPageSize() > 100) {
            throw new IllegalArgumentException("页大小需在1-100之间");
        }
        if (Objects.nonNull(queryVO.getTargetTypeCode())
                && InteractionEnum.TargetTypeEnum.getByCode(queryVO.getTargetTypeCode()) == null) {
            throw new IllegalArgumentException("无效的目标类型编码: " + queryVO.getTargetTypeCode());
        }
    }

    private void validateBatchParam(StoreTargetInteractionStatBatchReq queryVO) {
        boolean hasCondition = CollUtil.isNotEmpty(queryVO.getStoreIdList())
                || Objects.nonNull(queryVO.getTargetType())
                || CollUtil.isNotEmpty(queryVO.getTargetIdList());
        if (!hasCondition) {
            throw new IllegalArgumentException("批量查询至少需指定门店ID列表/目标类型/目标ID列表中的一个条件");
        }
        validateRangeParam("浏览量", queryVO.getViewCountRange());
        validateRangeParam("点赞量", queryVO.getLikeCountRange());
        validateRangeParam("分享量", queryVO.getShareCountRange());
    }

    private void validateRangeParam(String fieldName, Integer[] range) {
        if (Objects.isNull(range)) {
            return;
        }
        if (range.length != 2) {
            throw new IllegalArgumentException(fieldName + "范围参数需为长度2的数组([min, max])");
        }
        Integer min = range[0];
        Integer max = range[1];
        if (Objects.nonNull(min) && Objects.nonNull(max) && min > max) {
            throw new IllegalArgumentException(fieldName + "最小值不能大于最大值");
        }
    }

    private StoreTargetInteractionEvent convertReqToEvent(InteractionStatReq requestVO) {
        if (requestVO.getTargetType() == null) {
            throw new IllegalArgumentException("目标类型不能为空");
        }
        if (requestVO.getTargetId() == null) {
            throw new IllegalArgumentException("目标ID不能为空");
        }
        if (requestVO.getActionType() == null) {
            throw new IllegalArgumentException("互动类型不能为空");
        }
        InteractionEnum.TargetTypeEnum targetType = InteractionEnum.TargetTypeEnum.getByCode(requestVO.getTargetType());
        InteractionEnum.ActionTypeEnum actionType = InteractionEnum.ActionTypeEnum.getByCode(requestVO.getActionType());
        if (targetType == null) {
            throw new IllegalArgumentException("无效的目标类型: " + requestVO.getTargetType());
        }
        if (actionType == null) {
            throw new IllegalArgumentException("无效的互动类型: " + requestVO.getActionType());
        }
        StoreTargetInteractionEvent event = new StoreTargetInteractionEvent();
        event.setTargetType(targetType.getCode());
        event.setTargetId(requestVO.getTargetId());
        event.setActionType(actionType.getCode());
        event.setIncrement(Boolean.TRUE.equals(requestVO.getIsCancel()) ? -1 : 1);
        return event;
    }

    private String buildRedisStatKey(String targetTypeCode, Long targetId) {
        return String.format("%s%s:%s", StoreInteractionStatRedisConstant.REDIS_STAT_KEY_PREFIX, targetTypeCode, targetId);
    }

    private String buildRedisLockKeyForIncrement(String redisStatKey) {
        return "lock:finalValue:" + redisStatKey;
    }

    private String buildRedisLockKey(String targetTypeCode, Long targetId) {
        return String.format("%s%s:%s", StoreInteractionStatRedisConstant.REDIS_LOCK_KEY_PREFIX, targetTypeCode, targetId);
    }

    private StoreTargetInteractionStat queryStatFromDb(String targetType, Long targetId, Long publisherId) {
        StoreTargetInteractionStat dbStat = statRepository.findByStoreTarget(null, targetType, targetId);
        if (dbStat != null) {
            dbStat.setViewCount(Optional.ofNullable(dbStat.getViewCount()).orElse(0));
            dbStat.setLikeCount(Optional.ofNullable(dbStat.getLikeCount()).orElse(0));
            dbStat.setShareCount(Optional.ofNullable(dbStat.getShareCount()).orElse(0));
            return dbStat;
        }
        return initDefaultStat(targetType, targetId, publisherId);
    }

    private CacheKeyParam parseCacheKey(String cacheKey) {
        String[] keyParts = cacheKey.split(":");
        if (keyParts.length != 4 || !cacheKey.startsWith(StoreInteractionStatRedisConstant.REDIS_STAT_KEY_PREFIX)) {
            return null;
        }
        try {
            String targetTypeCode = keyParts[2];
            Long targetId = Long.parseLong(keyParts[3]);
            if (InteractionEnum.TargetTypeEnum.getByCode(targetTypeCode) == null) {
                return null;
            }
            return new CacheKeyParam(null, targetTypeCode, targetId, targetTypeCode);
        } catch (NumberFormatException e) {
            log.error("缓存 Key 数字参数解析失败: {}", cacheKey, e);
            return null;
        }
    }

    /**
     * 缓存中存储的最终统计值
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class FinalValueDTO {
        private Long viewCount;
        private Long likeCount;
        private Long shareCount;
    }
}
