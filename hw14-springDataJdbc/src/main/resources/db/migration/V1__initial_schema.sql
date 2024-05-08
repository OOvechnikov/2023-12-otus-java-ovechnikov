create table client
(
    id   uuid primary key,
    name varchar(50)
);

create table address
(
    client_id uuid primary key references client (id) on delete cascade on update cascade,
    street    varchar(500)
);

create table phone
(
    client_id uuid primary key references client (id) on delete cascade on update cascade,
    number    varchar(10)
);
