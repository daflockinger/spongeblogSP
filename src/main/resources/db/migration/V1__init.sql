### CREATE TABLES

-- create table blog (id bigint not null auto_increment, name varchar(255) not null, status varchar(255) not null, primary key (id));
-- create table blog_settings (blog_id bigint not null, settings varchar(255), settings_key VARCHAR(150) not null, primary key (blog_id, settings_key));
-- create table category (id bigint not null auto_increment, name varchar(255) not null, rank integer, parent_id bigint, primary key (id));
-- create table post (id bigint not null auto_increment, content TEXT not null, created datetime not null, modified datetime, status VARCHAR(150) not null, title VARCHAR(150) not null check (title<=150), author_id bigint, category_id bigint, primary key (id));
-- create table tag (id bigint not null auto_increment, name VARCHAR(150) not null check (name<=150), primary key (id));
-- create table post_tags (posts_id bigint not null, tags_id bigint not null);


-- create table user (id bigint not null auto_increment, login VARCHAR(150) not null check (login<=150), nick_name varchar(255), email varchar(255), password varchar(255) not null, registered datetime not null, primary key (id));
-- create table user_roles (user_id bigint not null, roles varchar(255));
-- create index IDX2jm25hjrq6iv4w8y1dhi0d9p4 on post (title);
-- create index IDXik65bluepv8oxdfvgbj5qdcsj on post (author_id);
-- create index IDXk7qygdxxdcc6x2fi4rx4m6t78 on post (author_id, category_id);
-- create index IDXqyeor3ogkedwcmi2eent46bc8 on post (author_id, status);
-- create index IDX8dd576pcv8njjj0syh5li09vs on post (status);
-- create index IDXaiqakbe9ybd4im6cce323g78w on post (status, category_id);
-- alter table post add constraint UK7cw8cbopgbty5qo4hkqxwp8ed unique (title);
-- create index IDX1wdpsed5kna2y38hnbgrnhi5b on tag (name);
-- alter table tag add constraint UK9c155tcbv3axt5uymdd9t3rqu unique (name);
-- create index IDXew1hvam8uwaknuaellwhqchhb on user (login);
-- alter table user add constraint UK6e59of1jvk758h7vp5h0phi59 unique (login);
-- alter table blog_settings add constraint FKpll4d82i03w7gn8qxbq39ngnp foreign key (blog_id) references blog (id);
-- alter table category add constraint FK2y94svpmqttx80mshyny85wqr foreign key (parent_id) references category (id);
-- alter table post add constraint FK12njtf8e0jmyb45lqfpt6ad89 foreign key (author_id) references user (id);
-- alter table post add constraint FKg6l1ydp1pwkmyj166teiuov1b foreign key (category_id) references category (id);
-- alter table post_tags add constraint FKs0eay5et5drt4luxysesaw5lj foreign key (posts_id) references post (id);
-- alter table post_tags add constraint FKgu6cvi4sb41by2x6n0kkx3pgh foreign key (tags_id) references tag (id);

-- alter table user_roles add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user (id);


