create schema if not exists prod;

create table if not exists prod.users
(
    id             varchar(36) not null
        primary key,
    creation_date  timestamp,
    last_edit_date timestamp,
    name           varchar(20),
    parent_name    varchar(20),
    password       varchar(80),
    surname        varchar(20),
    username       varchar(40) unique ,
    role           varchar
);

create table if not exists prod.news
(
    id             bigserial
        primary key,
    creation_date  timestamp,
    last_edit_date timestamp,
    text           varchar(2000),
    title          varchar(150),
    inserted_by_id varchar(36)
        references prod.users,
    updated_by_id  varchar(36)
        references prod.users
);

alter table prod.news
    owner to postgres;

create table if not exists prod.comments
(
    id             bigserial
        primary key,
    creation_date  timestamp,
    text           varchar(300),
    news_id        bigint
        references prod.news,
    inserted_by_id varchar(36)
        references prod.users
);

alter table prod.comments
    owner to postgres;