package com.wabradshaw.claptrap.generation;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * A set of tests for the SetupTemplate class.
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SetupTemplateTest {

    /**
     * Tests that a script without any placeholders will be left alone.
     */
    @Test
    fun testApply_noPlaceholders(){
        val template = SetupTemplate(1, "This won't be changed.")
        assertEquals("This won't be changed.", template.apply("who", "cares"))
    }

    /**
     * Tests a script for a typical joke.
     */
    @Test
    fun testApply_typicalJoke(){
        val template = SetupTemplate(1, "What type of $PRIMARY_PLACEHOLDER$NO_DETERMINER has $SECONDARY_PLACEHOLDER?")
        assertEquals("What type of catapult has a brim?", template.apply("a catapult", "a brim"))
    }

    /**
     * Tests that the primary placeholder can be used
     */
    @Test
    fun testApply_primaryReplaced(){
        val template = SetupTemplate(1, "a b c $PRIMARY_PLACEHOLDER c")
        assertEquals("a b c a b c", template.apply("a b", "cares"))
    }

    /**
     * Tests that the secondary placeholder can be used
     */
    @Test
    fun testApply_secondaryReplaced(){
        val template = SetupTemplate(1, "a b c $SECONDARY_PLACEHOLDER c")
        assertEquals("a b c a b c", template.apply("unused", "a b"))
    }

    /**
     * Tests that the primary placeholder can be used in combination with the no-determiner marker.
     */
    @Test
    fun testApply_primaryReplacedNoDet(){
        val template = SetupTemplate(1, "a b c a $PRIMARY_PLACEHOLDER$NO_DETERMINER c")
        assertEquals("a b c a b c", template.apply("a b", "cares"))
    }

    /**
     * Tests that the secondary placeholder can be used in combination with the no-determiner marker.
     */
    @Test
    fun testApply_secondaryReplacedNoDet(){
        val template = SetupTemplate(1, "a b c a $SECONDARY_PLACEHOLDER$NO_DETERMINER c")
        assertEquals("a b c a b c", template.apply("unused", "a b"))
    }
}
