package com.wabradshaw.claptrap.repositories.datamuse

import com.wabradshaw.claptrap.structure.PartOfSpeech
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * A set of tests for the DataMuseDTO class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataMuseWordDTOTest {

    /**
     * Tests that toWord will accurately fill in the spelling of the word.
     */
    @Test
    fun testToWord_spelling(){
        val dto = DataMuseWordDTO("elephant",
                                  2147483647,
                                   listOf("query","n","pron:EH1 L AH0 F AH0 N T ","f:10.408478"))
        val result = dto.toWord()

        assertEquals("elephant", result.spelling)
    }

    /**
     * Tests that toWord will accurately fill in the pronunciation of the word.
     */
    @Test
    fun testToWord_pronunciation(){
        val dto = DataMuseWordDTO("elephant",
                2147483647,
                listOf("query","n","pron:EH1 L AH0 F AH0 N T ","f:10.408478"))
        val result = dto.toWord()

        assertEquals("EH1 L AH0 F AH0 N T ", result.pronunciation)
    }

    /**
     * Tests that toWord will accurately fill in the frequency of the word.
     */
    @Test
    fun testToWord_frequency(){
        val dto = DataMuseWordDTO("elephant",
                2147483647,
                listOf("query","n","pron:EH1 L AH0 F AH0 N T ","f:10.408478"))
        val result = dto.toWord()

        assertEquals(10.408478, result.popularity)
    }

    /**
     * Tests that toWord will accurately fill in the part of speech for a noun.
     */
    @Test
    fun testToWord_pos_noun(){
        val dto = DataMuseWordDTO("run",
                2147483647,
                listOf("query","v","n","pron:R AH1 N ","f:189.036204"))
        val result = dto.toWord()

        assertEquals(PartOfSpeech.VERB, result.partOfSpeech)
    }

    /**
     * Tests that toWord will accurately fill in the part of speech for a verb.
     */
    @Test
    fun testToWord_pos_verb(){
        val dto = DataMuseWordDTO("run",
                2147483647,
                listOf("query","v","pron:P R OW0 L IH1 F ER0 EY0 T ","f:1.843760"))
        val result = dto.toWord()

        assertEquals(PartOfSpeech.VERB, result.partOfSpeech)
    }

    /**
     * Tests that toWord will accurately fill in the part of speech for a verb.
     */
    @Test
    fun testToWord_pos_adj(){
        val dto = DataMuseWordDTO("quirky",
                2147483647,
                listOf("query","adj","pron:K W ER1 K IY0 ","f:0.639397"))
        val result = dto.toWord()

        assertEquals(PartOfSpeech.ADJECTIVE, result.partOfSpeech)
    }

    /**
     * Tests that toWord will falsely fill in the part of speech for another pos and default to noun.
     */
    @Test
    fun testToWord_pos_other(){
        val dto = DataMuseWordDTO("that",
                2147483647,
                listOf("query","pron:DH AE1 T ","f:10691.065736"))
        val result = dto.toWord()

        assertEquals(PartOfSpeech.NOUN, result.partOfSpeech)
    }

    /**
     * Tests that toWord will use the first part of speech if multiple are provided, as that is the most common use.
     */
    @Test
    fun testToWord_pos_multiple(){
        val dto = DataMuseWordDTO("run",
                2147483647,
                listOf("query","v","n","pron:R AH1 N ","f:189.036204"))
        val result = dto.toWord()

        assertEquals(PartOfSpeech.VERB, result.partOfSpeech)
    }
}