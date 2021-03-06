DELETE FROM OrderPhotos;
DELETE FROM Orders;
DELETE FROM Users;

INSERT INTO Users (id, email, phoneNumber, firstName, lastName, userRoleId, passwordHash, salt, country, city, zip, address, title, additionalInfo, langId)
VALUES (1, 'admin@test.com', '380731234567', 'Piter', 'Lol', 1, '8bbefdbdeea504b1d886d071d071cc02eba8fd06cef7fe735a241107db052257', '3d47ccde-5b58-4c7b-a84c-28c27d566f8e', 'Ukraine',
					'Kyiv', 12345, 'Qwerty 1234C', 'Mr.', 'Friendly', 2);

INSERT INTO Users (id, email, userRoleId, passwordHash, salt, langId) VALUES (2, 'user2@test.com', 2, '93ba5ffe3e90c219572a823caf3d639c527f10a36d240f4a021ad4a367b7ebce', 'fd75bf19-948d-4b3e-b7c6-42dbace77271', 2);

INSERT INTO Orders (id, orderDate, statusId, userId, commentAdmin) VALUES (1, '2020-01-15 18:38:33', 1, 2, 'NEW with photo');
INSERT INTO Orders (id, orderDate, statusId, userId, commentAdmin) VALUES (2, '2020-01-21 18:38:33', 2, 2, 'OLD');
INSERT INTO Orders (id, orderDate, statusId, userId) VALUES (3, '2020-01-29 18:38:33', 3, 2);
INSERT INTO Orders (id, orderDate, statusId, userId) VALUES (4, '2020-01-29 18:38:33', 4, 2);
INSERT INTO Orders (id, orderDate, statusId, userId, commentAdmin) VALUES (5, '2020-01-15 18:38:33', 1, 2, 'new without photo');
INSERT INTO Orders (id, orderDate, statusId, userId, commentAdmin) VALUES (6, '2020-01-21 18:38:33', 2, 2, 'view and select, no selected photo');
INSERT INTO Orders (id, orderDate, statusId, userId, commentAdmin) VALUES (7, '2020-01-21 18:38:33', 3, 2, 'selected, no ready photo');
INSERT INTO Orders (id, orderDate, statusId, userId, commentAdmin) VALUES (8, '2020-01-21 18:38:33', 4, 2, 'ready, ready photo');
INSERT INTO Orders (id, orderDate, statusId, userId, commentAdmin) VALUES (9, '2020-01-21 18:38:33', 2, 2, 'test select photos');

INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (1, 'myPhoto1.jpg', 1, 1);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (2, 'myPhoto1.jpg', 2, 2);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (3, 'myPhoto1.jpg', 3, 3);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (4, 'myPhoto1.jpg', 4, 3);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (5, 'myPhoto2.jpg', 4, 2);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (6, 'myPhoto1.jpg', 6, 1);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (7, 'myPhoto1.jpg', 7, 2);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (8, 'myPhoto1.jpg', 8, 3);

INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (9, 'myPhoto1.jpg', 9, 1);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (10, 'myPhoto2.jpg', 9, 1);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (11, 'myPhoto3.jpg', 9, 1);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (12, 'myPhoto4.jpg', 9, 1);

COMMIT;