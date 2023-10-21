CREATE TABLE user_counter (
    user_id SERIAL PRIMARY KEY,
    counter BIGINT,
    version BIGINT
);

SELECT * FROM user_counter;