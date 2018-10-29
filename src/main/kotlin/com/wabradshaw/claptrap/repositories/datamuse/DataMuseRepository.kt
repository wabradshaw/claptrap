package com.wabradshaw.claptrap.repositories.datamuse

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wabradshaw.claptrap.repositories.DictionaryRepository
import com.wabradshaw.claptrap.repositories.LinguisticRepository
import com.wabradshaw.claptrap.structure.LinguisticSimilarity
import com.wabradshaw.claptrap.structure.LinguisticSubstitution
import com.wabradshaw.claptrap.structure.Word
import java.net.URL

class DataMuseRepository : DictionaryRepository, LinguisticRepository {

    val mapper = jacksonObjectMapper()

    override fun getWord(string: String): Word? {
        val url = URL("https://api.datamuse.com/words?sp=$string&qe=sp&md=frp&max=1")

        val words = mapper.readValue<List<DataMuseWordDTO>>(url)
        val result = words.getOrNull(0)?.toWord()

        return when(result?.spelling){
            string -> result
            else -> null
        }
    }

    override fun getSubstitutions(word: Word, validSimilarities: List<LinguisticSimilarity>): List<LinguisticSubstitution> {
        return validSimilarities.map{similarity -> getSimilarity(word, similarity)}.flatten()
    }

    private fun getSimilarity(word: Word, similarity: LinguisticSimilarity): List<LinguisticSubstitution>{
        val simCode = when(similarity){
            LinguisticSimilarity.RHYME -> "rhy"
            LinguisticSimilarity.NEAR_RHYME -> "nry"
            LinguisticSimilarity.HOMOPHONE -> "hom"
            LinguisticSimilarity.CONSONANT_MATCH -> "cns"
        }
        val url = URL("https://api.datamuse.com/words?rel_$simCode=${word.spelling}&md=frp")

        println(url.readText())
        val similarWords = mapper.readValue<List<DataMuseWordDTO>>(url)

        return similarWords.map{similar -> LinguisticSubstitution(similar.toWord(), word, similarity) }
    }
}
