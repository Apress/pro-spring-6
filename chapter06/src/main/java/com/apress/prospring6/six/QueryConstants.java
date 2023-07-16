/*
Freeware License, some rights reserved

Copyright (c) 2023 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospring6.six;

/**
 * Created by iuliana.cosmina on 10/05/2022
 */
public enum QueryConstants {
    ;


    public static final String FIND_BY_FIRST_NAME = "select id, first_name, last_name, birth_date from SINGER where first_name = :first_name";
    public static final String NAMED_FIND_NAME = "select CONCAT(first_name , ' ' , last_name) from SINGER where id = :singerId";
    public static final String PARAMETRIZED_FIND_NAME = "select CONCAT(first_name , ' ' , last_name) from SINGER where id = ?";
    public static final String FIND_NAME = "select first_name, last_name from SINGER where id=";

    public static final String SIMPLE_INSERT = "insert into SINGER (first_name, last_name, birth_date) values (?, ?, ?)";
    public static final String SIMPLE_DELETE = "delete from SINGER where id=?";
    public static final String ALL_SELECT = "select * from SINGER";
    public static final String ALL_JOIN_SELECT = "select s.id, s.first_name, s.last_name, s.birth_date, "+
            "a.id AS album_id, a.title, a.release_date " +
            "from SINGER s " +
            "left join ALBUM a on s.id = a.singer_id";

    public static final String UPDATE_SINGER = "update SINGER set first_name=:first_name, last_name=:last_name, birth_date=:birth_date where id=:id";

    public static final String INSERT_SINGER = "insert into SINGER (first_name, last_name, birth_date) values (:first_name, :last_name, :birth_date)";

    public static final String INSERT_SINGER_ALBUM = "insert into ALBUM (singer_id, title, release_date) values (:singer_id, :title, :release_date)";
    public static final String FIND_SINGER_ALBUM = "SELECT s.id, s.first_name, s.last_name, s.birth_date" +
            ", a.id AS album_id, a.title, a.release_date FROM SINGER s " +
            "LEFT JOIN ALBUM a ON s.id = a.singer_id";
}
