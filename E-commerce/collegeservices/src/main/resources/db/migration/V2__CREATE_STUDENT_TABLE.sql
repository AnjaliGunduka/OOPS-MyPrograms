CREATE TABLE IF NOT EXISTS STUDENT
(
   `ID` BIGINT NOT NULL AUTO_INCREMENT,
   `TYPE` VARCHAR (300) NOT NULL,
   `STATUS` VARCHAR (10) NOT NULL,
   `STUDENT_NAME` VARCHAR (300) NOT NULL,
   `COLLEGE_CODE` VARCHAR (20) NOT NULL,
   COLLEGE_ID BIGINT NOT NULL,
   PRIMARY KEY (ID),
   FOREIGN KEY (COLLEGE_ID) REFERENCES COLLEGE (ID)
);