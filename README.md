# MSSHOP

## 1. 项目介绍

### 项目简介

该项目基于移动端和 PC 端的两套系统：

- 移动端实现了六大功能模块，分别是查看商品列表模块、创建订单模块、查看订单列表模块、查询订单详情模块、取消订单模块和完结订单的功能模块。
- PC 端实现了八大功能模块，分别是查询商品分类模块、添加，查询，修改商品模块、修改商品状态模块、查询订单模块、取消订单模块和完结订单的功能模块。
- 项目的实现采用了 Spring Cloud Alibaba 微服务架构，实现了高性能，高扩展性。

### 项目业务

- 前端使用 Vue+ElementUI 进行搭建；前后端通过 HTTP 协议进行交互，url 入口统一切换 gateway 管理，前端向服务器请求服务时，只需传送请求方法和路径，获取打包为 json 的请求数据，再通过 axios 解析展示所需数据；
- 买家端登录时存储 token 信息，在用户浏览器保存一周，避免用户短时间内重复登录系统；注册时需通过手机验证码完成注册，验证码信息会在 redis 中保存1分钟；
- 买家可通过条件筛选商品，将商品添加进购物车，创建订单、查询和修改订单状态，商品信息、订单及订单详情信息存储在 MySQL 中，订单创建后发送消息至 rocketmq，用于提醒卖家端及时处理订单信息；
- rocketmq 消费到新订单消息后，与后端通过 websocket 长连接的卖家端会被提醒；卖家端可以对商品、买家订单进行管理，查看订单统计图，根据 Excel 文件导入商品或导出已有商品信息。

### 技术栈

后端：Spring Boot、Spring Cloud Alibaba、Mybatis、Mybatis-plus、MySQL、Redis

## 2. 工程文件说明

依赖模块（抽离出各个模块中通用的方法和依赖）

- base-service：无 java 代码，从依赖的层面复用，仅提供基础 pom 依赖
- common-service：提供表单验证，基础输入数据校验工具类，请求响应状态码及信息，swagger 接口测试配置类，自定义异常类以及统一异常处理
- repository-service：提供与持久层相关的依赖（mybatis plus 依赖、数据库驱动等通用配置）

父子级关系：base-service --> repository-service --> common-service

业务相关的模块：

- product-service：管理商品信息的模块，包含（模糊）查询商品列表、根据 id 查询商品详情、新增、修改商品信息、减库存以及导入导出 excel 商品信息接口；
- order-service：管理订单信息的模块，包含查询用户订单、查看订单详情、常见订单、修改订单状态（取消、完成）以及订单支付接口；
- account-service：处理登录注册的模块，包含用户注册，用户、管理员登录，校验 token 信息接口；
- sms-service：发送短信验证码服务的模块，使用 jdwx 第三方 API 实现（代码中使用接口调通但账户未充值的状态码为发送成功）；
- mq-service：rocketmq 监听并消费消息的模块，无 controller 层，分担 order-service 层的业务压力，与前端建立 websocket 长连接，消费到订单创建消息后前端做出提示。

