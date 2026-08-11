package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.UserCollectionRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.UserCollectionDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserCollectionDO;
import com.newzkl.platform.base.biz.user.model.relation.query.UserCollectionQuery;
import com.newzkl.platform.base.biz.user.model.relation.dto.UserCollectionDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户收藏仓储实现
 *
 * <p>迁移说明：新 common BaseDO 使用 delFlag(@TableLogic)，源 isDeleted 字段不存在，
 * 逻辑删除交由 MP 处理；isValid 为 CommonEnum.YesOrNo 枚举；findPageByUserId 保留 Page 分页壳直返。</p>
 *
 * @author sijiwang
 */
@Repository
@RequiredArgsConstructor
public class UserCollectionRepositoryImpl implements UserCollectionRepository {

    @Resource
    private UserCollectionDAO userCollectionMapper;

    /**
     * 领域实体转数据对象
     *
     * @param collection 领域实体
     * @return 数据对象
     */
    private UserCollectionDO toDO(UserCollectionDTO collection) {
        UserCollectionDO doObj = new UserCollectionDO();
        doObj.setId(collection.getId());
        doObj.setUserId(collection.getUserId());
        doObj.setUserName(collection.getUserName());
        doObj.setStoreId(collection.getStoreId());
        doObj.setStoreName(collection.getStoreName());
        doObj.setStoreDistributionId(collection.getStoreDistributionId());
        doObj.setSpuId(collection.getSpuId());
        doObj.setSpuName(collection.getSpuName());
        doObj.setSkuId(collection.getSkuId());
        doObj.setSkuName(collection.getSkuName());
        doObj.setPrice(collection.getPrice());
        doObj.setMainImage(collection.getMainImage());
        if (collection.getIsValid() != null) {
            doObj.setIsValid(CommonEnum.YesOrNo.getByBool(collection.getIsValid()));
        }
        doObj.setCreateTime(collection.getCreateTime());
        doObj.setUpdateTime(collection.getUpdateTime());
        return doObj;
    }

    /**
     * 数据对象转领域实体
     *
     * @param doObj 数据对象
     * @return 领域实体
     */
    private UserCollectionDTO toDomain(UserCollectionDO doObj) {
        if (doObj == null) {
            return null;
        }
        UserCollectionDTO collection = new UserCollectionDTO();
        collection.setId(doObj.getId());
        collection.setUserId(doObj.getUserId());
        collection.setUserName(doObj.getUserName());
        collection.setStoreId(doObj.getStoreId());
        collection.setStoreName(doObj.getStoreName());
        collection.setStoreDistributionId(doObj.getStoreDistributionId());
        collection.setSpuId(doObj.getSpuId());
        collection.setSpuName(doObj.getSpuName());
        collection.setSkuId(doObj.getSkuId());
        collection.setSkuName(doObj.getSkuName());
        collection.setPrice(doObj.getPrice());
        collection.setMainImage(doObj.getMainImage());
        collection.setIsValid(CommonEnum.YesOrNo.YES.equals(doObj.getIsValid()));
        collection.setIsDeleted(false);
        collection.setCreateTime(doObj.getCreateTime());
        collection.setUpdateTime(doObj.getUpdateTime());
        return collection;
    }

    @Override
    public UserCollectionDTO save(UserCollectionDTO userCollection) {
        UserCollectionDO doObj = toDO(userCollection);
        if (doObj.getId() == null) {
            userCollectionMapper.insert(doObj);
        } else {
            userCollectionMapper.updateById(doObj);
        }
        return toDomain(doObj);
    }

    @Override
    public Boolean validByDistributionId(Long distributionId, Integer isValid) {
        CommonEnum.YesOrNo validValue = CommonEnum.YesOrNo.reverse(isValid);
        int update = userCollectionMapper.update(new LambdaUpdateWrapper<UserCollectionDO>()
                .set(UserCollectionDO::getIsValid, CommonEnum.YesOrNo.getByCode(isValid))
                .eq(UserCollectionDO::getStoreDistributionId, distributionId)
                .eq(validValue != null, UserCollectionDO::getIsValid, validValue)
        );
        return update > 0;
    }

    @Override
    public List<UserCollectionDTO> findByUserId(Long userId) {
        UserCollectionQuery query = new UserCollectionQuery();
        query.setUserId(userId);
        List<UserCollectionDO> doList = userCollectionMapper.selectList(userCollectionMapper.getLw(query));
        return doList.stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<UserCollectionDTO> findWithDeletedByUserIdAndProductId(Long userId, Long storeDistributionId) {
        UserCollectionDO doObj = userCollectionMapper.selectWithDeletedByUserId(userId, storeDistributionId);
        return Optional.ofNullable(toDomain(doObj));
    }

    @Override
    public boolean logicDeleteByUserIdAndProductId(Long id) {
        int affectedRows = userCollectionMapper.deleteById(id);
        return affectedRows > 0;
    }

    @Override
    public Page<UserCollectionDTO> findPageByUserId(UserCollectionQuery query) {
        Page<UserCollectionDO> doPage = userCollectionMapper.selectPage(
                RepositorySupport.page(query), userCollectionMapper.getLw(query));
        return TransferUtils.transferPage(doPage, this::toDomain);
    }

    @Override
    public Boolean uncollectedInvalidProduct(Long accountId) {
        int effectRows = userCollectionMapper.update(new LambdaUpdateWrapper<UserCollectionDO>()
                .set(UserCollectionDO::getIsValid, CommonEnum.YesOrNo.NO)
                .eq(UserCollectionDO::getUserId, accountId)
                .eq(UserCollectionDO::getIsValid, CommonEnum.YesOrNo.YES)
        );
        return effectRows > 0;
    }
}
