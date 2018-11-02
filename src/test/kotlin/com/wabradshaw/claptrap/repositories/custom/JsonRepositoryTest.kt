package com.wabradshaw.claptrap.repositories.custom

import com.wabradshaw.claptrap.structure.LinguisticSimilarity
import com.wabradshaw.claptrap.structure.LinguisticSubstitution
import com.wabradshaw.claptrap.structure.PartOfSpeech
import com.wabradshaw.claptrap.structure.Word
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * A set of tests for the JsonRepository
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonRepositoryTest {

    private val cat = Word("cat", "cat", PartOfSpeech.NOUN, 100.0)
    private val bat = Word("bat", "bat", PartOfSpeech.NOUN, 100.0)
    private val hat = Word("hat", "hat", PartOfSpeech.NOUN, 100.0)
    private val rat = Word("rat", "rat", PartOfSpeech.NOUN, 100.0)
    private val unknwon = Word("zzz", "zzz", PartOfSpeech.UNKNOWN, 100.0)
    /**
     * Tests that getWord can return a word that is in't the dictionary.
     */
    @Test
    fun testGetWord_doesntExist(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"))
        val result = repo.getWord("ZZZ")
        assertEquals(null, result)
    }

    /**
     * Tests that getWord can return a word that is the only word in the dictionary.
     */
    @Test
    fun testGetWord_exists_onlyWord(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"))
        val result = repo.getWord("cat")
        assertEquals(cat, result)
    }

    /**
     * Tests the getLinguisticSubs method when the word doesn't exist in the dictionary.
     */
    @Test
    fun testGetLinguisticSubs_unknown(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"))
        val result = repo.getLinguisticSubs(unknwon)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getLinguisticSubs method when the word has one other word in its linguistic group
     */
    @Test
    fun testGetLinguisticSubs_onlyKnown(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catBat.json"))
        val result = repo.getLinguisticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
    }

    /**
     * Tests the getLinguisticSubs method when the word has several other words in its linguistic group
     */
    @Test
    fun testGetLinguisticSubs_multiple(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/ats.json"))
        val result = repo.getLinguisticSubs(cat)
        assertEquals(3, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
        assertEquals(LinguisticSubstitution(hat, cat, LinguisticSimilarity.RHYME), result[1])
        assertEquals(LinguisticSubstitution(rat, cat, LinguisticSimilarity.RHYME), result[2])
    }

    /**
     * Tests the getLinguisticSubs method when the word has one other word in its linguistic group
     */
    @Test
    fun testGetLinguisticSubs_ignoreOtherGroups(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catBatOwl.json"))
        val result = repo.getLinguisticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
    }
}