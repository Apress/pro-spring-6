insert into SINGER (id, first_name, last_name, birth_date) values (1, 'John', 'Mayer', '1977-10-16');
insert into SINGER (id, first_name, last_name, birth_date) values (2, 'Ben', 'Barnes', '1981-08-20');
insert into SINGER (id, first_name, last_name, birth_date) values (3, 'John', 'Butler', '1975-04-01');

insert into ALBUM (id, singer_id, title, release_date) values (1, 1, 'The Search For Everything', '2017-01-20');
insert into ALBUM (id, singer_id, title, release_date) values (2, 1, 'Battle Studies', '2009-11-17');
insert into ALBUM (id, singer_id, title, release_date) values (3, 2, ' 11:11 ', '2021-09-18');

insert into INSTRUMENT (instrument_id) values ('Guitar');
insert into INSTRUMENT (instrument_id) values ('Piano');
insert into INSTRUMENT (instrument_id) values ('Voice');
insert into INSTRUMENT (instrument_id) values ('Drums');
insert into INSTRUMENT (instrument_id) values ('Synthesizer');

insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (1, 'Guitar');
insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (1, 'Piano');
insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (2, 'Guitar');
insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (2, 'Piano');
insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (2, 'Drums');
