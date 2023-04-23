CREATE TABLE IF NOT EXISTS tariffTypes
(
     id serial not null primary key,
     name character varying(255) COLLATE pg_catalog."default"
);

CREATE TABLE IF NOT EXISTS tariffs
(
     id serial not null primary key,
     type int not null references tariffTypes (id),
     name character varying(255) COLLATE pg_catalog."default",
     price int not null,
     initial_value int not null
);

CREATE TABLE IF NOT EXISTS accounts
(
     id bigserial not null primary key,
     card_number character varying(255) COLLATE pg_catalog."default"
);

CREATE TABLE IF NOT EXISTS accountBalances
(
     id bigserial not null primary key,
     tariff int not null references tariffs (id),
     account bigint not null references accounts (id),
     remaining_value int not null,
     reserved_value int not null
);

CREATE TABLE IF NOT EXISTS blackList
(
    id bigserial not null primary key,
    account bigint not null references accounts (id),
    reason character varying(255) COLLATE pg_catalog."default"
);