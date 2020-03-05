CREATE TABLE Languages (
	id INT AUTO_INCREMENT PRIMARY KEY,
    short_name VARCHAR(3) NOT NULL,
    full_name  VARCHAR(15)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO Languages (short_name, full_name) VALUES ('ENG', 'English');
INSERT INTO Languages (short_name, full_name) VALUES ('GER', 'Deutsch');
INSERT INTO Languages (short_name, full_name) VALUES ('RUS', 'Russian');
COMMIT;

ALTER TABLE Users
ADD COLUMN langId INT DEFAULT(1),
ADD FOREIGN KEY (langId) REFERENCES Languages(id);