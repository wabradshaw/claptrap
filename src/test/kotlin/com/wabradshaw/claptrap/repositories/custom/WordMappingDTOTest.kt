package com.wabradshaw.claptrap.repositories.custom

import com.wabradshaw.claptrap.structure.Form
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
        val dto = WordMappingDTO("cat", "_at", "Noun", false)
        val result = dto.toWord()
        assertEquals("cat", result.spelling)
    }

    /**
     * Tests that toWord produces a word with the spelling as the pronunciation.
     */
    @Test
    fun testToWord_pronunciation(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false)
        val result = dto.toWord()
        assertEquals("cat", result.pronunciation)
    }

    /**
     * Tests that toWord produces a word with an arbitrary frequency.
     */
    @Test
    fun testToWord_frequency(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false)
        val result = dto.toWord()
        assertEquals(100.0, result.frequency)
    }

    /**
     * Tests that toWord can handle a noun.
     */
    @Test
    fun testToWord_pos_noun(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false)
        val result = dto.toWord()
        assertEquals(PartOfSpeech.NOUN, result.partOfSpeech)
    }

    /**
     * Tests that toWord can handle a verb.
     */
    @Test
    fun testToWord_pos_verb(){
        val dto = WordMappingDTO("sat", "_at", "Verb", false)
        val result = dto.toWord()
        assertEquals(PartOfSpeech.VERB, result.partOfSpeech)
    }

    /**
     * Tests that toWord can handle an adjective.
     */
    @Test
    fun testToWord_pos_adj(){
        val dto = WordMappingDTO("fat", "_at", "Adj", false)
        val result = dto.toWord()
        assertEquals(PartOfSpeech.ADJECTIVE, result.partOfSpeech)
    }

    /**
     * Tests that toWord can handle a noun.
     */
    @Test
    fun testToWord_pos_unknown(){
        val dto = WordMappingDTO("qat", "_at", "???", false)
        val result = dto.toWord()
        assertEquals(PartOfSpeech.UNKNOWN, result.partOfSpeech)
    }

    /**
     * Tests that toWord can handle an unspecified form.
     */
    @Test
    fun testToWord_form_unspecified(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false)
        val result = dto.toWord()
        assertEquals(Form.NORMAL, result.form)
    }

    /**
     * Tests that toWord can handle an unknown form.
     */
    @Test
    fun testToWord_form_unknown(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false, "cabbage")
        val result = dto.toWord()
        assertEquals(Form.NORMAL, result.form)
    }

    /**
     * Tests that toWord can handle a normal form.
     */
    @Test
    fun testToWord_form_normal(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false, "Normal")
        val result = dto.toWord()
        assertEquals(Form.NORMAL, result.form)
    }

    /**
     * Tests that toWord can handle a plural form.
     */
    @Test
    fun testToWord_form_plural(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false, "Plural")
        val result = dto.toWord()
        assertEquals(Form.PLURAL, result.form)
    }

    /**
     * Tests that toWord can handle an uncount form.
     */
    @Test
    fun testToWord_form_uncount(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false, "Uncount")
        val result = dto.toWord()
        assertEquals(Form.UNCOUNT, result.form)
    }

    /**
     * Tests that toWord can handle a unique form.
     */
    @Test
    fun testToWord_form_unique(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false, "Unique")
        val result = dto.toWord()
        assertEquals(Form.UNIQUE, result.form)
    }

    /**
     * Tests that toWord can handle a proper noun form.
     */
    @Test
    fun testToWord_form_name(){
        val dto = WordMappingDTO("cat", "_at", "Noun", false, "Name")
        val result = dto.toWord()
        assertEquals(Form.PROPER_NOUN, result.form)
    }

    /**
     * Tests that toWord can handle a person form.
     */
    @Test
    fun testToWord_form_person() {
        val dto = WordMappingDTO("cat", "_at", "Noun", false, "Person")
        val result = dto.toWord()
        assertEquals(Form.PERSON, result.form)
    }
}