package com.newzkl.platform.base.biz.course.infrastructure.adapt.repository;

import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseChapterRepository;
import com.newzkl.platform.base.biz.course.infrastructure.convert.CourseUnitConverter;
import com.newzkl.platform.base.biz.course.infrastructure.dao.CourseChapterDAO;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseChapterDO;
import com.newzkl.platform.base.biz.course.model.chapter.query.CourseChapterQuery;
import com.newzkl.platform.base.biz.course.model.chapter.req.CourseChapterReq;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterRes;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterStatRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 课程章节仓储实现
 *
 * <p>时长入参单位秒({@code Double})、库中单位百分秒({@code Integer}), 换算经 {@link CourseUnitConverter}
 * 于读写边界完成。时长字段名在 Req/Res 与 DO 中同名异型, 排除 Hutool 自动拷贝后显式换算。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class CourseChapterRepositoryImpl extends RepositorySupport implements CourseChapterRepository {

    /**
     * 时长字段名, 同名异型(秒 Double / 百分秒 Integer), 排除 Hutool 自动拷贝
     */
    private static final String[] DURATION_PROPS = {"durationCentisecond"};

    private final CourseChapterDAO courseChapterDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(CourseChapterReq req) {
        CopyOptions opts = CopyOptions.create().setIgnoreProperties(DURATION_PROPS);
        CourseChapterDO chapterDO = TransferUtils.transfer(req, CourseChapterDO::new, opts);
        Integer centisecond = CourseUnitConverter.secondsToCentisecond(req.getDurationCentisecond());
        chapterDO.setDurationCentisecond(centisecond);
        chapterDO.setDurationDesc(CourseUnitConverter.formatCentisecond((long) centisecond));
        if (chapterDO.getPublishTime() == null) {
            chapterDO.setPublishTime(LocalDateTime.now());
        }
        courseChapterDAO.insertOrUpdate(chapterDO);
        return chapterDO.getId();
    }

    @Override
    public CourseChapterRes detail(Long id) {
        if (id == null) {
            return null;
        }
        return toRes(courseChapterDAO.selectById(id));
    }

    @Override
    public Page<CourseChapterRes> pageList(CourseChapterQuery query) {
        Page<CourseChapterDO> page = courseChapterDAO.selectPage(RepositorySupport.page(query),
                courseChapterDAO.getLw(query));
        return TransferUtils.transferPage(page, this::toRes);
    }

    @Override
    public List<CourseChapterRes> listByCourseId(Long courseId) {
        BaseLambdaQueryWrapper<CourseChapterDO> wrapper = new BaseLambdaQueryWrapper<CourseChapterDO>()
                .notNullEq(CourseChapterDO::getCourseId, courseId);
        wrapper.orderByAsc(CourseChapterDO::getChapterNum);
        return TransferUtils.transfers(courseChapterDAO.selectList(wrapper), this::toRes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEnabled(Long id, CommonEnum.YesOrNo isEnabled) {
        CourseChapterDO chapterDO = new CourseChapterDO();
        chapterDO.setId(id);
        chapterDO.setIsEnabled(isEnabled);
        return courseChapterDAO.updateById(chapterDO) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFree(Long id, CommonEnum.YesOrNo isFree) {
        CourseChapterDO chapterDO = new CourseChapterDO();
        chapterDO.setId(id);
        chapterDO.setIsFree(isFree);
        return courseChapterDAO.updateById(chapterDO) > 0;
    }

    @Override
    public boolean existsChapterNum(Long courseId, Integer chapterNum, Long excludeId) {
        BaseLambdaQueryWrapper<CourseChapterDO> wrapper = new BaseLambdaQueryWrapper<CourseChapterDO>()
                .notNullEq(CourseChapterDO::getCourseId, courseId)
                .notNullEq(CourseChapterDO::getChapterNum, chapterNum)
                .notNullNe(CourseChapterDO::getId, excludeId);
        return courseChapterDAO.selectCount(wrapper) > 0;
    }

    @Override
    public List<CourseChapterStatRes> batchStat(List<Long> courseIdList) {
        if (courseIdList == null || courseIdList.isEmpty()) {
            return Collections.emptyList();
        }
        List<CourseChapterStatRes> statList = courseChapterDAO.batchStat(courseIdList);
        statList.forEach(stat ->
                stat.setTotalDurationSeconds(CourseUnitConverter.centisecondToSeconds(stat.getTotalDurationCentisecond())));
        return statList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> idList) {
        return courseChapterDAO.deleteByIds(idList) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recover(Long id) {
        return recoverDeleteById(id, CourseChapterDO.class);
    }

    /**
     * DO 转 Res, 百分秒转秒、免费与启用描述在此填充
     *
     * @param chapterDO 章节数据对象
     * @return 章节视图, 入参为空返回 null
     */
    private CourseChapterRes toRes(CourseChapterDO chapterDO) {
        if (chapterDO == null) {
            return null;
        }
        CopyOptions opts = CopyOptions.create().setIgnoreProperties(DURATION_PROPS);
        CourseChapterRes res = TransferUtils.transfer(chapterDO, CourseChapterRes::new, opts);
        res.setDurationCentisecond(CourseUnitConverter.centisecondToSeconds(chapterDO.getDurationCentisecond()));
        res.setIsFreeDesc(chapterDO.getIsFree() == CommonEnum.YesOrNo.YES ? "免费" : "付费");
        res.setIsEnabledDesc(chapterDO.getIsEnabled() == CommonEnum.YesOrNo.YES ? "启用" : "禁用");
        return res;
    }
}
