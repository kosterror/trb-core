create table transaction
(
    id               uuid primary key,
    date             timestamp   not null,
    payer_account_id uuid,
    payee_account_id uuid,
    amount           bigint      not null,
    type             varchar(64) not null,
    state            varchar(64) not null,
    code             int         not null,
    foreign key (payer_account_id) references account (id) on delete restrict,
    foreign key (payee_account_id) references account (id) on delete restrict
);