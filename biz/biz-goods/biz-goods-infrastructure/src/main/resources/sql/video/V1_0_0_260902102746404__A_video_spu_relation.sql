ALTER TABLE
  `video_spu_relation` MODIFY COLUMN `video_id` bigint NULL COMMENT '视频 ID',
  MODIFY COLUMN `spu_id` bigint NULL COMMENT '商品 SPU ID',
  MODIFY COLUMN `type` int NULL COMMENT '视频类型(1 短视频，2 长视频)',
  COMMENT = '视频-商品关联数据对象

<p>纯关系表, 无主键与审计列, 故不继承 {@code BaseDO} (照 new-scm)。</p>';
