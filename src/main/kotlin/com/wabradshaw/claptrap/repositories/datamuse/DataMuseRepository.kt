package com.wabradshaw.claptrap.repositories.datamuse

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wabradshaw.claptrap.repositories.DictionaryRepository
import com.wabradshaw.claptrap.repositories.LinguisticRepository
import com.wabradshaw.claptrap.repositories.SemanticRepository
import com.wabradshaw.claptrap.structure.*
import java.net.URL
import java.net.URLEncoder

class DataMuseRepository : DictionaryRepository, LinguisticRepository, SemanticRepository {

    val mapper = jacksonObjectMapper()

    override fun getWord(string: String): Word? {
        val url = URL("https://api.datamuse.com/words?sp=${clean(string)}&qe=sp&md=frp&max=1")

        val words = mapper.readValue<List<DataMuseWordDTO>>(url)
        val result = words.getOrNull(0)?.toWord()

        return when(result?.spelling){
            string -> result
            else -> null
        }
    }

    fun clean(string: String): String{
        return URLEncoder.encode(string, "utf-8")
    }

    override fun getLinguisticSubs(word: Word, validSimilarities: List<LinguisticSimilarity>): List<LinguisticSubstitution> {
        return validSimilarities.map{similarity -> getSimilarity(word, similarity)}.flatten()
    }

    override fun getSemanticSubs(word: Word, validRelationships: List<Relationship>): List<SemanticSubstitution> {
        return validRelationships.map{relationship -> getRelationship(word, relationship)}.flatten()
    }

    private fun getSimilarity(word: Word, similarity: LinguisticSimilarity): List<LinguisticSubstitution>{
        val simCode = when(similarity){
            LinguisticSimilarity.RHYME -> "rhy"
            LinguisticSimilarity.NEAR_RHYME -> "nry"
            LinguisticSimilarity.HOMOPHONE -> "hom"
            LinguisticSimilarity.CONSONANT_MATCH -> "cns"
        }
        val url = URL("https://api.datamuse.com/words?rel_$simCode=${clean(word.spelling)}&md=frp")
        println(url);
        println(url.readText())
        val similarWords = mapper.readValue<List<DataMuseWordDTO>>(url)

        return similarWords.map{similar -> LinguisticSubstitution(similar.toWord(), word, similarity) }
    }

    private fun getRelationship(word: Word, relationship: Relationship): List<SemanticSubstitution>{
        val simCode = when(relationship){
            Relationship.SYNONYM -> "syn"
            Relationship.IS_A -> "spc"
            Relationship.INCLUDES -> "gen"
            Relationship.HAS_A -> "com"
            Relationship.PART_OF -> "par"
            Relationship.IS_NOT -> "ant"
        }
        val url = URL("https://api.datamuse.com/words?rel_$simCode=${clean(word.spelling)}&md=frp")
        println(url);
        println(url.readText())
        val similarWords = mapper.readValue<List<DataMuseWordDTO>>(url)

        return similarWords.map{similar -> SemanticSubstitution(similar.toWord(), word, relationship) }
    }
}

