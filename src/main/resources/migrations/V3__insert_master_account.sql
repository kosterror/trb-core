insert into account (id, type, balance, currency, client_full_name, external_client_id, creation_date, closing_date,
                     is_closed)
values (gen_random_uuid(), 'MASTER', 1000000000000000000, 'GBP', 'trust-bank', null, current_timestamp, null, false);