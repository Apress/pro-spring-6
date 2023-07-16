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
package com.apress.prospring6.seven.util;

import org.jooq.codegen.GenerationTool;

import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by iuliana.cosmina on 19/06/2022
 */
public class GenerateJOOQSources {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateJOOQSources.class);

    public static void main(String... args) throws Exception {
        if (args.length > 0) { // run with any argument to run this one
            // programmatic  version 1
            LOGGER.info("... Generating jOOQ using programmatic version 1 with XML configuration ...");
            URL resource = GenerateJOOQSources.class.getResource("/jooq-config.xml");
            assert resource != null;
            final File jooqCfg = Paths.get(resource.toURI()).toFile();

            GenerationTool.generate(
                    Files.readString(jooqCfg.toPath())
            );
        } else {
            // programmatic version 2
            LOGGER.info("... Generating jOOQ using programmatic version 2 with programmatic configuration ...");
            GenerationTool.generate(new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver("org.mariadb.jdbc.Driver")
                        .withUrl("jdbc:mariadb://localhost:3306/musicdb")
                        .withUser("prospring6")
                        .withPassword("prospring6"))
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withName("org.jooq.meta.mariadb.MariaDBDatabase")
                                .withInputSchema("musicdb")
                                .withIncludes(".*"))
                        .withGenerate(
                        new Generate()
                                .withPojos(true)
                                .withPojosToString(true)
                                .withDaos(true))
                        .withTarget(new Target()
                                .withPackageName("com.apress.prospring6.seven.jooq.generated")
                                .withDirectory("./chapter07-jooq/src/main/generated"))));
        }
    }
}
