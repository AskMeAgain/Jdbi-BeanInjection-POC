CREATE TABLE sample_table
(
    id           uuid PRIMARY KEY,
    column_one   VARCHAR(255),
    column_two   VARCHAR(255),
    column_three VARCHAR(255),
    json_data    JSONB
);

CREATE TABLE child_table
(
    id              uuid PRIMARY KEY,
    sample_table_id UUID NOT NULL REFERENCES sample_table (id) ON DELETE CASCADE,
    extra_column    VARCHAR(255)
);
