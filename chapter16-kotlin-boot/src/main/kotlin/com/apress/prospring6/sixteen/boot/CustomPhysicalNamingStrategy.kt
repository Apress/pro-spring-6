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

import org.hibernate.boot.model.naming.Identifier
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*

@Component
class CustomPhysicalNamingStrategy : PhysicalNamingStrategyStandardImpl(), Serializable {

    override fun toPhysicalTableName(logicalName: Identifier?, context: JdbcEnvironment): Identifier? {
        return apply(logicalName)
    }

    override fun toPhysicalColumnName(logicalName: Identifier?, context: JdbcEnvironment): Identifier? {
        return apply(logicalName)
    }

    private fun apply(name: Identifier?): Identifier? {
        if (name == null) {
            return null
        }
        val builder = StringBuilder(name.text.replace('.', '_'))
        var i = 1
        while (i < builder.length - 1) {
            if (isUnderscoreRequired(builder[i - 1], builder[i], builder[i + 1])) {
                builder.insert(i++, '_')
            }
            i++
        }
        return Identifier.toIdentifier(builder.toString().uppercase(Locale.getDefault()))
    }

    private fun isUnderscoreRequired(before: Char, current: Char, after: Char): Boolean {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after)
    }
}

