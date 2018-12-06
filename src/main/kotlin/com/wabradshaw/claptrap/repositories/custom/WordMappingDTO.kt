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
                     val from: List<String> = emptyList(),
                     @JsonProperty("is") val synonym: List<String> = emptyList(),
                     val typeOf: List<String> = emptyList(),
                     val supertypeOf: List<String> = emptyList(),
                     val nearlyIs: List<String> = emptyList(),
                     val property: List<String> = emptyList(),
                     val acts: List<String> = emptyList(),
                     val actsCont: List<String> = emptyList(),
                     val recipient: List<String> = emptyList(),
                     val recipientPast: List<String> = emptyList()) {

    val partOfSpeech = when (pos){
        "Noun" -> PartOfSpeech.NOUN
        "Verb" -> PartOfSpeech.VERB
        "Adj" -> PartOfSpeech.ADJECTIVE
        else -> PartOfSpeech.UNKNOWN
    }

    fun toWord(): Word {
        return Word(spelling, spelling, partOfSpeech, 100.0)
    }
}