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

    /**
     * Tests the primary pos constraint for a noun when looking for a noun
     */
    @Test
    fun testPrimaryPos_matches(){
        val constraint = SetupConstraint(SetupConstraintType.PRIMARY_POS, "NOUN")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertTrue(constraint.applies(spec))
    }

    /**
     * Tests the primary pos constraint for a verb when looking for a noun
     */
    @Test
    fun testPrimaryPos_clashes(){
        val constraint = SetupConstraint(SetupConstraintType.PRIMARY_POS, "NOUN")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(verb, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }

    /**
     * Tests the primary pos constraint when looking for a noun, but missing a primary setup
     */
    @Test
    fun testPrimaryPos_missing(){
        val constraint = SetupConstraint(SetupConstraintType.PRIMARY_POS, "NOUN")
        val spec = JokeSpec("tiger",
                null,
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }

    /**
     * Tests the secondary pos constraint for a noun when looking for a noun
     */
    @Test
    fun testSecondaryPos_matches(){
        val constraint = SetupConstraint(SetupConstraintType.SECONDARY_POS, "NOUN")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertTrue(constraint.applies(spec))
    }

    /**
     * Tests the secondary pos constraint for a verb when looking for a noun
     */
    @Test
    fun testSecondaryPos_clashes(){
        val constraint = SetupConstraint(SetupConstraintType.SECONDARY_POS, "NOUN")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(verb, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }
    
    /**
     * Tests the nucleus pos constraint for a noun when looking for a noun
     */
    @Test
    fun testNucleusPos_matches(){
        val constraint = SetupConstraint(SetupConstraintType.NUCLEUS_POS, "NOUN")
        val spec = JokeSpec("tiger",
                noun,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertTrue(constraint.applies(spec))
    }

    /**
     * Tests the nucleus pos constraint for a verb when looking for a noun
     */
    @Test
    fun testNucleusPos_clashes(){
        val constraint = SetupConstraint(SetupConstraintType.NUCLEUS_POS, "NOUN")
        val spec = JokeSpec("tiger",
                verb,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }

    /**
     * Tests the nucleus pos constraint when looking for a noun, but missing a nucleus setup
     */
    @Test
    fun testNucleusPos_missing(){
        val constraint = SetupConstraint(SetupConstraintType.NUCLEUS_POS, "NOUN")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }

    /**
     * Tests the primary relationship constraint for IS_A when looking for IS_A
     */
    @Test
    fun testPrimaryRelationship_matches(){
        val constraint = SetupConstraint(SetupConstraintType.PRIMARY_RELATIONSHIP, "IS_A")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertTrue(constraint.applies(spec))
    }

    /**
     * Tests the primary relationship constraint for ON when looking for IS_A
     */
    @Test
    fun testPrimaryRelationship_clashes(){
        val constraint = SetupConstraint(SetupConstraintType.PRIMARY_RELATIONSHIP, "IS_A")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.ON),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }

    /**
     * Tests the primary relationship constraint when looking for IS_A, but missing a primary setup
     */
    @Test
    fun testPrimaryRelationship_missing(){
        val constraint = SetupConstraint(SetupConstraintType.PRIMARY_RELATIONSHIP, "IS_A")
        val spec = JokeSpec("tiger",
                null,
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }

    /**
     * Tests the secondary relationship constraint for IS_A when looking for IS_A
     */
    @Test
    fun testSecondaryRelationship_matches(){
        val constraint = SetupConstraint(SetupConstraintType.SECONDARY_RELATIONSHIP, "IS_A")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                linguisticSubstitution)
        assertTrue(constraint.applies(spec))
    }

    /**
     * Tests the secondary relationship constraint for ON when looking for IS_A
     */
    @Test
    fun testSecondaryRelationship_clashes(){
        val constraint = SetupConstraint(SetupConstraintType.SECONDARY_RELATIONSHIP, "IS_A")
        val spec = JokeSpec("tiger",
                null,
                SemanticSubstitution(noun, noun, Relationship.IS_A),
                SemanticSubstitution(noun, noun, Relationship.ON),
                linguisticSubstitution)
        assertFalse(constraint.applies(spec))
    }
}