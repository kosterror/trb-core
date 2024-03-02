create table transaction
(
    id               uuid primary key,
    date             timestamp   not null,
    payer_account_id uuid,
    payee_account_id uuid,
    amount           bigint      not null,
    type             varchar(64) not null,
    state            varchar(64) not null,
    code smallint not null
);