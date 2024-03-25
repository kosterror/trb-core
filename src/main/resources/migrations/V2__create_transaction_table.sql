create table transaction
(
    id               uuid primary key,
    external_id uuid            not null unique,
    date        timestamp       not null,
    payer_account_id uuid,
    payee_account_id uuid,
    amount      decimal(100, 2) not null,
    currency    varchar(64)     not null,
    type        varchar(64)     not null
);