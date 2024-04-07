CREATE TABLE category (
        id bigint auto_increment primary key,
        name varchar(255) not null,
        is_active boolean not null
);