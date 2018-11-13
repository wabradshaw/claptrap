package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.structure.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * A set of tests for the JokeSubstituter.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JokeSubstituterTest {

    /**
     * Creates a JokeSpec with the supplied substitution. Used to make it quicker to build test cases.
     */
    private fun testSpec(nucleus: String, original: String, replacement: String): JokeSpec {
        val fakeWord= Word("a","a",PartOfSpeech.NOUN, 1.0)
        val semantic =  SemanticSubstitution(fakeWord, fakeWord, Relationship.IS_A)
        val linguistic = LinguisticSubstitution(Word(replacement,replacement,PartOfSpeech.NOUN, 1.0),
                                                Word(original,original,PartOfSpeech.NOUN, 1.0),
                                                LinguisticSimilarity.RHYME)
        return JokeSpec(nucleus, null, null, semantic, linguistic)
    }

    /**
     * Tests that a substitution can be done at the start of a word.
     */
    @Test
    fun testStartsWith(){
        val result = JokeSubstituter().createJokeWord(testSpec("catapult","cat", "shat"))
        assertEquals("shat-apult", result)
    }

    /**
     * Tests that a substitution can be done at the end of a word.
     */
    @Test
    fun testEndsWith(){
        val result = JokeSubstituter().createJokeWord(testSpec("bobcat","cat", "shat"))
        assertEquals("bob-shat", result)
    }

    /**
     * Tests that a substitution can be done in the middle of a word.
     */
    @Test
    fun testMiddleOf(){
        val result = JokeSubstituter().createJokeWord(testSpec("bobcats","cat", "shat"))
        assertEquals("bob-shat-s", result)
    }

    /**
     * Tests that a substitution which can be done at the start, end, or middle of a word will use the start.
     */
    @Test
    fun testMultipleUse(){
        val result = JokeSubstituter().createJokeWord(testSpec("abcabcabcabc","abc", "def"))
        assertEquals("def-abcabcabc", result)
    }

    /**
     * Tests that a substitution which can be done at the end, or middle of a word will use the end.
     */
    @Test
    fun testMiddleEnd(){
        val result = JokeSubstituter().createJokeWord(testSpec("zzzabcabcabc","abc", "def"))
        assertEquals("zzzabcabc-def", result)
    }

    /**
     * Tests that a substitution which can be done at several points in the middle of a word will only do the first of
     * them.
     */
    @Test
    fun testMultipleMiddle(){
        val result = JokeSubstituter().createJokeWord(testSpec("zzzabcabczzz","abc", "def"))
        assertEquals("zzz-def-abczzz", result)
    }
}