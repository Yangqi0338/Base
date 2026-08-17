package com.newzkl.platform.base.biz.content.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.domain.adapt.repository.VideoCategoryRepository;
import com.newzkl.platform.base.biz.content.infrastructure.dao.ContentVideoCategoryDAO;
import com.newzkl.platform.base.biz.content.infrastructure.entity.VideoCategoryDO;
import com.newzkl.platform.base.biz.content.model.common.res.ContentPage;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.biz.content.model.videocategory.entity.VideoCategory;
import com.newzkl.platform.base.biz.content.model.videocategory.query.VideoCategoryPageQuery;
import com.newzkl.platform.base.biz.content.model.videocategory.req.VideoCategoryReq;
import com.newzkl.platform.base.biz.content.model.videocategory.res.VideoCategoryRes;
import com.newzkl.platform.base.biz.content.model.videocategory.vo.VideoCategoryWeightVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 视频分类仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.repository.VideoCategoryRepositoryImpl}</p>
 *
 * @author KC
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class VideoCategoryRepositoryImpl implements VideoCategoryRepository {

    private final ContentVideoCategoryDAO contentVideoCategoryDAO;

    @Override
    public ContentPage<VideoCategoryRes> getCategoryPage(VideoCategoryPageQuery query) {
        Page<VideoCategoryDO> page = contentVideoCategoryDAO.selectPage(RepositorySupport.page(query),
                new BaseLambdaQueryWrapper<VideoCategoryDO>()
                        .notEmptyLike(VideoCategoryDO::getName, query.getName())
                        .orderByAsc(VideoCategoryDO::getSort)
                        .orderByDesc(VideoCategoryDO::getCreateTime));
        return ContentPage.of(page.getCurrent(), page.getSize(), page.getTotal(),
                TransferUtils.transfers(page.getRecords(), VideoCategoryRes::new));
    }

    @Override
    public VideoCategory getById(Long id) {
        return TransferUtils.transfer(contentVideoCategoryDAO.selectById(id), VideoCategory::new);
    }

    @Override
    public VideoCategoryRes getResById(Long id) {
        return TransferUtils.transfer(contentVideoCategoryDAO.selectById(id), VideoCategoryRes::new);
    }

    @Override
    public List<VideoCategory> getByIdList(List<Long> idList) {
        return TransferUtils.transfers(contentVideoCategoryDAO.selectByIds(idList), VideoCategory::new);
    }

    @Override
    public void save(VideoCategoryReq req) {
        contentVideoCategoryDAO.insert(TransferUtils.transfer(req, VideoCategoryDO::new));
    }

    @Override
    public void update(VideoCategoryReq req) {
        contentVideoCategoryDAO.updateById(TransferUtils.transfer(req, VideoCategoryDO::new));
    }

    @Override
    public void delete(Long id) {
        contentVideoCategoryDAO.deleteById(id);
    }

    @Override
    public void updateVideoCount(Long id, Integer num) {
        contentVideoCategoryDAO.update(new LambdaUpdateWrapper<VideoCategoryDO>()
                .setSql(" video_count = video_count + (" + num + ")")
                .eq(VideoCategoryDO::getId, id));
    }

    @Override
    public List<VideoCategoryRes> getCategoryList(List<RecommendGroupEnum> recommendGroups) {
        BaseLambdaQueryWrapper<VideoCategoryDO> queryWrapper = new BaseLambdaQueryWrapper<VideoCategoryDO>();
        queryWrapper.likeList(VideoCategoryDO::getRecommendGroups, recommendGroups);
        queryWrapper.eq(VideoCategoryDO::getIsEnabled, 1)
                .orderByAsc(VideoCategoryDO::getSort)
                .orderByDesc(VideoCategoryDO::getCreateTime);
        return TransferUtils.transfers(contentVideoCategoryDAO.selectList(queryWrapper), VideoCategoryRes::new);
    }

    @Override
    public List<VideoCategoryWeightVO> getCategoryWeightList() {
        List<VideoCategoryDO> list = contentVideoCategoryDAO.selectList(
                new BaseLambdaQueryWrapper<VideoCategoryDO>().eq(VideoCategoryDO::getIsEnabled, 1));
        List<VideoCategoryWeightVO> batchItems = list.stream()
                .map(videoCategoryDO -> new VideoCategoryWeightVO(videoCategoryDO.getId(), videoCategoryDO.getWeight()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(batchItems)) {
            return batchItems;
        }
        int totalWeight = batchItems.stream()
                .mapToInt(VideoCategoryWeightVO::getWeight)
                .sum();

        // 总权重为0时, 平均分配 1-10
        if (totalWeight == 0) {
            int size = batchItems.size();
            int avg = 10 / size;
            int remainder = 10 % size;
            for (int i = 0; i < size; i++) {
                int weightRatio = (i < remainder) ? avg + 1 : avg;
                batchItems.get(i).setWeightRatio(weightRatio);
            }
        } else {
            // 计算并分配合计为 10 的整数权重
            int sumAssigned = 0;
            List<Double> exactValues = new ArrayList<>();
            for (VideoCategoryWeightVO item : batchItems) {
                exactValues.add((double) item.getWeight() / totalWeight * 10);
            }
            // 先取整数部分
            int[] intValues = new int[batchItems.size()];
            for (int i = 0; i < exactValues.size(); i++) {
                intValues[i] = (int) Math.floor(exactValues.get(i));
                sumAssigned += intValues[i];
            }
            // 余量按小数部分从大到小补齐
            int remaining = 10 - sumAssigned;
            List<Integer> sortedIndices = IntStream.range(0, exactValues.size())
                    .boxed()
                    .sorted((a, b) -> Double.compare(
                            exactValues.get(b) - intValues[b],
                            exactValues.get(a) - intValues[a]
                    ))
                    .collect(Collectors.toList());
            for (int i = 0; i < remaining; i++) {
                intValues[sortedIndices.get(i)]++;
            }
            for (int i = 0; i < batchItems.size(); i++) {
                batchItems.get(i).setWeightRatio(intValues[i]);
            }
        }
        return batchItems;
    }
}
