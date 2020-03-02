
INSERT INTO Users (email, userRoleId, passwordHash, salt) VALUES ('testusers1@d.com', 2, '93ba5ffe3e90c219572a823caf3d639c527f10a36d240f4a021ad4a367b7ebce', 'fd75bf19-948d-4b3e-b7c6-42dbace77271');
INSERT INTO Users (email, userRoleId, passwordHash, salt) VALUES ('testuser2@d.com', 2, '93ba5ffe3e90c219572a823caf3d639c527f10a36d240f4a021ad4a367b7ebce', 'fd75bf19-948d-4b3e-b7c6-42dbace77271');

INSERT INTO Orders (orderDate, statusId, userId, comment) VALUES ('2020-01-15 18:38:33', 1, 1, 'NEW for userId=1');
INSERT INTO Orders (orderDate, statusId, userId, comment) VALUES ('2020-01-21 18:38:33', 1, 2, 'NEW for userId=2');
INSERT INTO Orders (orderDate, statusId, userId, comment) VALUES ('2020-01-21 18:38:33', 2, 2, 'OLD');
INSERT INTO Orders (orderDate, statusId, userId) VALUES ('2020-01-29 18:38:33', 3, 2);
INSERT INTO Orders (orderDate, statusId, userId) VALUES ('2020-01-29 18:38:33', 4, 2);
