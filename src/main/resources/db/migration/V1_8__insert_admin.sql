INSERT INTO Users (email, phoneNumber, firstName, lastName, userRoleId, passwordHash, salt)
VALUES ('test@test.com', NULL, 'admin', 'admin', '1', '96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1e', '123');
COMMIT;