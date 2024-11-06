CREATE TABLE IF NOT EXISTS positions (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(255) NOT NULL,
    position_size INT NOT NULL,
    type VARCHAR(5) NOT NULL
);
