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

}