package com.wabradshaw.claptrap.generation;

import com.wabradshaw.claptrap.structure.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * A set of tests for the SetupTemplate class.
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SetupTemplateTest {

    val spec = JokeSpec(
            "catapult",
            null,
            null,
            SemanticSubstitution(Word("a brim", "brim", PartOfSpeech.NOUN, 1.0),
                                 Word("hat", "hat", PartOfSpeech.NOUN, 1.0),
                                 Relationship.HAS_A),
            LinguisticSubstitution(Word("hat", "hat", PartOfSpeech.NOUN, 1.0),
                                   Word("cat", "cat", PartOfSpeech.NOUN, 1.0),
                                   LinguisticSimilarity.RHYME)
    )
    val determinerManager = DeterminerManager()

    /**
     * Tests that a template with no constraints applies.
     */
    @Test
    fun testIsValid_noConstraints(){
        val template = SetupTemplate("1", emptyList(),"Irrelevant.")
        assertTrue(template.isValid(spec))
    }

    /**
     * Tests that a template with a valid constraint applies.
     */
    @Test
    fun testIsValid_oneConstraint_valid(){
        val template = SetupTemplate("1",
                                     listOf(TemplateConstraint(TemplateConstraintType.SECONDARY_POS, "NOUN")),
                                     "Irrelevant.")
        assertTrue(template.isValid(spec))
    }

    /**
     * Tests that a template with an invalid constraint doesn't apply.
     */
    @Test
    fun testIsValid_oneConstraint_invalid(){
        val template = SetupTemplate("1",
                listOf(TemplateConstraint(TemplateConstraintType.SECONDARY_POS, "VERB")),
                "Irrelevant.")
        assertFalse(template.isValid(spec))
    }

    /**
     * Tests that a template with multiple valid constraints applies.
     */
    @Test
    fun testIsValid_multipleConstraints_valid(){
        val template = SetupTemplate("1",
                listOf(TemplateConstraint(TemplateConstraintType.SECONDARY_POS, "NOUN"),
                       TemplateConstraint(TemplateConstraintType.NUCLEUS_KNOWN, "false"),
                       TemplateConstraint(TemplateConstraintType.SECONDARY_RELATIONSHIP, "HAS_A")),
                "Irrelevant.")
        assertTrue(template.isValid(spec))
    }

    /**
     * Tests that a template with one invalid constraint among several valid ones doesn't apply.
     */
    @Test
    fun testIsValid_multipleConstraints_invalid(){
        val template = SetupTemplate("1",
                listOf(TemplateConstraint(TemplateConstraintType.SECONDARY_POS, "NOUN"),
                        TemplateConstraint(TemplateConstraintType.NUCLEUS_KNOWN, "true"),
                        TemplateConstraint(TemplateConstraintType.SECONDARY_RELATIONSHIP, "HAS_A")),
                "Irrelevant.")
        assertFalse(template.isValid(spec))
    }

    /**
     * Tests that a script without any placeholders will be left alone.
     */
    @Test
    fun testApply_noPlaceholders(){
        val template = SetupTemplate("1", emptyList(),"This won't be changed.")
        assertEquals("This won't be changed.", template.apply("who", "cares", determinerManager))
    }

    /**
     * Tests a script for a typical joke.
     */
    @Test
    fun testApply_typicalJoke(){
        val template = SetupTemplate("1", emptyList(),"What type of $PRIMARY_PLACEHOLDER$NO_DETERMINER has $SECONDARY_PLACEHOLDER?")
        assertEquals("What type of catapult has a brim?", template.apply("a catapult", "a brim", determinerManager))
    }

    /**
     * Tests that the template can be capitalised.
     */
    @Test
    fun testApply_capitalises(){
        val template = SetupTemplate("1", emptyList(),"a b c")
        assertEquals("A b c", template.apply("unused", "unused", determinerManager))
    }

    /**
     * Tests that the primary placeholder can be used
     */
    @Test
    fun testApply_primaryReplaced(){
        val template = SetupTemplate("1", emptyList(),"a b c $PRIMARY_PLACEHOLDER c")
        assertEquals("A b c a b c", template.apply("a b", "cares", determinerManager))
    }

    /**
     * Tests that the secondary placeholder can be used
     */
    @Test
    fun testApply_secondaryReplaced(){
        val template = SetupTemplate("1", emptyList(),"a b c $SECONDARY_PLACEHOLDER c")
        assertEquals("A b c a b c", template.apply("unused", "a b", determinerManager))
    }

    /**
     * Tests that the primary placeholder can be used in combination with the no-determiner marker.
     */
    @Test
    fun testApply_primaryReplacedNoDet(){
        val template = SetupTemplate("1", emptyList(),"a b c a $PRIMARY_PLACEHOLDER$NO_DETERMINER c")
        assertEquals("A b c a b c", template.apply("a b", "cares", determinerManager))
    }

    /**
     * Tests that the secondary placeholder can be used in combination with the no-determiner marker.
     */
    @Test
    fun testApply_secondaryReplacedNoDet(){
        val template = SetupTemplate("1", emptyList(),"a b c a $SECONDARY_PLACEHOLDER$NO_DETERMINER c")
        assertEquals("A b c a b c", template.apply("unused", "a b", determinerManager))
    }
}
