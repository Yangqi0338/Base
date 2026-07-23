package com.newzkl.platform.base.biz.store.model.fitment.vo;

import com.newzkl.platform.base.common.core.utils.common.JsonUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author niu
 * @description: 装修页面
 * @date 2024/3/29 14:45
 */
public class FitmentPageVO {

    private Long id;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 1：平台商品 2：平台+自营
     */
    private Integer pageType;

    /**
     * 内容
     */
    private String content;

    /**
     * 商品id
     */
    private List<Long> goodsId;

    /**
     * 市场id
     */
    private List<Long> markets;

    /**
     * 键值对
     */
    private String kv;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Long> getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        if (goodsId == null) {
            this.goodsId = null;
        }else {
            this.goodsId = JsonUtils.jsonToList(goodsId, Long.class);
        }
    }

    public List<Long> getMarkets() {
        return markets;
    }

    public void setMarkets(String markets) {
        if (goodsId == null) {
            this.markets = null;
        }else {
            this.markets = JsonUtils.jsonToList(markets, Long.class);
        }
    }

    public String getKv() {
        return kv;
    }

    public void setKv(String kv) {
        this.kv = kv;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
