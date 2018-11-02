package com.wabradshaw.claptrap.repositories.custom

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wabradshaw.claptrap.repositories.DictionaryRepository
import com.wabradshaw.claptrap.repositories.LinguisticRepository
import com.wabradshaw.claptrap.repositories.SemanticRepository
import com.wabradshaw.claptrap.structure.*
import java.io.InputStream

/**
 * A JsonRepository is a dictionary that uses a custom json dictionary to store relationships between words. As this
 * is a manually created dictionary, it is only suitable for substring matches, not for accepting any word the user can
 * think of.
 *
 * Json is supplied in the following structure:
 *
 * [
 *   {
 *      "spelling": "cat", // how the word is spelled
 *      "group": "_at", // the linguistic group for the word
 *      "pos": "Noun", // the part of speech for the word. Can be "Noun", "Verb", "Adj" or anything else for unknown
 *      "adult": false, // true if the word would be unsuitable for children
 *      "has": ["whiskers", "a tail"] // a list of descriptions for what a noun has. Can be empty.
 *   },
 *   { ... }
 * ]
 *
 * @param jsonSource An input stream pointing to the json containing the dictionary. Defaults to the standard one.
 * @param showAdult Whether or not adult words can be included in jokes. Defaults to true.
 * @param validRelationships A list of the semantic relationships that can be used in jokes. Defaults to all of those
 *                           included in the default json.
 */
class JsonRepository(jsonSource: InputStream = object{}.javaClass.getResourceAsStream("/customDictionary.json"),
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
        val detailedWord = wordMap[word.spelling]
        return when(detailedWord == null){
            true -> emptyList()
            false -> validRelationships.flatMap{relationship -> getRelationship(detailedWord!!, relationship)}
        }
    }

    /**
     * Gets all of the substitutions for the given relationship.
     */
    private fun getRelationship(detailedWord: WordMappingDTO, relationship: Relationship): Iterable<SemanticSubstitution> {
        return when(relationship){
            Relationship.HAS_A -> detailedWord.has.map{SemanticSubstitution(Word(it, it, PartOfSpeech.UNKNOWN, 100.0), detailedWord.toWord(), relationship)}
            else -> emptyList()
        }
    }

}