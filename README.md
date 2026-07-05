校园二手交易平台
项目简介
校园二手交易平台是一个面向校内学生的闲置物品交易系统，主要用于解决校园内教材、数码产品、生活用品、运动用品等二手物品发布、浏览、搜索和交易流转的问题。平台提供中文前端操作页面，用户可以快速登录、发布商品、浏览商品、筛选分类、发起订单，并完成卖家确认、交易完成或取消订单等流程。
本项目采用前后端分离思路开发。后端基于 Spring Boot 提供 RESTful API，负责用户认证、商品管理、订单管理、缓存、搜索和数据持久化；前端采用原生 HTML、CSS、JavaScript 构建轻量化页面，适合本地演示、课程设计和项目答辩展示。
项目特点
支持校园二手商品发布、浏览、搜索、分类筛选和订单管理。
使用 JWT 实现无状态登录认证，保护需要登录后才能访问的接口。
使用 MySQL 存储用户、商品和订单等核心业务数据。
使用 Flyway 管理数据库表结构和初始化示例数据。
使用 Redis 缓存首页热门商品，提高访问效率。
使用 Elasticsearch 支持商品标题和描述检索，搜索服务不可用时可降级到数据库模糊查询。
前端页面已完成中文化，适合直接用于中文项目展示。
技术栈
后端
Java 8
Spring Boot 2.7.18
Spring Web
Spring Security
Spring Data JPA
Spring Data Redis
Spring Data Elasticsearch
JWT
Flyway
MySQL 8
Redis 7
Elasticsearch 7.17
前端
HTML5
CSS3
JavaScript
原生 Fetch API
本地环境
Maven
Docker / Docker Compose
Python HTTP Server，用于启动静态前端页面
功能模块
用户模块
用户注册
用户登录
JWT 令牌生成与校验
获取当前登录用户信息
区分普通学生和管理员角色
商品模块
发布商品
浏览在售商品
查看商品详情
按分类筛选商品
搜索商品标题和描述
查看我的商品
商品发布、下架、售出等状态管理
订单模块
买家创建订单
卖家确认订单
买卖双方完成交易
买卖双方取消订单
查看我的订单
订单状态流转控制
搜索与缓存模块
Elasticsearch 商品搜索
MySQL 模糊查询降级
Redis 首页商品缓存
商品状态变化后自动清理缓存
项目目录结构
Campus Second-hand Trading Platform
├── frontend                         前端静态页面
│   ├── index.html                   页面结构
│   ├── styles.css                   页面样式
│   └── app.js                       页面交互和接口请求
├── src
│   └── main
│       ├── java/com/campus/trade
│       │   ├── common               通用响应、错误码、异常处理
│       │   ├── config               Security、Redis 等配置
│       │   ├── controller           REST 接口控制器
│       │   ├── dto                  请求和响应数据对象
│       │   ├── entity               JPA 实体、枚举、ES 文档
│       │   ├── repository           JPA 和 Elasticsearch 数据访问层
│       │   ├── security             JWT、用户认证、当前用户工具
│       │   └── service              核心业务逻辑
│       └── resources
│           ├── application.yml      项目配置
│           └── db/migration         Flyway 数据库迁移脚本
├── docs
│   └── api-examples.md              常用接口请求示例
├── docker-compose.yml               MySQL、Redis、Elasticsearch 本地环境
├── pom.xml                          Maven 项目配置
└── README.md                        项目说明文档
快速启动
1. 启动基础服务
在项目根目录执行：
docker compose up -d
如果本机 Docker Compose 版本较旧，可以使用：
docker-compose up -d
启动后会创建以下服务：
服务	地址	说明
MySQL	localhost:3307	项目数据库
Redis	localhost:6379	缓存服务
Elasticsearch	http://localhost:9200	商品搜索服务

2. 启动后端服务
在项目根目录执行：
mvn spring-boot:run
后端默认启动地址：
http://localhost:8081/api
健康检查地址：
http://localhost:8081/api/health
3. 启动前端页面
进入前端目录：
cd frontend
启动静态页面服务：
python -m http.server 5173 --bind 127.0.0.1
浏览器访问：
http://localhost:5173
默认账号
数据库初始化后会创建以下示例账号，密码均为：
123456
登录账号	页面显示	角色	说明
alice	小艾	学生	示例学生账号一
bob	小博	学生	示例学生账号二
admin	管理员	管理员	管理员示例账号

