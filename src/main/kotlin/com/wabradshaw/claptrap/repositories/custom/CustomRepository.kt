package com.wabradshaw.claptrap.repositories.custom

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sun.org.apache.regexp.internal.RE
import com.wabradshaw.claptrap.repositories.DictionaryRepository
import com.wabradshaw.claptrap.repositories.LinguisticRepository
import com.wabradshaw.claptrap.repositories.SemanticRepository
import com.wabradshaw.claptrap.structure.*
import java.io.InputStream

class CustomRepository(private val jsonSource: InputStream,
                       private val showAdult: Boolean = true,
                       private val validRelationships: List<Relationship>
                       = listOf(Relationship.HAS_A))
    : DictionaryRepository, SemanticRepository, LinguisticRepository {

    private val allWords = jacksonObjectMapper().readValue<List<WordMappingDTO>>(jsonSource).filter{showAdult || !it.adult}
    private val wordMap = allWords.associateBy {it.spelling}
    private val groupMap = allWords.groupBy { word -> word.group }

    override fun getWord(string: String): Word? {
        return wordMap[string]?.toWord()
    }

    override fun getLinguisticSubs(word: Word): List<LinguisticSubstitution> {
        val allSubs = groupMap.getOrDefault(wordMap[word.spelling]?.group, emptyList())
        return allSubs.filter{it.spelling != word.spelling}
                      .map{ LinguisticSubstitution(it.toWord(), word, LinguisticSimilarity.RHYME) }
    }

    override fun getSemanticSubs(word: Word): List<SemanticSubstitution> {
        val detailedWord = wordMap[word.spelling] ?: throw IllegalArgumentException(word.spelling + " does not exist in the dictionary.")
        return validRelationships.flatMap{relationship -> getRelationship(detailedWord, relationship)}
    }

    private fun getRelationship(detailedWord: WordMappingDTO, relationship: Relationship): Iterable<SemanticSubstitution> {
        return when(relationship){
            Relationship.HAS_A -> detailedWord.has.map{SemanticSubstitution(Word(it, it, PartOfSpeech.UNKNOWN, 100.0), detailedWord.toWord(), relationship)}
            else -> emptyList()
        }
    }

}