INSERT INTO Users (id, email, userRoleId, passwordHash, salt, langId) VALUES (4, 'user4@test.com', 2, '93ba5ffe3e90c219572a823caf3d639c527f10a36d240f4a021ad4a367b7ebce', 'fd75bf19-948d-4b3e-b7c6-42dbace77271', 2);
INSERT INTO Users (id, email, userRoleId, passwordHash, salt, langId) VALUES (5, 'user5@test.com', 2, '93ba5ffe3e90c219572a823caf3d639c527f10a36d240f4a021ad4a367b7ebce', 'fd75bf19-948d-4b3e-b7c6-42dbace77271', 2);

INSERT INTO Orders (id, orderDate, statusId, userId, commentAdmin) VALUES (10, '2020-01-15 18:38:33', 1, 4, 'NEW with photo');
INSERT INTO Orders (id, orderDate, statusId, userId) VALUES (11, '2020-01-15 18:38:33', 2, 5);
INSERT INTO Orders (id, orderDate, statusId, userId) VALUES (12, '2020-01-15 18:38:33', 3, 5);
INSERT INTO Orders (id, orderDate, statusId, userId, commentAdmin) VALUES (13, '2020-01-15 18:38:33', 1, 5, 'NEW');
