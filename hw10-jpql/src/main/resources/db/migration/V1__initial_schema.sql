create sequence hibernate_sequence start with 1 increment by 1;

create sequence client_seq start with 1 increment by 1;
create sequence phone_seq start with 1 increment by 1;
create sequence address_seq start with 1 increment by 1;

CREATE TABLE IF NOT EXISTS address
(
    id bigint NOT NULL,
    street character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT address_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS client
(
    id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    client bigint,
    CONSTRAINT client_pkey PRIMARY KEY (id),
    CONSTRAINT fk8wwbfrxtpdhquhkxq6i9tg5yk FOREIGN KEY (client)
        REFERENCES public.address (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS phone
(
    id bigint NOT NULL,
    "number" character varying(255) COLLATE pg_catalog."default",
    client_id bigint,
    CONSTRAINT phone_pkey PRIMARY KEY (id),
    CONSTRAINT fkqm6tehjpnsdta8v8ns2rvb476 FOREIGN KEY (client_id)
        REFERENCES public.client (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);