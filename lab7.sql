create table if not exists flats
(
    id              serial
        primary key,
    name            varchar(255) not null,
    coordinates_x   double precision,
    coordinates_y   bigint,
    creation_date   text,
    area bigint,
    number_of_rooms integer,
    living_space integer, 
    number_of_bathrooms bigint,
    view varchar(255), 
    house_name varchar(255),
    house_year integer,
    house_number_of_floors bigint,
    house_number_of_flats_on_floor bigint,
        owner_id        integer
        references users
            on delete cascade
);

create table if not exists  users
(
    id       serial
        primary key,
    login    varchar(255) not null
        unique,
    password varchar(255) not null
);





