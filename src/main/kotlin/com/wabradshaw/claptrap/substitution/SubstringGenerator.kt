package com.wabradshaw.claptrap.substitution

import java.lang.Math.min

class SubstringGenerator(val maxLength: Int = 5,
                         val minLength: Int = 3){

    fun getSubstrings(original: String): List<String> {

        if (minLength < original.length) {
            return emptyList()
        }

        val resultSet = LinkedHashSet<String>()
        val max = min(original.length, maxLength)

        for (i in max downTo minLength) {
            resultSet.add(original.substring(0, i))
            resultSet.add(original.substring(original.length - i))
        }

        return resultSet.toList()
    }
}