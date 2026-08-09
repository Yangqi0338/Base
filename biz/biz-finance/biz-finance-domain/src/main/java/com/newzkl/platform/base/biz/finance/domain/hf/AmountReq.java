package com.newzkl.platform.base.biz.finance.domain.hf;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 金额相关的内部请求类
 *
 * @author kc
 */
abstract class AmountReq {

    /**
     * 汇付支付请求
     *
     * @author niu
     * @date 2025-08-25 17:26:49
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class PayReq extends Base.Req {

        /**
         * 商品描述
         */
        private String goods_desc;
        /**
         * 交易类型
         */
        private String trade_type;
        /**
         * 交易金额
         */
        private String trans_amt;
        /**
         * 回调地址
         */
        private String notify_url;
    }

    /**
     * 支付状态查询请求类
     *
     * @author niu
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class PayStateReq extends Base.Req {

        /**
         * 原交易请求日期 String(8) Y 格式：yyyyMMdd；示例值：20220925
         */
        @Pattern(regexp = "^\\d{8}$", message = "原交易请求日期格式必须为yyyyMMdd")
        private String org_req_date;

        /**
         * 汇付服务订单号
         */
        @Size(max = 32, message = "汇付服务订单号长度不能超过32位")
        private String out_ord_id;

        /**
         * 原交易全局流水号 String(128) N org_hf_seq_id，org_party_order_id，org_req_seq_id三选一；
         * 示例值：0030default220825182711P099ac1f343f00000
         */
        @Size(max = 128, message = "原交易全局流水号长度不能超过128位")
        private String org_hf_seq_id;

        /**
         * 原交易请求流水号 String(128) N org_hf_seq_id，org_party_order_id，org_req_seq_id三选一；
         * 示例值：202110210012100005
         */
        @Size(max = 128, message = "原交易请求流水号长度不能超过128位")
        private String org_req_seq_id;

        /**
         * 校验原交易ID三选一规则
         */
        @AssertTrue(message = "out_ord_id,org_hf_seq_id,org_req_seq_id 必填其一")
        public boolean isOriginalOrderIdValid() {
            return StrUtil.isNotBlank(out_ord_id)
                    || StrUtil.isNotBlank(org_hf_seq_id)
                    || StrUtil.isNotBlank(org_req_seq_id);
        }

        /**
         * 校验原机构请求日期
         */
        @AssertTrue(message = "原机构请求日期不能为空")
        public boolean isOrgReqDateValid() {
            return StrUtil.isNotBlank(org_hf_seq_id) || StrUtil.isNotBlank(org_req_date);
        }
    }

    /**
     * 汇付取现请求
     *
     * @author niu
     * @date 2025-08-25 17:26:49
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class WithdrawReq extends Base.Req {

        /**
         * 取现金额 String(14) Y 单位元，需保留小数点后两位，示例值：1.00
         */
        @NotBlank(message = "取现金额不能为空")
        @Digits(integer = 12, fraction = 2, message = "取现金额格式错误（整数部分最多12位，小数部分2位）")
        @DecimalMin(value = "0.01", message = "取现金额最低为0.01元")
        private String cash_amt;

        /**
         * 账户号 String(32) N 可指定账户号，仅支持基本户、现金户，不填默认为基本户；示例值：F00598600
         */
        @Size(max = 32, message = "账户号长度不能超过32位")
        private String acct_id;

        /**
         * 到账日期类型 String(3) Y D0：当日到账；当日交易资金当天可取现到账；T1：次工作日到账；D1：次自然日到账；DM：当日到账；到账资金不包括当天的交易资金；示例值：D0
         */
        @NotBlank(message = "到账日期类型不能为空")
        @Size(max = 3, message = "到账日期类型长度不能超过3位")
        @Pattern(regexp = "^(D0|T1|D1|DM)$", message = "到账日期类型只能是D0、T1、D1、DM")
        private String into_acct_date_type;

        /**
         * 取现卡序列号 String(20) Y 绑定取现卡后可获取取现卡序列号；示例值：10004053462
         */
        @NotBlank(message = "取现卡序列号不能为空")
        @Size(max = 20, message = "取现卡序列号长度不能超过20位")
        private String token_no;

        /**
         * 取现渠道 String(2) N 00：汇付（为空默认）； 10：中信e账通；示例值：00
         */
        @Size(max = 2, message = "取现渠道长度不能超过2位")
        @Pattern(regexp = "^(00|10)?$", message = "取现渠道只能是00或10（为空默认00）")
        private String enchashment_channel;

        /**
         * 中信e账通手续费承担方 String(1) N 1：总部承担；2：自身承担。默认为2。
         */
        @Size(max = 1, message = "中信e账通手续费承担方长度不能超过1位")
        @Pattern(regexp = "^(1|2)?$", message = "中信e账通手续费承担方只能是1或2（为空默认2）")
        private String fee_type;

        /**
         * 备注 String(100) N 示例值：备注
         */
        @Size(max = 100, message = "备注长度不能超过100位")
        private String remark;

        /**
         * 异步通知地址 String(128) N 示例值：http://service.example.com/to/path
         */
        @Size(max = 128, message = "异步通知地址长度不能超过128位")
        @Pattern(regexp = "^https?://.*$", message = "异步通知地址需为合法的HTTP/HTTPS URL")
        private String notify_url;
    }

    /**
     * 汇付退款请求
     *
     * @author niu
     * @date 2025-08-25 17:26:49
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class RefundReq extends Base.Req {

        /**
         * 申请退款金额 String(14) Y 单位元，需保留小数点后两位；示例值：1.00，最低传入0.01
         * 注意：如果原交易是延时交易，退款金额必须小于等于待确认金额
         */
        @NotBlank(message = "申请退款金额不能为空")
        @Digits(integer = 11, fraction = 2, message = "申请退款金额格式错误")
        @DecimalMin(value = "0.01", message = "申请退款金额最低为0.01元")
        private String ord_amt;

        /**
         * 原交易请求日期 String(8) Y 格式：yyyyMMdd；示例值：20220925
         */
        @NotBlank(message = "原交易请求日期不能为空")
        @Pattern(regexp = "^\\d{8}$", message = "原交易请求日期格式必须为yyyyMMdd")
        private String org_req_date;

        /**
         * 原交易全局流水号 String(128) N org_hf_seq_id，org_party_order_id，org_req_seq_id三选一；
         * 示例值：0030default220825182711P099ac1f343f00000
         */
        @Size(max = 128, message = "原交易全局流水号长度不能超过128位")
        private String org_hf_seq_id;

        /**
         * 原交易微信支付宝的商户单号 String(64) N org_hf_seq_id，org_party_order_id，org_req_seq_id三选一；
         * 示例值：03232109190255105603561；参见用户账单说明
         */
        @Size(max = 64, message = "原交易微信支付宝的商户单号长度不能超过64位")
        private String org_party_order_id;

        /**
         * 原交易请求流水号 String(128) N org_hf_seq_id，org_party_order_id，org_req_seq_id三选一；
         * 示例值：202110210012100005
         */
        @Size(max = 128, message = "原交易请求流水号长度不能超过128位")
        private String org_req_seq_id;
        /**
         * 分账对象 String(2048) N 分账信息，jsonObject字符串
         */
        @Valid
        private AcctSplitBunch acct_split_bunch;
        /**
         * 聚合正扫微信拓展参数集合 String(2048) N 直连模式需要提供
         */
        @Valid
        private RefundReq.WxData wx_data;
        /**
         * 数字货币扩展参数集合 String(2048) N 数字货币扩展参数集合，jsonObject字符串
         */
        @Valid
        private RefundReq.DigitalCurrencyData digital_currency_data;
        /**
         * 补贴支付信息 String N jsonArray字符串；参见《补贴支付信息》
         */
        @Valid
        private List<RefundReq.CombinedpayData> combinedpay_data;
        /**
         * 补贴支付手续费承担方信息 String N jsonObject字符串
         */
        @Valid
        private RefundReq.CombinedpayDataFeeInfo combinedpay_data_fee_info;
        /**
         * 备注 String(84) N 原样返回；示例值：备注
         */
        @Size(max = 84, message = "备注长度不能超过84位")
        private String remark;
        /**
         * 是否垫资退款 String(2) N Y 是垫资出款， N 是普通出款，为空默认N；示例值： N
         * 注意：延时交易退款在【交易确认退款】接口中设置loan_flag为垫资，本接口不可再次设置垫资
         */
        @Size(max = 2, message = "是否垫资退款长度不能超过2位")
        @Pattern(regexp = "^(Y|N)?$", message = "是否垫资退款只能是Y或N（为空默认N）")
        private String loan_flag;
        /**
         * 垫资承担者 String(32) N 垫资方的huifu_id；示例值：6666000123123123
         * 为空则各自承担。不为空走第三方垫资，目前支持商户垫资；
         */
        @Size(max = 32, message = "垫资承担者长度不能超过32位")
        private String loan_undertaker;
        /**
         * 垫资账户类型 String(2) N 01:基本户, 05: 充值户, 默认充值户；示例值：01
         */
        @Size(max = 2, message = "垫资账户类型长度不能超过2位")
        @Pattern(regexp = "^(01|05)?$", message = "垫资账户类型只能是01或05（为空默认05）")
        private String loan_acct_type;
        /**
         * 安全信息 String(2048) N 安全信息，jsonObject字符串
         */
        @Valid
        private RefundReq.RiskCheckData risk_check_data;
        /**
         * 设备信息 String(2048) N 设备信息，jsonObject字符串
         */
        @Valid
        private RefundReq.TerminalDeviceData terminal_device_data;
        /**
         * 异步通知地址 String(512) N 示例值： http://service.example.com/to/path
         */
        @Size(max = 512, message = "异步通知地址长度不能超过512位")
        @Pattern(regexp = "^https?://.*$", message = "异步通知地址需为合法的HTTP/HTTPS URL")
        private String notify_url;
        /**
         * 银联参数集合 String(2048) N 银联参数集合；jsonObject字符串
         */
        @Valid
        private RefundReq.UnionpayData unionpay_data;
        /**
         * 退款原因 String(200) N 退款原因，当订单退款金额小于等于1元且为部分退款时，退款原因将不会退款详情展示
         */
        @Size(max = 200, message = "退款原因长度不能超过200位")
        private String refund_desc;

        /**
         * 校验原交易ID三选一规则
         */
        @AssertTrue(message = "org_hf_seq_id、org_party_order_id、org_req_seq_id必须三选一")
        public boolean isOriginalOrderIdValid() {
            return StrUtil.isNotBlank(org_hf_seq_id)
                    || StrUtil.isNotBlank(org_party_order_id)
                    || StrUtil.isNotBlank(org_req_seq_id);
        }

        /**
         * 分账对象-内部类
         */
        @Data
        public static class AcctSplitBunch {
            /**
             * 分账信息列表 Array(2048) N 分账明细
             */
            @Valid
            private List<RefundReq.AcctSplitBunch.AcctInfos> acct_infos;

            /**
             * 分账明细-内部类
             */
            @Data
            public static class AcctInfos {
                /**
                 * 分账金额 String(14) Y 单位元，需保留小数点后两位，示例值：1.00，最低传入0.01
                 */
                @NotBlank(message = "分账金额不能为空")
                @Digits(integer = 11, fraction = 2, message = "分账金额格式错误")
                @DecimalMin(value = "0.01", message = "分账金额最低为0.01元")
                private String div_amt;

                /**
                 * 分账接收方ID String(32) Y 斗拱开户时生成；示例值：6666000108854952
                 */
                @NotBlank(message = "分账接收方ID不能为空")
                @Size(max = 32, message = "分账接收方ID长度不能超过32位")
                private String huifu_id;

                /**
                 * 垫资金额 String(12) N 单位元，需保留小数点后两位，示例值：1.00，最低传入0.01；
                 * 注：若由第三方全额垫资，则不传该字段
                 */
                @NotBlank(message = "垫资金额不能为空")
                @Digits(integer = 9, fraction = 2, message = "垫资金额格式错误")
                @DecimalMin(value = "0.01", message = "垫资金额最低为0.01元")
                private String part_loan_amt;
            }
        }

        /**
         * 微信拓展参数集合-内部类
         */
        @Data
        public static class WxData {
            /**
             * 退款商品详情 Object N 原订单为单品优惠订单 & 非全额退款时，退款商品详情必填
             * 原订单为单品优惠订单规则：原订单的 wx_data 中，promotion_flag值为“Y-是”。
             */
            @Valid
            private RefundReq.WxData.Detail detail;

            /**
             * 退款商品详情-内部类
             */
            @Data
            public static class Detail {
                /**
                 * 商品详情列表 Array N
                 */
                @Valid
                private List<RefundReq.WxData.Detail.GoodsDetail> goods_detail;

                /**
                 * 商品详情-内部类
                 */
                @Data
                public static class GoodsDetail {
                    /**
                     * 商品编码 String(32) Y 商户系统的商品编码。由半角的大小写字母、数字、中划线、下划线中的一种或几种组成，示例值：6934572310301
                     */
                    @NotBlank(message = "商品编码不能为空")
                    @Size(max = 32, message = "商品编码长度不能超过32位")
                    private String goods_id;

                    /**
                     * 微信支付商品编码 String(32) N 微信支付定义的统一商品编号（没有可不传）；示例值：
                     */
                    @Size(max = 32, message = "微信支付商品编码长度不能超过32位")
                    private String wxpay_goods_id;

                    /**
                     * 商品名称 String(14) N 示例值：华为手机
                     */
                    @Size(max = 14, message = "商品名称长度不能超过14位")
                    private String goods_name;

                    /**
                     * 商品退款金额 String(11) Y 示例值：1.00
                     */
                    @NotBlank(message = "商品退款金额不能为空")
                    @Digits(integer = 8, fraction = 2, message = "商品退款金额格式错误")
                    @DecimalMin(value = "0.01", message = "商品退款金额最低为0.01元")
                    private String refund_amt;

                    /**
                     * 商品退款数量 Integer(11) Y 示例值：1
                     */
                    @NotNull(message = "商品退款数量不能为空")
                    @Min(value = 1, message = "商品退款数量最低为1")
                    private Integer refund_quantity;

                    /**
                     * 商品单价 String(11) Y 单位:元，示例值：1.00。如果商户有优惠，需传输商户优惠后的单价；
                     * 例如：用户对一笔100元的订单使用了商场发的纸质优惠券100-50元，则活动商品的单价应为原单价-50元；
                     */
                    @NotBlank(message = "商品单价不能为空")
                    @Digits(integer = 8, fraction = 2, message = "商品单价格式错误")
                    @DecimalMin(value = "0.01", message = "商品单价最低为0.01元")
                    private String price;
                }
            }
        }

        /**
         * 数字货币扩展参数集合-内部类
         */
        @Data
        public static class DigitalCurrencyData {
            /**
             * 退款原因 String(60) N 数字货币退款必填；示例值：退货
             */
            @Size(max = 60, message = "数字货币退款原因长度不能超过60位")
            private String refund_desc;
        }

        /**
         * 补贴支付信息-内部类
         */
        @Data
        public static class CombinedpayData {
            /**
             * 汇付商户号 String(32) Y 渠道与一级代理商的直属商户ID
             */
            @NotBlank(message = "汇付商户号不能为空")
            @Size(max = 32, message = "汇付商户号长度不能超过32位")
            private String huifu_id;

            /**
             * 补贴方类型 String(32) Y channel-渠道，merchant-总部商户，agent-代理，mertomer-商户；示例值：channel
             */
            @NotBlank(message = "补贴方类型不能为空")
            @Size(max = 32, message = "补贴方类型长度不能超过32位")
            @Pattern(regexp = "^(channel|merchant|agent|mertomer)$", message = "补贴方类型只能是channel、merchant、agent、mertomer")
            private String user_type;

            /**
             * 补贴方账户号 String(32) Y 营销补贴方账户号；示例值：F00598600
             */
            @NotBlank(message = "补贴方账户号不能为空")
            @Size(max = 32, message = "补贴方账户号长度不能超过32位")
            private String acct_id;

            /**
             * 补贴金额 String(14) Y 单位元，需保留小数点后两位，示例值：1.00，最低传入0.01
             */
            @NotBlank(message = "补贴金额不能为空")
            @Digits(integer = 11, fraction = 2, message = "补贴金额格式错误")
            @DecimalMin(value = "0.01", message = "补贴金额最低为0.01元")
            private String amount;
        }

        /**
         * 补贴支付手续费承担方信息-内部类
         */
        @Data
        public static class CombinedpayDataFeeInfo {
            /**
             * 补贴支付手续费承担方汇付编号 String(32) N 示例值：6666000123120001
             */
            @Size(max = 32, message = "补贴支付手续费承担方汇付编号长度不能超过32位")
            private String huifu_id;

            /**
             * 补贴支付手续费承担方账户号 String(32) N 补贴支付手续费承担方账户号；示例值：F00598610
             */
            @Size(max = 32, message = "补贴支付手续费承担方账户号长度不能超过32位")
            private String acct_id;
        }

        /**
         * 安全信息-内部类
         */
        @Data
        public static class RiskCheckData {
            /**
             * ip地址 String(32) N IP地址、经纬度、基站地址最少要送其中一项；示例值：192.68.34.22
             */
            @Size(max = 32, message = "IP地址长度不能超过32位")
            @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", message = "IP地址格式不合法")

            private String ip_addr;

            /**
             * 基站地址 String(32) N IP地址、经纬度、基站地址最少要送其中一项；
             * 【mcc】+【mnc】+【location_cd】+【lbs_num】
             * - mcc:移动国家代码，460代表中国；3位长
             * - mnc：移动网络号码；2位长；
             * - location_cd：位置区域码，16进制，5位长
             * - lbs_num：基站编号，16进制，5位长
             * - 注意若位数不足用空格补足；
             * 示例值：460001039217563，460（mcc)， 00(mnc)，10392(location_cd)， 17563(lbs_num)
             */
            @Size(max = 32, message = "基站地址长度不能超过32位")
            private String base_station;

            /**
             * 纬度 String(32) N 纬度整数位不超过2位，小数位不超过6位；
             * 格式：+表示北纬，-表示南纬。示例值：+37.12；
             * IP地址、经纬度、基站地址最少要送其中一项
             */
            @Size(max = 32, message = "纬度长度不能超过32位")
            @Pattern(regexp = "^[+-]?\\d{1,2}\\.\\d{1,6}$", message = "纬度格式不合法（整数位≤2位，小数位≤6位）")
            private String latitude;

            /**
             * 经度 String(32) N 经度整数位不超过3位，小数位不超过5位；
             * 格式：+表示东经，-表示西经。示例值：-121.213；
             * IP地址、经纬度、基站地址最少要送其中一项
             */
            @Size(max = 32, message = "经度长度不能超过32位")
            @Pattern(regexp = "^[+-]?\\d{1,3}\\.\\d{1,5}$", message = "经度格式不合法（整数位≤3位，小数位≤5位）")
            private String longitude;

            /**
             * 校验安全信息至少一项必填
             */
            @AssertTrue(message = "IP地址、基站地址、经纬度至少需填写一项")
            public boolean isRiskDataValid() {
                return StrUtil.isNotBlank(ip_addr)
                        || StrUtil.isNotBlank(base_station)
                        || (StrUtil.isNotBlank(latitude) && StrUtil.isNotBlank(longitude));
            }
        }

        /**
         * 设备信息-内部类
         */
        @Data
        public static class TerminalDeviceData {
            /**
             * 设备类型 String(2) N 1:手机，2:平板，3:手表，4:PC；示例值：1
             */
            @Size(max = 2, message = "设备类型长度不能超过2位")
            @Pattern(regexp = "^(1|2|3|4)?$", message = "设备类型只能是1、2、3、4")
            private String device_type;

            /**
             * 交易设备IP String(64) N 用于标识交易设备IP地址，绑卡设备所在的公网IP，可用于定位所属地区，
             * 不是wifi连接时的局域网IP。示例值：10.10.0.1（IPv4）；目前暂传IPv4格式。
             * ABCD:EF01:2345:6789:ABCD:EF01:2345:6789（IPv6）；
             */
            @Size(max = 64, message = "交易设备IP长度不能超过64位")
            @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^[0-9a-fA-F:]+$", message = "交易设备IP格式不合法（支持IPv4/IPv6）")

            private String device_ip;

            /**
             * 交易设备MAC String(64) N 示例值：F0E1D2C3B4A5
             */
            @Size(max = 64, message = "交易设备MAC长度不能超过64位")
            private String device_mac;

            /**
             * 交易设备IMEI String(64) N 移动终端设备的唯一标识；示例值：460030912121001
             */
            @Size(max = 64, message = "交易设备IMEI长度不能超过64位")
            private String device_imei;

            /**
             * 交易设备IMSI String(64) N 示例值：460030912121001
             */
            @Size(max = 64, message = "交易设备IMSI长度不能超过64位")
            private String device_imsi;

            /**
             * 交易设备ICCID String(64) N 示例值：898600680113F0123014
             */
            @Size(max = 64, message = "交易设备ICCID长度不能超过64位")
            private String device_icc_id;

            /**
             * 交易设备WIFIMAC String(64) N 示例值：968778695A4B
             */
            @Size(max = 64, message = "交易设备WIFIMAC长度不能超过64位")
            private String device_wifi_mac;

            /**
             * 交易设备GPS String(64) N 示例值：20.346790,-4.654321
             */
            @Size(max = 64, message = "交易设备GPS长度不能超过64位")
            private String device_gps;
        }

        /**
         * 银联参数集合-内部类
         */
        @Data
        public static class UnionpayData {
            /**
             * 收款方附加数据 String(3000) N 请参考银联收款方附加数据(addn_data)说明
             */
            @Size(max = 3000, message = "收款方附加数据长度不能超过3000位")
            private String addn_data;
        }
    }

    /**
     * 汇付转出请求
     *
     * @author niu
     * @date 2025-08-25 17:26:49
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class RollOutReq extends Base.Req {

        /**
         * 出款方商户号 String(32) Y 出款方商户号，示例值：6666000109812124
         */
        @NotBlank(message = "出款方商户号不能为空")
        @Size(max = 32, message = "出款方商户号长度不能超过32位")
        private String out_huifu_id;

        /**
         * 支付金额 String(14) Y 单元：元。保留两位小数。示例值：1.23
         */
        @NotBlank(message = "支付金额不能为空")
        @Digits(integer = 12, fraction = 2, message = "支付金额格式错误（整数部分最多12位，小数部分2位）")
        @DecimalMin(value = "0.01", message = "支付金额最低为0.01元")
        private String ord_amt;

        /**
         * 商品描述 String(256) N 示例值：商品描述
         */
        @Size(max = 256, message = "商品描述长度不能超过256位")
        private String good_desc;

        /**
         * 备注 String(256) N 示例值：备注
         */
        @Size(max = 256, message = "备注长度不能超过256位")
        private String remark;

        /**
         * 是否延迟交易 String(1) N Y 为延迟，N为不延迟，不传默认N；示例值：Y
         */
        @Size(max = 1, message = "是否延迟交易长度不能超过1位")
        @Pattern(regexp = "^(Y|N)?$", message = "是否延迟交易只能是Y或N（为空默认N）")
        private String delay_acct_flag;

        /**
         * 分账对象 Y json格式；余额支付支持付款给多账户
         */
        @Valid
        @NotNull(message = "分账对象不能为空")
        private RollOutReq.AcctSplitBunch acct_split_bunch;

        /**
         * 安全信息 Y json格式
         */
        @Valid
        @NotNull(message = "安全信息不能为空")
        private RollOutReq.RiskCheckData risk_check_data;

        /**
         * 出款方账户号 String(32) N 只支持基本户和现金户；示例值：F00598600
         */
        @Size(max = 32, message = "出款方账户号长度不能超过32位")
        private String out_acct_id;

        /**
         * 资金类型 String(16) C 资金类型。支付渠道为中信E管家时，资金类型必填（详见说明）
         */
        @Size(max = 16, message = "资金类型长度不能超过16位")
        private String fund_type;

        /**
         * 支付渠道 String(8) N 支付渠道。HUIFU=汇付账务(默认)；ZXE=中信E管家
         */
        @Size(max = 8, message = "支付渠道长度不能超过8位")
        @Pattern(regexp = "^(HUIFU|ZXE)?$", message = "支付渠道只能是HUIFU或ZXE（为空默认HUIFU）")
        private String acct_channel;

        /**
         * 灵活用工标志 String(1) N Y：灵活用工，N：非灵活用工(默认)；示例值：Y
         */
        @Size(max = 1, message = "灵活用工标志长度不能超过1位")
        @Pattern(regexp = "^(Y|N)?$", message = "灵活用工标志只能是Y或N（为空默认N）")
        private String hyc_flag;

        /**
         * 灵活用工平台 String(3) N 灵活用工平台 LJH：乐接活，HYC：汇优财，HXY：汇薪云（默认）。仅在hyc_flag=Y时起作用
         */
        @Size(max = 3, message = "灵活用工平台长度不能超过3位")
        @Pattern(regexp = "^(LJH|HYC|HXY)?$", message = "灵活用工平台只能是LJH、HYC、HXY（为空默认HXY）")
        private String lg_platform_type;

        /**
         * 落地公司商户号 String(18) N 灵活用工平台为汇优财、汇薪云时必填！示例值：6666000109812124
         */
        @Size(max = 18, message = "落地公司商户号长度不能超过18位")
        private String bmember_id;

        /**
         * 乐接活请求参数集合 N 乐接活请求参数集合。jsonObject字符串
         */
        @Valid
        private RollOutReq.LjhData ljh_data;

        /**
         * 异步通知地址 String(256) N 灵活用工标识Y时且灵活用工平台为汇优财，若交易同步受理成功，会收到异步通知。示例值：https://callback.service.com/xx
         */
        @Size(max = 256, message = "异步通知地址长度不能超过256位")
        @Pattern(regexp = "^https?://.*$", message = "异步通知地址需为合法的HTTP/HTTPS URL")
        private String notify_url;

        /**
         * 手续费承担方标识 String(4) C 余额支付手续费承担方标识；商户余额支付扣收规则为接口指定承担方时必填！枚举值：OUT：出款方；IN：分账接受方。示例值：IN
         */
        @Size(max = 4, message = "手续费承担方标识长度不能超过4位")
        @Pattern(regexp = "^(OUT|IN)?$", message = "手续费承担方标识只能是OUT或IN")
        private String trans_fee_take_flag;

        /**
         * 余额支付安全核验方式 String(16) N SMS-短信验证
         */
        @Size(max = 16, message = "余额支付安全核验方式长度不能超过16位")
        @Pattern(regexp = "^(SMS)?$", message = "余额支付安全核验方式暂仅支持SMS")
        private String verify_type;

        /**
         * 核验值 String(64) C verify_type不为空时必填。当verify_type=SMS时，填写用户收到的短信验证码
         */
        @Size(max = 64, message = "核验值长度不能超过64位")
        private String verify_value;

        /**
         * 校验：支付渠道为ZXE（中信E管家）时，资金类型必填
         */
        @AssertTrue(message = "支付渠道为中信E管家时，资金类型不能为空")
        public boolean isFundTypeValid() {
            if ("ZXE".equals(acct_channel)) {
                return StrUtil.isNotBlank(fund_type);
            }
            return true;
        }

        /**
         * 校验：灵活用工标志为Y时的规则
         */
        @AssertTrue(message = "灵活用工平台为汇优财/HYC或汇薪云/HXY时，落地公司商户号不能为空")
        public boolean isHycFlagValid() {
            if ("Y".equals(hyc_flag)) {
                // 灵活用工平台为HYC/HXY时，bmember_id必填
                if ("HYC".equals(lg_platform_type) || "HXY".equals(lg_platform_type)) {
                    return StrUtil.isNotBlank(bmember_id);
                }
                // 灵活用工平台为LJH时，ljh_data的tax_area_id和template_id必填
                if ("LJH".equals(lg_platform_type)) {
                    return ljh_data != null
                            && StrUtil.isNotBlank(ljh_data.getTax_area_id())
                            && StrUtil.isNotBlank(ljh_data.getTemplate_id());
                }
            }
            return true;
        }

        /**
         * 校验：核验方式不为空时，核验值必填
         */
        @AssertTrue(message = "核验方式不为空时，核验值不能为空")
        public boolean isVerifyValueValid() {
            return StrUtil.isBlank(verify_type) || StrUtil.isNotBlank(verify_value);
        }

        // -------------------------- 嵌套对象：分账对象 --------------------------
        @Data
        public static class AcctSplitBunch {
            /**
             * 分账明细 Array N
             */
            @Valid
            @NotEmpty(message = "分账明细不能为空")
            private List<RollOutReq.AcctInfo> acct_infos;
            /**
             * 百分比分账标志 String(1) N Y:使用百分比分账；示例值：Y
             */
            @Size(max = 1, message = "百分比分账标志长度不能超过1位")
            @Pattern(regexp = "^(Y|N)?$", message = "百分比分账标志只能是Y或N")
            private String percentage_flag;
            /**
             * 是否净值分账 String(1) N Y:使用净值分账，仅在交易手续费由出款方承担且使用百分比分账时起作用；示例值：Y
             */
            @Size(max = 1, message = "是否净值分账长度不能超过1位")
            @Pattern(regexp = "^(Y|N)?$", message = "是否净值分账只能是Y或N")
            private String is_clean_split;

            public AcctSplitBunch(List<RollOutReq.AcctInfo> acct_infos, String percentage_flag, String is_clean_split) {
                this(acct_infos);
                this.percentage_flag = percentage_flag;
                this.is_clean_split = is_clean_split;
            }

            public AcctSplitBunch(List<RollOutReq.AcctInfo> acct_infos) {
                this.acct_infos = acct_infos;
                if (CollUtil.isNotEmpty(acct_infos)) {
                    acct_infos.forEach(it -> it.setPercentage_flag(percentage_flag));
                }
            }

            /**
             * 校验：百分比分账时，分账百分比之和为100%
             */
            @AssertTrue(message = "百分比分账时，分账百分比之和必须为100.00%")
            public boolean isPercentageSumValid() {
                if ("Y".equals(percentage_flag) && acct_infos != null && !acct_infos.isEmpty()) {
                    BigDecimal total = BigDecimal.ZERO;
                    for (RollOutReq.AcctInfo info : acct_infos) {
                        if (info.getPercentage_div() != null) {
                            total = total.add(new BigDecimal(info.getPercentage_div()));
                        }
                    }
                    return total.compareTo(new BigDecimal("100.00")) == 0;
                }
                return true;
            }
        }

        // -------------------------- 条件校验规则 --------------------------

        @Data
        public static class AcctInfo {
            private String percentage_flag;
            /**
             * 分账金额 String(14) N 单位元，需保留小数点后两位，示例值：1.00 ,最低传入0.01
             */
            @Digits(integer = 12, fraction = 2, message = "分账金额格式错误（整数部分最多12位，小数部分2位）")
            @DecimalMin(value = "0.01", message = "分账金额最低为0.01元")
            private String div_amt;

            /**
             * 分账接收方ID String(32) Y 开户自动生成编号；示例值：6666000108854952
             */
            @NotBlank(message = "分账接收方ID不能为空")
            @Size(max = 32, message = "分账接收方ID长度不能超过32位")
            private String huifu_id;

            /**
             * 账户号 String(32) N 可指定账户号，仅支持基本户、现金户，不填默认为基本户，示例值：F00598600
             */
            @Size(max = 32, message = "账户号长度不能超过32位")
            private String acct_id;

            /**
             * 分账百分比% String(6) N 示例值：23.50，表示23.50%。仅在percentage_flag=Y时起作用
             */
            @Digits(integer = 3, fraction = 2, message = "分账百分比格式错误（整数部分最多3位，小数部分2位）")
            @DecimalMin(value = "0.01", message = "分账百分比最低为0.01%")
            @DecimalMax(value = "100.00", message = "分账百分比最高为100.00%")
            private String percentage_div;

            /**
             * 校验：非百分比分账时，分账金额必填
             */
            @AssertTrue(message = "非百分比分账时，分账金额不能为空")
            public boolean isDivAmtValid() {
                if (!"Y".equals(percentage_flag)) {
                    return StrUtil.isNotBlank(div_amt);
                }
                return true;
            }
        }

        // -------------------------- 嵌套对象：安全信息 --------------------------
        @Data
        public static class RiskCheckData {
            /**
             * 产品子类 String(20) N 示例值：1
             */
            @Size(max = 20, message = "产品子类长度不能超过20位")
            private String sub_product;

            /**
             * 转账原因 String(2) Y 01：卡券推广类；02：卡券核销类；03：消费；04：工资代发；05：分润；06：灵活用工；示例值：01
             */
            @NotBlank(message = "转账原因不能为空")
            @Size(max = 2, message = "转账原因长度不能超过2位")
            @Pattern(regexp = "^(01|02|03|04|05|06)$", message = "转账原因只能是01、02、03、04、05、06")
            private String transfer_type;

            /**
             * 纬度 String(32) N 格式：+表示北纬，-表示南纬。纬度整数位不超过2位，小数位不超过6位。示例值：+37.12
             */
            @Size(max = 32, message = "纬度长度不能超过32位")
            @Pattern(regexp = "^[+-]?\\d{1,2}\\.\\d{1,6}$", message = "纬度格式不合法（整数位≤2位，小数位≤6位）")
            private String latitude;

            /**
             * 经度 String(32) N 格式：+表示东经，-表示西经；经度整数位不超过3位，小数位不超过5位；示例值：-121.213
             */
            @Size(max = 32, message = "经度长度不能超过32位")
            @Pattern(regexp = "^[+-]?\\d{1,3}\\.\\d{1,5}$", message = "经度格式不合法（整数位≤3位，小数位≤5位）")
            private String longitude;

            /**
             * 基站地址 String(32) N 【mcc】+【mnc】+【location_cd】+【lbs_num】
             */
            @Size(max = 32, message = "基站地址长度不能超过32位")
            private String base_station;

            /**
             * IP地址 String(32) N 示例值：172.28.52.52
             */
            @Size(max = 32, message = "IP地址长度不能超过32位")
            @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", message = "IP地址格式不合法")
            private String ip_addr;

            /**
             * 校验：IP地址、经纬度、基站地址至少填写一项
             */
            @AssertTrue(message = "IP地址、经纬度、基站地址至少需填写一项")
            public boolean isRiskDataValid() {
                return StrUtil.isNotBlank(ip_addr)
                        || StrUtil.isNotBlank(base_station)
                        || (StrUtil.isNotBlank(latitude) && StrUtil.isNotBlank(longitude));
            }
        }

        // -------------------------- 嵌套对象：乐接活请求参数 --------------------------
        @Data
        public static class LjhData {
            /**
             * 税源地ID String(64) N 税源地ID。灵活用工平台为乐接活时必填！
             */
            @Size(max = 64, message = "税源地ID长度不能超过64位")
            private String tax_area_id;

            /**
             * 任务模板ID String(64) N 任务模板ID。灵活用工平台为乐接活时必填！
             */
            @Size(max = 64, message = "任务模板ID长度不能超过64位")
            private String template_id;
        }
    }

}
