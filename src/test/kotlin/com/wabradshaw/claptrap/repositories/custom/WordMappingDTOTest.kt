package com.wabradshaw.claptrap.repositories.custom

import com.wabradshaw.claptrap.structure.PartOfSpeech
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance


/**
 * A set of tests for the WordMappingDTO class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WordMappingDTOTest {

    /**
     * Tests that toWord produces a word with the right spelling.
     */
    @Test
    fun testToWord_spelling(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false, emptyList())
        val result = dto.toWord()
        assertEquals("cat", result.spelling);
    }

    /**
     * Tests that toWord produces a word with the spelling as the pronunciation.
     */
    @Test
    fun testToWord_pronunciation(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false, emptyList())
        val result = dto.toWord()
        assertEquals("cat", result.pronunciation);
    }

    /**
     * Tests that toWord produces a word with an arbitrary frequency.
     */
    @Test
    fun testToWord_frequency(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false, emptyList())
        val result = dto.toWord()
        assertEquals(100.0, result.popularity);
    }

    /**
     * Tests that toWord can handle a noun.
     */
    @Test
    fun testToWord_pos_noun(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false, emptyList())
        val result = dto.toWord()
        assertEquals(PartOfSpeech.NOUN, result.partOfSpeech);
    }

    /**
     * Tests that toWord can handle a verb.
     */
    @Test
    fun testToWord_pos_verb(){
        val dto = WordMappingDTO("sat", "_at", "Verb", false, emptyList())
        val result = dto.toWord()
        assertEquals(PartOfSpeech.VERB, result.partOfSpeech);
    }

    /**
     * Tests that toWord can handle an adjective.
     */
    @Test
    fun testToWord_pos_adj(){
        val dto = WordMappingDTO("fat", "_at", "Adj", false, emptyList())
        val result = dto.toWord()
        assertEquals(PartOfSpeech.ADJECTIVE, result.partOfSpeech);
    }

    /**
     * Tests that toWord can handle a noun.
     */
    @Test
    fun testToWord_pos_unknown(){
        val dto = WordMappingDTO("qat", "_at", "???", false, emptyList())
        val result = dto.toWord()
        assertEquals(PartOfSpeech.UNKNOWN, result.partOfSpeech);
    }
}