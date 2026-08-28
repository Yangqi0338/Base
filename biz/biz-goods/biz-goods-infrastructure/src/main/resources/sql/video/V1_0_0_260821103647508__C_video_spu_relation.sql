CREATE TABLE `video_spu_relation` (
  `video_id` bigint NULL COMMENT '视频 ID',
  `spu_id` bigint NULL COMMENT '商品 SPU ID',
  `type` int NULL COMMENT '视频类型(1 短视频，2 长视频)'
) COMMENT = '视频-商品关联数据对象

<p>纯关系表, 无主键与审计列, 故不继承 {@code BaseDO} (照 new-scm)。</p>';
