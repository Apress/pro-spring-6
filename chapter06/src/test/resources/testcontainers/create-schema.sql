CREATE TABLE SINGER (
       ID INT NOT NULL AUTO_INCREMENT
     , FIRST_NAME VARCHAR(60) NOT NULL
     , LAST_NAME VARCHAR(40) NOT NULL
     , BIRTH_DATE DATE
     , UNIQUE (FIRST_NAME, LAST_NAME)
     , PRIMARY KEY (ID)
);

CREATE TABLE ALBUM (
       ID INT NOT NULL AUTO_INCREMENT
     , SINGER_ID INT NOT NULL
     , TITLE VARCHAR(100) NOT NULL
     , RELEASE_DATE DATE
     , UNIQUE (SINGER_ID, TITLE)
     , PRIMARY KEY (ID)
     , CONSTRAINT FK_ALBUM FOREIGN KEY (SINGER_ID)
                  REFERENCES SINGER (ID)
);

insert into SINGER (id, first_name, last_name, birth_date) values (1, 'John', 'Mayer', '1977-10-16');
insert into SINGER (id, first_name, last_name, birth_date) values (2, 'Ben', 'Barnes', '1981-08-20');
insert into SINGER (id, first_name, last_name, birth_date) values (3, 'John', 'Butler', '1975-04-01');

insert into ALBUM (id, singer_id, title, release_date) values (1, 1, 'The Search For Everything', '2017-01-20');
insert into ALBUM (id, singer_id, title, release_date) values (2, 1, 'Battle Studies', '2009-11-17');
insert into ALBUM (id, singer_id, title, release_date) values (3, 2, ' 11:11 ', '2021-09-18');