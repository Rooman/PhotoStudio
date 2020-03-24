DELETE FROM OrderPhotos;
DELETE FROM Orders;
DELETE FROM Users;

INSERT INTO Users (id, email, phoneNumber, firstName, lastName, userRoleId, passwordHash, salt, country, city, zip, address, title, additionalInfo)
VALUES (1, 'admin@test.com', '380731234567', 'Piter', 'Lol', 1, '8bbefdbdeea504b1d886d071d071cc02eba8fd06cef7fe735a241107db052257', '3d47ccde-5b58-4c7b-a84c-28c27d566f8e', 'Ukraine',
					'Kyiv', 12345, 'Qwerty 1234C', 'Mr.', 'Friendly');

INSERT INTO Users (id, email, userRoleId, passwordHash, salt) VALUES (2, 'user2@test.com', 2, '93ba5ffe3e90c219572a823caf3d639c527f10a36d240f4a021ad4a367b7ebce', 'fd75bf19-948d-4b3e-b7c6-42dbace77271');

INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (1, '2020-01-15 18:38:33', 1, 2, 'NEW with photo');
INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (2, '2020-01-21 18:38:33', 2, 2, 'OLD');
INSERT INTO Orders (id, orderDate, statusId, userId) VALUES (3, '2020-01-29 18:38:33', 3, 2);
INSERT INTO Orders (id, orderDate, statusId, userId) VALUES (4, '2020-01-29 18:38:33', 4, 2);
INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (5, '2020-01-15 18:38:33', 1, 2, 'new without photo');
INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (6, '2020-01-21 18:38:33', 2, 2, 'view and select, no selected photo');
INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (7, '2020-01-21 18:38:33', 3, 2, 'selected, no ready photo');
INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (8, '2020-01-21 18:38:33', 4, 2, 'ready, ready photo');

INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (1, '/home/myPhoto1', 1, 1);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (2, '/home/myPhoto1', 2, 2);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (3, '/home/myPhoto1', 3, 3);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (4, '/home/myPhoto1', 4, 3);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (5, '/home/myPhoto1', 4, 2);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (6, '/home/myPhoto1', 6, 1);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (7, '/home/myPhoto1', 7, 2);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (8, '/home/myPhoto1', 8, 3);

COMMIT;