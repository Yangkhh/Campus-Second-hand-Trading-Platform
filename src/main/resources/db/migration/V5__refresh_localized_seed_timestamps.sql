UPDATE products
SET updated_at = NOW()
WHERE id IN (1, 2, 3, 4);
