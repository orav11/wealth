drop table if exists rich_person;

create table rich_person(
    id bigint primary key,
    first_name varchar(100),
    last_name varchar(100),
    fortune varchar(100)
);