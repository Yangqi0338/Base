package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 脉脉通-用户主页信息出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.account.model.res.UserHomePageRes},
 * 字段含义逐字沿用旧源码注释。</p>
 *
 * @author KC
 */
@Data
public class UserHomePageRes implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String head;

    /**
     * 背景图
     */
    private String backgroundImg;

    /**
     * 当前用户是否关注该用户
     */
    private Boolean isFollowed;

    /**
     * 关注数（我关注的人数）
     */
    private Integer followingCount;

    /**
     * 粉丝数（关注我的人数）
     */
    private Integer followerCount;

    /**
     * 是否有门店
     */
    private Boolean hasStore;

    /**
     * 门店信息（如果有门店）
     */
    private StoreInfo storeInfo;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 门店信息
     */
    @Data
    public static class StoreInfo implements Serializable {

        /**
         * 门店ID
         */
        private Long storeId;

        /**
         * 门店名称
         */
        private String storeName;

        /**
         * 门店头像/logo
         */
        private String storeLogo;

        /**
         * 门店商品列表（最多3个）
         */
        private List<GoodsInfo> goodsList;
    }

    /**
     * 商品信息
     */
    @Data
    public static class GoodsInfo implements Serializable {

        /**
         * 铺货ID
         */
        private Long distributionId;

        /**
         * 商品ID
         */
        private Long goodsId;

        /**
         * 商品名称
         */
        private String name;

        /**
         * 商品图片
         */
        private String img;

        /**
         * 销售价 (Money, 落库 BIGINT 分)
         */
        private Money sellPrice;
    }
}
