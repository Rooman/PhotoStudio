DELETE FROM PhotoStatus;
INSERT INTO PhotoStatus (statusName) VALUES ('UNSELECTED');
INSERT INTO PhotoStatus (statusName) VALUES ('SELECTED');
INSERT INTO PhotoStatus (statusName) VALUES ('PAID');
COMMIT;