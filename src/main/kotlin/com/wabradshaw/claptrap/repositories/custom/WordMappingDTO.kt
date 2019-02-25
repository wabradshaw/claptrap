package com.wabradshaw.claptrap.repositories.custom

import com.fasterxml.jackson.annotation.JsonProperty
import com.wabradshaw.claptrap.structure.Form
import com.wabradshaw.claptrap.structure.PartOfSpeech
import com.wabradshaw.claptrap.structure.Word

class WordMappingDTO(val spelling: String,
                     val group: String,
                     val pos: String,
                     val adult: Boolean,
                     @JsonProperty("form") val formString: String = "Normal",
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

    private val form = when (formString){
        "Normal" -> Form.NORMAL
        "Plural" -> Form.PLURAL
        "Uncount" -> Form.UNCOUNT
        "Person" -> Form.PERSON
        "Name" -> Form.PROPER_NOUN
        "Unique" -> Form.UNIQUE
        else -> Form.NORMAL
    }

    fun toWord(): Word {
        return Word(spelling, spelling, partOfSpeech, 100.0, form)
    }
}