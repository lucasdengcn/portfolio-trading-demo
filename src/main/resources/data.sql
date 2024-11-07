CREATE TABLE IF NOT EXISTS positions (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(255) NOT NULL,
    position_size INT NOT NULL,
    symbol_type int NOT NULL,
    rel_stock_symbol VARCHAR(255) NULL,
    latest_price DOUBLE NOT NULL DEFAULT 0,
    latest_nav DOUBLE NOT NULL DEFAULT 0,
    create_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE positions
ADD CONSTRAINT uk_positions_symbol UNIQUE (symbol);