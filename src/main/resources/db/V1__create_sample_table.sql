CREATE TABLE sample_table
(
    id           SERIAL PRIMARY KEY,
    column_one   VARCHAR(255),
    column_two   VARCHAR(255),
    column_three VARCHAR(255),
    json_data JSONB
);