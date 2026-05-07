CREATE TABLE IF NOT EXISTS product_price_audit (
    audit_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    old_price DECIMAL(19, 2),
    new_price DECIMAL(19, 2),
    change_timestamp TIMESTAMP NOT NULL,
    changed_by VARCHAR(255)
);
