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

    /**
     * Tests that a substitution which can be done at the start of multiple words will use the start of the first.
     */
    @Test
    fun testTwoWords_startStart() {
        val result = JokeSubstituter().createJokeWord(testSpec("abc1 abc2","abc", "def"))
        assertEquals("def-1 abc2", result)
    }

    /**
     * Tests that a substitution which can be done in the middle of one word or the start of the next will use the start
     * of the second.
     */
    @Test
    fun testTwoWords_middleStart() {
        val result = JokeSubstituter().createJokeWord(testSpec("1abc1 abc2","abc", "def"))
        assertEquals("1abc1 def-2", result)
    }

    /**
     * Tests that a substitution which can be done at the end of one word or the start of the next will use the start
     * of the second.
     */
    @Test
    fun testTwoWords_endStart() {
        val result = JokeSubstituter().createJokeWord(testSpec("1abc abc2","abc", "def"))
        assertEquals("1abc def-2", result)
    }

    /**
     * Tests that a substitution which can be done at the start of one word or the middle of the next will use the
     * start of the first.
     */
    @Test
    fun testTwoWords_startMiddle() {
        val result = JokeSubstituter().createJokeWord(testSpec("abc1 2abc2","abc", "def"))
        assertEquals("def-1 2abc2", result)
    }

    /**
     * Tests that a substitution which can be done in the middle of multiple words will use the middle of the first.
     */
    @Test
    fun testTwoWords_middleMiddle() {
        val result = JokeSubstituter().createJokeWord(testSpec("1abc1 2abc2","abc", "def"))
        assertEquals("1-def-1 2abc2", result)
    }

    /**
     * Tests that a substitution which can be done at the end of one word or the middle of the next will use the end
     * of the first.
     */
    @Test
    fun testTwoWords_endMiddle() {
        val result = JokeSubstituter().createJokeWord(testSpec("1abc 2abc2","abc", "def"))
        assertEquals("1-def 2abc2", result)
    }

    /**
     * Tests that a substitution which can be done at the start of one word or the end of the next will use the
     * start of the first.
     */
    @Test
    fun testTwoWords_startEnd() {
        val result = JokeSubstituter().createJokeWord(testSpec("abc1 2abc","abc", "def"))
        assertEquals("def-1 2abc", result)
    }

    /**
     * Tests that a substitution which can be done in the middle of one word or the end of the next will use the end
     * of the second.
     */
    @Test
    fun testTwoWords_middleEnd() {
        val result = JokeSubstituter().createJokeWord(testSpec("1abc1 2abc","abc", "def"))
        assertEquals("1abc1 2-def", result)
    }

    /**
     * Tests that a substitution which can be done at the end of one word or the middle of the next will use the end
     * of the first.
     */
    @Test
    fun testTwoWords_endEnd() {
        val result = JokeSubstituter().createJokeWord(testSpec("1abc 2abc","abc", "def"))
        assertEquals("1-def 2abc", result)
    }

    /**
     * Tests that a substitution which can be done at the end of one word or the middle of the next will use the end
     * of the first.
     */
    @Test
    fun testManyWords_firstValidStart() {
        val result = JokeSubstituter().createJokeWord(testSpec("xyz nabc abc abc","abc", "def"))
        assertEquals("xyz nabc def abc", result)
    }

    /**
     * Tests that a substitution next to an existing hyphen won't lead to double hyphens.
     */
    @Test
    fun testAlreadyHyphenatedBySubstitution(){
        val result = JokeSubstituter().createJokeWord(testSpec("half-mad","mad", "fad"))
        assertEquals("half-fad", result)
    }

    /**
     * Tests that a substitution with an existing hyphen away from the substitute won't remove that hyphen.
     */
    @Test
    fun testAlreadyHyphenatedAwayFromSubstitution(){
        val result = JokeSubstituter().createJokeWord(testSpec("cat-in-the-hat","cat", "shat"))
        assertEquals("shat-in-the-hat", result)
    }

    /**
     * Tests that a substitution after a space won't lead to an orphaned hyphen.
     */
    @Test
    fun testSpaceBeforeSubstitution(){
        val result = JokeSubstituter().createJokeWord(testSpec("top hat","hat", "fat"))
        assertEquals("top fat", result)
    }

    /**
     * Tests that a substitution before a space won't lead to an orphaned hyphen.
     */
    @Test
    fun testSpaceAfterSubstitution(){
        val result = JokeSubstituter().createJokeWord(testSpec("top hat","top", "cop"))
        assertEquals("cop hat", result)
    }

    /**
     * Tests that a substitution away from a space will look correct.
     */
    @Test
    fun testSpaceAwayFromSubstitution(){
        val result = JokeSubstituter().createJokeWord(testSpec("washing machine","wash", "posh"))
        assertEquals("posh-ing machine", result)
    }

    /**
     * Tests that a substitution isn't case sensitive at the start.
     */
    @Test
    fun testIgnoreCase_start(){
        val result = JokeSubstituter().createJokeWord(testSpec("Donald Trump","don", "con"))
        assertEquals("con-ald Trump", result)
    }

    /**
     * Tests that a substitution isn't case sensitive at the end.
     */
    @Test
    fun testIgnoreCase_end(){
        val result = JokeSubstituter().createJokeWord(testSpec("parrot","rot", "Scot"))
        assertEquals("par-Scot", result)
    }

    /**
     * Tests that a substitution isn't case sensitive in the middle.
     */
    @Test
    fun testIgnoreCase_middle(){
        val result = JokeSubstituter().createJokeWord(testSpec("Harry Potter","pot", "Scot"))
        assertEquals("Harry Scot-ter", result)
    }
}