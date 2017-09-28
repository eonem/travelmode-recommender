CREATE DATABASE TMR;

USE TMR;

CREATE TABLE TRAVEL
(
  id          INT AUTO_INCREMENT PRIMARY KEY,
  userid      VARCHAR(256),
  origin      TEXT,
  destination TEXT,
  departure   DATETIME
);