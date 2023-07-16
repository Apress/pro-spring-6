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

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.Throws

@Transactional
@Service
class SingerService (private val repository: SingerRepository){

    @Throws(NotFoundException::class)
    fun findAll(): List<Singer> {
        val singers: List<Singer> = repository.findAll() as List<Singer>
        if (singers.isEmpty()) {
            throw NotFoundException(Singer::javaClass.name)
        }
        return singers
    }

    @Throws(NotFoundException::class)
    fun findById(id: Long?): Singer? {
        return id?.let { repository.findById(id).orElseThrow { NotFoundException(Singer::javaClass.name, id) }}
    }

    @Throws(DataIntegrityViolationException::class)
    fun save(singer: Singer?): Singer? {
        return singer?.let { repository.save(it) }
    }

    @Throws(NotFoundException::class, DataIntegrityViolationException::class)
    fun update(id: Long?, singer: Singer): Singer? {
        return id?.let { repository.findById(id).map { update(it, singer, repository) }.orElseThrow { NotFoundException(Singer::javaClass.name, id)  }}
    }

    @Throws(NotFoundException::class)
    fun delete(id: Long?) {
        id?.let { repository.findById(id).orElseThrow { NotFoundException(Singer::javaClass.name, id)  } }
        id?.let { repository.deleteById(id) }
    }

    fun update(it: Singer, singer: Singer, repository: SingerRepository): Singer {
        it.firstName = singer.firstName
        it.lastName = singer.lastName
        it.birthDate = singer.birthDate
        return repository.save(it)
    }
    
}