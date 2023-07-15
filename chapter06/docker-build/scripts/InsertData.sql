insert into SINGER (id, first_name, last_name, birth_date) values (1, 'John', 'Mayer', '1977-10-16');
insert into SINGER (id, first_name, last_name, birth_date) values (2, 'Ben', 'Barnes', '1981-08-20');
insert into SINGER (id, first_name, last_name, birth_date) values (3, 'John', 'Butler', '1975-04-01');

insert into ALBUM (id, singer_id, title, release_date) values (1, 1, 'The Search For Everything', '2017-01-20');
insert into ALBUM (id, singer_id, title, release_date) values (2, 1, 'Battle Studies', '2009-11-17');
insert into ALBUM (id, singer_id, title, release_date) values (3, 2, ' 11:11 ', '2021-09-18');

/* view the inserted data */
SELECT * FROM SINGER;
SELECT * FROM ALBUM;

/* simple join sample */
SELECT * FROM ALBUM A, SINGER S
WHERE SINGER_ID = S.ID AND S.ID=1;