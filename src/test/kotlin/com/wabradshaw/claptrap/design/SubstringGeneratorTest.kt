package com.wabradshaw.claptrap.design

import com.wabradshaw.claptrap.design.SubstringGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * A set of tests for the SubstringGenerator class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubstringGeneratorTest {

    /**
     * Tests the substring generator with the default settings on a word which is longer than the default maximum.
     */
    @Test
    fun testTypical(){
        val generator = SubstringGenerator()
        val result = generator.getSubstrings("abcdefgh")
        assertEquals(listOf("abcde", "defgh", "abcd", "efgh", "abc", "fgh", "ab", "gh"), result)
    }

    /**
     * Tests the substring generator with the default settings on a word which is shorter than the default maximum,
     * but longer than the default minimum.
     */
    @Test
    fun testShorterThanMax(){
        val generator = SubstringGenerator()
        val result = generator.getSubstrings("abcd")
        assertEquals(listOf("abcd", "abc", "bcd", "ab", "cd"), result)
    }

    /**
     * Tests the substring generator with the default settings on a word which is shorter than the default minimum will
     * produce an empty list.
     */
    @Test
    fun testShorterThanMin(){
        val generator = SubstringGenerator()
        val result = generator.getSubstrings("a")
        assertEquals(emptyList<String>(), result)
    }

    /**
     * Tests the substring generator can use a different maximum string length.
     */
    @Test
    fun testChangeMax(){
        val generator = SubstringGenerator(maxLength = 6)
        val result = generator.getSubstrings("abcdefgh")
        assertEquals(listOf("abcdef", "cdefgh", "abcde", "defgh", "abcd", "efgh", "abc", "fgh", "ab", "gh"), result)
    }

    /**
     * Tests the substring generator can use a different minimum string length.
     */
    @Test
    fun testChangeMin(){
        val generator = SubstringGenerator(minLength = 1)
        val result = generator.getSubstrings("abcdefgh")
        assertEquals(listOf("abcde", "defgh", "abcd", "efgh", "abc", "fgh", "ab", "gh", "a", "h"), result)
    }

    /**
     * Tests the substring generator can use different maximum and minimum string length at the same time.
     */
    @Test
    fun testChangeMaxAndMin(){
        val generator = SubstringGenerator(maxLength = 6, minLength = 4)
        val result = generator.getSubstrings("abcdefgh")
        assertEquals(listOf("abcdef", "cdefgh", "abcde", "defgh", "abcd", "efgh"), result)
    }

    /**
     * Tests that supplying a maxLengh shorter than the minLength will return an empty list.
     */
    @Test
    fun testMaxShorterThanMin(){
        val generator = SubstringGenerator(maxLength = 3, minLength = 5)
        val result = generator.getSubstrings("abcdefgh")
        assertEquals(emptyList<String>(), result)
    }
}