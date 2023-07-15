
insert into SINGER (id, first_name, last_name, birth_date) values (5, 'Nina', 'Simone', '1933-02-21');

insert into ALBUM (id, singer_id, title, release_date) values (4, 5, 'Little Girl Blue', '1959-02-20');
insert into ALBUM (id, singer_id, title, release_date) values (5, 5, 'Forbidden Fruit', '1961-08-18');
insert into ALBUM (id, singer_id, title, release_date) values (6, 5, 'I Put a Spell on You', '1965-06-15');

insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (5, 'Voice');
insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (5, 'Piano');
