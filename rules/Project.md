---
type: project
scope: meta
priority: reference
---
# Project — Base 项目元信息

## 模块

| 模块 | artifactId | 职责 |
|------|-----------|------|
| 根 | Base | 聚合 + parent(spring-boot-starter-parent) + dependencyManagement + processor 配置 |
| BOM | bom | 内部模块版本统一管理 |
| common 聚合 | common | 聚合 ddd + core |
| ddd 聚合 | common-ddd | 聚合 5 分层基类 |
| ddd-model | ddd-model | 共享内核基类 |
| ddd-domain | ddd-domain | 通用 domain service 基类 / Repository / Api port |
| ddd-application | ddd-application | 通用编排基类（留位） |
| ddd-action | ddd-action | 全局异常 / controller 基类 / 通用读 Command / web config |
| ddd-infrastructure | ddd-infrastructure | BaseRepositoryImpl / getLw / BaseDO |
| core 聚合 | common-core | 聚合技术中间件 |
| core-utils | core-utils | TransferUtils / ID / 业务码 / 金额 / JSON |
| core-redis | core-redis | RedisUtil / Redisson 锁 |
| core-rocketmq | core-rocketmq | MQUtil / @MQProducer/Consumer |
| biz 聚合 | biz | 业务服务域聚合（modules 待 grill 定案填入） |

## 依赖坐标

Spring Boot 3.2.5 / Spring Cloud 2023.0.1 / Spring Cloud Alibaba 2023.0.1.0 / Dubbo 3.3.2 / MyBatis-Plus 3.5.15 / Sa-Token 1.38.0 / Redisson 3.29.0 / RocketMQ 2.2.0 / MapStruct-Plus 1.5.0 / record-builder 44 / Lombok 1.18.42 / Hutool 5.8.35 / fastjson2 2.0.49。

## 项目间关联

- Base 被 `building-scm`(内供应链后台 starter) / `building-mmt`(脉脉通 C 端 starter) 依赖组装。
- 参考源：`/d/project/adopt-chicken-deploy`(模板权威) + `/d/project/zhongze/adopt-chicken`(codegraph 索引)。
- 实现参考：`building-old`(人工重构,只读不迁) + `new-scm`(旧仓,只删简)。

## 外部系统引用

MySQL 8.0.25 / Redis(Redisson) / RocketMQ / Nacos(注册+配置) / 七牛 CDN。具体地址在各 app 的 Nacos 配置。
