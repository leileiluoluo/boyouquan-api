# 博友圈后端源码

博友圈（v2）是一个前后端分离的 Web 项目，该工程（[boyouquan-api](https://github.com/leileiluoluo/boyouquan-api)
）为后端部分。前端部分请参阅 [boyouquan-ui](https://github.com/leileiluoluo/boyouquan-ui)。

博友圈后端为前端提供 REST API，使用 Java 语言编写，使用了 Spring Boot + MyBatis 框架，使用的数据库为 MySQL。

下面介绍一下该工程的架构以及启动和调试方式。

## 工程架构

该工程架构如下图所示，自上而下使用了经典的三层模式：即控制器层（Controller Layer）、服务层（Service Layer）和数据访问层（DAO
Layer）。

![服务架构](./images/readme/v2/boyouquan-backend-architecture.svg)

- 控制器层包含一组 SpringMVC 控制器，负责请求的接收、参数校验、服务调用和结果的返回；
- 服务层包含一组服务，负责核心业务逻辑处理；
- 数据访问层包含一组 MyBatis 接口，负责与数据库的交互。

此外，附加的调度器层（Scheduler Layer）和帮手层（Helper Layer）则分别包含了一组定时任务和辅助工具类。

## 设置与启动

启动前请确保您已安装 Java（17 及以上）和 MySQL（8.0 及以上）。

### 数据库创建及 DDL 执行

本工程已将所有用到的 MySQL 建库与建表语句置于 `sql/ddl` 目录下。

首先，在您本地安装的 MySQL 上执行如下建库语句：

[./sql/ddl/database.sql](./sql/ddl/database.sql)

然后，在建好的数据库上执行 [./sql/ddl/](./sql/ddl/) 目录下除 `database.sql` 之外的所有 SQL 文件中的 DDL 语句。

最后，需要对 `user` 表执行一条插入一句，以设置管理后台的管理员账号和密码：

```sql
-- root/root
INSERT INTO `user` (`username`, `md5password`, `role`, `deleted`)
  VALUES ('root', md5('root'), 'admin', false);
```

### 编译与运行

在工程根目录使用如下 Maven 命令进行打包：

```shell
./mvnw clean package
```

打包完成后，使用如下命令设置环境变量，并运行：

```shell
export DATABASE_URL="jdbc:mysql://localhost:3306/boyouquan?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8"
export DATABASE_USER=root # 换成你的账号
export DATABASE_PASSWORD=root # 换成你的密码
```

```shell
java -jar ./target/boyouquan-api-2.0.jar
```

若需要开启邮件发送以及博客年龄获取功能，在启动前还需要设置如下环境变量：

```shell
export EMAIL_HOST=smtp.xxx.com # 您的邮箱服务器 SMTP 地址，如 QQ 邮箱的 SMTP 地址为 smtp.exmail.qq.com
export EMAIL_PORT=465 # 您的邮箱服务器 SMTP 端口，如 QQ 邮箱的 SMTP 端口为 465
export EMAIL_USERNAME=someone@some-domain.com # 您的邮箱账号
export EMAIL_PASSWORD=xxx # 您的邮箱密码
export EMAIL_ENABLE=true  # 开启邮件发送功能
export DOMAIN_WHOIS_QUERY_API_KEY=xxx # 支持博客年龄获取，注册 jsonwhoisapi.com 网站，申请免费的 API_KEY
```

程序启动后，尝试使用命令行或在浏览器访问一下博客列表
API（[http://localhost:8080/api/blogs](http://localhost:8080/api/blogs)），无初始数据，所以会返回一个空列表。

```shell
curl http://localhost:8080/api/blogs

{"pageNo":1,"pageSize":10,"total":0,"results":[]}
```

至此，后端程序启动成功，接下来可以接着启动 [前端程序](https://github.com/leileiluoluo/boyouquan-ui)
，启动成功后即可以进行整体调试与测试了。
