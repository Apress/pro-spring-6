CREATE TABLE SINGER (
                        ID INT NOT NULL AUTO_INCREMENT
    , VERSION INT NOT NULL DEFAULT 0
    , FIRST_NAME VARCHAR(60) NOT NULL
    , LAST_NAME VARCHAR(40) NOT NULL
    , BIRTH_DATE DATE
    , PHOTO LONGBLOB NULL
    , UNIQUE (FIRST_NAME, LAST_NAME)
    , PRIMARY KEY (ID)
);

CREATE TABLE ALBUM (
                       ID INT NOT NULL AUTO_INCREMENT
    , VERSION INT NOT NULL DEFAULT 0
    , SINGER_ID INT NOT NULL
    , TITLE VARCHAR(100) NOT NULL
    , RELEASE_DATE DATE
    , UNIQUE (SINGER_ID, TITLE)
    , PRIMARY KEY (ID)
    , CONSTRAINT FK_ALBUM FOREIGN KEY (SINGER_ID)
        REFERENCES SINGER (ID)
);

CREATE TABLE INSTRUMENT (
                            INSTRUMENT_ID VARCHAR(20) NOT NULL
    , PRIMARY KEY (INSTRUMENT_ID)
);

CREATE TABLE SINGER_INSTRUMENT (
                                   SINGER_ID INT NOT NULL
    , INSTRUMENT_ID VARCHAR(20) NOT NULL
    , PRIMARY KEY (SINGER_ID, INSTRUMENT_ID)
    , CONSTRAINT FK_SINGER_INSTRUMENT_1 FOREIGN KEY (SINGER_ID)
        REFERENCES SINGER (ID) ON DELETE CASCADE
    , CONSTRAINT FK_SINGER_INSTRUMENT_2 FOREIGN KEY (INSTRUMENT_ID)
        REFERENCES INSTRUMENT (INSTRUMENT_ID)
);

-- org/springframework/security/core/userdetails/jdbc/users.ddl  re-written for Maria DB
create table users(
                      username varchar(50) not null primary key,
                      password varchar(500) not null,
                      enabled boolean not null
);

create table authorities (
                             username varchar(50) not null,
                             authority varchar(50) not null,
                             constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (username,authority);