INSERT INTO Users (id, email, userRoleId, passwordHash, salt, langId) VALUES (3, 'mymail3@d.com', 2, '93ba5ffe3e90c219572a823caf3d639c527f10a36d240f4a021ad4a367b7ebce', 'fd75bf19-948d-4b3e-b7c6-42dbace77271', 1);

INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (5, '2020-01-15 18:38:33', 1, 3, 'NEW');
INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (6, '2020-01-15 18:38:33', 1, 3, 'NEW');

INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (4, '/home/myPhoto1', 5, 1);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (5, '/home/myPhoto2', 5, 1);
