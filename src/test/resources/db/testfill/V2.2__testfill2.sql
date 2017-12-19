insert into category (id, name, rank, parent_id, page_id) values (3, 'funky category', 1, 1, 8);

insert into post (id, content, created, modified, status, title, author_id, category_id) values (7, 'never say yesterday', '2017-12-24', null, 'PAGE_PUBLIC', 'some other time part 2', 2, null);
insert into post (id, content, created, modified, status, title, author_id, category_id) values (8, 'crispier than ever', '2017-12-31', null, 'PAGE_PUBLIC', 'also nix', 2, 3);

