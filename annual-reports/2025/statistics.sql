-- total blogs collected
select count(*) from blog where draft=false and collected_at >= '2025-01-01';
-- system collected
select count(*) from blog where draft=false and self_submitted=false and collected_at >= '2025-01-01';
-- self submitted
select count(*) from blog where draft=false and self_submitted=true and collected_at >= '2025-01-01';

-- total posts
select count(*) from post where draft=false and deleted=false and published_at >= '2025-01-01';
-- recommended
select count(*) from post where draft=false and deleted=false and recommended=true and published_at >= '2025-01-01';
-- pinned
select count(distinct link) from pin_history where pinned_at >= '2025-01-01';

-- total moments
select count(*) from moment where deleted=false;

-- total access
select sum(amount) from access where year_month_str >= '2025/01';
-- most accessed blog
select blog_domain_name, sum(amount) from access where year_month_str >= '2025/01' group by blog_domain_name order by sum(amount) desc limit 10;

-- most published blog
select blog_domain_name, count(*) from post where draft=false and deleted=false and published_at >= '2025-01-01' group by blog_domain_name order by count(*) desc limit 10;

-- most accessed post
select link, sum(amount) from access where year_month_str >= '2025/01' group by link order by sum(amount) desc limit 10;

-- most pinned blog
select blog_domain_name, count(*) from pin_history where pinned_at >= '2025-01-01' group by blog_domain_name order by count(*) desc limit 10;

-- most recommended blog
select blog_domain_name, count(*) from post where draft=false and deleted=false and recommended=true and published_at >= '2025-01-01' group by blog_domain_name order by count(*) desc limit 10;

-- most searched intimacy blog
select source_blog_domain_name, count(*) from blog_intimacy_search_history where searched_at >= '2025-01-01' group by source_blog_domain_name order by count(*) desc limit 10;
select target_blog_domain_name, count(*) from blog_intimacy_search_history where searched_at >= '2025-01-01' group by target_blog_domain_name order by count(*) desc limit 10;

-- planet shuttle
-- total initializer
select count(distinct blog_domain_name) from planet_shuttle where year_month_str >= '2025/01';
-- total access count
select sum(amount) from planet_shuttle where year_month_str >= '2025/01';
-- top initializer
select blog_domain_name, sum(amount) from planet_shuttle where year_month_str >= '2025/01' group by blog_domain_name order by sum(amount) desc limit 10;
-- top target blogs
select to_blog_address, sum(amount) from planet_shuttle where year_month_str >= '2025/01' group by to_blog_address order by sum(amount) desc limit 10;
