create table if not exists public.short_links
(
    id         bigint  not null primary key,
    is_deleted boolean not null,
    hash       varchar(255),
    link       varchar(255)
);

CREATE INDEX if not exists idx_hashcode ON short_links(hash)