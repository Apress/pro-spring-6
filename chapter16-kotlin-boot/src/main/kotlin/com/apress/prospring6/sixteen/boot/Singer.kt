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

package com.apress.prospring6.sixteen.boot

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Entity
@Table(name = "SINGER")
data class Singer (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID") @JsonIgnore // do not serialize
        var id: Long? = null,

        @Version @Column(name = "VERSION") @JsonIgnore // do not serialize
        var version: Int = 0,

        @Column(name = "FIRST_NAME") @NotEmpty @Size(min = 2, max = 30)
        var firstName:  String?,

        @Column(name = "LAST_NAME")
        var lastName: @NotEmpty @Size(min = 2, max = 30) String? = null,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Column(name = "BIRTH_DATE")
        var birthDate: LocalDate? = null
)