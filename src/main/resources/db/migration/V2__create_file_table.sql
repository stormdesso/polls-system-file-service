create table if not exists file
(
    id            bigint generated always as identity
        primary key,
    poll_id       bigint                                           not null,
    original_name varchar(100)                                     not null,
    data          bytea                                            not null,
    type          varchar(20) default 'unknown'::character varying not null,
    size          varchar(10)                                      not null,
    is_deleted    boolean     default false                        not null
);

comment on column file.poll_id is 'Номер опроса, к которому прикреплён файл';
comment on column file.original_name is 'Исходное название';
comment on column file.data is 'Данные';
comment on column file.type is 'Тип файла';
comment on column file.size is 'Размер файла';
comment on column file.is_deleted is 'Помечен на удаление';
