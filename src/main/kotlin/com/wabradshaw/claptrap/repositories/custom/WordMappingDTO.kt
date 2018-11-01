package com.wabradshaw.claptrap.repositories.custom

import com.wabradshaw.claptrap.structure.PartOfSpeech
import com.wabradshaw.claptrap.structure.Word

class WordMappingDTO(val spelling: String, val group: String, val pos: String, val adult: Boolean, val has: List<String>) {

    private val partOfSpeech = when (pos){
        "Noun" -> PartOfSpeech.NOUN
        "Verb" -> PartOfSpeech.VERB
        "Adj" -> PartOfSpeech.ADJECTIVE
        else -> PartOfSpeech.NOUN
    }

    fun toWord(): Word {
        return Word(spelling, spelling, partOfSpeech, 100.0)
    }
}