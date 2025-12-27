## How to change domain name?

```sql
-- additional
update blog_request set rss_address='xxx' where rss_address like '%lhasa.icu%';
update blog set domain_name='blog.lhasa.icu', address='xxx', rss_address='xxx' where domain_name='lhasa.icu';

-- main
update post set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update domain_name_info set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update access set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update moment set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update pin_history set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update planet_shuttle set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update blog_location set domain_name='blog.lhasa.icu' where domain_name='lhasa.icu';

delete from blog_status where blog_domain_name='lhasa.icu';
delete from friend_link where source_blog_domain_name='lhasa.icu';
delete from friend_link where target_blog_domain_name='lhasa.icu';
delete from blog_intimacy_search_history where source_blog_domain_name='lhasa.icu';
delete from blog_intimacy_search_history where target_blog_domain_name='lhasa.icu';

-- optional
-- insert into domain_name_change(old_domain_name, new_domain_name, changed_at, deleted) values ('lhasa.icu', 'blog.lhasa.icu', now(), false);
```
