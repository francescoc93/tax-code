DROP TABLE IF EXISTS PEOPLE;
DROP TABLE IF EXISTS CITIES;

CREATE TABLE PEOPLE (
    NAME_TAX_CODE VARCHAR(3) NOT NULL,
    SURNAME_TAX_CODE VARCHAR(3) NOT NULL,
    DATE_BIRTH_TAX_CODE VARCHAR(5) NOT NULL,
    CITY_TAX_CODE VARCHAR(4) NOT NULL,
    VALIDATION_CHARACTER_TAX_CODE VARCHAR(1) NOT NULL,
    NAME VARCHAR(128) NOT NULL,
    SURNAME VARCHAR(128) NOT NULL,
    CITY_BIRTH VARCHAR(128) NOT NULL,
    DATE_BIRTH VARCHAR(128) NOT NULL,
    GENDER INTEGER NOT NULL,
    PRIMARY KEY (NAME_TAX_CODE,SURNAME_TAX_CODE,DATE_BIRTH_TAX_CODE,DATE_BIRTH_TAX_CODE,CITY_TAX_CODE,VALIDATION_CHARACTER_TAX_CODE )
);

CREATE TABLE CITIES (
    CITY_CODE VARCHAR(4) NOT NULL,
    CITY VARCHAR(255) NOT NULL,
    PRIMARY KEY (CITY_CODE)
);