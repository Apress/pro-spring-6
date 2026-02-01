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
package com.apress.prospring6.twenty.boot.service;

import com.apress.prospring6.twenty.boot.model.Singer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by iuliana on 03/04/2023
 */
public interface SingerService {

    Flux<Singer> findAll();

    Mono<Singer> findById(Long id);

    Mono<Singer> findByFirstNameAndLastName(String firstName, String lastName);
    Flux<Singer> findByFirstName(String firstName);

    Flux<Singer> findByCriteriaDto(CriteriaDto criteria);

    Mono<Singer> save(Singer singer);

    Mono<Singer> update(Long id, Singer updateData);

    Mono<Void> delete(Long id);


    @Getter
    @Setter
    @NoArgsConstructor
    class CriteriaDto {
        private String fieldName;

        private String fieldValue;
    }

    class CriteriaValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return (CriteriaDto.class).isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            ValidationUtils.rejectIfEmpty(errors, "fieldName", "required", new Object[] { "fieldName" }, "Field Name is required!");
            ValidationUtils.rejectIfEmpty(errors, "fieldValue", "required", new Object[] { "fieldValue" }, "Field Value is required!");
        }
    }

    enum FieldGroup {
        FIRSTNAME,
        LASTNAME,
        BIRTHDATE;

        public static FieldGroup getField(String field){
            return FieldGroup.valueOf(field.toUpperCase());
        }
    }

}
