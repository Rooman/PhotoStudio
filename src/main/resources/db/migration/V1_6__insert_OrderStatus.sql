DELETE FROM OrderStatus;
INSERT INTO OrderStatus (StatusName) VALUES ('NEW');
INSERT INTO OrderStatus (StatusName) VALUES ('VIEW AND SELECT');
INSERT INTO OrderStatus (StatusName) VALUES ('SELECTED');
INSERT INTO OrderStatus (StatusName) VALUES ('READY');
COMMIT;