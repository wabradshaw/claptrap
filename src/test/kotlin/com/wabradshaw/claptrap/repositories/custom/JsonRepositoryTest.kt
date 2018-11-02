package com.wabradshaw.claptrap.repositories.custom

import com.wabradshaw.claptrap.structure.*
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
    private val twat = Word("twat", "twat", PartOfSpeech.NOUN, 100.0)

    private val unknown = Word("zzz", "zzz", PartOfSpeech.UNKNOWN, 100.0)
    private val quark = Word("quark", "quark", PartOfSpeech.NOUN, 100.0)

    private val tail = Word("a tail", "a tail", PartOfSpeech.UNKNOWN, 100.0)
    private val whiskers = Word("whiskers", "whiskers", PartOfSpeech.UNKNOWN, 100.0)

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
        val result = repo.getLinguisticSubs(unknown)
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
     * Tests the getLinguisticSubs method when the word has one other word in its linguistic group, and there are words
     * not in the same group.
     */
    @Test
    fun testGetLinguisticSubs_ignoreOtherGroups(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catBatOwl.json"))
        val result = repo.getLinguisticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that isn't in the dictionary.
     */
    @Test
    fun testGetSemanticSubs_unknown(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"))
        val result = repo.getSemanticSubs(unknown);
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains has substitutions.
     */
    @Test
    fun testGetSemanticSubs_has_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                                  validRelationships = listOf(Relationship.HAS_A))
        val result = repo.getSemanticSubs(cat);
        assertEquals(2, result.size)
        assertEquals(SemanticSubstitution(tail, cat, Relationship.HAS_A), result[0])
        assertEquals(SemanticSubstitution(whiskers, cat, Relationship.HAS_A), result[1])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any has substitutions.
     */
    @Test
    fun testGetSemanticSubs_has_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                                  validRelationships = listOf(Relationship.HAS_A))
        val result = repo.getSemanticSubs(quark);
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests that an adult word can be returned by getWords if showAdult is true.
     */
    @Test
    fun testShowAdult_true_getWords(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catBatOwlTwat.json"),
                                  showAdult = true)
        val result = repo.getWord("twat")
        assertEquals(twat, result)
    }

    /**
     * Tests that an adult word won't be returned by getWords if showAdult is false.
     */
    @Test
    fun testShowAdult_false_getWords(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catBatOwlTwat.json"),
                showAdult = false)
        val result = repo.getWord("twat")
        assertEquals(null, result)
    }

    /**
     * Tests that an adult word can be returned by getLinguisticSubs if showAdult is true.
     */
    @Test
    fun testShowAdult_true_getLinguisticSubs(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catBatOwlTwat.json"),
                showAdult = true)
        val result = repo.getLinguisticSubs(cat)
        assertEquals(2, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
        assertEquals(LinguisticSubstitution(twat, cat, LinguisticSimilarity.RHYME), result[1])
    }

    /**
     * Tests that an adult word won't be returned by getLinguisticSubs if showAdult is false.
     */
    @Test
    fun testShowAdult_false_getLinguisticSubs(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catBatOwlTwat.json"),
                showAdult = false)
        val result = repo.getLinguisticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
    }

}