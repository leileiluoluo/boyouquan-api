# MyBatis Mapper 到 JPA Repository 转换报告

本报告展示从 `com.boyouquan.dao` 包下 MyBatis Mapper 接口到 `com.boyouquan.repository` 包下新建 Repository 接口的映射关系。每一行对应一个原始方法与其在新 Repository 中的同名方法。SQL 已通过 `@Query(nativeQuery = true)` 注解嵌入；对包含复杂动态 `<if>/<choose>/<foreach>` 的场景做了简化或以逻辑表达式替代。

> 说明：动态 SQL 已尽可能使用参数逻辑 `( :param IS NULL OR ... )`、`IN (:list)` 与 `CASE` 等方式近似；复杂批量操作（如 MyBatis `batchSave`）在 JPA 中以单行插入方法留空批量能力（需在调用端循环）。

| Mapper 接口 | 原方法 | Repository 接口 | 新方法 |
|-------------|--------|----------------|--------|
| AccessDaoMapper | countAll | AccessRepository | countAll |
| AccessDaoMapper | getMostAccessedBlogDomainNameInLastMonth | AccessRepository | getMostAccessedBlogDomainNameInLastMonth |
| AccessDaoMapper | getMostAccessedLinkByBlogDomainName | AccessRepository | getMostAccessedLinkByBlogDomainName |
| AccessDaoMapper | getBlogAccessSeriesInLatestOneYear | AccessRepository | getBlogAccessSeriesInLatestOneYear |
| AccessDaoMapper | countByBlogDomainName | AccessRepository | countByBlogDomainName |
| AccessDaoMapper | countByBlogDomainNameAndStartDate | AccessRepository | countByBlogDomainNameAndStartDate |
| AccessDaoMapper | listBlogAccessCount | AccessRepository | listBlogAccessCount |
| AccessDaoMapper | countByLink | AccessRepository | countByLink |
| AccessDaoMapper | save | AccessRepository | save |
| AccessDaoMapper | deleteByBlogDomainName | AccessRepository | deleteByBlogDomainName |
| BlogDaoMapper | listPopularBlogs | BlogRepository | listPopularBlogs |
| BlogDaoMapper | listByRandom | BlogRepository | listByRandom |
| BlogDaoMapper | countAll | BlogRepository | countAll |
| BlogDaoMapper | listAll | BlogRepository | listAll |
| BlogDaoMapper | listAllDomainNames | BlogRepository | listAllDomainNames |
| BlogDaoMapper | listRecentCollected | BlogRepository | listRecentCollected |
| BlogDaoMapper | countWithKeyword | BlogRepository | countWithKeyword |
| BlogDaoMapper | listWithKeyWord | BlogRepository | listWithKeyWord |
| BlogDaoMapper | listBlogCollectedAt | BlogRepository | listBlogCollectedAt |
| BlogDaoMapper | existsByDomainName | BlogRepository | existsByDomainName |
| BlogDaoMapper | existsByRssAddress | BlogRepository | existsByRssAddress |
| BlogDaoMapper | getByDomainName | BlogRepository | getByDomainName |
| BlogDaoMapper | listByAdminEmail | BlogRepository | listByAdminEmail |
| BlogDaoMapper | getByShortDomainName | BlogRepository | getByShortDomainName |
| BlogDaoMapper | getByAddress | BlogRepository | getByAddress |
| BlogDaoMapper | getByRSSAddress | BlogRepository | getByRSSAddress |
| BlogDaoMapper | getByMd5AdminEmail | BlogRepository | getByMd5AdminEmail |
| BlogDaoMapper | getJoinYearsByDomainName | BlogRepository | getJoinYearsByDomainName |
| BlogDaoMapper | save | BlogRepository | save |
| BlogDaoMapper | update | BlogRepository | update |
| BlogDaoMapper | updateGravatarValidFlag | BlogRepository | updateGravatarValidFlag |
| BlogDaoMapper | markAsInvalid | BlogRepository | markAsInvalid |
| BlogDaoMapper | deleteByDomainName | BlogRepository | deleteByDomainName |
| BlogIntimacySearchHistoryDaoMapper | listLatest | BlogIntimacySearchHistoryRepository | listLatest |
| BlogIntimacySearchHistoryDaoMapper | save | BlogIntimacySearchHistoryRepository | save |
| BlogLocationDaoMapper | existsByDomainName | BlogLocationRepository | existsByDomainName |
| BlogLocationDaoMapper | getByDomainName | BlogLocationRepository | getByDomainName |
| BlogLocationDaoMapper | save | BlogLocationRepository | save |
| BlogLocationDaoMapper | update | BlogLocationRepository | update |
| BlogLocationDaoMapper | deleteByDomainName | BlogLocationRepository | deleteByDomainName |
| BlogRequestDaoMapper | listBySelfSubmittedAndStatuses | BlogRequestRepository | listBySelfSubmittedAndStatuses |
| BlogRequestDaoMapper | countBySelfSubmittedAndStatuses | BlogRequestRepository | countBySelfSubmittedAndStatuses |
| BlogRequestDaoMapper | listByStatuses | BlogRequestRepository | listByStatuses |
| BlogRequestDaoMapper | countByStatuses | BlogRequestRepository | countByStatuses |
| BlogRequestDaoMapper | listByStatus | BlogRequestRepository | listByStatus |
| BlogRequestDaoMapper | getById | BlogRequestRepository | getById |
| BlogRequestDaoMapper | getByRssAddress | BlogRequestRepository | getByRssAddress |
| BlogRequestDaoMapper | update | BlogRequestRepository | update |
| BlogRequestDaoMapper | submit | BlogRequestRepository | submit |
| BlogRequestDaoMapper | deleteByRssAddress | BlogRequestRepository | deleteByRssAddress |
| BlogStatusDaoMapper | getLatestByBlogDomainName | BlogStatusRepository | getLatestByBlogDomainName |
| BlogStatusDaoMapper | save | BlogStatusRepository | save |
| BlogStatusDaoMapper | deleteByBlogDomainName | BlogStatusRepository | deleteByBlogDomainName |
| DomainNameChangeDaoMapper | existsByOldDomainName | DomainNameChangeRepository | existsByOldDomainName |
| DomainNameChangeDaoMapper | getByOldDomainName | DomainNameChangeRepository | getByOldDomainName |
| DomainNameChangeDaoMapper | save | DomainNameChangeRepository | save |
| DomainNameInfoDaoMapper | getByBlogDomainName | DomainNameInfoRepository | getByBlogDomainName |
| DomainNameInfoDaoMapper | save | DomainNameInfoRepository | save |
| DomainNameInfoDaoMapper | update | DomainNameInfoRepository | update |
| EmailLogDaoMapper | existsByBlogDomainNameAndType | EmailLogRepository | existsByBlogDomainNameAndType |
| EmailLogDaoMapper | getLatestByBlogDomainNameAndType | EmailLogRepository | getLatestByBlogDomainNameAndType |
| EmailLogDaoMapper | save | EmailLogRepository | save |
| EmailValidationDaoMapper | getByEmailAndCode | EmailValidationRepository | getByEmailAndCode |
| EmailValidationDaoMapper | countTodayIssuedByEmail | EmailValidationRepository | countTodayIssuedByEmail |
| EmailValidationDaoMapper | save | EmailValidationRepository | save |
| FriendLinkDaoMapper | listAll | FriendLinkRepository | listAll |
| FriendLinkDaoMapper | listBySourceBlogDomainName | FriendLinkRepository | listBySourceBlogDomainName |
| FriendLinkDaoMapper | getBySourceAndTargetBlogDomainName | FriendLinkRepository | getBySourceAndTargetBlogDomainName |
| FriendLinkDaoMapper | getDirectTargetBlogDomainNames | FriendLinkRepository | getDirectTargetBlogDomainNames |
| FriendLinkDaoMapper | getDirectSourceBlogDomainNames | FriendLinkRepository | getDirectSourceBlogDomainNames |
| FriendLinkDaoMapper | listAllSourceBlogs | FriendLinkRepository | listAllSourceBlogs |
| FriendLinkDaoMapper | listAllTargetBlogs | FriendLinkRepository | listAllTargetBlogs |
| FriendLinkDaoMapper | getJobMaxCreatedAt | FriendLinkRepository | getJobMaxCreatedAt |
| FriendLinkDaoMapper | deleteBySourceBlogDomainName | FriendLinkRepository | deleteBySourceBlogDomainName |
| FriendLinkDaoMapper | deleteByTargetBlogDomainName | FriendLinkRepository | deleteByTargetBlogDomainName |
| FriendLinkDaoMapper | save | FriendLinkRepository | save |
| LikeDaoMapper | countByTypeAndEntityId | LikeRepository | countByTypeAndEntityId |
| LikeDaoMapper | add | LikeRepository | add |
| MomentDaoMapper | list | MomentRepository | list |
| MomentDaoMapper | count | MomentRepository | count |
| MomentDaoMapper | existsByBlogDomainNameAndDescription | MomentRepository | existsByBlogDomainNameAndDescription |
| MomentDaoMapper | save | MomentRepository | save |
| MonthlySelectedDaoMapper | listYearMonthStrs | MonthlySelectedRepository | listYearMonthStrs |
| MonthlySelectedDaoMapper | listSelectedPostsByYearMonthStr | MonthlySelectedRepository | listSelectedPostsByYearMonthStr |
| PinHistoryDaoMapper | save | PinHistoryRepository | save |
| PinHistoryDaoMapper | listLinksByBlogDomainName | PinHistoryRepository | listLinksByBlogDomainName |
| PlanetShuttleDaoMapper | getMostInitiatedBlogDomainNameInLastMonth | PlanetShuttleRepository | getMostInitiatedBlogDomainNameInLastMonth |
| PlanetShuttleDaoMapper | getLatestInitiatedYearMonthStr | PlanetShuttleRepository | getLatestInitiatedYearMonthStr |
| PlanetShuttleDaoMapper | getBlogInitiatedSeriesInLatestOneYear | PlanetShuttleRepository | getBlogInitiatedSeriesInLatestOneYear |
| PlanetShuttleDaoMapper | countInitiatedByBlogDomainName | PlanetShuttleRepository | countInitiatedByBlogDomainName |
| PlanetShuttleDaoMapper | save | PlanetShuttleRepository | save |
| PostDaoMapper | getMostPublishedInLatestOneMonth | PostRepository | getMostPublishedInLatestOneMonth |
| PostDaoMapper | getBlogPostPublishSeriesInLatestOneYear | PostRepository | getBlogPostPublishSeriesInLatestOneYear |
| PostDaoMapper | countWithKeyWord | PostRepository | countWithKeyWord |
| PostDaoMapper | listWithKeyWord | PostRepository | listWithKeyWord |
| PostDaoMapper | countAll | PostRepository | countAll |
| PostDaoMapper | getLatestPublishedAtByBlogDomainName | PostRepository | getLatestPublishedAtByBlogDomainName |
| PostDaoMapper | countByBlogDomainName | PostRepository | countByBlogDomainName |
| PostDaoMapper | countByBlogDomainNameAndStartDate | PostRepository | countByBlogDomainNameAndStartDate |
| PostDaoMapper | countByDraftAndBlogDomainName | PostRepository | countByDraftAndBlogDomainName |
| PostDaoMapper | listByDraftAndBlogDomainName | PostRepository | listByDraftAndBlogDomainName |
| PostDaoMapper | listRecommendedByBlogDomainName | PostRepository | listRecommendedByBlogDomainName |
| PostDaoMapper | listBlogPostCount | PostRepository | listBlogPostCount |
| PostDaoMapper | getByLink | PostRepository | getByLink |
| PostDaoMapper | existsByLink | PostRepository | existsByLink |
| PostDaoMapper | existsByTitle | PostRepository | existsByTitle |
| PostDaoMapper | batchSave | PostRepository | save (*单条插入近似*) |
| PostDaoMapper | save | PostRepository | save |
| PostDaoMapper | batchUpdateDraftByBlogDomainName | PostRepository | batchUpdateDraftByBlogDomainName |
| PostDaoMapper | deleteByBlogDomainName | PostRepository | deleteByBlogDomainName |
| PostDaoMapper | deleteByLink | PostRepository | deleteByLink |
| PostDaoMapper | recommendByLink | PostRepository | recommendByLink |
| PostDaoMapper | unpinByLink | PostRepository | unpinByLink |
| PostDaoMapper | pinByLink | PostRepository | pinByLink |
| PostImageDaoMapper | getByLink | PostImageRepository | getByLink |
| PostImageDaoMapper | getImageURLByLink | PostImageRepository | getImageURLByLink |
| PostImageDaoMapper | existsImageURLByLink | PostImageRepository | existsImageURLByLink |
| PostImageDaoMapper | save | PostImageRepository | save |
| PostImageDaoMapper | update | PostImageRepository | update |
| SubscriptionDaoMapper | listSubscribedByType | SubscriptionRepository | listSubscribedByType |
| SubscriptionDaoMapper | existsSubscribedByEmail | SubscriptionRepository | existsSubscribedByEmail |
| SubscriptionDaoMapper | existsSubscribedByEmailAndType | SubscriptionRepository | existsSubscribedByEmailAndType |
| SubscriptionDaoMapper | listSubscribedByEmail | SubscriptionRepository | listSubscribedByEmail |
| SubscriptionDaoMapper | subscribe | SubscriptionRepository | subscribe |
| SubscriptionDaoMapper | unsubscribe | SubscriptionRepository | unsubscribe |
| UserDaoMapper | getUserByUsername | UserRepository | getUserByUsername |

## 后续建议

1. 为每个返回实体建立对应的 `@Entity` 映射，或使用接口/DTO 投影，避免当前使用未注册实体类导致的运行期映射问题。
2. 复查动态 SQL 近似转换（排序逻辑、可选筛选、集合绑定）是否符合业务需求；必要时拆分为多个更明确的方法。 
3. 批量操作（原 MyBatis `batchSave`）可改为 Spring Data 的 `saveAll`（需真实 JPA 实体）。
4. 使用 `@Modifying` 的更新/插入方法建议统一加上 `@Transactional`（已添加），并在服务层做好异常回滚策略。

## 局限与注意

- 多数接口目前继承的是 `Repository<Object, Long>` 仅用于激活 Spring Data 机制；一旦有真实实体请替换为 `JpaRepository<ActualEntity, IdType>`。
- 原始动态 SQL 的严格等价迁移需要更细分的方法或 Querydsl / Specification 支持，本次为直接近似。 
- 某些统计类返回（如聚合列）若无法直接映射到已有 POJO，可能需要新建投影接口或使用 `List<Object[]>`。
