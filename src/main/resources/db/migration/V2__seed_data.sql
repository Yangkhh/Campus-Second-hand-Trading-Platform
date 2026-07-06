INSERT INTO users (username, email, password, nickname, student_no, campus, phone, role, status, created_at, updated_at)
VALUES
('alice', 'alice@example.edu', '$2a$10$Lr3cfTF39dnVPsEfQjX6uuiqEmZkT1nxP/DFWbJ65oiJz8BdPZ0Wu', 'Alice', '20260001', 'Main Campus', '13800000001', 'STUDENT', 'ACTIVE', NOW(), NOW()),
('bob', 'bob@example.edu', '$2a$10$Lr3cfTF39dnVPsEfQjX6uuiqEmZkT1nxP/DFWbJ65oiJz8BdPZ0Wu', 'Bob', '20260002', 'North Campus', '13800000002', 'STUDENT', 'ACTIVE', NOW(), NOW()),
('admin', 'admin@example.edu', '$2a$10$Lr3cfTF39dnVPsEfQjX6uuiqEmZkT1nxP/DFWbJ65oiJz8BdPZ0Wu', 'Admin', '00000000', 'Main Campus', '13800000000', 'ADMIN', 'ACTIVE', NOW(), NOW());

INSERT INTO products (seller_id, title, description, price, category, condition_text, image_urls, trade_location, status, view_count, favorite_count, created_at, updated_at)
VALUES
(1, 'Data Structures Textbook', 'Clean second-hand data structures textbook with notes in the first chapter only.', 35.00, 'Books', 'Good', '', 'Library Gate', 'ON_SALE', 24, 8, NOW(), NOW()),
(1, 'Mechanical Keyboard 87 Keys', 'Compact keyboard with blue switches, suitable for dorm study desk.', 120.00, 'Electronics', 'Almost New', '', 'Dormitory Building 3', 'ON_SALE', 48, 12, NOW(), NOW()),
(2, 'Dorm Desk Lamp', 'Adjustable LED desk lamp with three brightness levels.', 28.00, 'Daily Goods', 'Good', '', 'Canteen Entrance', 'ON_SALE', 18, 5, NOW(), NOW()),
(2, 'Basketball', 'Outdoor basketball used for one semester.', 45.00, 'Sports', 'Used', '', 'Sports Field', 'ON_SALE', 31, 7, NOW(), NOW());
