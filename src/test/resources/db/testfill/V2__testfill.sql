insert into user (id, login, nick_name, password, registered, email) values (1,'flo','daflo','secret',NOW(),'flo@kinger.cc');
insert into user_roles (user_id, roles) values (1, 'ADMIN');
insert into user (id, login, nick_name, password, registered, email) values (2,'nobody','body','secret',NOW(),'no@body.cc');

insert into blog (id, name, status) values (1,'test blog','ACTIVE');
insert into blog_settings (blog_id, settings, settings_key) values (1,'all-black','blog theme');
insert into blog_settings (blog_id, settings, settings_key) values (1,'hide','footer');

insert into category (id, name, rank, parent_id) values (1, 'main category', 1, null);
insert into category (id, name, rank, parent_id) values (2, 'sub category', 1, 1);

insert into post (id, content, created, modified, status, title, author_id, category_id) values (1, 'some content...', '2017-03-06', '2017-03-07', 'PUBLIC', 'somethings', 1, 1);
insert into post (id, content, created, modified, status, title, author_id, category_id) values (2, 'another content...', '2017-03-03', '2017-03-04', 'PUBLIC', 'always', 1, 2);
insert into post (id, content, created, modified, status, title, author_id, category_id) values (3, 'not so fresh...', '2017-02-15', '2017-02-25', 'PUBLIC', 'but', 1, 1);
insert into post (id, content, created, modified, status, title, author_id, category_id) values (4, 'never gone, always tuned', '2014-04-23', null, 'DELETED', 'never change', 1, 2);
insert into post (id, content, created, modified, status, title, author_id, category_id) values (5, 'never say yesterday', '2016-12-24', null, 'PUBLIC', 'some other time', 1, 1);
insert into post (id, content, created, modified, status, title, author_id, category_id) values (6, 'crispier than ever', '2017-01-01', null, 'PUBLIC', 'also', 1, 2);


insert into tag (id, name) values (1, 'fancy');
insert into tag (id, name) values (2, 'cold');
insert into tag (id, name) values (3, 'guide');

insert into post_tags (posts_id, tags_id) values (1,1);
insert into post_tags (posts_id, tags_id) values (2,1);
insert into post_tags (posts_id, tags_id) values (1,2);


truncate table blog_settings_aud;
truncate table blog_aud;
truncate table category_aud;
truncate table user_roles_aud;
truncate table user_aud;
truncate table post_tags_aud;
truncate table tag_aud;
truncate table post_aud;