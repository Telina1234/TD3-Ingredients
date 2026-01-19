
CREATE TYPE unit_type AS ENUM ('KG', 'L', 'PCS');


CREATE TABLE IF NOT EXISTS dish (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(255),
    dish_type dish_type,
    selling_price NUMERIC(10,2)
    );

CREATE TABLE IF NOT EXISTS ingredient (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(255),
    price NUMERIC(10,2),
    category ingredient_category
    );

CREATE TABLE IF NOT EXISTS dish_ingredient (
                                               id SERIAL PRIMARY KEY,
                                               id_dish INT REFERENCES dish(id),
    id_ingredient INT REFERENCES ingredient(id),
    quantity_required NUMERIC(10,2),
    unit unit_type
    );
