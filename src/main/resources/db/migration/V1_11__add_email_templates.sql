CREATE TABLE EmailTemplates (
	langId INT NOT NULL,
    orderStatusId INT NOT NULL,
    subject VARCHAR(100) NOT NULL,
    body    VARCHAR(100),
    CONSTRAINT FK_EmailLanguages FOREIGN KEY (langId) REFERENCES Languages(id),
    CONSTRAINT FK_EmailOrderStatus FOREIGN KEY (orderStatusId) REFERENCES OrderStatus(id),
    UNIQUE (langId, orderStatusId),
    PRIMARY KEY (langId, orderStatusId)
) DEFAULT CHARSET=utf8mb4;

INSERT INTO EmailTemplates(langId, orderStatusId, subject, body)
VALUES (1, 2, 'Ihre Bestellung %d ist fertig.', 'Sie können die Fotos jetzt auswählen.');
INSERT INTO EmailTemplates(langId, orderStatusId, subject, body)
VALUES (2, 2, 'Your order %d is ready.', 'You can select the photos now.');
INSERT INTO EmailTemplates(langId, orderStatusId, subject, body)
VALUES (3, 2, 'Ваш заказ %d готов.', 'Теперь Вы можете приступить к выбору фотографий.');
INSERT INTO EmailTemplates(langId, orderStatusId, subject, body)
VALUES (1, 3, 'Benutzer %s hat Fotos für die Bestellung %d ausgewählt.', 'Benutzer %s hat Fotos für die Bestellung %d ausgewählt.');
INSERT INTO EmailTemplates(langId, orderStatusId, subject, body)
VALUES (2, 3, 'User %s has selected photo for order %d', 'User %s has selected photo for order %d');
INSERT INTO EmailTemplates(langId, orderStatusId, subject, body)
VALUES (3, 3, 'Пользователь %s выбрал фотографии в заказе %d.', 'Пользователь %s выбрал фотографии в заказе %d.');
INSERT INTO EmailTemplates(langId, orderStatusId, subject, body)
VALUES (1, 4, 'Ihre Bestellung %d ist fertig.', 'Sie können ausgewählte Fotos jetzt herunterladen.');
INSERT INTO EmailTemplates(langId, orderStatusId, subject, body)
VALUES (2, 4, 'Your Order %d is ready.', 'You can download selected photos now.');
INSERT INTO EmailTemplates(langId, orderStatusId, subject, body)
VALUES (3, 4, 'Ваш заказ %d готов.', 'Теперь Вы можете загрузить выбранные фотографии.');
COMMIT;
