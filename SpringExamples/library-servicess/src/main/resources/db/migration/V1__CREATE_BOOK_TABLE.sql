CREATE TABLE IF NOT EXISTS BOOK
(
   `ID` BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
   `TITLE` VARCHAR (300) NOT NULL,
  `PUBLISHDATE` DATETIME NOT NULL,
   `AUTHOR` VARCHAR (300) NOT NULL,
   `STATUS` VARCHAR (10) NOT NULL,
   `PRICE` DOUBLE NOT NULL,
   `PUBLISHER` VARCHAR (300) NOT NULL UNIQUE,
   `LANGUAGE` VARCHAR (300) NOT NULL,
   `ISBN13` VARCHAR (20) NULL,
   `NUMOFCOPIES` INTEGER NOT NULL,
   `EDITION` VARCHAR (20) NULL,
   PRIMARY KEY (ID)
);