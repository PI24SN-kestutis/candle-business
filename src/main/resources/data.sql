CREATE TRIGGER IF NOT EXISTS trg_product_price_update
AFTER UPDATE ON product
FOR EACH ROW
WHEN (OLD.price <> NEW.price)
BEGIN
    INSERT INTO product_price_audit (product_id, old_price, new_price, change_timestamp, changed_by)
    VALUES (OLD.id, OLD.price, NEW.price, NOW(), CURRENT_USER());
END;
