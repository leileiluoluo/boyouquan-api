# 将 MyBatis 替换为 JPA

## 1 背景知识

当前项目是一个使用 Maven 管理的 Spring Boot 项目，源码位于 `src/main/java` 下，配置文件位于 `src/main/resources` 下。

当前数据库访问层使用的是 MyBatis。Mapper 接口定义于包 `com.boyouquan.dao` 下，Mapper 配置文件 `XxxDaoMapper.xml` 位于 resources 的 mapper 目录下。

## 2 任务简介

现在我们想将数据库访问层由 MyBatis 更改为 JPA。请自动抓取 MyBatis Mapper 接口中的方法名称与 Mapper 配置文件里的 SQL 语句，然后在 `com.boyouquan.repository` 下编写与 Mapper 接口对应的 Repository 接口、定义与 Mapper 接口中方法同名的方法并使用 `@Query(value = "", nativeQuery = true)` 注解填入 Mapper 配置文件里对应的 SQL。

## 3 实现步骤

### 3.1 编写与 Mapper 接口对应的 Repository 接口

首先，需要找到 `com.boyouquan.dao` 包下所有的 Mapper 接口，然后在 `com.boyouquan.repository` 包下新建对应的 Repository 接口。

如下为一个示例。

`com.boyouquan.dao` 包下原始的 `BlogDaoMapper` 接口定义如下：

```java
package com.boyouquan.dao;

public interface BlogDaoMapper {
}
```

那么在 `com.boyouquan.repository` 包下新建一个与 `BlogDaoMapper` 接口对应的 Repository 接口 `BlogRepository`，并让该 Repository 继承 `CrudRepository`：

```java
package com.boyouquan.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends CrudRepository<Blog, Long> {
}
```

### 3.2 在 Repository 接口定义与 Mapper 接口中方法同名的方法

接下来，扫描原始 Mapper 接口中所有定义好的方法，然后在对应的 Repository 接口新建与之同名的方法。

如下为一个示例。

原始 `BlogDaoMapper` 包含一组方法：

```java
package com.boyouquan.dao;

public interface BlogDaoMapper {

    Blog getByAddress(String address);

    List<PopularBlog> listPopularBlogs(int limit);

    ...
}
```

那么在新建的 `BlogRepository` 接口里定义一组与 `BlogDaoMapper` 接口里方法同名的方法：

```java
package com.boyouquan.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends CrudRepository<Blog, Long> {

    Blog getByAddress(String address);

    List<PopularBlog> listPopularBlogs(int limit);

    ...
}
```

### 3.3 针对 Mapper 接口中的方法，从 XxxMapper.xml 抓取与之对应的 SQL

对于一个给定的 Mapper 接口，需要到 `resources/mapper` 文件夹下找到与之对应的配置文件；然后针对 Mapper 接口中的方法，从 `XxxMapper.xml` 配置文件抓取出所对应的 SQL。

如，`BlogDaoMapper` 接口的 `getByAddress` 方法在 Mapper 配置文件 `resources/mapper/BlogDaoMapper.xml` 中的配置如下：

```xml
<select id="getByAddress" resultType="com.boyouquan.model.Blog">
    SELECT
    <include refid="select_columns"/>
    FROM blog
    WHERE address=#{address}
</select>
```

抓取 SQL 时，看到有 `include` 标签，要在文件中找到标签定义的地方：

```xml
<sql id="select_columns">
    domain_name as domainName,
    admin_email as adminEmail,
    name,
    address,
    rss_address as rssAddress,
    description,
    self_submitted as selfSubmitted,
    collected_at as collectedAt,
    updated_at as updatedAt,
    valid,
    gravatar_valid as gravatarValid,
    draft,
    deleted
</sql>
```

然后抓取出 `getByAddress` 方法所使用的完整 SQL：

```sql
SELECT
    domain_name as domainName,
    admin_email as adminEmail,
    name,
    address,
    rss_address as rssAddress,
    description,
    self_submitted as selfSubmitted,
    collected_at as collectedAt,
    updated_at as updatedAt,
    valid,
    gravatar_valid as gravatarValid,
    draft,
    deleted
FROM blog
WHERE address=#{address}
```

