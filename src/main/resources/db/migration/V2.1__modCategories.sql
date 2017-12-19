# added page-ID column for categories
alter table category add page_id bigint;
alter table category_aud add page_id bigint;