create table verification_token(
    id bigint auto_increment primary key,
    expiration_date timestamp,
    token varchar(255),
    user_id bigint not null ,
    foreign key (user_id) references user(id)
)