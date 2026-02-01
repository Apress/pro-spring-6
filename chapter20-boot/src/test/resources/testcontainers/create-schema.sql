CREATE TABLE singer (
                       id INT NOT NULL AUTO_INCREMENT
    , first_name VARCHAR(60) NOT NULL
    , last_name VARCHAR(40) NOT NULL
    , birth_date DATE
    , UNIQUE (first_name, last_name)
    , PRIMARY KEY (id)
);

insert into singer (id, first_name, last_name, birth_date) values (1, 'John', 'Mayer', '1977-10-16');
insert into singer (id, first_name, last_name, birth_date) values (2, 'Ben', 'Barnes', '1981-08-20');
insert into singer (id, first_name, last_name, birth_date) values (3, 'John', 'Butler', '1975-04-01');
insert into singer (id, first_name, last_name, birth_date) values (4, 'Eric', 'Clapton', '1945-03-30');