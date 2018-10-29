package com.wabradshaw.claptrap.repositories.datamuse

import com.wabradshaw.claptrap.repositories.datamuse.DataMuseRepository
import com.wabradshaw.claptrap.structure.LinguisticSimilarity
import com.wabradshaw.claptrap.structure.PartOfSpeech
import com.wabradshaw.claptrap.structure.Word
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataMuseRepositoryTest {

    val repo: DataMuseRepository = DataMuseRepository()

    @Test
    fun testGetWord_known(){
        var result = repo.getWord("that");
        println(result);
    }

    @Test
    fun testGetSubstitutions_known(){
        var word = Word("horse", "", PartOfSpeech.NOUN, 10.0)
        var result = repo.getSubstitutions(word, listOf(LinguisticSimilarity.RHYME))
        println(result);
    }
}