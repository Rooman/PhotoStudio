INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (1, '2020-01-15 18:38:33', 1, 1, 'Without photo');
INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (2, '2020-01-21 18:38:33', 2, 1, '1 photo');
INSERT INTO Orders (id, orderDate, statusId, userId, comment) VALUES (3, '2020-01-29 18:38:33', 2, 1, '3 photo on disk');

INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (1, '/home/myPhoto1', 2, 1);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (2, '/home/myPhoto1', 3, 2);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (3, '/home/myPhoto2', 3, 2);
INSERT INTO OrderPhotos (id, source, orderId, photoStatusId) VALUES (4, '/home/myPhoto3', 3, 2);