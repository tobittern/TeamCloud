## 平台简介(公测)

> RuoYi-Cloud-Plus `微服务通用权限管理系统` 重写 RuoYi-Cloud 全方位升级(不兼容原框架)

> 系统演示: [传送门](https://gitee.com/JavaLionLi/RuoYi-Vue-Plus/wikis/系统演示?sort_id=4836388) 分布式集群版本(功能一致)

| 功能介绍         | 使用技术                     | 文档地址                                                                                              | 特性注意事项                       |
|--------------|--------------------------|---------------------------------------------------------------------------------------------------|------------------------------|
| 微服务权限管理系统    | RuoYi-Cloud-Plus         | [RuoYi-Cloud-Plus官网](https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus)                               | 重写 RuoYi-Cloud 全方位升级(不兼容原框架) |
| 分布式集群分支      | RuoYi-Vue-Plus           | [RuoYi-Vue-Plus官网](https://gitee.com/JavaLionLi/RuoYi-Vue-Plus)                                   | 重写 RuoYi-Vue (不兼容原框架)        |
| 前端开发框架       | Vue、Element UI           | [Element UI官网](https://element.eleme.cn/#/zh-CN)                                                  |                              |
| 后端开发框架       | SpringBoot               | [SpringBoot官网](https://spring.io/projects/spring-boot/#learn)                                     |                              |
| 微服务开发框架      | SpringCloud              | [SpringCloud官网](https://spring.io/projects/spring-cloud)                                          |                              |
| 微服务开发框架      | SpringCloudAlibaba       | [SpringCloudAlibaba官网](https://spring.io/projects/spring-cloud-alibaba)                           |                              |
| 容器框架         | Undertow                 | [Undertow官网](https://undertow.io/)                                                                | 基于 XNIO 的高性能容器               |
| 权限认证框架       | Sa-Token、Jwt             | [Sa-Token官网](https://sa-token.dev33.cn/)                                                          | 强解耦、强扩展                      |
| 关系数据库        | MySQL                    | [MySQL官网](https://dev.mysql.com/)                                                                 | 适配 8.X 最低 5.7                |
| 关系数据库(未完成)   | Oracle                   | [Oracle官网](https://www.oracle.com/cn/database/)                                                   | 适配 12c                       |
| 关系数据库(未完成)   | PostgreSQL               | [PostgreSQL官网](https://www.postgresql.org/)                                                       | 适配 14                        |
| 关系数据库(未完成)   | SQLServer                | [SQLServer官网](https://docs.microsoft.com/zh-cn/sql/sql-server)                                    | 适配 2019                      |
| 缓存数据库        | Redis                    | [Redis官网](https://redis.io/)                                                                      | 适配 6.X 最低 5.X                |
| 分布式注册中心      | Alibaba Nacos            | [Alibaba Nacos文档](https://nacos.io/zh-cn/docs/quick-start.html)                                   | 采用2.X 基于GRPC通信高性能            |
| 分布式配置中心      | Alibaba Nacos            | [Alibaba Nacos文档](https://nacos.io/zh-cn/docs/quick-start.html)                                   | 采用2.X 基于GRPC通信高性能            |
| 服务网关         | SpringCloud Gateway      | [SpringCloud Gateway文档](https://spring.io/projects/spring-cloud-gateway)                          | 响应式高性能网关                     |
| 负载均衡         | SpringCloud Loadbalancer | [SpringCloud Loadbalancer文档](https://spring.io/guides/gs/spring-cloud-loadbalancer/)              | 负载均衡处理                       |
| RPC远程调用      | Apache Dubbo             | [Apache Dubbo官网](https://dubbo.apache.org/zh/)                                                    | 原生态使用体验、高性能                  |
| 分布式限流熔断      | Alibaba Sentinel         | [Alibaba Sentinel文档](https://sentinelguard.io/zh-cn/)                                             | 无侵入、高扩展                      |
| 分布式事务        | Alibaba Seata            | [Alibaba Seata文档](http://seata.io/zh-cn/)                                                         | 无侵入、高扩展 支持 四种模式              |
| 分布式消息队列(未完成) | SpringCloud Stream       | [SpringCloud Stream文档](https://spring.io/projects/spring-cloud-stream)                            | 门面框架兼容各种MQ集成                 |
| 分布式消息队列(未完成) | Apache Kafka             | [Apache Kafka文档](https://kafka.apache.org/)                                                       | 高性能高速度                       |
| 分布式消息队列(未完成) | Apache RocketMQ          | [Apache RocketMQ文档](http://rocketmq.apache.org/)                                                  | 高可用功能多样                      |
| 分布式消息队列(未完成) | RabbitMQ                 | [RabbitMQ文档](https://www.rabbitmq.com/)                                                           | 支持各种扩展插件功能多样性                |
| 分布式搜索引擎(未完成) | ElasticSearch            | [ElasticSearch官网](https://www.elastic.co/cn/elasticsearch/)                                       | 业界知名                         |
| 分布式链路追踪(未完成) | Apache SkyWalking        | [Apache SkyWalking文档](https://skywalking.apache.org/docs/)                                        | 链路追踪、网格分析、度量聚合、可视化           |
| 分布式日志中心(未完成) | ELK                      | [ElasticSearch官网](https://www.elastic.co/cn/elasticsearch/)                                       | ELK业界成熟解决方案                  |
| 分布式锁         | Lock4j                   | [Lock4j官网](https://gitee.com/baomidou/lock4j)                                                     | 注解锁、工具锁 多种多样                 |
| 分布式幂等        | Redisson                 | [Lock4j文档](https://gitee.com/baomidou/lock4j)                                                     | 拦截重复提交                       |
| 分布式任务调度      | Xxl-Job                  | [Xxl-Job官网](https://www.xuxueli.com/xxl-job/)                                                     | 高性能 高可靠 易扩展                  |
| 分布式文件存储      | Minio                    | [Minio文档](https://docs.min.io/)                                                                   | 本地存储                         |
| 分布式云存储       | 七牛、阿里、腾讯                 | [OSS使用文档](https://gitee.com/JavaLionLi/RuoYi-Vue-Plus/wikis/pages?sort_id=4359146&doc_id=1469725) | 云存储                          |
| 分布式监控(未完成)   | Prometheus、Grafana       | [Prometheus文档](https://prometheus.io/docs/introduction/overview/)                                 | 全方位性能监控                      |
| 服务监控         | SpringBoot-Admin         | [SpringBoot-Admin文档](https://codecentric.github.io/spring-boot-admin/current/)                    | 全方位服务监控                      |
| 数据库框架        | Mybatis-Plus             | [Mybatis-Plus文档](https://baomidou.com/guide/)                                                     | 快速 CRUD 增加开发效率               |
| 数据库框架        | P6spy                    | [p6spy官网](https://p6spy.readthedocs.io/)                                                          | 更强劲的 SQL 分析                  |
| 多数据源框架       | Dynamic-Datasource       | [dynamic-ds文档](https://www.kancloud.cn/tracy5546/dynamic-datasource/content)                      | 支持主从与多种类数据库异构                |
| 序列化框架        | Jackson                  | [Jackson官网](https://github.com/FasterXML/jackson)                                                 | 统一使用 jackson 高效可靠            |
| Redis客户端     | Redisson                 | [Redisson文档](https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95)                        | 支持单机、集群配置                    |
| 校验框架         | Validation               | [Validation文档](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/)    | 增强接口安全性、严谨性 支持国际化            |
| Excel框架      | Alibaba EasyExcel        | [EasyExcel文档](https://www.yuque.com/easyexcel/doc/easyexcel)                                      | 性能优异 扩展性强                    |
| 文档框架         | Knife4j                  | [Knife4j文档](https://doc.xiaominfo.com/knife4j/documentation/)                                     | 美化接口文档                       |
| 工具类框架        | Hutool、Lombok            | [Hutool文档](https://www.hutool.cn/docs/)                                                           | 减少代码冗余 增加安全性                 |
| 代码生成器        | 适配MP、Knife4j规范化代码        | [Hutool文档](https://www.hutool.cn/docs/)                                                           | 一键生成前后端代码                    |
| 部署方式         | Docker                   | [Docker文档](https://docs.docker.com/)                                                              | 容器编排 一键部署业务集群                |
| 国际化          | SpringMessage            | [SpringMVC文档](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc)   | Spring标准国际化方案                |

## 参考文档

使用框架前请仔细阅读文档重点注意事项
<br>
>[初始化项目 必看](https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus/wikis/pages?sort_id=5279751&doc_id=2056143)
>>[https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus/wikis/pages?sort_id=5279751&doc_id=2056143](https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus/wikis/pages?sort_id=5279751&doc_id=2056143)
>
>[应用部署 Wiki](https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus/wikis/pages?sort_id=5305504&doc_id=2056143)
>>[https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus/wikis/pages?sort_id=5305504&doc_id=2056143](https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus/wikis/pages?sort_id=5305504&doc_id=2056143)
>
>[参考文档 Wiki](https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus/wikis/pages)
>>[https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus/wikis/pages](https://gitee.com/JavaLionLi/RuoYi-Cloud-Plus/wikis/pages)


## 软件架构图

<img src="https://oscimg.oschina.net/oscnet/up-82e9722ecb846786405a904bafcf19f73f3.png"/>



### 其他
* github 地址 [RuoYi-Cloud-Plus-github](https://github.com/JavaLionLi/RuoYi-Cloud-Plus)
* 分离版分支 [RuoYi-Vue-Plus](https://gitee.com/JavaLionLi/RuoYi-Vue-Plus)
* 单模块 fast 分支 [RuoYi-Vue-Plus-fast](https://gitee.com/JavaLionLi/RuoYi-Vue-Plus/tree/fast/)


## 业务功能

| 功能 | 介绍 |
|---|---|
| 用户管理 | 用户是系统操作者，该功能主要完成系统用户配置。 |
| 部门管理 | 配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。 |
| 岗位管理 | 配置系统用户所属担任职务。 |
| 菜单管理 | 配置系统菜单，操作权限，按钮权限标识等。 |
| 角色管理 | 角色菜单权限分配、设置角色按机构进行数据范围权限划分。 |
| 字典管理 | 对系统中经常使用的一些较为固定的数据进行维护。 |
| 参数管理 | 对系统动态配置常用参数。 |
| 通知公告 | 系统通知公告信息发布维护。 |
| 操作日志 | 系统正常操作日志记录和查询；系统异常信息日志记录和查询。 |
| 登录日志 | 系统登录日志记录查询包含登录异常。 |
| 文件管理 | 系统文件上传、下载等管理。 |
| 定时任务 | 在线（添加、修改、删除)任务调度包含执行结果日志。 |
| 代码生成 | 前后端代码的生成（java、html、xml、sql）支持CRUD下载 。 |
| 系统接口 | 根据业务代码自动生成相关的api接口文档。 |
| 服务监控 | 监视集群系统CPU、内存、磁盘、堆栈、在线日志、Spring相关配置等。 |
| 缓存监控 | 对系统的缓存信息查询，命令统计等。 |
| 在线构建器 | 拖动表单元素生成相应的HTML代码。 |
| 连接池监视 | 监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。 |
| 使用案例 | 系统的一些功能案例 |