前端登录页面已将账号显示为中文选项，选择对应账号并输入默认密码即可登录。
常用接口
接口基础路径：
http://localhost:8081/api
方法	路径	权限	功能
GET	/health	公开	健康检查
POST	/auth/register	公开	用户注册
POST	/auth/login	公开	用户登录
GET	/auth/me	登录	获取当前用户信息
GET	/products	公开	获取在售商品列表
GET	/products/home	公开	获取首页热门商品
GET	/products/{id}	公开	获取商品详情
GET	/products/mine	登录	获取我的商品
POST	/products	登录	创建商品
PUT	/products/{id}	登录且为卖家	修改商品
POST	/products/{id}/publish	登录且为卖家	发布商品
POST	/products/{id}/off-shelf	登录且为卖家	下架商品
GET	/search/products	公开	搜索商品
POST	/orders	登录	创建订单
GET	/orders	登录	查看我的订单
GET	/orders/{id}	登录且为交易参与方	查看订单详情
POST	/orders/{id}/confirm	登录且为卖家	确认订单
POST	/orders/{id}/complete	登录且为交易参与方	完成交易
POST	/orders/{id}/cancel	登录且为交易参与方	取消订单

更多接口调用示例可查看：
docs/api-examples.md
核心业务流程
商品状态流转
草稿 -> 在售 -> 交易中 -> 已售出
     -> 已下架 -> 在售
对应后端状态：
DRAFT -> ON_SALE -> LOCKED -> SOLD
      -> OFF_SHELF -> ON_SALE
订单状态流转
待卖家确认 -> 已确认 -> 已完成
           -> 已取消
对应后端状态：
PENDING -> CONFIRMED -> COMPLETED
        -> CANCELLED
当买家下单后，商品会从“在售”变为“交易中”；如果订单取消，商品会重新回到“在售”；如果交易完成，商品会变为“已售出”。
配置说明
项目默认配置位于：
src/main/resources/application.yml
常用环境变量如下：
环境变量	默认值	说明
MYSQL_HOST	localhost	MySQL 主机
MYSQL_PORT	3307	MySQL 端口
MYSQL_DATABASE	campus_trade	数据库名称
MYSQL_USER	campus	数据库用户名
MYSQL_PASSWORD	campus123	数据库密码
REDIS_HOST	localhost	Redis 主机
REDIS_PORT	6379	Redis 端口
ELASTICSEARCH_URIS	http://localhost:9200	Elasticsearch 地址
JWT_SECRET	本地开发密钥	JWT 签名密钥
JWT_EXPIRATION_MINUTES	1440	JWT 有效时间

可参考 .env.example 修改本地运行参数。
数据库说明
项目使用 Flyway 自动管理数据库迁移，启动后端时会自动执行：
创建用户表、商品表、订单表。
初始化示例用户和示例商品。
修复示例账号密码。
中文化示例数据，便于前端展示。
迁移脚本目录：
src/main/resources/db/migration
前端说明
前端页面位于 frontend 目录，主要由三个文件组成：
index.html：页面结构。
styles.css：页面样式。
app.js：接口请求、页面渲染、登录、发布商品、下单和订单操作。
前端默认请求后端地址：
http://localhost:8081/api
如果后端端口发生变化，需要同步修改 frontend/app.js 中的 API_BASE。
注意事项
请先启动 MySQL、Redis 和 Elasticsearch，再启动后端服务。
本项目 Docker MySQL 默认映射到宿主机 3307 端口，避免和本机已有 MySQL 的 3306 冲突。
需要登录的接口必须携带请求头：Authorization: Bearer <token>。
前端页面显示为中文，但后端内部枚举仍使用英文状态值，这是为了保证接口和业务逻辑稳定。
Elasticsearch 未启动时，搜索功能会尝试降级为 MySQL 模糊查询，不影响核心商品浏览和交易流程。
项目总结
本项目实现了一个完整的校园二手交易业务闭环，覆盖用户登录、商品发布、商品浏览、商品搜索、订单创建、卖家确认、交易完成和订单取消等核心功能。项目结构清晰，技术栈完整，既适合用于学习 Spring Boot 前后端分离开发，也适合作为课程设计、毕业设计或个人项目作品进行展示。