create table blog (id bigint not null auto_increment, name varchar(255) not null, status varchar(255) not null, primary key (id));
create table blog_aud (id bigint not null, rev integer not null, revtype tinyint, name varchar(255), status varchar(255), primary key (id, rev));
create table blog_settings (blog_id bigint not null, settings varchar(255), settings_key VARCHAR(150) not null, primary key (blog_id, settings_key));
create table blog_settings_aud (rev integer not null, blog_id bigint not null, settings varchar(255) not null, settings_key VARCHAR(150) not null, revtype tinyint, primary key (rev, blog_id, settings, settings_key));
create table category (id bigint not null auto_increment, name varchar(255) not null, rank integer, parent_id bigint, primary key (id));
create table category_aud (id bigint not null, rev integer not null, revtype tinyint, name varchar(255), rank integer, parent_id bigint, primary key (id, rev));
create table post (id bigint not null auto_increment, content TEXT not null, created datetime not null, modified datetime, status VARCHAR(150) not null, title VARCHAR(150) not null, author_id bigint, category_id bigint, primary key (id));
create table post_aud (id bigint not null, rev integer not null, revtype tinyint, content TEXT, created datetime, modified datetime, status VARCHAR(150), title VARCHAR(150), author_id bigint, category_id bigint, primary key (id, rev));
create table post_tags (posts_id bigint not null, tags_id bigint not null);
create table post_tags_aud (rev integer not null, posts_id bigint not null, tags_id bigint not null, revtype tinyint, primary key (rev, posts_id, tags_id));
create table revinfo (rev integer not null auto_increment, revtstmp bigint, primary key (rev));
create table tag (id bigint not null auto_increment, name varchar(150) not null, primary key (id));
create table tag_aud (id bigint not null, rev integer not null, revtype tinyint, name varchar(255), primary key (id, rev));
create table user (id bigint not null auto_increment, email varchar(255), login varchar(150) not null, nick_name varchar(255), password varchar(255) not null, registered datetime not null, primary key (id));
create table user_aud (id bigint not null, rev integer not null, revtype tinyint, email varchar(255), login varchar(255), nick_name varchar(255), password varchar(255), registered datetime, primary key (id, rev));
create table user_roles (user_id bigint not null, roles varchar(255));
create table user_roles_aud (rev integer not null, user_id bigint not null, roles varchar(255) not null, revtype tinyint, primary key (rev, user_id, roles));
create index IDX2jm25hjrq6iv4w8y1dhi0d9p4 on post (title);
create index IDXik65bluepv8oxdfvgbj5qdcsj on post (author_id);
create index IDXk7qygdxxdcc6x2fi4rx4m6t78 on post (author_id, category_id);
create index IDXqyeor3ogkedwcmi2eent46bc8 on post (author_id, status);
create index IDX8dd576pcv8njjj0syh5li09vs on post (status);
create index IDXaiqakbe9ybd4im6cce323g78w on post (status, category_id);
alter table post add constraint UK2jm25hjrq6iv4w8y1dhi0d9p4 unique (title);
create index IDX1wdpsed5kna2y38hnbgrnhi5b on tag (name);
alter table tag add constraint UK1wdpsed5kna2y38hnbgrnhi5b unique (name);
create index IDXew1hvam8uwaknuaellwhqchhb on user (login);
alter table user add constraint UKew1hvam8uwaknuaellwhqchhb unique (login);
alter table blog_aud add constraint FKk6v0w38fnv4iosbe86de0nxho foreign key (rev) references revinfo (rev);
alter table blog_settings add constraint FKpll4d82i03w7gn8qxbq39ngnp foreign key (blog_id) references blog (id);
alter table blog_settings_aud add constraint FK18rc1t1es90ptpiq0cnwjyrdd foreign key (rev) references revinfo (rev);
alter table category add constraint FK2y94svpmqttx80mshyny85wqr foreign key (parent_id) references category (id);
alter table category_aud add constraint FKc9m640crhsib2ws80um6xuk1w foreign key (rev) references revinfo (rev);
alter table post add constraint FK12njtf8e0jmyb45lqfpt6ad89 foreign key (author_id) references user (id);
alter table post add constraint FKg6l1ydp1pwkmyj166teiuov1b foreign key (category_id) references category (id);
alter table post_aud add constraint FKeywddrxhmaq98hs8wpgbbdrq2 foreign key (rev) references revinfo (rev);
alter table post_tags add constraint FKpoyg307ed2w6nbcthawvphds4 foreign key (tags_id) references tag (id);
alter table post_tags add constraint FKtirdxxf9hhm0mlqpn4tvqg3ag foreign key (posts_id) references post (id);
alter table post_tags_aud add constraint FKpmt4km1m69uvjyflb3ds0ii89 foreign key (rev) references revinfo (rev);
alter table tag_aud add constraint FKep272jdrgxgmq608l5y3792jn foreign key (rev) references revinfo (rev);
alter table user_aud add constraint FK89ntto9kobwahrwxbne2nqcnr foreign key (rev) references revinfo (rev);
alter table user_roles add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user (id);
alter table user_roles_aud add constraint FKox6xyy64fyq0y3dvv5ve53a0h foreign key (rev) references revinfo (rev);