CREATE TABLE sample_table
(
    id           uuid PRIMARY KEY,
    column_one   text[],
    column_two   text,
    column_three text,
    json_data    JSONB
);

CREATE TABLE child_table
(
    id              uuid PRIMARY KEY,
    sample_table_id UUID NOT NULL REFERENCES sample_table (id) ON DELETE CASCADE,
    extra_column    text
);
