create table if not exists public.short_links
(
    is_deleted boolean not null,
    hash       varchar(5) not null primary key,
    link       varchar(2048)
);

CREATE INDEX if not exists idx_hashcode ON short_links(hash)