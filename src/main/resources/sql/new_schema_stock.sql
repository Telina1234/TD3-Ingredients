
CREATE TYPE movement_type AS ENUM ('IN', 'OUT');

CREATE TABLE IF NOT EXISTS stock_movement (
                                              id SERIAL PRIMARY KEY,
                                              id_ingredient INT REFERENCES ingredient(id),
    quantity NUMERIC(10,2),
    type movement_type,
    unit unit_type,
    creation_datetime TIMESTAMP
    );
