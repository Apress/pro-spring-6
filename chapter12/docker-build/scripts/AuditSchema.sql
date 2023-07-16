
CREATE TABLE SINGER_AUDIT (
    ID INT NOT NULL AUTO_INCREMENT
    , FIRST_NAME VARCHAR(60) NOT NULL
    , LAST_NAME VARCHAR(40) NOT NULL
    , BIRTH_DATE DATE
    , VERSION INT NOT NULL DEFAULT 0
    , CREATED_BY VARCHAR(20)
    , CREATED_DATE TIMESTAMP
    , LAST_MODIFIED_BY VARCHAR(20)
    , LAST_MODIFIED_DATE TIMESTAMP
    , UNIQUE UQ_SINGER_AUDIT_1 (FIRST_NAME, LAST_NAME)
    , PRIMARY KEY (ID)
);

-- Envers tables

CREATE TABLE SINGER_AUDIT_H (
    ID INT NOT NULL AUTO_INCREMENT
    , FIRST_NAME VARCHAR(60)
    , LAST_NAME VARCHAR(40)
    , BIRTH_DATE DATE
    , VERSION INT DEFAULT 0
    , CREATED_BY VARCHAR(20)
    , CREATED_DATE TIMESTAMP
    , LAST_MODIFIED_BY VARCHAR(20)
    , LAST_MODIFIED_DATE TIMESTAMP
    , AUDIT_REVISION INT NOT NULL
    , ACTION_TYPE INT
    , AUDIT_REVISION_END INT
    , AUDIT_REVISION_END_TS TIMESTAMP
    , UNIQUE UQ_SINGER_AUDIT_H_1 (FIRST_NAME, LAST_NAME)
    , PRIMARY KEY (ID, AUDIT_REVISION)
);

CREATE TABLE REVINFO (
     REV INT NOT NULL AUTO_INCREMENT
    , REVTSTMP BIGINT
    , PRIMARY KEY (REV)
);

ALTER TABLE SINGER_AUDIT_H
    ADD CONSTRAINT SINGER_AUDIT_H_TO_REVISION
        FOREIGN KEY ( AUDIT_REVISION ) REFERENCES REVINFO( REV);