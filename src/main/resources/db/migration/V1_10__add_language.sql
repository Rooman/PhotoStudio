CREATE TABLE Languages (
	id INT AUTO_INCREMENT PRIMARY KEY,
    shortName VARCHAR(3) NOT NULL,
    fullName  VARCHAR(15)
) DEFAULT CHARSET=utf8mb4;

INSERT INTO Languages (shortName, fullName) VALUES ('EN', 'English');
INSERT INTO Languages (shortName, fullName) VALUES ('DE', 'Deutsch');
INSERT INTO Languages (shortName, fullName) VALUES ('RU', 'Русский');
COMMIT;

ALTER TABLE Users
ADD COLUMN langId INT;

UPDATE Users
SET langId = 1;

ALTER TABLE Users
ADD FOREIGN KEY (langId) REFERENCES Languages(id);