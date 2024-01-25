create table user (
                      id bigint auto_increment primary key,
                      first_name varchar(255) not null,
                      second_name varchar(255) not null,
                      email varchar(255) not null,
                      password varchar(255) not null,
                      is_enabled boolean not null
#                       role_id bigint not null ,
#                       foreign key (role_id) references role(id)
);