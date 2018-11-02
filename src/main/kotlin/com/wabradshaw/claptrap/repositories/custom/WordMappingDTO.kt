package com.wabradshaw.claptrap.repositories.custom

import com.fasterxml.jackson.annotation.JsonProperty
import com.wabradshaw.claptrap.structure.PartOfSpeech
import com.wabradshaw.claptrap.structure.Word

class WordMappingDTO(val spelling: String,
                     val group: String,
                     val pos: String,
                     val adult: Boolean,
                     val has: List<String> = emptyList(),
                     @JsonProperty("in") val inside: List<String> = emptyList(),
                     val on: List<String> = emptyList(),
                     val from: List<String> = emptyList()) {

    private val partOfSpeech = when (pos){
        "Noun" -> PartOfSpeech.NOUN
        "Verb" -> PartOfSpeech.VERB
        "Adj" -> PartOfSpeech.ADJECTIVE
        else -> PartOfSpeech.UNKNOWN
    }

    fun toWord(): Word {
        return Word(spelling, spelling, partOfSpeech, 100.0)
    }
}