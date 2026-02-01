insert into SINGER (id, first_name, last_name, birth_date, pseudonym, genre) values (1, 'John', 'Mayer', '1977-10-16', 'John Clayton Mayer', 'pop/rock/blues');
insert into SINGER (id, first_name, last_name, birth_date, pseudonym, genre) values (2, 'Ben', 'Barnes', '1981-08-20', 'Benjamin Thomas Barnes','pop');
insert into SINGER (id, first_name, last_name, birth_date, pseudonym, genre) values (3, 'John', 'Butler', '1975-04-01', 'John Charles Wiltshire-Butler', 'alternative rock/funk');
insert into SINGER (id, first_name, last_name, birth_date, genre) values (4, 'Eric', 'Clapton', '1945-03-30', 'rock/blues');
insert into SINGER (id, first_name, last_name, birth_date, genre) values (5, 'B.B.', 'King', '1925-09-16', 'electric blues/ rock and roll');
insert into SINGER (id, first_name, last_name, birth_date, genre) values (6, 'Jimi', 'Hendrix', '1942-11-27', 'rock/blues');
insert into SINGER (id, first_name, last_name, birth_date, genre) values (7, 'Jimmy', 'Page', '1944-01-09', 'rock/blues/heavy metal');


insert into AWARD (id, singer_id, year, type, item_name, award_name) values (1, 1, 2008, 'T', 'Gravity', 'Best Solo Rock Vocal Performance');
insert into AWARD (id, singer_id, year, type, item_name, award_name) values (2, 1, 2008, 'T', 'Say', 'Best Male Pop Vocal Performance');
insert into AWARD (id, singer_id, year, type, item_name, award_name) values (3, 1, 2006, 'A', 'Continuum', 'Best Pop Vocal Album');
insert into AWARD (id, singer_id, year, type, item_name, award_name) values (4, 1, 2006, 'A', 'Waiting on the World to Change', 'Best Male Pop Vocal Performance');
insert into AWARD (id, singer_id, year, type, item_name, award_name) values (5, 1, 2002, 'T', 'Your Body Is a Wonderland', 'Best Male Pop Vocal Performance');
insert into AWARD (id, singer_id, year, type, item_name, award_name) values (6, 4, 2007, 'A', 'Road to Escondido', 'Best Contemporary Blues Album');
insert into AWARD (id, singer_id, year, type, item_name, award_name) values (7, 4, 2001, 'T', 'Reptile', 'Best Pop Instrumental Performance');
insert into AWARD (id, singer_id, year, type, item_name, award_name) values (8, 4, 2000, 'A', 'Riding with the King', 'Best Traditional Blues Album/Best Traditional Blues Recording');
insert into AWARD (id, singer_id, year, type, item_name, award_name) values (9, 4, 1999, 'T', 'Calling', 'Best Rock Instrumental Performance');
insert into AWARD (id, singer_id, year, type, item_name, award_name) values (10, 4, 1998, 'T', 'My Father''s Eyes', 'Best Male Pop Vocal Performance');

insert into INSTRUMENT (instrument_id) values ('Guitar');
insert into INSTRUMENT (instrument_id) values ('Piano');
insert into INSTRUMENT (instrument_id) values ('Voice');
insert into INSTRUMENT (instrument_id) values ('Drums');
insert into INSTRUMENT (instrument_id) values ('Flute');
insert into INSTRUMENT (instrument_id) values ('Harmonica');
insert into INSTRUMENT (instrument_id) values ('Synthesizer');

insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (1, 'Guitar');
insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (1, 'Piano');
insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (1, 'Harmonica');
insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (2, 'Guitar');
insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (2, 'Piano');
insert into SINGER_INSTRUMENT(singer_id, instrument_id) values (2, 'Drums');
