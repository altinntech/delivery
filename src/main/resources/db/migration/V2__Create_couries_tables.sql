CREATE TABLE couriers (
    id  UUID PRIMARY KEY ,
    name VARCHAR (255),
    speed INTEGER,
    current_l_x INTEGER,
    current_l_y INTEGER
);

CREATE TABLE couriers_storageplaces (
    id UUID PRIMARY KEY ,
    courier_id UUID NOT NULL ,
    name VARCHAR (255),
    volume INTEGER,
    order_id UUID,
    FOREIGN KEY (courier_id) REFERENCES couriers(id)
)