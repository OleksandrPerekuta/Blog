create table user (
                      id bigint auto_increment primary key,
                      username varchar(255) not null,
                      email varchar(255) not null,
                      password varchar(255) not null,
                      is_enabled boolean not null
);