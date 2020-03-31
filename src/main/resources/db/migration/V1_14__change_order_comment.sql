ALTER TABLE Orders RENAME COLUMN comment TO commentAdmin;
ALTER TABLE Orders
ADD COLUMN commentUser VARCHAR(500);
