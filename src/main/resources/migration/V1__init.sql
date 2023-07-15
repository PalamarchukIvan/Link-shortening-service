create table public.short_links
(
    is_deleted boolean not null,
    id         bigint  not null
        primary key,
    hash       varchar(255),
    link       varchar(255)
);

alter table public.short_links
    owner to postgres;

CREATE INDEX idx_hashcode ON short_links(hash)