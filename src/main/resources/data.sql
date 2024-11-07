CREATE TABLE IF NOT EXISTS STOCKS (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(255) NOT NULL,
    latest_price DOUBLE NOT NULL DEFAULT 0,
    create_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS uk_stocks_symbol on STOCKS(symbol);

CREATE TABLE IF NOT EXISTS OPTIONS (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(255) NOT NULL,
    latest_price DOUBLE NOT NULL DEFAULT 0,
    symbol_type int NOT NULL,
    rel_stock_symbol VARCHAR(255) NOT NULL,
    maturity int NOT NULL DEFAULT 0,
    strike_price DOUBLE NOT NULL DEFAULT 0,
    create_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS uk_options_symbol on OPTIONS(symbol);
CREATE INDEX IF NOT EXISTS idx_options_rel_stock_symbol on OPTIONS(rel_stock_symbol);

CREATE TABLE IF NOT EXISTS POSITIONS (
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

CREATE INDEX IF NOT EXISTS uk_positions_symbol on POSITIONS(symbol);
CREATE INDEX IF NOT EXISTS idx_positions_rel_stock_symbol on POSITIONS(rel_stock_symbol);
