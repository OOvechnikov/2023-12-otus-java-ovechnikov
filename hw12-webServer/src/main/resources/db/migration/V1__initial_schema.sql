create table address
(
    id uuid primary key,
    street varchar(500)
);

create table client
(
    id   uuid primary key,
    name varchar(50),
    address_id uuid unique references address (id) on delete cascade on UPDATE cascade
);

create table phone
(
    id uuid primary key,
    number varchar(10),
    client_id uuid references client (id) on delete cascade on update cascade
);
