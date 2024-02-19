create table post
(
    id           bigint auto_increment primary key,
    title        varchar(255),
    text         text,
    published_at timestamp,
    is_edited    boolean,
    user_id      bigint not null,
    foreign key (user_id) references user (id)
)