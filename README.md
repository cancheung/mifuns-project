# mifuns-project

### 一、框架概述
 * 项目使用 spring 、 springmvc 、mybatis
 * 日志实现:slf4j-api,logback-classic,logback-core  其中 排除 common-logging，使用jcl-over-slf4j
 * 后期业务量大可以集成dubbo


### 二、模块介绍
# (1) 基础模块
 * mifuns-common 基础工具类模块
 * mifuns-config 公共配置模块,分为dev,test,main；maven参数-P test
 * mifuns-db 数据源项目，实现读写分离，贡献出统一数据源供上层调用。
 * mifuns-security 通用的安全模块
 * mifuns-facade-* 业务分模块 接口类 包含 domain，如果使用dubbo ,可以只依赖接口
# (2) 服务接口
 * mifuns-facade-customer 通知模块实体类，接口，枚举 ，根据业务模块分
# (3) 服务实现模块
 * mifuns-service-customer