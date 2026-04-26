CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL,
    total_copies INT NOT NULL,
    available_amount INT NOT NULL,

    CONSTRAINT chk_copies CHECK (available_amount <= total_copies),
    CONSTRAINT chk_positive CHECK (total_copies > 0)
);