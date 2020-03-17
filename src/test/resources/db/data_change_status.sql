DELETE FROM OrderPhotos;
DELETE FROM Orders;
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