没有 `include` 标签的 SQL 则直接抓取即可。

### 3.4 在 Repository 接口为方法添加 @Query 注解并填入对应的 SQL

接下来是本次实现的重点：使用 `@Query` 注解将上一步找到的 SQL 填入与 Mapper 接口对应的 Repository 接口方法上。

填入 SQL 时，需要遵循两个原则：

- a）如果对应的方法能使用 JPA 的命名方法实现就不要使用自定义查询方式实现。所以方法名可以根据需要做相应的更改，只要在最后的报告中做出说明即可；

如，在 BlogRepository 中实现 `getByAddress` 方法时，没必要使用自定义查询，写成：

```java
package com.boyouquan.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends CrudRepository<Blog, Long> {

    @Query(value = "SELECT * FROM blog WHERE address=#{address}", nativeQuery = true)
    Blog findByAddress(String address);
}
```

而应使用 JPA 的命名方法，写成：

```java
package com.boyouquan.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends CrudRepository<Blog, Long> {

    Blog findByAddress(String address);
}
```

对于这一方法名的变更，只需要在最后的报告中做出说明即可。

- b）使用 `@Query` 注解指定自定义查询时，如果对应的 SQL 语句太长，则 `value` 字段需要用 Java 13 引入的三个引号引用文本块的方式实现，而不要将一个长 SQL 放在一个单行上，影响阅读。

如，`BlogDaoMapper` 接口的 `listPopularBlogs` 方法在 Mapper 配置文件 `BlogDaoMapper.xml` 中的 SQL 很长。在 BlogRepository 中实现 `listPopularBlogs` 方法时，须写成：

```java
package com.boyouquan.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends CrudRepository<Blog, Long> {

    @Query(value = """
            WITH moment_activities AS (
              SELECT
                m.blog_domain_name,
                MAX(m.created_at) as latest_active_time,
                'moment' as type
              FROM
                moment m
                INNER JOIN blog b ON m.blog_domain_name = b.domain_name
              WHERE
                m.deleted = false
                AND b.draft = false
                AND b.deleted = false
                AND b.valid = true
                AND b.gravatar_valid = true
              GROUP BY
                m.blog_domain_name
            ),
            post_activities AS (
              SELECT
                p.blog_domain_name,
                MAX(p.published_at) as latest_active_time,
                'post' as type
              FROM
                post p
                INNER JOIN blog b ON p.blog_domain_name = b.domain_name
              WHERE
                p.deleted = false
                AND p.draft = false
                AND b.draft = false
                AND b.deleted = false
                AND b.valid = true
                AND b.gravatar_valid = true
              GROUP BY
                p.blog_domain_name
            ),
            combined_activities AS (
              SELECT
                blog_domain_name,
                latest_active_time,
                type
              FROM
                moment_activities
              UNION ALL
              SELECT
                blog_domain_name,
                latest_active_time,
                type
              FROM
                post_activities
            ),
            ranked_activities AS (
              SELECT
                blog_domain_name as blogDomainName,
                latest_active_time as latestActiveTime,
                type,
                ROW_NUMBER() OVER (
                  PARTITION BY blog_domain_name
                  ORDER BY
                    latest_active_time DESC
                ) as rn
              FROM
                combined_activities
            )
            SELECT
              blogDomainName,
              latestActiveTime,
              type
            FROM
              ranked_activities
            WHERE
              rn = 1
            ORDER BY
              latestActiveTime DESC
            LIMIT
              :limit
            """, nativeQuery = true)
    List<PopularBlog> listPopularBlogs(int limit);
}
```

## 4 报告生成

所有步骤完成后，请在项目根目录生成一个 `report.md` 报告文件，以表格的方式展示原始 Mapper 接口、原始 Mapper 接口中的方法、新生成的 Repository 、新生成的 Repository 中的方法的对应关系。

示例：

| Mapper 接口   | 原方法       | Repository 接口 | 新方法        |
| ------------- | ------------ | --------------- | ------------- |
| BlogDaoMapper | getByAddress | BlogRepository  | findByAddress |
