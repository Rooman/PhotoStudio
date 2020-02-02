INSERT INTO UserRole (roleName) VALUES ('ADMIN');
INSERT INTO UserRole (roleName) VALUES ('USER');

INSERT INTO UserGender (genderName) VALUES ('Male');
INSERT INTO UserGender (genderName) VALUES ('Female');

INSERT INTO Users (email, phoneNumber, firstName, lastName, genderId, userRoleId, passwordHash, salt, country, city, zip, address) VALUES ('mymail@d.com', 380731234567, 'Piter', 'Lol', 1, 1, '8bbefdbdeea504b1d886d071d071cc02eba8fd06cef7fe735a241107db052257', '3d47ccde-5b58-4c7b-a84c-28c27d566f8e', 'Ukraine',
					'Kyiv', 12345, 'Qwerty 1234C');

INSERT INTO Users (email, userRoleId, passwordHash, salt) VALUES ('mymail2@d.com', 2, '93ba5ffe3e90c219572a823caf3d639c527f10a36d240f4a021ad4a367b7ebce', 'fd75bf19-948d-4b3e-b7c6-42dbace77271');

INSERT INTO OrderStatus (statusName) VALUES ('New');
INSERT INTO OrderStatus (statusName) VALUES ('View and Select');
INSERT INTO OrderStatus (statusName) VALUES ('Selected');
INSERT INTO OrderStatus (statusName) VALUES ('Ready');

INSERT INTO Orders (orderDate, statusId, userId, comment) VALUES ('2020-01-15 18:38:33', 1, 1, 'NEW');
INSERT INTO Orders (orderDate, statusId, userId, comment) VALUES ('2020-01-21 18:38:33', 2, 2, 'OLD');
INSERT INTO Orders (orderDate, statusId, userId) VALUES ('2020-01-29 18:38:33', 3, 1);
INSERT INTO Orders (orderDate, statusId, userId) VALUES ('2020-01-29 18:38:33', 4, 2);

INSERT INTO PhotoStatus (statusName) VALUES ('New');
INSERT INTO PhotoStatus (statusName) VALUES ('Done');

INSERT INTO OrderPhotos (source, orderId, photoStatusId) VALUES ('/home/myPhoto1', 1, 1);
INSERT INTO OrderPhotos (source, orderId, photoStatusId) VALUES ('/home/myPhoto2', 2, 2);