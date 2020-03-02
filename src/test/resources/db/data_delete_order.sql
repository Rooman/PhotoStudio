INSERT INTO Users (email, phoneNumber, firstName, lastName, userRoleId, passwordHash, salt)
VALUES ('test@test.com', NULL, 'admin', 'admin', '1', '96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1e', '123');

INSERT INTO Orders (orderDate, statusId, userId, comment) VALUES ('2020-01-15 18:38:33', 1, 1, 'Without photo');
INSERT INTO Orders (orderDate, statusId, userId, comment) VALUES ('2020-01-21 18:38:33', 2, 1, '1 photo');
INSERT INTO Orders (orderDate, statusId, userId, comment) VALUES ('2020-01-29 18:38:33', 2, 1, '3 photo on disk');

INSERT INTO OrderPhotos (source, orderId, photoStatusId) VALUES ('/home/myPhoto1', 2, 1);
INSERT INTO OrderPhotos (source, orderId, photoStatusId) VALUES ('/home/myPhoto1', 3, 2);
INSERT INTO OrderPhotos (source, orderId, photoStatusId) VALUES ('/home/myPhoto2', 3, 2);
INSERT INTO OrderPhotos (source, orderId, photoStatusId) VALUES ('/home/myPhoto3', 3, 2);