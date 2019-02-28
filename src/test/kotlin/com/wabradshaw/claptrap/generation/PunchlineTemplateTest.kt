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
class PunchlineTemplateTest {

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
        val template = PunchlineTemplate("1", emptyList(),"Irrelevant.")
        assertTrue(template.isValid(spec))
    }

    /**
     * Tests that a template with a valid constraint applies.
     */
    @Test
    fun testIsValid_oneConstraint_valid(){
        val template = PunchlineTemplate("1",
                                         listOf(TemplateConstraint(TemplateConstraintType.SECONDARY_POS, "NOUN")),
                                         "Irrelevant.")
        assertTrue(template.isValid(spec))
    }

    /**
     * Tests that a template with an invalid constraint doesn't apply.
     */
    @Test
    fun testIsValid_oneConstraint_invalid(){
        val template = PunchlineTemplate("1",
                                         listOf(TemplateConstraint(TemplateConstraintType.SECONDARY_POS, "VERB")),
                                         "Irrelevant.")
        assertFalse(template.isValid(spec))
    }

    /**
     * Tests that a template with multiple valid constraints applies.
     */
    @Test
    fun testIsValid_multipleConstraints_valid(){
        val template = PunchlineTemplate("1",
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
        val template = PunchlineTemplate("1",
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
        val template = PunchlineTemplate("1", emptyList(),"This won't be changed.")
        assertEquals("This won't be changed.", template.apply("who cares", determinerManager))
    }

    /**
     * Tests a script for a typical joke.
     */
    @Test
    fun testApply_joke(){
        val template = PunchlineTemplate("1", emptyList(),"It's $PUN_PLACEHOLDER!")
        assertEquals("It's a fire-at!", template.apply("a fire-at", determinerManager))
    }

    /**
     * Tests a script that removes a determiner.
     */
    @Test
    fun testApply_removeDet(){
        val template = PunchlineTemplate("1", emptyList(),"The $PUN_PLACEHOLDER$NO_DETERMINER!")
        assertEquals("The moon!", template.apply("a moon", determinerManager))
    }

    /**
     * Tests a script will capitalise the first character.
     */
    @Test
    fun testApply_capitalise_start(){
        val template = PunchlineTemplate("1", emptyList(),"$PUN_PLACEHOLDER!")
        assertEquals("A fire-at!", template.apply("a fire-at", determinerManager))
    }

    /**
     * Tests a script will capitalise the first character, even if a determiner was removed.
     */
    @Test
    fun testApply_capitalise_removeDet(){
        val template = PunchlineTemplate("1", emptyList(),"$PUN_PLACEHOLDER$NO_DETERMINER!")
        assertEquals("Fire-at!", template.apply("a fire-at", determinerManager))
    }

}
