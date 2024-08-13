create table category
(
    is_default  bit          not null,
    created_at  datetime(6) not null,
    id          bigint       not null auto_increment,
    member_id   bigint       not null,
    modified_at datetime(6) not null,
    name        varchar(255) not null,
    primary key (id)
);

create table member
(
    created_at  datetime(6) not null,
    id          bigint       not null auto_increment,
    modified_at datetime(6) not null,
    email       varchar(255) not null,
    password    varchar(255) not null,
    username    varchar(255) not null,
    primary key (id)
);

create table snippet
(
    ordinal     integer      not null,
    created_at  datetime(6) not null,
    id          bigint       not null auto_increment,
    modified_at datetime(6) not null,
    template_id bigint       not null,
    content     TEXT         not null,
    filename    varchar(255) not null,
    primary key (id)
);

create table tag
(
    created_at  datetime(6) not null,
    id          bigint       not null auto_increment,
    modified_at datetime(6) not null,
    name        varchar(255) not null,
    primary key (id)
);

create table template
(
    category_id bigint       not null,
    created_at  datetime(6) not null,
    id          bigint       not null auto_increment,
    member_id   bigint       not null,
    modified_at datetime(6) not null,
    description TEXT,
    title       varchar(255) not null,
    primary key (id)
);

create table template_tag
(
    created_at  datetime(6) not null,
    modified_at datetime(6) not null,
    tag_id      bigint not null,
    template_id bigint not null,
    primary key (tag_id, template_id)
);

create table thumbnail_snippet
(
    created_at  datetime(6) not null,
    id          bigint not null auto_increment,
    modified_at datetime(6) not null,
    snippet_id  bigint not null,
    template_id bigint not null,
    primary key (id)
);

alter table category
    add constraint name_with_member unique (member_id, name);
alter table member
    add constraint UKmbmcqelty0fbrvxp1q58dn57t unique (email);
alter table member
    add constraint UKgc3jmn7c2abyo3wf6syln5t2i unique (username);
alter table thumbnail_snippet
    add constraint UKg3n4dnryxewrvb8du167uijwn unique (snippet_id);
alter table thumbnail_snippet
    add constraint UKpj1ejy0otq23kfkis7o3mioqx unique (template_id);
alter table category
    add constraint FKd7qtd46ngp06lnc19g6wtoh8t
        foreign key (member_id)
            references member (id);
alter table snippet
    add constraint FKlft9j4cliatmtij1yr5xh6xk2
        foreign key (template_id)
            references template (id);
alter table template
    add constraint FKfke34lch7qf4xixqnyj3h12m7
        foreign key (category_id)
            references category (id);
alter table template
    add constraint FKodvst19gfi7in0ox3dfocuejx
        foreign key (member_id)
            references member (id);
alter table template_tag
    add constraint FK460d2kdmrpffumqaahvh414mv
        foreign key (tag_id)
            references tag (id);
alter table template_tag
    add constraint FKnq3tyggcae26yafql1l0rc91l
        foreign key (template_id)
            references template (id);
alter table thumbnail_snippet
    add constraint FK5adu2g3fumims6lwiu4w4srcs
        foreign key (snippet_id)
            references snippet (id);
alter table thumbnail_snippet
    add constraint FKs3gprh3h7k6b9v414ib76ys9q
        foreign key (template_id)
            references template (id);
