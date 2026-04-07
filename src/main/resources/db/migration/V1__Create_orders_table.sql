CREATE TABLE orders
(
    id UUID PRIMARY KEY,
    location_x  INTEGER,
    location_y  INTEGER,
    volume      INTEGER,
    status_code SMALLINT,
    courierId   UUID
);