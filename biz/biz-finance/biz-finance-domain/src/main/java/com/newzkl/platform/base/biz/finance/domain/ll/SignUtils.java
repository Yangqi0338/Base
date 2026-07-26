package com.newzkl.platform.base.biz.finance.domain.ll;

import com.newzkl.platform.base.biz.finance.model.support.FinanceProperties.LianLianProperties;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HexFormat;

/**
 * 连连 (LianLian) 加签验签工具。
 *
 * <p>迁移自 new-scm {@code application.utils.SignUtils} + {@code RSASign} + {@code Md5Algorithm} 三合一。
 * 三处改造:</p>
 * <ol>
 *   <li><b>密钥外置</b>: 旧类内 {@code PUBLIC_KEY}/{@code PRIVATE_KEY} 明文常量删除,
 *       改读 {@link LianLianProperties}(Nacos {@code scm.fi.lianlian.*} 注入)。</li>
 *   <li><b>去 sun.misc</b>: 旧 {@code BASE64Encoder/BASE64Decoder} 在 JDK17+ 已移除。
 *       逐调用点判定编码语义, 见各方法 JavaDoc。</li>
 *   <li><b>去自研 Base64/Md5 工具</b>: 旧 {@code utils.Base64.getBytesBASE64} 内部实际已是
 *       {@code java.util.Base64.getDecoder()}, 旧 {@code Md5Algorithm} 为手写小写 hex,
 *       等价于 {@link HexFormat#of()}, 均直接内联。</li>
 * </ol>
 *
 * <p>算法与旧实现严格一致: 摘要 MD5(小写 hex 串) → {@code MD5withRSA} 签名 → Base64。</p>
 *
 * @author KC
 */
@Slf4j
public final class SignUtils {

    /**
     * 密钥算法。
     */
    private static final String ALGORITHM = "RSA";

    /**
     * 签名算法, 与连连约定为 MD5withRSA (注意非汇付的 SHA256WithRSA)。
     */
    private static final String SIGN_ALGORITHM = "MD5withRSA";

    private SignUtils() {
    }

    /**
     * 对报文加签, 结果写入连连要求的 {@code Signature-Data} 请求头。
     *
     * <p>Base64 决策: 旧实现走 {@code commons-codec Base64.encodeBase64}(<b>不换行</b>),
     * 故此处用 {@link Base64#getEncoder()} 而非 {@code getMimeEncoder()} —— 签名串进 HTTP header,
     * 含换行会破坏报文, 语义与旧实现一致。</p>
     *
     * @param signStr 待签名源串 (通常是请求体 JSON)
     * @return Base64 签名串; 签名失败返回 {@code null}
     */
    public static String sign(String signStr) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(LianLianProperties.privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey privateKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(keySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(md5Hex(signStr).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            log.error("连连加签失败, {}", e.getMessage());
            return null;
        }
    }

    /**
     * 校验连连回调报文签名。
     *
     * <p>Base64 决策: 签名串取自 HTTP 报文, 天然无换行, 用 {@link Base64#getDecoder()}
     * (旧 {@code utils.Base64.getBytesBASE64} 内部即为此实现)。</p>
     *
     * @param signStr   源串 (回调原始报文)
     * @param signedStr 连连给出的签名串
     * @return 验签通过返回 {@code true}; 异常或不通过返回 {@code false}
     */
    public static boolean checkSign(String signStr, String signedStr) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(LianLianProperties.publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey publicKey = KeyFactory.getInstance(ALGORITHM).generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(md5Hex(signStr).getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(signedStr));
        } catch (Exception e) {
            log.error("连连验签异常, {}", e.getMessage());
            return false;
        }
    }

    /**
     * 用连连公钥加密敏感串 (如支付密码)。
     *
     * <p>Base64 决策: 旧实现 {@code new BASE64Encoder().encode()} 每 76 字符插换行 (等价 MIME),
     * 但密文作为 JSON 字段值提交, 换行会被 JSON 转义成 {@code \n} 并导致连连解密失败;
     * 且旧 {@code RSASign.encrypt} 同场景已用不换行的 {@code commons-codec} 实现。
     * 故此处统一取 {@link Base64#getEncoder()}(<b>不换行</b>), 属旧实现缺陷修正。</p>
     *
     * @param source 明文
     * @return Base64 密文
     * @throws Exception 加密失败时抛出
     */
    public static String encrypt(String source) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(LianLianProperties.publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        PublicKey publicKey = KeyFactory.getInstance(ALGORITHM).generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(source.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * MD5 摘要并转小写 16 进制串。
     *
     * <p>等价旧 {@code Md5Algorithm.md5Digest} 的手写 hexDigits 实现。</p>
     *
     * @param src 源串
     * @return 32 位小写 hex 摘要
     * @throws Exception MD5 算法不可用时抛出
     */
    static String md5Hex(String src) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        return HexFormat.of().formatHex(digest.digest(src.getBytes(StandardCharsets.UTF_8)));
    }
}
