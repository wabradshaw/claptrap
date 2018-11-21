package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.structure.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * A set of tests for SetupConstraints.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SetupConstraintTest {

    val noun = Word("cat", "cat", PartOfSpeech.NOUN, 1.0)
    val verb = Word("run", "run", PartOfSpeech.VERB, 1.0)

    val linguisticSubstitution = LinguisticSubstitution(noun, noun, LinguisticSimilarity.RHYME)

    /**
     * Tests the primary known constraint type when looking for a known word, and it exists
     */
    @Test
    fun testPrimaryKnown_known_true(){
        val constraint = SetupConstraint(SetupConstraintType.PRIMARY_KNOWN, "true")
        val spec = JokeSpec("tiger",
                            null,
                            SemanticSubstitution(noun, noun, Relationship.IS_A),
                            SemanticSubstitution(noun, noun, Relationship.IS_A),
                            linguisticSubstitution)
        assertTrue(constraint.applies(spec))
    }

    /**
     * Tests the primary known constraint type when looking for a known word, and it doesn't exist
     */
    @Test
    fun testPrimaryKnown_known_false(){
        val constraint = SetupConstraint(SetupConstraintType.PRIMARY_KNOWN, "true")
        val spec = JokeSpec("tiger",
                null,
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }
    
    /**
     * Tests the primary known constraint type when looking for an unknown word, and it doesn't exist
     */
    @Test
    fun testPrimaryKnown_unknown_true(){
        val constraint = SetupConstraint(SetupConstraintType.PRIMARY_KNOWN, "false")
        val spec = JokeSpec("tiger",
                null,
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertTrue(constraint.applies(spec))
    }

    /**
     * Tests the primary known constraint type when looking for an unknown word, and it exists
     */
    @Test
    fun testPrimaryKnown_unknown_false(){
        val constraint = SetupConstraint(SetupConstraintType.PRIMARY_KNOWN, "false")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }


    /**
     * Tests the nucleus known constraint type when looking for a known word, and it exists
     */
    @Test
    fun testNucleusKnown_known_true(){
        val constraint = SetupConstraint(SetupConstraintType.NUCLEUS_KNOWN, "true")
        val spec = JokeSpec("tiger",
                noun,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertTrue(constraint.applies(spec))
    }

    /**
     * Tests the nucleus known constraint type when looking for a known word, and it doesn't exist
     */
    @Test
    fun testNucleusKnown_known_false(){
        val constraint = SetupConstraint(SetupConstraintType.NUCLEUS_KNOWN, "true")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }

    /**
     * Tests the nucleus known constraint type when looking for an unknown word, and it doesn't exist
     */
    @Test
    fun testNucleusKnown_unknown_true(){
        val constraint = SetupConstraint(SetupConstraintType.NUCLEUS_KNOWN, "false")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertTrue(constraint.applies(spec))
    }

    /**
     * Tests the nucleus known constraint type when looking for an unknown word, and it exists
     */
    @Test
    fun testNucleusKnown_unknown_false(){
        val constraint = SetupConstraint(SetupConstraintType.NUCLEUS_KNOWN, "false")
        val spec = JokeSpec("tiger",
                noun,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }
}