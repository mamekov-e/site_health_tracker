create type site_status as enum ('UP', 'DOWN', 'ADDED_TO_GROUP', 'DELETED', 'DELETED_FROM_GROUP');
create type site_group_status as enum ('ALL_UP', 'ALL_DOWN', 'PARTIAL_UP', 'NO_SITES');

CREATE CAST (varchar AS site_status) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS site_group_status) WITH INOUT AS IMPLICIT;

create table roles
(
    id   smallserial primary key,
    name varchar(256) not null
);
insert into roles (name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');

CREATE TABLE users
(
    id                 BIGSERIAL PRIMARY KEY,
    email              VARCHAR(50)  NOT NULL UNIQUE,
    password           VARCHAR(120) NOT NULL,
    first_name         VARCHAR(50),
    last_name          VARCHAR(50),
    middle_name        VARCHAR(50),
    phone              VARCHAR(255),
    email_confirmed    BOOLEAN   DEFAULT FALSE,
    created_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table sites
(
    id                         bigserial primary key,
    name                       varchar(256) not null,
    description                text,
    url                        text         not null,
    site_health_check_interval bigserial    not null,
    user_id                    bigint references users (id),
    status                     site_status  not null
);

create table site_groups
(
    id          bigserial primary key,
    name        varchar(256) not null,
    description text,
    status      site_group_status default 'NO_SITES',
    user_id     bigint references users (id)
);
create table group_site
(
    site_id  bigint references sites (id),
    group_id bigint references site_groups (id)
);
create table site_check_logs
(
    id         bigserial primary key,
    check_time timestamp   not null default now(),
    status     site_status not null,
    site_id    bigint references sites (id)
);
create table emails
(
    id                   bigserial primary key,
    address              varchar(256) not null,
    verification_code    varchar(256),
    code_expiration_time timestamp,
    enabled              bool default false
);
create table telegram_users
(
    id                       bigserial primary key,
    username                 varchar(256),
    enabled                  bool default false,
    disabled_expiration_time timestamp
);