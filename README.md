# mifuns-project

### 一、框架概述
 * 项目使用 spring 、 springmvc 、mybatis
 * 日志实现:slf4j-api,logback-classic,logback-core  其中 排除 common-logging，使用jcl-over-slf4j
 * 使用shiro 实现权限控制，后台业务权限配置，见后续说明
 * 项目部署：使用Hudson持续集成，连接git,编译打包代码，可以通过Hudson将代码传送到服务器。
 * 如果使用分库分表，可以集成sharding-jdbc

### 二、模块介绍
#### (1) 基础模块
 * mifuns-common 基础工具类模块
 * mifuns-config 公共配置模块,分为dev,test,main；maven参数-P test
 * mifuns-db 数据源项目，实现读写分离，贡献出统一数据源供上层调用。
 * mifuns-security 通用的安全模块

#### (2) 服务接口模块
 * mifuns-facade-customer 通知模块实体类，接口，枚举 ，根据业务模块分

#### (3) 服务实现模块
 * mifuns-service-customer

#### (4) web模块
 * mifuns-admin-manager 系统后台管理模块,可添加其他管理系统模块，如客服管理后台，运营后台等
 * mifuns-app-customer 用户app接口后台，使用restful编程风格

### 二、命名规范
 * 后台工程以admin命名，格式： 项目名-admin-manager ,基础包名com.mifuns.项目.admin.controller(工程名称)
 * app工程以app命名，格式：项目名-app-customer 基础包名com.mifuns.项目名.controller(工程名称)
 * 基础工程以common命名，格式：项目-common-功能名 命名
 * 接口工程以facade命名，格式：项目名-facade-模块名 基础包名com.mifuns.项目名.facade.user(工程名称),包含实体类entity，枚举enums，接口service，常量enums, mybatis接口mapper，和mybatis-xml文件
 * 接口实现工程以service命名，格式：项目名-service-模块名 基础包名com.mifuns.项目名.service.user(工程名称),包含实现service，

### 三、工具
  * mifuns-common
    * 金额类型使用BigDecimal,
    * excel 工具WorkbookUtil
    * MapPlaceholderHelper 属性替换工具
    * ObjectUtil 对象copy,以及属性复制
    * JacksonUtil  Json格式化工具
    * DateUtils    日期工具

### 四、开发环境配置
  * 安装环境推荐使用Intellij idea， jdk1.7、maven3.3以上、mysql5.5以上、redis（后续）
  * git检出代码 ，导入 Intellij idea ,选择pom.xml导入 ，配置 idea编码 urf-8,项目默认jdk1.7，配置maven