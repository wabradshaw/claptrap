package com.wabradshaw.claptrap.generation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * A set of tests for the DeterminerManager
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeterminerManagerTest {

    private val manager = DeterminerManager()

    /**
     * Tests that the determiner a can be removed.
     */
    @Test
    fun testRemoveA(){
        val result = manager.removeDeterminer("a cat")
        assertEquals("cat", result)
    }

    /**
     * Tests that the determiner "an" can be removed.
     */
    @Test
    fun testRemoveAn(){
        val result = manager.removeDeterminer("an owl")
        assertEquals("owl", result)
    }

    /**
     * Tests that the determiner "the" can be removed.
     */
    @Test
    fun testRemoveThe(){
        val result = manager.removeDeterminer("the sun")
        assertEquals("sun", result)
    }

    /**
     * Tests that an "a" at the end of a word won't be removed.
     */
    @Test
    fun testRemoveA_separateWords(){
        val result = manager.removeDeterminer("agenda opening")
        assertEquals("agenda opening", result)
    }

    /**
     * Tests that an "an" at the end of a word won't be removed.
     */
    @Test
    fun testRemoveAn_separateWords(){
        val result = manager.removeDeterminer("simian skills")
        assertEquals("simian skills", result)
    }

    /**
     * Tests that a "the" at the end of a word won't be removed.
     */
    @Test
    fun testRemoveThe_separateWords(){
        val result = manager.removeDeterminer("tithe offering")
        assertEquals("tithe offering", result)
    }

    /**
     * Tests that a word that starts with a normal consonant will return "a".
     */
    @Test
    fun testDefault_consonant(){
        val result = manager.chooseDefaultDeterminer("cat")
        assertEquals("a", result)
    }

    /**
     * Tests that a word that starts with a normal vowel will return "an".
     */
    @Test
    fun testDefault_vowel(){
        val result = manager.chooseDefaultDeterminer("octopus")
        assertEquals("an", result)
    }

    /**
     * Tests that a word that starts with an unusual consonant can return "an".
     */
    @Test
    fun testDefault_awkwardConsonant(){
        val result = manager.chooseDefaultDeterminer("honor")
        assertEquals("an", result)
    }

    /**
     * Tests that a word that starts with an unusual vowel can return "a".
     */
    @Test
    fun testDefault_awkwardVowel(){
        val result = manager.chooseDefaultDeterminer("university")
        assertEquals("a", result)
    }

    /**
     * Tests that a word that starts with a capital can return "an".
     */
    @Test
    fun testDefault_caseSensitive(){
        val result = manager.chooseDefaultDeterminer("NPC")
        assertEquals("an", result)
    }
}