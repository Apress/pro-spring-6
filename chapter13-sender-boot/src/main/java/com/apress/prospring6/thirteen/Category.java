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
package com.apress.prospring6.thirteen;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by iuliana on 17/11/2022
 */
@JsonSerialize(using = Category.CategorySerializer.class)
@JsonDeserialize(using = Category.CategoryDeserializer.class)
@Getter
@RequiredArgsConstructor
public enum Category {
    PERSONAL("Personal"),
    FORMAL("Formal"),
    MISC("Miscellaneous")
    ;
    private final String name;

    public static Category eventOf(final String value) {
        var result = Arrays.stream(Category.values()).filter(m -> m.getName().equalsIgnoreCase(value)).findAny();
        return result.orElse(null);
    }

    public static final class CategorySerializer extends JsonSerializer<Category> {
        @Override
        public void serialize(final Category enumValue, final JsonGenerator gen, final SerializerProvider serializer) throws IOException {
            gen.writeString(enumValue.getName());
        }
    }

    public final static class CategoryDeserializer extends JsonDeserializer<Category> {
        @Override
        public Category deserialize(final JsonParser parser, final DeserializationContext context) throws IOException, JsonProcessingException
        {
            final String jsonValue = parser.getText();
            return Category.eventOf(jsonValue);
        }
    }
}
