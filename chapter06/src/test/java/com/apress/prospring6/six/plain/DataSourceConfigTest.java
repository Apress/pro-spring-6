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
package com.apress.prospring6.six.plain;

import com.apress.prospring6.six.config.EmbeddedJdbcConfig;
import com.apress.prospring6.six.config.BasicDataSourceCfg;
import com.apress.prospring6.six.config.SimpleDataSourceCfg;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by iuliana.cosmina on 05/05/2022
 */
public class DataSourceConfigTest {
    private static Logger LOGGER = LoggerFactory.getLogger(DataSourceConfigTest.class);

    @Disabled("needs MariaDB running, set up container, comment this to run")
    @Test
    public void testSimpleDataSource() throws SQLException {
        var ctx = new AnnotationConfigApplicationContext(SimpleDataSourceCfg.class);
        var dataSource = ctx.getBean("dataSource", DataSource.class);
        assertNotNull(dataSource);
        testDataSource(dataSource);
        ctx.close();
    }

    @Disabled("needs MariaDB running, set up container, comment this to run")
    @Test
    public void testBasicDataSource() throws SQLException {
        var ctx = new AnnotationConfigApplicationContext(BasicDataSourceCfg.class);
        var dataSource = ctx.getBean("dataSource", DataSource.class);
        assertNotNull(dataSource);
        testDataSource(dataSource);
        ctx.close();
    }

    @Test
    public void testEmbeddedDataSource() throws SQLException {
        var ctx = new AnnotationConfigApplicationContext(EmbeddedJdbcConfig.class);
        var dataSource = ctx.getBean("dataSource", DataSource.class);
        assertNotNull(dataSource);
        testDataSource(dataSource);
        ctx.close();
    }


    @Disabled("needs MariaDB running, set up container, comment this to run")
    @Test
    public void testSpringJdbc() throws SQLException {
        var ctx = new AnnotationConfigApplicationContext(SpringDatasourceCfg.class);
        var dataSource = ctx.getBean("dataSource", DataSource.class);
        assertNotNull(dataSource);
        testDataSource(dataSource);
        var singerDao = ctx.getBean("singerDao", SingerDao.class);
        assertEquals("John Mayer", singerDao.findNameById(1L));
        ctx.close();
    }

    private void testDataSource(DataSource dataSource) throws SQLException{
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement("SELECT 1");
             var resultSet = statement.executeQuery()){
            while (resultSet.next()) {
                int mockVal = resultSet.getInt("1");
                assertEquals(1, mockVal);
            }
        } catch (Exception e) {
            LOGGER.debug("Something unexpected happened.", e);
        }
    }
}
