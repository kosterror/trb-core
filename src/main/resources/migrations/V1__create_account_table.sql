create table account
(
    id                 uuid primary key,
    type               varchar(64)  not null,
    balance            bigint       not null,
    client_full_name   varchar(256) not null,
    external_client_id uuid,
    creation_date      timestamp    not null,
    closing_date       timestamp,
    is_closed          boolean      not null default false
);