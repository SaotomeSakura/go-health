CREATE TABLE tickets (
    id VARCHAR(255) PRIMARY KEY,
    description TEXT NOT NULL,
    parent_id VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
