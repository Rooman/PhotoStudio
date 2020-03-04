CREATE TABLE Languages (
	id INT AUTO_INCREMENT PRIMARY KEY,
    short_name VARCHAR(3) NOT NULL,
    full_name  VARCHAR(15)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;