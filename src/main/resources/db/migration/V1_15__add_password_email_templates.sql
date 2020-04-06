  CREATE TABLE PasswordEmailTemplate (
	id INT AUTO_INCREMENT,
    langId INT NOT NULL,
    subject VARCHAR(500) NOT NULL,
    body    VARCHAR(500) NOT NULL,
    messageType VARCHAR(45) NOT NULL,
    CONSTRAINT FK_PasswordEmailLanguages FOREIGN KEY (langId) REFERENCES Languages(id),
    UNIQUE (langId, messageType),
    PRIMARY KEY (id)
) DEFAULT CHARSET=utf8mb4;

INSERT INTO PasswordEmailTemplate (id, langId, subject, body, messageType)
VALUES (1, 1, 'Miari Fotografie Passwort zurücksetzen', 'Ihr neues Passwort lautet <password>', 'RESET PASSWORD');
INSERT INTO PasswordEmailTemplate (id, langId, subject, body, messageType)
VALUES (2, 2, 'Miari Fotografie Reset password', 'Your new password is <password>', 'RESET PASSWORD');
INSERT INTO PasswordEmailTemplate (id, langId, subject, body, messageType)
VALUES (3, 3, 'Miari Fotografie Сброс пароля', 'Ваш новый пароль <password>', 'RESET PASSWORD');


