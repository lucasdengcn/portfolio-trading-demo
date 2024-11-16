CREATE TABLE IF NOT EXISTS stocks (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(255) NOT NULL,
    latest_price DOUBLE NOT NULL DEFAULT 0,
    create_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_stocks_symbol on stocks(symbol);

CREATE TABLE IF NOT EXISTS options (
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

CREATE UNIQUE INDEX IF NOT EXISTS uk_options_symbol on options(symbol);
CREATE INDEX IF NOT EXISTS idx_options_rel_stock_symbol on options(rel_stock_symbol);

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

CREATE UNIQUE INDEX IF NOT EXISTS uk_positions_symbol on positions(symbol);
CREATE INDEX IF NOT EXISTS idx_positions_rel_stock_symbol on positions(rel_stock_symbol);
