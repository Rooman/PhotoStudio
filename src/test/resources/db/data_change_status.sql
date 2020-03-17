DELETE FROM OrderPhotos;
DELETE FROM Orders;
INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (1, '2020-01-15 18:38:33', 1, 2, 'NEW');
INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (2, '2020-01-21 18:38:33', 2, 2, 'OLD');
INSERT INTO Orders (id, orderDate, statusId, userId) VALUES (3, '2020-01-29 18:38:33', 3, 2);
INSERT INTO Orders (id, orderDate, statusId, userId) VALUES (4, '2020-01-29 18:38:33', 4, 2);
