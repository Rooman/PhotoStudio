CREATE TABLE UserRole (
	id INT AUTO_INCREMENT PRIMARY KEY,
    roleName VARCHAR(20) NOT NULL
);

CREATE TABLE UserGender (
	id INT AUTO_INCREMENT PRIMARY KEY,
    genderName VARCHAR(20) NOT NULL
);


CREATE TABLE Users (
	id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(400) UNIQUE NOT NULL,
    phoneNumber BIGINT UNSIGNED,
    firstName VARCHAR(100),
    lastName VARCHAR(100),
    genderId INT,
    userRoleId INT NOT NULL,
    passwordHash VARCHAR(200) NOT NULL,
    salt VARCHAR(500) NOT NULL,
    country VARCHAR(50),
    city VARCHAR(50),
    zip INT UNSIGNED,
    street VARCHAR(100),
    buildingNumber INT UNSIGNED,
    INDEX(phoneNumber),
    FOREIGN KEY (userRoleId) REFERENCES UserRole(id),
    FOREIGN KEY (genderId) REFERENCES UserGender(id)
);


CREATE TABLE OrderStatus (
	id INT AUTO_INCREMENT PRIMARY KEY,
    statusName VARCHAR(50) NOT NULL
);

CREATE TABLE Orders (
	id INT AUTO_INCREMENT PRIMARY KEY,
    orderDate TIMESTAMP NOT NULL,
    statusId INT NOT NULL,
    userId INT NOT NULL,
    comment VARCHAR(500),
    INDEX(orderDate),
    FOREIGN KEY (statusId) REFERENCES OrderStatus(id),
    FOREIGN KEY (userId) REFERENCES Users(id)
);

CREATE TABLE PhotoStatus (
	id INT AUTO_INCREMENT PRIMARY KEY,
    statusName VARCHAR(50) NOT NULL
);

CREATE TABLE OrderPhotos (
	id INT AUTO_INCREMENT PRIMARY KEY,
    source VARCHAR(200) NOT NULL,
    orderId INT NOT NULL,
    photoStatusId INT NOT NULL,
    FOREIGN KEY (orderId) REFERENCES Orders(id),
    FOREIGN KEY (photoStatusId) REFERENCES PhotoStatus(id)
);
