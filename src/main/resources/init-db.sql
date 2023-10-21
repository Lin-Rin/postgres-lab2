CREATE TABLE user_counter (
    user_id SERIAL PRIMARY KEY,
    counter BIGINT,
    version BIGINT
);

INSERT INTO user_counter (user_id, counter, version) VALUES (?, ?, ?);

SELECT * FROM user_counter;

UPDATE user_counter SET counter=1, version=1 WHERE user_id=4 AND version=0;

-- drop table user_counter;

-- CREATE TABLE table1 (number BIGINT);
-- INSERT INTO table1 (number) VALUES (1);
-- INSERT INTO table1 (number) VALUES (2);
-- SELECT * FROM table1;