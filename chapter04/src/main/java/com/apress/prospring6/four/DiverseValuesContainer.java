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
package com.apress.prospring6.four;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by iuliana.cosmina on 26/03/2022
 */

@Component
class ValuesHolder {

    List<String> stringList;
    InputStream inputStream;

    public ValuesHolder() {
        this.stringList = List.of("Mayer", "Psihoza", "Mazikeen");
        try {
            this.inputStream = new FileInputStream(
                    System.getProperty("java.io.tmpdir")
                            + System.getProperty("file.separator")
                            + "test.txt"
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // we are not interested in the exception that much
        }
    }

    public List<String> getStringList() {
        return stringList;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}

@Component("builtInSample")
public class DiverseValuesContainer {
    private byte[] bytes;                 // ByteArrayPropertyEditor
    private Character character;          //CharacterEditor
    private Class<?> cls;                    // ClassEditor
    private Boolean trueOrFalse;          // CustomBooleanEditor
    private List<String> stringList;      // CustomCollectionEditor
    private Date date;                    // CustomDateEditor
    private Float floatValue;             // CustomNumberEditor
    private File file;                    // FileEditor
    private InputStream stream;           // InputStreamEditor
    private Locale locale;                // LocaleEditor
    private Pattern pattern;              // PatternEditor
    private Properties properties;        // PropertiesEditor
    private String trimString;            // StringTrimmerEditor
    private URL url;                      // URLEditor

    private static Logger LOGGER = LoggerFactory.getLogger(DiverseValuesContainer.class);

    @Value("A")
    public void setCharacter(Character character) {
        LOGGER.info("Setting character: {}", character);
        this.character = character;
    }

    @Value("java.lang.String")
    public void setCls(Class<?> cls) {
        LOGGER.info("Setting class: {}" , cls.getName());
        this.cls = cls;
    }

    @Value("#{systemProperties['java.io.tmpdir']}#{systemProperties['file.separator']}test.txt")
    public void setFile(File file) {
        LOGGER.info("Setting file: {}" , file.getAbsolutePath());
        this.file = file;
    }

    @Value("en_US")
    public void setLocale(Locale locale) {
        LOGGER.info("Setting locale: {}" , locale.getDisplayName());
        this.locale = locale;
    }

    @Value("name=Ben age=41")
    public void setProperties(Properties properties) {
        LOGGER.info("Loaded {}" , properties.size() + " properties");
        this.properties = properties;
    }

    @Value("https://iuliana-cosmina.com")
    public void setUrl(URL url) {
        LOGGER.info("Setting URL: {}" , url.toExternalForm());
        this.url = url;
    }

    @Value("John Mayer")
    public void setBytes(byte... bytes) {
        LOGGER.info("Setting bytes: {}" , Arrays.toString(bytes));
        this.bytes = bytes;
    }

    @Value("true")
    public void setTrueOrFalse(Boolean trueOrFalse) {
        LOGGER.info("Setting Boolean: {}" , trueOrFalse);
        this.trueOrFalse = trueOrFalse;
    }

    @Value("#{valuesHolder.stringList}")
    public void setStringList(List<String> stringList) {
        LOGGER.info("Setting stringList with: {}" , stringList);
        this.stringList = stringList;
    }

    @Value("20/08/1981")
    public void setDate(Date date) {
        LOGGER.info("Setting date: {}" , date);
        this.date = date;
    }

    @Value("123.45678")
    public void setFloatValue(Float floatValue) {
        LOGGER.info("Setting float value: {}" , floatValue);
        this.floatValue = floatValue;
    }

    @Value("#{valuesHolder.inputStream}")
    public void setStream(InputStream stream) {
        this.stream = stream;
        LOGGER.info("Setting stream & reading from it: {}" ,
                new BufferedReader(new InputStreamReader(stream)).lines().parallel().collect(Collectors.joining("\n")));
    }

    @Value("a*b")
    public void setPattern(Pattern pattern) {
        LOGGER.info("Setting pattern: {}" , pattern);
        this.pattern = pattern;
    }

    @Value("   String need trimming   ")
    public void setTrimString(String trimString) {
        LOGGER.info("Setting trim string: {}" , trimString);
        this.trimString = trimString;
    }

    public static void main(String... args) throws Exception {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        Path path = Files.createFile(Path.of(baseDir.getAbsolutePath(), "test.txt"));
        Files.writeString(path, "Hello World!");
        path.toFile().deleteOnExit();

        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(ValuesHolder.class, DiverseValuesContainer.class);
        ctx.refresh();

        ctx.close();
    }

}
