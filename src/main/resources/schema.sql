-- drop table if exists bookings;
-- drop table if exists comments;
-- drop table if exists items;
-- drop table if exists requests;
-- drop table if exists users;

-- users
create table if not exists users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL unique,
    CONSTRAINT pk_user PRIMARY KEY (id)
    );


-- requests
create table if not exists requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description TEXT NOT NULL,
    requestor_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requestor FOREIGN KEY (requestor_id) REFERENCES users(id) ON DELETE CASCADE
);


-- items
create table if not exists items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    is_available BOOLEAN NOT NULL,
    owner_id BIGINT NOT NULL,
    request_id BIGINT null,
    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_request FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE CASCADE
);

-- comments
create table if not exists comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text TEXT NOT NULL,
    author_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE not null,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);
-- bookings
create table if not exists bookings(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status VARCHAR(50) null,
    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT fk_booker FOREIGN KEY (booker_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);