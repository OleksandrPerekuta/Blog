create table comment(
    id      bigint auto_increment primary key,
    text    text not null ,
    user_id bigint not null,
    post_id bigint not null ,
    parent_comment_id bigint,
    foreign key (user_id) references user (id),
    foreign key (post_id) references post (id),
    foreign key (parent_comment_id) references comment (id)
)