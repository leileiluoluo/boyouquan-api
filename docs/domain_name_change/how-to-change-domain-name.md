## How to change domain name?

```sql
update blog set domain_name='changkun.de' where domain_name='changkun.de/blog';
update post set blog_domain_name='changkun.de' where blog_domain_name='changkun.de/blog';
update domain_name_info set blog_domain_name='changkun.de' where blog_domain_name='changkun.de/blog';
update access set blog_domain_name='changkun.de' where blog_domain_name='changkun.de/blog';
update blog_location set domain_name='changkun.de' where domain_name='changkun.de/blog';
delete from friend_link where source_blog_domain_name='changkun.de/blog';
delete from friend_link where target_blog_domain_name='changkun.de/blog';
```