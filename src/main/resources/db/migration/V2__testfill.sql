insert into user (id, login, nick_name, password, registered, email) values (1,"flo","daflo","secret",NOW(),"flo@kinger.cc");
insert into user_roles (user_id, roles) values (1, "ADMIN");
insert into user (id, login, nick_name, password, registered, email) values (2,"nobody","body","secret",NOW(),"no@body.cc");


insert into blog (id, name, status) values (1,"test blog","ACTIVE");
insert into blog_settings (blog_id, settings, settings_key) values (1,"blog theme","all-black");
insert into blog_settings (blog_id, settings, settings_key) values (1,"footer","hide");

insert into category (id, name, rank, parent_id) values (1, "main category", 1, null);
insert into category (id, name, rank, parent_id) values (2, "sub category", 1, 1);

insert into post (id, content, created, modified, status, title, author_id, category_id) values (1, "some content...", NOW(), null, "PUBLIC", "some topic", 1, 1);
insert into post (id, content, created, modified, status, title, author_id, category_id) values (2, "another content...", NOW(), null, "PUBLIC", "another topic", 1, 2);

insert into tag (id, name) values (1, "fancy");
insert into tag (id, name) values (2, "cold");
insert into tag (id, name) values (3, "guide");

insert into tag_posts (tags_id, posts_id) values (1,1);
insert into tag_posts (tags_id, posts_id) values (1,2);
insert into tag_posts (tags_id, posts_id) values (2,1);