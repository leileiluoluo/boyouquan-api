## How to change domain name?

```sql
update blog set domain_name='blog.lhasa.icu' where domain_name='lhasa.icu';
update post set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update domain_name_info set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update access set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update moment set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update pin_history set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update planet_shuttle set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update blog_status set blog_domain_name='blog.lhasa.icu' where blog_domain_name='lhasa.icu';
update blog_location set domain_name='blog.lhasa.icu' where domain_name='lhasa.icu';
delete from friend_link where source_blog_domain_name='lhasa.icu';
delete from friend_link where target_blog_domain_name='lhasa.icu';
delete from blog_intimacy_search_history where source_blog_domain_name='lhasa.icu';
delete from blog_intimacy_search_history where target_blog_domain_name='lhasa.icu';
```