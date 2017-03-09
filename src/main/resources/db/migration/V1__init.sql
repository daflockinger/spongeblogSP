### CREATE TABLES

create table blog (id bigint not null auto_increment, name varchar(255) not null, status varchar(255) not null, primary key (id));
create table blog_settings (blog_id bigint not null, settings varchar(255), settings_key VARCHAR(150) not null, primary key (blog_id, settings_key));
create table category (id bigint not null auto_increment, name varchar(255) not null, rank integer, parent_id bigint, primary key (id));
create table post (id bigint not null auto_increment, content TEXT not null, created datetime not null, modified datetime, status VARCHAR(150) not null, title VARCHAR(150) not null check (title<=150), author_id bigint, category_id bigint, primary key (id));
create table tag (id bigint not null auto_increment, name VARCHAR(150) not null check (name<=150), primary key (id));
create table tag_posts (tags_id bigint not null, posts_id bigint not null);
create table user (id bigint not null auto_increment, login VARCHAR(150) not null check (login<=150), nick_name varchar(255), email varchar(255), password varchar(255) not null, registered datetime not null, primary key (id));
create table user_roles (user_id bigint not null, roles varchar(255));
create index IDX2jm25hjrq6iv4w8y1dhi0d9p4 on post (title);
create index IDXik65bluepv8oxdfvgbj5qdcsj on post (author_id);
create index IDXk7qygdxxdcc6x2fi4rx4m6t78 on post (author_id, category_id);
create index IDXqyeor3ogkedwcmi2eent46bc8 on post (author_id, status);
create index IDX8dd576pcv8njjj0syh5li09vs on post (status);
create index IDXaiqakbe9ybd4im6cce323g78w on post (status, category_id);
alter table post add constraint UK7cw8cbopgbty5qo4hkqxwp8ed unique (title);
create index IDX1wdpsed5kna2y38hnbgrnhi5b on tag (name);
alter table tag add constraint UK9c155tcbv3axt5uymdd9t3rqu unique (name);
create index IDXew1hvam8uwaknuaellwhqchhb on user (login);
alter table user add constraint UK6e59of1jvk758h7vp5h0phi59 unique (login);
alter table blog_settings add constraint FKpll4d82i03w7gn8qxbq39ngnp foreign key (blog_id) references blog (id);
alter table category add constraint FK2y94svpmqttx80mshyny85wqr foreign key (parent_id) references category (id);
alter table post add constraint FK12njtf8e0jmyb45lqfpt6ad89 foreign key (author_id) references user (id);
alter table post add constraint FKg6l1ydp1pwkmyj166teiuov1b foreign key (category_id) references category (id);
alter table tag_posts add constraint FKs0eay5et5drt4luxysesaw5lj foreign key (posts_id) references post (id);
alter table tag_posts add constraint FKgu6cvi4sb41by2x6n0kkx3pgh foreign key (tags_id) references tag (id);
alter table user_roles add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user (id);

